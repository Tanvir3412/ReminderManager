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

public class CreateListDialog extends AppCompatDialogFragment {
    private EditText listNameInput;
    private Button createListButton;
    private AlertDialog.Builder dialogBuilder;
    private CreateListDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialogBuilder = new AlertDialog.Builder(getActivity());
        final View createListPopupView = getLayoutInflater().inflate(R.layout.activity_create_list, null);
        dialogBuilder.setView(createListPopupView);

        AlertDialog listDialog = dialogBuilder.create();

        listNameInput = createListPopupView.findViewById(R.id.ListNameInput);
        createListButton = createListPopupView.findViewById(R.id.CreateListButton);
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String listName = listNameInput.getText().toString();
                DBHelper dbHelper = new DBHelper(getContext());
                if (dbHelper.hasList(listName))
                    Toast.makeText(getContext(), "List name must be unique", Toast.LENGTH_SHORT).show();
                else {
                    listener.addNewList(listName);
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
            listener = (CreateListDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ListDialogListener");
        }
    }

    public interface CreateListDialogListener {
        void addNewList(String listName);
    }
}