package edu.qc.seclass.glm;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Calendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Reminder implements Serializable {
    private String reminderName;
    private String reminderType;
    private String reminderDescription;
    private String reminderDate;
    private String reminderTime;
    private Calendar calendar = null;
    private boolean reminderChecked = false;
    private int reminderRepeats;

    public Reminder (String name, String type) {
        reminderName = name;
        reminderType = type;
    }

    public String getName() {
        return reminderName;
    }

    public void setName(String n) {
        reminderName = n;
    }

    public String getType() {
        return reminderType;
    }

    public void setType(String t) {
        reminderType = t;
    }

    public String getDescription() {
        return reminderDescription;
    }

    public void setDescription(String description) {
        reminderDescription = description;
    }

    public boolean getCheck () {
        return reminderChecked;
    }

    public void setCheck(boolean check) {
        reminderChecked = check;
    }

    public void setDateTime(String date, String time) {
        reminderDate = date;
        reminderTime = time;

        if (!date.equals("") && !time.equals("")) {
            Calendar c = Calendar.getInstance();

            int firstSlash = reminderDate.indexOf('/');
            int secondSlash = reminderDate.indexOf('/', firstSlash+1);
            c.set(Calendar.MONTH, Integer.parseInt(reminderDate.substring(0, firstSlash))-1);
            c.set(Calendar.DAY_OF_MONTH, Integer.parseInt(reminderDate.substring(firstSlash+1, secondSlash)));
            c.set(Calendar.YEAR, Integer.parseInt(reminderDate.substring(secondSlash+1)));

            int colon = reminderTime.indexOf(':');
            int space = reminderTime.indexOf(' ');
            int hour = Integer.parseInt(reminderTime.substring(0, colon));
            if (reminderTime.substring(space+1).equals("PM"))
                hour += 12;
            c.set(Calendar.HOUR_OF_DAY, hour);
            c.set(Calendar.MINUTE, Integer.parseInt(reminderTime.substring(colon+1, space)));
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            calendar = c;
        }

    }

    public String getDate(){
        return reminderDate;
    }

    public String getTime(){
        return reminderTime;
    }

    public void setRepeat(int repeat){
        reminderRepeats = repeat;
    }

    public int getRepeat(){
        return reminderRepeats;
    }

    public String getRepeatString(){
        String repeatString = "";
        switch(reminderRepeats) {
            case 0: repeatString = "None";
                break;
            case 1: repeatString = "Daily";
                break;
            case 2: repeatString = "Weekly";
                break;
            case 3: repeatString = "Monthly";
                break;
        }
        return repeatString;
    }

    public static byte[] toByteArray(Reminder reminder) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(outputStream);
            out.writeObject(reminder);
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

    public static Reminder fromByteArray(byte[] bytes) {
        if(bytes == null)
            return null;

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(inputStream);
            Reminder reminder = (Reminder) in.readObject();
            return reminder;
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

    public void updateDate(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        try {
            cal.setTime(dateFormat.parse(reminderDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String newDate = "";
        switch(reminderRepeats) {
            case 0: break;
            case 1: cal.add(Calendar.DAY_OF_MONTH, 1);
                    newDate = dateFormat.format(cal.getTime());
                    setDateTime(newDate, reminderTime);
                    break;
            case 2: cal.add(Calendar.DAY_OF_MONTH, 7);
                    newDate = dateFormat.format(cal.getTime());
                    setDateTime(newDate, reminderTime);
                    break;
            case 3: cal.add(Calendar.MONTH, 1);
                    newDate = dateFormat.format(cal.getTime());
                    setDateTime(newDate, reminderTime);
                    break;
        }
    }

    @Override
    public String toString() {
        return "Reminder{" +
                "reminderName='" + reminderName + '\'' +
                ", reminderType='" + reminderType + '\'' +
                '}';
    }

    public long getUTC() {
        if (calendar == null)
            return 0;
        return calendar.getTimeInMillis();
    }

    public java.util.Date getDateTime() {
        if (calendar == null)
            return null;
        return calendar.getTime();
    }
}