package edu.qc.seclass.glm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ReminderBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "reminderNotify");
        builder.setSmallIcon(R.mipmap.ic_launcher);

        Reminder r = Reminder.fromByteArray((byte[])intent.getExtras().get("Reminder"));
        String listName = (String) intent.getExtras().get("List");
        DBHelper dbHelper = new DBHelper(context);
        int id = dbHelper.getReminderId(r.getName(), listName);

        builder.setContentTitle(r.getType() + ": " + r.getName());
        builder.setContentText("Will begin soon");
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(id, builder.build());
    }
}
