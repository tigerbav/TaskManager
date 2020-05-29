package ua.bozhko.taskmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.core.app.NotificationCompat;

import ua.bozhko.taskmanager.WorkingSpace.MainActivity;



public class Receiver extends BroadcastReceiver {//широковещательный приемник) да, я такой
//работает даже после перезагрузки в отличии от Service
    @Override
    public void onReceive(Context context, Intent intent) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.NOTIFICATION_MAIN_TEXT, intent.getStringExtra(Constants.GLOBAL_LIST));
        editor.putString(Constants.MAIN_LIST, intent.getStringExtra(Constants.MAIN_LIST));
        editor.putString(Constants.FROM, intent.getStringExtra(Constants.FROM));
        editor.putString(Constants.TO, intent.getStringExtra(Constants.TO));
        editor.apply();


        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.notification_icon, options);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent i2 = new Intent(context, MainActivity.class);
        i2.putExtra(Constants.NOTIFICATION_MAIN_TEXT, intent.getStringExtra(Constants.GLOBAL_LIST));
        i2.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent pi = PendingIntent.getActivity(context, 100, i2, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = null;
        NotificationChannel mChannel = null;

        String message = "Go to the next Task";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new NotificationCompat.Builder(context, "MyChannel")
                    .setWhen(System.currentTimeMillis());
            mChannel = new NotificationChannel("MyChannel", context.getString(R.string.app_name).toUpperCase(), NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel.
            mChannel.setDescription(message);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.setVibrationPattern(new long[]{500,500,500,500,500,500,500,500,500});
            notificationManager.createNotificationChannel(mChannel);
        } else {
            builder = new NotificationCompat.Builder(context, null)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
        }
        builder.setContentIntent(pi)
                .setSmallIcon(R.drawable.notification_icon)
                .setLargeIcon(bitmap)
                .setVibrate(new long[] { 500,500,500,500,500,500,500,500,500})
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setAutoCancel(true)
        .setStyle(new NotificationCompat.InboxStyle()
                .addLine(message)
                .addLine("Let`s go to the task \"" + intent.getStringExtra(Constants.MAIN_LIST) + "\""));
        notificationManager.notify(100, builder.build());
    }
}