package net.youtoolife.myfcmexample;


import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

/**
 * Created by youtoolife on 4/7/18.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {


    private static final int NOTIFY_ID = 101998;

    public static final  String TOKEN_BROADCAST = "fcmTB";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d("__notification: ", "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d("__notificationData: ", "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d("__notificationDataBody:", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            Context context = getApplicationContext();

            boolean myActivityActive = false;
            ActivityManager am = (ActivityManager) getApplicationContext() .getSystemService(Context.ACTIVITY_SERVICE);

            List<ActivityManager.RunningTaskInfo> alltasks = am.getRunningTasks(1);
            for (ActivityManager.RunningTaskInfo task : alltasks) {
                Log.d("NotTaskU", task.topActivity.getClassName());
                if(task.topActivity.getClassName().equals(ContActivity.class.getClass().getSimpleName())) {
                        Log.d("task", "true");
                    myActivityActive = true;
                    break;
                }

            }


            if (myActivityActive) {

            } else {

                Intent notificationIntent = new Intent(context, ContActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent contentIntent = PendingIntent.getActivity(context,
                        0, notificationIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);

                // до версии Android 8.0 API 26
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

                long[] vibrate = new long[]{1000, 100, 500};
                builder.setContentIntent(contentIntent)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        // обязательные настройки
                        //.setFullScreenIntent(contentIntent, true)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setContentTitle(remoteMessage.getNotification().getTitle()) // Заголовок уведомления
                        .setContentText(remoteMessage.getNotification().getBody()) // Текст уведомления
                        // необязательные настройки
                        ///.setStyle(new NotificationCompat.BigTextStyle().bigText("itsBIG:\n"+remoteMessage.getNotification().getBody()))
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round)) // большая
                        // картинка
                        //.setTicker(res.getString(R.string.warning)) // текст в строке состояния
                        //.setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setTicker("ticker: " + remoteMessage.getNotification().getBody())
                        .setWhen(System.currentTimeMillis())
                        .setVibrate(vibrate)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                        .setVisibility(Notification.VISIBILITY_PUBLIC)
                        .addAction(R.mipmap.ic_launcher, "Открыть", contentIntent)
                        .setCategory(NotificationCompat.CATEGORY_STATUS)

                        .setAutoCancel(true);


                NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
                for (String s:remoteMessage.getNotification().getBody().split("\\n")) {
                    style.addLine(s);
                }
                style.setSummaryText("сообщений: "+String.valueOf(remoteMessage.getNotification().getBody().split("\n").length));
                builder.setStyle(style);

                NotificationManager notificationManager =
                        (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                // Альтернативный вариант
                // NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);


                notificationManager.notify(NOTIFY_ID, builder.build());

            /*NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.mipmap.ic_launcher_round)
                            .setContentTitle(remoteMessage.getNotification().getTitle())
                            .setContentText(remoteMessage.getNotification().getBody());

            Intent resultIntent = new Intent(context, ResultActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(ResultActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
            mBuilder.setContentIntent(resultPendingIntent);

            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            mNotificationManager.notify(0,mBuilder.build());*/
            }
            getApplicationContext().sendBroadcast(new Intent(TOKEN_BROADCAST));
        }

    }
}
