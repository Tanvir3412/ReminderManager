package edu.qc.seclass.glm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ViewReminderDialog extends AppCompatDialogFragment {

    private TextView reminderName;
    private TextView reminderType;
    private TextView reminderDescription;
    private TextView reminderDate;
    private TextView reminderTime;
    private TextView reminderRepeat;
    private Button cancelButton;
    private AlertDialog.Builder dialogBuilder;
    private Reminder reminder;

    public ViewReminderDialog(Reminder reminder) {
        this.reminder = reminder;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View popupView = getLayoutInflater().inflate(R.layout.activity_view_reminder, null);
        dialogBuilder.setView(popupView);

        AlertDialog reminderDialog = dialogBuilder.create();

        reminderName = popupView.findViewById(R.id.ReminderName);
        reminderName.append(" " + reminder.getName());
        reminderType = popupView.findViewById(R.id.ReminderType);
        reminderType.append(" " + reminder.getType());
        reminderDescription = popupView.findViewById(R.id.ReminderDescription);
        reminderDescription.append(" " + reminder.getDescription());
        reminderDate = popupView.findViewById(R.id.ReminderDate);
        reminderDate.append(" " + reminder.getDate());
        reminderTime = popupView.findViewById(R.id.ReminderTime);
        reminderTime.append(" " + reminder.getTime());
        reminderRepeat = popupView.findViewById(R.id.ReminderRepeats);
        reminderRepeat.append(" " + reminder.getRepeatString());
        cancelButton = popupView.findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                reminderDialog.dismiss();
            }
        });
        return reminderDialog;
    }
}