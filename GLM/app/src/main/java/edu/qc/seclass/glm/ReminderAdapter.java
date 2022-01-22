package edu.qc.seclass.glm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView reminderNameTV;
        public TextView descriptionTV;
        public CheckBox reminderCB;
        public ImageButton editButton;
        public ImageButton infoButton;
        public ReminderListener reminderListener;

        public ViewHolder(View itemView, ReminderListener reminderListener) {
            super(itemView);

            this.reminderListener = reminderListener;
            reminderNameTV = (TextView) itemView.findViewById(R.id.ReminderItem);
            descriptionTV = (TextView) itemView.findViewById(R.id.ReminderListDescription);
            reminderCB = (CheckBox) itemView.findViewById(R.id.ReminderCheckbox);
            editButton = (ImageButton) itemView.findViewById(R.id.ReminderEditButton);
            infoButton = (ImageButton) itemView.findViewById(R.id.ReminderInfoButton);
        }

        @Override
        public void onClick(View v){
            reminderListener.onReminderClick(getAdapterPosition());
        }
    }

    private ReminderList list;
    private ReminderListener reminderListener;
    private LinkedList<Reminder> remindersToDelete;

    public ReminderAdapter(ReminderList list, ReminderListener reminderListener) {
        this.list = list;
        this.reminderListener = reminderListener;
        remindersToDelete = new LinkedList<>();
        for (int i = 0; i < list.getSize(); i++) {
            if (list.getReminder(i).getCheck())
                remindersToDelete.add(list.getReminder(i));
        }
    }

    @Override
    public ReminderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View reminderView = inflater.inflate(R.layout.row_reminderlayout, parent, false);

        if(!remindersToDelete.isEmpty()){
            reminderListener.deleteButtonVisibility(true, remindersToDelete);
        }

        ViewHolder viewHolder = new ViewHolder(reminderView, reminderListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReminderAdapter.ViewHolder holder, int index) {
        Reminder reminder = list.getReminder(index);

        TextView nameTV = holder.reminderNameTV;
        nameTV.setText(reminder.getName());

        TextView descTV = holder.descriptionTV;
        descTV.setText(getDateTime(reminder));
        if(getDateTime(reminder).equalsIgnoreCase("")) descTV.setVisibility(View.GONE);
        else descTV.setVisibility(View.VISIBLE);

        ImageButton editButton = holder.editButton;
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderListener.editReminderPopup(reminder);
            }
        });
        ImageButton infoButton = holder.infoButton;
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reminderListener.viewReminderPopup(reminder);
            }
        });
        CheckBox cb = holder.reminderCB;
        if (reminder.getCheck()) {
            cb.setChecked(reminder.getCheck());
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(cb.getContext());
                dbHelper.setReminderChecked(reminder.getName(), list.getName(), cb.isChecked());
                if (cb.isChecked()) {
                    reminder.setCheck(cb.isChecked());
                    remindersToDelete.add(reminder);
                    if(!remindersToDelete.isEmpty()){
                        reminderListener.deleteButtonVisibility(cb.isChecked(), remindersToDelete);
                    }
                } else {
                    reminder.setCheck(cb.isChecked());
                    remindersToDelete.remove(reminder);
                    if(remindersToDelete.isEmpty()) {
                        reminderListener.deleteButtonVisibility(cb.isChecked(), remindersToDelete);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.getSize();
    }

    public interface ReminderListener {
        void onReminderClick(int position);
        void deleteButtonVisibility(boolean check, LinkedList<Reminder> remindersToBeDeleted);
        void editReminderPopup(Reminder reminder);
        void viewReminderPopup(Reminder reminder);
    }

    public String getDateTime(Reminder reminder){
        if((!reminder.getDate().equalsIgnoreCase("") || reminder.getDate() != null) && (reminder.getTime() == null || reminder.getTime().equalsIgnoreCase(""))) return reminder.getDate();
        else if((reminder.getDate().equalsIgnoreCase("") || reminder.getDate() == null) && (reminder.getTime() != null || !reminder.getTime().equalsIgnoreCase(""))) return reminder.getTime();
        else if((reminder.getDate().equalsIgnoreCase("") || reminder.getDate() == null) && (reminder.getTime() == null || reminder.getTime().equalsIgnoreCase(""))) return "";
        else return reminder.getDate() + " - " + reminder.getTime();
    }
}
