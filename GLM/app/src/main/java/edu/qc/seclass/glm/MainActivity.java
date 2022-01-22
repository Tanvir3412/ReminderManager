package edu.qc.seclass.glm;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements CreateListDialog.CreateListDialogListener, EditListDialog.EditListDialogListener, ListAdapter.ListListener {
    private LinkedList<ReminderList> lists = new LinkedList<ReminderList>();
    private FloatingActionButton btn_createList;
    private FloatingActionButton btn_deleteLists;
    private RecyclerView listOfReminderLists;
    private LinkedList<ReminderList> listsToBeDeleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Defined Variables in View
        btn_createList = findViewById(R.id.AddListButton);
        listOfReminderLists = findViewById(R.id.ReminderList_List);
        btn_deleteLists = findViewById(R.id.DeleteListButton);

        //db functionality
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        lists = dbHelper.getAllLists();

        ListAdapter adapter = new ListAdapter(lists, this);
        listOfReminderLists.setAdapter(adapter);
        listOfReminderLists.setLayoutManager(new LinearLayoutManager(this));

        //Create List Button Function
        btn_createList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListPopup();
            }
        });

        //Delete checked lists
        btn_deleteLists.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < listsToBeDeleted.size(); i++) {
                    dbHelper.deleteList(listsToBeDeleted.get(i).getName());
                    lists = dbHelper.getAllLists();

                    btn_deleteLists.setVisibility(INVISIBLE);

                    //Refresh recycler view
                    ListAdapter adapter = new ListAdapter(lists, MainActivity.this);
                    listOfReminderLists.setAdapter(adapter);
                    listOfReminderLists.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                }
            }
        });
    }

    //Function to display create_list layout to popup screen
    public void createListPopup() {
        CreateListDialog createListPopup = new CreateListDialog();
        createListPopup.show(getSupportFragmentManager(), "Create List Dialog");
    }

    public void deleteButtonVisibility(boolean check, LinkedList<ReminderList> listsToBeDeleted) {
        this.listsToBeDeleted = listsToBeDeleted;
        if (check) {
            btn_deleteLists.setVisibility(VISIBLE);
        } else {
            btn_deleteLists.setVisibility(INVISIBLE);
        }
    }

    @Override
    public void addNewList(String listName) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        boolean inserted = dbHelper.addList(new ReminderList(listName));
        if (!inserted)
            Toast.makeText(MainActivity.this, "Failed to create List", Toast.LENGTH_SHORT).show();
        lists = dbHelper.getAllLists();

        //Refreshes List with new list
        ListAdapter adapter = new ListAdapter(lists, this);
        listOfReminderLists.setAdapter(adapter);
        listOfReminderLists.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onListClick(int position) {
        ReminderList remList = lists.get(position);
        Intent intent = new Intent(this, ReminderListViewActivity.class);
        intent.putExtra("List", remList.getName());
        startActivity(intent);
    }

    @Override
    public void editListPopup(ReminderList list) {
        EditListDialog editListPopup = new EditListDialog(list);
        editListPopup.show(getSupportFragmentManager(), "Edit List Dialog");
    }

    @Override
    public void editList(String oldName, String newName) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        boolean inserted = dbHelper.editList(oldName, newName);
        if (!inserted)
            Toast.makeText(MainActivity.this, "Failed to edit List", Toast.LENGTH_SHORT).show();
        lists = dbHelper.getAllLists();

        //Refreshes Lists with new list
        ListAdapter adapter = new ListAdapter(lists, this);
        listOfReminderLists.setAdapter(adapter);
        listOfReminderLists.setLayoutManager(new LinearLayoutManager(this));
    }
}