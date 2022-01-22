package edu.qc.seclass.glm;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class ReminderListViewActivity extends AppCompatActivity implements CreateReminderDialog.CreateReminderDialogListener, EditReminderDialog.EditReminderDialogListener, ReminderAdapter.ReminderListener {
    private ReminderList reminderList;
    private RecyclerView remindersView;
    private TextView listName;
    private FloatingActionButton deleteRemindersButton;
    private FloatingActionButton addButton;
    private String name;
    private LinkedList<Reminder> remindersToBeDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list_view);

        remindersView = findViewById(R.id.ReminderList);
        listName = findViewById(R.id.ListNameHeader);
        addButton = findViewById(R.id.AddReminderButton);
        deleteRemindersButton = findViewById(R.id.DeleteReminderButton);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createReminderPopup();
            }
        });

        //Access database to get list information
        DBHelper dbHelper = new DBHelper(ReminderListViewActivity.this);
        name = (String) getIntent().getExtras().get("List");
        reminderList = dbHelper.getReminders(name);

        listName.setText(reminderList.getName());

        ReminderAdapter adapter = new ReminderAdapter(reminderList, this);
        remindersView.setAdapter(adapter);
        remindersView.setLayoutManager(new LinearLayoutManager(this));

        AlarmManager alarmManager = (AlarmManager) getBaseContext().getSystemService(this.ALARM_SERVICE);
        //Delete checked reminders
        deleteRemindersButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < remindersToBeDeleted.size(); i++) {
                    //Cancel notifications for the reminder if it had one and it has not gone off yet
                    if (!remindersToBeDeleted.get(i).getDate().equals("")
                            && !remindersToBeDeleted.get(i).getTime().equals("")
                            && remindersToBeDeleted.get(i).getUTC() - 1000*60*5 > System.currentTimeMillis()) {
                        int id = dbHelper.getReminderId(remindersToBeDeleted.get(i).getName(), name);
                        Intent intent = new Intent(ReminderListViewActivity.this, ReminderBroadcast.class);
                        PendingIntent p = PendingIntent.getBroadcast(ReminderListViewActivity.this, id, intent, 0);
                        alarmManager.cancel(p);
                    }

                    dbHelper.deleteReminder(remindersToBeDeleted.get(i).getName(), reminderList.getName());
                    reminderList = dbHelper.getReminders(reminderList.getName());
                }

                deleteRemindersButton.setVisibility(INVISIBLE);

                //Refresh recycler view
                ReminderAdapter adapter = new ReminderAdapter(reminderList, ReminderListViewActivity.this);
                remindersView.setAdapter(adapter);
                remindersView.setLayoutManager(new LinearLayoutManager(ReminderListViewActivity.this));
            }
        });
    }

    public void createReminderPopup(){
        CreateReminderDialog createReminderPopup = new CreateReminderDialog(name, this);
        createReminderPopup.show(getSupportFragmentManager(), "Create Reminder Dialog");
    }

    @Override
    public void editReminderPopup(Reminder reminder) {
        EditReminderDialog editReminderPopup = new EditReminderDialog(reminder, name, this);
        editReminderPopup.show(getSupportFragmentManager(), "Edit Reminder Dialog");
    }

    @Override
    public void viewReminderPopup(Reminder reminder) {
        ViewReminderDialog viewReminderPopup = new ViewReminderDialog(reminder);
        viewReminderPopup.show(getSupportFragmentManager(), "View Reminder Dialog");
    }

    @Override
    public void addNewReminder(Reminder reminder) {
        DBHelper dbHelper = new DBHelper(ReminderListViewActivity.this);
        boolean inserted = dbHelper.addReminder(reminder, reminderList.getName());
        if (!inserted)
            Toast.makeText(ReminderListViewActivity.this, "Failed to create Reminder", Toast.LENGTH_SHORT).show();
        reminderList = dbHelper.getReminders(name);

        //Refreshes Reminders inside Reminder List
        ReminderAdapter adapter = new ReminderAdapter(reminderList, this);
        remindersView.setAdapter(adapter);
        remindersView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void editReminder(Reminder reminderEdit, Reminder originalReminder) {
        DBHelper dbHelper = new DBHelper(ReminderListViewActivity.this);
        boolean inserted = dbHelper.editReminder(reminderEdit, originalReminder, reminderList.getName());
        if (!inserted)
            Toast.makeText(ReminderListViewActivity.this, "Failed to edit Reminder", Toast.LENGTH_SHORT).show();
        reminderList = dbHelper.getReminders(name);

        //Refreshes Reminders inside Reminder List
        ReminderAdapter adapter = new ReminderAdapter(reminderList, this);
        remindersView.setAdapter(adapter);
        remindersView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onReminderClick(int position) {

    }

    public void deleteButtonVisibility(boolean check, LinkedList<Reminder> remindersToBeDeleted){
        this.remindersToBeDeleted = remindersToBeDeleted;
        if(check){ deleteRemindersButton.setVisibility(VISIBLE); }
        else{ deleteRemindersButton.setVisibility(INVISIBLE); }
    }
}