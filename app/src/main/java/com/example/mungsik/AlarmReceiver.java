package com.example.mungsik;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;
    NotificationChannel channel;

    @Override
    public void onReceive(Context context, Intent intent) {
        BlueToothController mBluetoothController = BlueToothController.getBluetoothController();

        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        channel = new NotificationChannel("default", "alarm channel", NotificationManager.IMPORTANCE_HIGH);

        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }

        builder.setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setContentTitle("Feeding Time!!")
                .setContentText("Pet Feeding Time Alert!!");

        if ( mBluetoothController.isConnected() ){
            mBluetoothController.execute();
        }

        if (notificationManager != null) {
            notificationManager.notify(1234, builder.build());
        }
    }
}