package com.um.safesip.member4;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.ArrayList;
import java.util.List;

public class NotificationHelper {
    public static final String CHANNEL_ID = "safesip_repairs_channel";
    private static final List<NotificationRecord> historyStore = new ArrayList<>();
    private static int notificationCounter = 1000;

    public static void createChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Water Station Repair Alerts",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Alerts when a reported UM water station is serviced or repaired");
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    public static NotificationRecord triggerStationRepairedAlert(Context context,
                                                                 String reportId,
                                                                 String stationName,
                                                                 String issueCategory) {
        createChannel(context);

        String title = "Station Serviced: " + stationName;
        String body = "Your report (" + issueCategory + ") is now marked as repaired.";
        long now = System.currentTimeMillis();

        NotificationRecord record = new NotificationRecord(reportId, stationName, title, body, now);
        historyStore.add(0, record);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                        == PackageManager.PERMISSION_GRANTED) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(context).notify(notificationCounter++, builder.build());
        }

        return record;
    }

    public static List<NotificationRecord> getHistory() {
        return new ArrayList<>(historyStore);
    }
}