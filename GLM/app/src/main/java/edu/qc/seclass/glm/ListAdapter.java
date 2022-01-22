package edu.qc.seclass.glm;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder>{
    private LinkedList<ReminderList> list;
    private ListListener listListener;
    private LinkedList<ReminderList> listsToDelete;

    public ListAdapter(LinkedList<ReminderList> list, ListListener listListener) {
        this.list = list;
        this.listListener = listListener;
        this.listsToDelete = new LinkedList<>();
        for (int i = 0; i < this.list.size(); i++) {
            if (list.get(i).getCheck())
                listsToDelete.add(list.get(i));
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView listNameTV;
        public CheckBox listCB;
        public ImageButton editListButton;
        public ListListener listListener;

        public ViewHolder(View itemView, ListListener listListener) {
            super(itemView);

            this.listListener = listListener;
            listNameTV = (TextView) itemView.findViewById(R.id.ListItem);
            listCB = (CheckBox) itemView.findViewById(R.id.ListCheckbox);
            editListButton = itemView.findViewById(R.id.EditListButton);

            listNameTV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listListener.onListClick(getAdapterPosition());
        }
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View listView = inflater.inflate(R.layout.row_listlayout, parent, false);

        if(!listsToDelete.isEmpty()){
            listListener.deleteButtonVisibility(true, listsToDelete);
        }

        ListAdapter.ViewHolder viewHolder = new ListAdapter.ViewHolder(listView, listListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ListAdapter.ViewHolder holder, int index) {
        ReminderList reminderList = list.get(index);

        TextView nameTV = holder.listNameTV;
        nameTV.setText(reminderList.getName());
        ImageButton btn_editList = holder.editListButton;
        btn_editList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listListener.editListPopup(reminderList);
            }
        });
        CheckBox cb = holder.listCB;
        if(reminderList.getCheck()){
            cb.setChecked(reminderList.getCheck());
        }
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DBHelper dbHelper = new DBHelper(cb.getContext());
                dbHelper.setListChecked(reminderList.getName(), cb.isChecked());
                if (cb.isChecked()) {
                    reminderList.setCheck(cb.isChecked());
                    listsToDelete.add(reminderList);
                    if(!listsToDelete.isEmpty()){
                        listListener.deleteButtonVisibility(cb.isChecked(), listsToDelete);
                    }
                } else {
                    reminderList.setCheck(cb.isChecked());
                    listsToDelete.remove(reminderList);
                    if(listsToDelete.isEmpty()) {
                        listListener.deleteButtonVisibility(cb.isChecked(), listsToDelete);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    //Used for when a list is clicked on
    public interface ListListener {
        void onListClick(int position);
        void deleteButtonVisibility(boolean check, LinkedList<ReminderList> listsToBeDeleted);
        void editListPopup(ReminderList list);
    }
}
