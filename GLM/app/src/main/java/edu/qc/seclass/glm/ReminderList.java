package edu.qc.seclass.glm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

public class ReminderList extends QuickSort implements Serializable {
    private LinkedList<Reminder> reminderList;
    private String listName;
    private boolean listChecked = false;

    public ReminderList(String name) {
        listName = name;
        reminderList = new LinkedList<>();
    }

    public String getName() {
        return listName;
    }

    public void setName(String n) {
        listName = n;
    }

    public void addReminder(Reminder r) {
        reminderList.add(r);
    }

    public boolean deleteReminder(String name) {
        for (int i = 0; i < reminderList.size(); i++) {
            if (reminderList.get(i).getName().equals(name)) {
                reminderList.remove(i);
                return true;
            }
        }
        return false;
    }

    public Reminder getReminder(int index) {
        return reminderList.get(index);
    }

    public boolean getCheck () {
        return listChecked;
    }

    public void setCheck(boolean check) {
        listChecked = check;
    }

    public int getSize() {
        return reminderList.size();
    }

    public void updateReminder (Reminder oldR, Reminder newR) {
        for (int i = 0; i < reminderList.size(); i++) {
            if (reminderList.get(i).getName().equals(oldR.getName())) {
                reminderList.add(i + 1, newR);
                reminderList.remove(i);
                break;
            }
        }
    }

    public static byte[] toByteArray(ReminderList list) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(list);
            out.flush();
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ReminderList fromByteArray(byte[] bytes) {
        if(bytes == null)
            return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(inputStream);
            ReminderList list = (ReminderList) in.readObject();
            return list;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void sortListByType(){
        //Converts Linked List to Array by casting each object in the object array to Reminder
        Object[] objectArr = reminderList.toArray();
        Reminder[] reminderArray = new Reminder[objectArr.length];
        for(int i = 0; i < objectArr.length; i++){
            reminderArray[i] = (Reminder) objectArr[i];
        }
        //Uses Quicksort to sort Array
        quickSort(reminderArray, 0, reminderArray.length - 1);
        //Stores Sorted Array into ReminderList (Linked List)
        reminderList = new LinkedList<>(Arrays.asList(reminderArray));
    }

    public void checkDates(){
        //Gets Today's Date
        Calendar cal = Calendar.getInstance();
        String todayDate = ((cal.get(Calendar.MONTH)+1) + "/" + cal.get(Calendar.DAY_OF_MONTH)  + "/" + cal.get(Calendar.YEAR));
        //Gets Today's Time
        int hours = cal.get(Calendar.HOUR), minutes = cal.get(Calendar.MINUTE);
        if(cal.get(Calendar.AM_PM) == Calendar.PM) hours+=12;
        SimpleDateFormat format24Hours = new SimpleDateFormat("hh:mm");
        Date date = null;
        try {
            date = format24Hours.parse(hours+":"+minutes);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat format12Hours = new SimpleDateFormat("hh:mm aa");
        String todayTime = format12Hours.format(date);
        for(Reminder r : reminderList){
            if(todayDate.compareTo(r.getDate()) == 0 && !r.getTime().equals("")){
                if(todayTime.compareTo(r.getTime()) >= 0){
                    r.updateDate();
                }
            }
            else if(todayDate.compareTo(r.getDate()) >= 0){
                r.updateDate();
            }
        }
    }
}