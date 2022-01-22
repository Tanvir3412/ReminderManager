package edu.qc.seclass.glm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.LinkedList;

public class DBHelper extends SQLiteOpenHelper {
    public static final String REMINDER_LIST = "REMINDER_LIST";
    public static final String column_LIST_NAME = "LIST_NAME";
    public static final String column_LIST_CHECKED = "LIST_CHECKED";

    public static final String REMINDERS = "REMINDERS";
    public static final String column_REMINDER_NAME = "REMINDER_NAME";
    public static final String column_REMINDER_LIST_NAME = "REMINDER_LIST_NAME";
    public static final String column_REMINDER_TYPE = "REMINDER_TYPE";
    public static final String column_REMINDER_CHECKED = "REMINDER_CHECKED";
    public static final String column_REMINDER_OBJECT = "REMINDER_OBJECT";

    public DBHelper(@Nullable Context context) {
        super(context, "reminder.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creates table where the lists will be stored
        String createListTable = "CREATE TABLE " + REMINDER_LIST + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                column_LIST_NAME + " TEXT, " + column_LIST_CHECKED + " BOOL);";
        db.execSQL(createListTable);

        //Creates the table where the reminders will be stored
        String createReminderTable = "CREATE TABLE " + REMINDERS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                column_REMINDER_NAME + " TEXT, " + column_REMINDER_LIST_NAME + " TEXT, " + column_REMINDER_TYPE +
                " TEXT, " + column_REMINDER_CHECKED + " BOOL, " + column_REMINDER_OBJECT + " BLOB);";
        db.execSQL(createReminderTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addList(ReminderList list) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(column_LIST_NAME, list.getName());
        cv.put(column_LIST_CHECKED, list.getCheck());

        long insert = db.insert(REMINDER_LIST, null, cv);
        db.close();
        return insert != -1;
    }

    public boolean editList(String oldName, String newName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updated = false;

        ContentValues cv = new ContentValues();
        cv.put(column_LIST_NAME, newName);
        long update = db.update(REMINDER_LIST, cv, column_LIST_NAME + " IS '" + oldName + "'",
                null);
        if (update != -1)
            updated = true;

        cv = new ContentValues();
        cv.put(column_REMINDER_LIST_NAME, newName);
        update = db.update(REMINDERS, cv, column_REMINDER_LIST_NAME + " IS '" + oldName + "'",
                null);
        if (update == -1)
            updated = false;

        db.close();
        return updated;
    }

    public boolean addReminder(Reminder reminder, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(column_REMINDER_NAME, reminder.getName());
        cv.put(column_REMINDER_LIST_NAME, listName);
        cv.put(column_REMINDER_TYPE, reminder.getType());
        cv.put(column_REMINDER_CHECKED, reminder.getCheck());
        cv.put(column_REMINDER_OBJECT, Reminder.toByteArray(reminder));

        long update = db.insert(REMINDERS, null, cv);
        db.close();
        if (update == -1)
            return false;
        return true;
    }

    public boolean editReminder(Reminder newReminder, Reminder oldReminder, String listName) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean updated = false;

        ContentValues cv = new ContentValues();
        cv.put(column_REMINDER_NAME, newReminder.getName());
        cv.put(column_REMINDER_LIST_NAME, listName);
        cv.put(column_REMINDER_TYPE, newReminder.getType());
        cv.put(column_REMINDER_CHECKED, newReminder.getCheck());
        cv.put(column_REMINDER_OBJECT, Reminder.toByteArray(newReminder));

        long update = db.update(REMINDERS, cv, column_REMINDER_NAME + " = '" + oldReminder.getName() +
                "' AND " + column_REMINDER_LIST_NAME + " = '" + listName + "'", null);
        db.close();
        if (update == -1)
            return false;
        return true;
    }

    public LinkedList<ReminderList> getAllLists() {
        LinkedList<ReminderList> returned = new LinkedList<>();
        LinkedList<String> names = new LinkedList<>();
        LinkedList<Boolean> checks = new LinkedList<>();

        String query = "SELECT " + column_LIST_NAME + ", " + column_LIST_CHECKED + " FROM " + REMINDER_LIST;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            //loop through results and create ReminderList objects
            do {
                String name = cursor.getString(0);
                names.add(name);
                boolean check = cursor.getInt(1) > 0;
                checks.add(check);
            } while (cursor.moveToNext());
        } else {

        }
        cursor.close();

        for (int i = 0; i < names.size(); i++) {
            query = "SELECT " + column_REMINDER_OBJECT + " FROM " + REMINDERS;
            cursor = db.rawQuery(query, null);

            ReminderList list = new ReminderList(names.get(i));
            list.setCheck(checks.get(i));
            if (cursor.moveToFirst()) {
                do {
                    Reminder r = Reminder.fromByteArray(cursor.getBlob(0));
                    list.addReminder(r);
                } while (cursor.moveToNext());
            }
            returned.add(list);
            cursor.close();
        }

        db.close();
        return returned;
    }

