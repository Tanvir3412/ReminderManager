package edu.qc.seclass.glm;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class SelectExistingReminderDialog extends AppCompatDialogFragment {
    private AlertDialog.Builder dialogBuilder;
    private SelectExistingReminderDialogListener listener;
    private AutoCompleteTextView reminderNameDropdown;
    private AutoCompleteTextView reminderTypeDropdown;
    private Button cancelButton;
    private Button selectButton;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View popupView = getLayoutInflater().inflate(R.layout.activity_select_existing_reminder, null);
        dialogBuilder.setView(popupView);

        AlertDialog reminderDialog = dialogBuilder.create();

        DBHelper dbHelper = new DBHelper(getContext());

        reminderNameDropdown = popupView.findViewById(R.id.ReminderNameDropdown);
        String[] reminderNames = dbHelper.getReminderNames();
        ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(getContext(), R.layout.row_dropdown_listlayout, reminderNames);
        reminderNameDropdown.setAdapter(nameAdapter);

        reminderTypeDropdown = popupView.findViewById(R.id.ReminderTypeDropdown);
        String[] reminderTypes = dbHelper.getTypeNames();
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(getContext(), R.layout.row_dropdown_listlayout, reminderTypes);
        reminderTypeDropdown.setAdapter(typeAdapter);

        cancelButton = popupView.findViewById(R.id.CancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderDialog.dismiss();
            }
        });
        selectButton = popupView.findViewById(R.id.SelectButton);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reminderName = reminderNameDropdown.getText().toString();
                String reminderType = reminderTypeDropdown.getText().toString();
                //Adds Reminder inputs to previous activity through method
                listener.addExistingReminder(reminderName, reminderType);

                //Toast.makeText(getContext(), "Unable to get existing reminder", Toast.LENGTH_SHORT).show();
                reminderDialog.dismiss();
            }
        });
        return reminderDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SelectExistingReminderDialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SelectExistingReminderDialogListener");
        }
    }

    public interface SelectExistingReminderDialogListener {
        void addExistingReminder(String reminderName, String reminderType);
    }
}
