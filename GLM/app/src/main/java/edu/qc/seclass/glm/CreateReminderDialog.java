package edu.qc.seclass.glm;

import static android.content.Context.ALARM_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ibm.icu.util.TimeZone;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class CreateReminderDialog extends AppCompatDialogFragment implements SelectExistingReminderDialog.SelectExistingReminderDialogListener {

    public EditText reminderNameInput;
    public EditText reminderTypeInput;
    private EditText reminderDescriptionInput;
    private Button dateInput;
    private Button timeInput;
    private Button clearDateButton;
    private Button clearTimeButton;
    private RadioGroup repeatOptions;
    private Button selectExistingReminderButton;
    private Button createReminderButton;

    private AlertDialog.Builder dialogBuilder;
    private CreateReminderDialogListener listener;
    private DatePickerDialog.OnDateSetListener dateListener;
    private TimePickerDialog.OnTimeSetListener timeListener;
    private String listName;
    private Activity activity;

    public CreateReminderDialog(String listName, Activity activity) {
        super();
        this.listName = listName;
        this.activity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View popupView = getLayoutInflater().inflate(R.layout.activity_create_reminder, null);
        dialogBuilder.setView(popupView);

        AlertDialog reminderDialog = dialogBuilder.create();

        reminderNameInput = popupView.findViewById(R.id.ReminderNameText);
        reminderTypeInput = popupView.findViewById(R.id.ReminderTypeText);
        reminderDescriptionInput = popupView.findViewById(R.id.ReminderDescriptionText);

        dateInput = popupView.findViewById(R.id.ReminderDateText);
        dateInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialogPopup();
            }
        });
        getDate();

        timeInput = popupView.findViewById(R.id.ReminderTimeText);
        timeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialogPopup();
            }
        });
        getTime();

        clearDateButton = popupView.findViewById(R.id.ClearDateButton);
        clearDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateInput.setText("Select Date");
            }
        });

        clearTimeButton = popupView.findViewById(R.id.ClearTimeButton);
        clearTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeInput.setText("Select Time");
            }
        });

        repeatOptions = popupView.findViewById(R.id.RepeatOptions);
        ((RadioButton)repeatOptions.getChildAt(0)).setChecked(true); // Sets Repeat Option to None (By Default)
        selectExistingReminderButton = popupView.findViewById(R.id.ExistingReminderButton);
        selectExistingReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectExistingReminderPopup();
            }
        });
        createReminderButton = popupView.findViewById(R.id.SaveReminderButton);
        createReminderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Convert Inputs and Store Them into Variables
                String reminderName = reminderNameInput.getText().toString();
                String reminderType = reminderTypeInput.getText().toString();
                String reminderDescription = reminderDescriptionInput.getText().toString();
                String reminderDate = dateInput.getText().toString();
                String reminderTime = timeInput.getText().toString();

                //Grabbing Selected Radio Button
                int optionID = repeatOptions.getCheckedRadioButtonId();
                int optionIndex = repeatOptions.indexOfChild(repeatOptions.findViewById(optionID));

                DBHelper dbHelper = new DBHelper(getContext());
                if(reminderName.equals("") || reminderType.equals("")) {
                    Toast.makeText(getContext(), "Please Enter a Name/Type for this Reminder", Toast.LENGTH_SHORT).show();
                }
                else if (dbHelper.hasReminder(reminderName, listName)) {
                    Toast.makeText(getContext(), "Reminder names must be unique within a list", Toast.LENGTH_SHORT).show();
                }
                else if (reminderDate.equals("Select Date") && !reminderTime.equals("Select Time")) {
                    Toast.makeText(getContext(), "No Date Entered: Please Enter a Date", Toast.LENGTH_SHORT).show();
                }
                else if (reminderDate.equals("Select Date") && optionIndex != 0) {
                    Toast.makeText(getContext(), "No Date Entered:\nEither Set Repeat to None or Enter a Date", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Create Reminder
                    Reminder newReminder = new Reminder(reminderName, reminderType);
                    newReminder.setDescription(reminderDescription);
                    if(reminderDate.equalsIgnoreCase("Select Date")) reminderDate = "";
                    if(reminderTime.equalsIgnoreCase("Select Time")) reminderTime = "";
                    newReminder.setDateTime(reminderDate, reminderTime);
                    newReminder.setRepeat(optionIndex);

                    //Add Reminder through Method
                    listener.addNewReminder(newReminder);
                    reminderDialog.dismiss();

                    //Create Notification for reminder
                    if (!reminderDate.equals("") && !reminderTime.equals("")) {
                        createNotificationChannel();
                        Intent intent = new Intent(activity, ReminderBroadcast.class);
                        intent.putExtra("Reminder", Reminder.toByteArray(newReminder));
                        intent.putExtra("List", listName);

                        int id = dbHelper.getReminderId(newReminder.getName(), listName);

                        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity,
                                id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager = getSystemService(getContext(), AlarmManager.class);

                        Toast.makeText(getContext(), "Reminder set for " + newReminder.getDate() + " - " + newReminder.getTime(), Toast.LENGTH_LONG).show();
                        long notifyTime = 1000 * 60 * 5;
                        if (newReminder.getUTC() - System.currentTimeMillis() < notifyTime)
                            notifyTime = newReminder.getUTC() - System.currentTimeMillis();

                        alarmManager.set(AlarmManager.RTC_WAKEUP,newReminder.getUTC() - notifyTime, pendingIntent);
                    }
                }
            }
        });
        return reminderDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CreateReminderDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CreateDialogListener");
        }
    }

    public interface CreateReminderDialogListener {
        void addNewReminder(Reminder reminder);
    }

    //Function to display create_list layout to popup screen
    public void selectExistingReminderPopup(){
        SelectExistingReminderDialog selectExistingReminderPopup = new SelectExistingReminderDialog();
        selectExistingReminderPopup.setTargetFragment(CreateReminderDialog.this, 1);
        selectExistingReminderPopup.show(getFragmentManager(), "Select Existing Reminder Dialog");
    }

    @Override
    public void addExistingReminder(String reminderName, String reminderType) {
        if(!reminderName.equals("Name")) { reminderNameInput.setText(reminderName); }
        if(!reminderType.equals("Type")) { reminderTypeInput.setText(reminderType); }
    }

    public void datePickerDialogPopup(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateListener, year, month, day);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }


    public void getDate(){
        dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateInput.setText((month+1) + "/" + dayOfMonth  + "/" + year);
            }
        };
    }

    public void timePickerDialogPopup(){
        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR), minutes = calendar.get(Calendar.MINUTE);
        if(calendar.get(Calendar.AM_PM) == Calendar.PM) hours+=12;
        TimePickerDialog timeDialog = new TimePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth, timeListener, hours, minutes, false);
        timeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timeDialog.show();
    }

    public void getTime(){
        timeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String time = hourOfDay + ":" + minute;
                SimpleDateFormat format24Hours = new SimpleDateFormat("hh:mm");
                Date date = null;
                try {
                    date = format24Hours.parse(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat format12Hours = new SimpleDateFormat("hh:mm aa");
                timeInput.setText(format12Hours.format(date));
            }
        };
    }

    public void createNotificationChannel () {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderChannel";
            String description = "Channel for the Reminder Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("reminderNotify", name, importance);
            channel.setDescription(description);

            NotificationManager manager = getSystemService(getContext(), NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
}