    public ReminderList getReminders(String listName) {
        String query = "SELECT " + column_REMINDER_OBJECT + " FROM " + REMINDERS + " WHERE " + column_REMINDER_LIST_NAME +
                " IS '" + listName + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        ReminderList list = new ReminderList(listName);
        if (cursor.moveToFirst()) {
            do {
                Reminder r = Reminder.fromByteArray(cursor.getBlob(0));
                list.addReminder(r);
            } while (cursor.moveToNext());
        }

        list.sortListByType();
        list.checkDates();

        cursor.close();
        db.close();
        return list;
    }

    public boolean hasList(String name) {
        boolean ans;
        String query = "SELECT " + column_LIST_NAME + " FROM " + REMINDER_LIST + " WHERE " + column_LIST_NAME +
                " IS '" + name + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        ans = cursor.getCount() != 0;

        cursor.close();
        db.close();
        return ans;
    }

    public boolean hasReminder(String name, String listName) {
        boolean ans = false;
        String query = "SELECT " + column_REMINDER_NAME + " FROM " + REMINDERS + " WHERE " + column_REMINDER_LIST_NAME +
                " IS '" + listName + "' AND " + column_REMINDER_NAME + " IS '" + name + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() == 1)
            ans = true;

        cursor.close();
        db.close();
        return ans;
    }

    public boolean isListChecked(String name) {
        String query = "SELECT " + column_LIST_CHECKED + " FROM " + REMINDER_LIST + " WHERE " + column_LIST_NAME +
                " IS '" + name + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        boolean checked = cursor.getInt(0) != 0;

        cursor.close();
        db.close();

        return checked;
    }

    public boolean setListChecked(String name, boolean setChecked) {
        ContentValues cv = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        cv.put(column_LIST_CHECKED, setChecked);
        long update = db.update(REMINDER_LIST, cv, column_LIST_NAME + " = '" + name + "'", null);

        db.close();
        return update != -1;
    }

    public boolean setReminderChecked(String name, String listName, boolean setChecked) {
        SQLiteDatabase db = getWritableDatabase();

        String query = "SELECT " + column_REMINDER_OBJECT + " FROM " + REMINDERS + " WHERE " + column_REMINDER_LIST_NAME +
                " IS '" + listName + "' AND " + column_REMINDER_NAME + " IS '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Reminder r = Reminder.fromByteArray(cursor.getBlob(0));
        r.setCheck(setChecked);

        ContentValues cv = new ContentValues();
        cv.put(column_REMINDER_CHECKED, setChecked);
        cv.put(column_REMINDER_OBJECT, Reminder.toByteArray(r));
        long update = db.update(REMINDERS, cv, column_REMINDER_NAME + " = '" + name + "' AND " +
                column_REMINDER_LIST_NAME + " = '" + listName + "'", null);

        cursor.close();
        db.close();
        if (update == -1)
            return false;
        return true;
    }

    public boolean deleteList(String name) {
        SQLiteDatabase db = getWritableDatabase();
        boolean deleted = false;

        long delete = db.delete(REMINDER_LIST, column_LIST_NAME + " IS '" + name + "'", null);
        if (delete != -1)
            deleted = true;

        delete = db.delete(REMINDERS, column_REMINDER_LIST_NAME + " IS '" + name + "'", null);
        if (delete == -1)
            deleted = false;

        db.close();
        return deleted;
    }

    public boolean deleteReminder(String name, String listName) {
        SQLiteDatabase db = getWritableDatabase();

        long delete = db.delete(REMINDERS, column_REMINDER_NAME + " IS '" + name + "' AND " +
                column_REMINDER_LIST_NAME + " IS '" + listName + "'", null);

        db.close();
        return delete != -1;
    }

    public String[] getReminderNames() {
        String query = "SELECT " + column_REMINDER_NAME + " FROM " + REMINDERS;
        LinkedList<String> results = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                if (!results.contains(name))
                    results.add(name);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return results.toArray(new String[0]);
    }

    public String[] getTypeNames() {
        String query = "SELECT " + column_REMINDER_TYPE + " FROM " + REMINDERS;
        LinkedList<String> results = new LinkedList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                if (!results.contains(name))
                    results.add(name);
            } while (cursor.moveToNext());
        } else {

        }

        cursor.close();
        db.close();
        return results.toArray(new String[0]);
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("delete from " + REMINDERS);
        db.execSQL("delete from " + REMINDER_LIST);
        db.close();
    }

    public int getReminderId (String name, String listName) {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT ID FROM " + REMINDERS + " WHERE " + column_REMINDER_LIST_NAME + " IS '" +
                listName + "' AND " + column_REMINDER_NAME + " IS '" + name + "'";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() < 1)
            return -1;
        cursor.moveToFirst();
        int id = cursor.getInt(0);

        cursor.close();
        db.close();
        return id;
    }
}
