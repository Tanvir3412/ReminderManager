package edu.qc.seclass.glm;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditListDialog extends AppCompatDialogFragment {

    private EditText listNameInput;
    private Button saveButton;
    private AlertDialog.Builder dialogBuilder;
    private EditListDialogListener listener;
    private ReminderList list;

    public EditListDialog(ReminderList list) {
        this.list = list;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View popupView = getLayoutInflater().inflate(R.layout.activity_edit_list, null);
        dialogBuilder.setView(popupView);

        AlertDialog listDialog = dialogBuilder.create();

        listNameInput = popupView.findViewById(R.id.ListNameInput);
        listNameInput.setText(list.getName());
        saveButton = popupView.findViewById(R.id.SaveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = listNameInput.getText().toString();
                DBHelper dbHelper = new DBHelper(getContext());
                if (!listName.equals(list.getName()) && dbHelper.hasList(listName))
                    Toast.makeText(getContext(), "List names must be unique", Toast.LENGTH_SHORT).show();
                else {
                    listener.editList(list.getName(), listName);
                    listDialog.dismiss();
                }
            }
        });
        return listDialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (EditListDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement EditListDialogListener");
        }
    }

    public interface EditListDialogListener {
        void editList(String oldName, String newName);
    }
}