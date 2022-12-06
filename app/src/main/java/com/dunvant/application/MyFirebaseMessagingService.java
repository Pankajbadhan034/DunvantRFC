package com.dunvant.application;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.dunvant.application.child.ChildMainScreen;
import com.dunvant.application.coach.CoachMainScreen;
import com.dunvant.application.parent.ParentMainScreen;
import com.dunvant.application.utils.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import androidx.core.app.NotificationCompat;
public class MyFirebaseMessagingService  extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    String body, userType, title;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map<String, String> data = remoteMessage.getData();
        Log.d(TAG, "GOT DATA : "+data);

        body = data.get("body");
        title = data.get("title");
        userType = data.get("usertype");

        if (remoteMessage.getData().size() == 0) {
            sendNotification(body);
        } else {
            sendNotification(title, body, data.get("notification_data"));
        }

//        showNotification(body, userType, remoteMessage);

        // Turn on the screen for notification
        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        boolean result= Build.VERSION.SDK_INT>= Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isInteractive()|| Build.VERSION.SDK_INT< Build.VERSION_CODES.KITKAT_WATCH&&powerManager.isScreenOn();

        if (!result){
            PowerManager.WakeLock wl = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |PowerManager.ACQUIRE_CAUSES_WAKEUP |PowerManager.ON_AFTER_RELEASE,"MH24_SCREENLOCK");
            wl.acquire(10000);
            PowerManager.WakeLock wl_cpu = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"MH24_SCREENLOCK");
            wl_cpu.acquire(10000);
        }

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(Utilities.getNotificationIcon())
                .setColor(getResources().getColor(R.color.colorPrimary))
                .setContentTitle("Ospreys")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification(String title, String messageToBeShown, String jsonData) {

        Class targetClass = null;

        try {
            JSONObject jsonObject = new JSONObject(jsonData);

            JSONObject notificationObject = jsonObject.getJSONObject("notification");

            String clickAction = notificationObject.getString("click_action");

            switch (clickAction) {
                case "PARENT_NOTIFICATION":
                    targetClass = ParentMainScreen.class;
                    break;
                case "CHILD_NOTIFICATION":
                    targetClass = ChildMainScreen.class;
                    break;
                case "COACH_NOTIFICATION":
                    targetClass = CoachMainScreen.class;
                    break;
                default:
                    targetClass = SplashScreen.class;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MyFirebaseMessagingService.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        Intent intent = new Intent(this, targetClass);
        intent.putExtra("notification_data", jsonData);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"101")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(Utilities.getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageToBeShown)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.BLUE, 200, 200)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setContentIntent(pendingIntent);

//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        notificationManager.notify(0, notificationBuilder.build());

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        createChannel(notificationManager);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    public void createChannel(NotificationManager notificationManager){
        if (Build.VERSION.SDK_INT < 26) {
            return;
        }
        NotificationChannel channel = new NotificationChannel("101","name", NotificationManager.IMPORTANCE_HIGH);
        channel.setDescription("Description");
        notificationManager.createNotificationChannel(channel);
    }

    public void showNotification(String description, String userType, RemoteMessage remoteMessage){
        System.out.println("TypeUser::"+userType);

        Intent intent;

//        if(userType.equalsIgnoreCase("patient")){
        intent = new Intent(this, SplashScreen.class);
//        }else if(userType.equalsIgnoreCase("provider")) {
//
//            intent = new Intent(this, SplashScreen.class);
//        }else{
//            intent = new Intent(this, SplashScreen.class);
//        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"101")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon))
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle("Ospreys")
                .setContentText(description)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setLights(Color.BLUE, 200, 200)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND | Notification.FLAG_SHOW_LIGHTS)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int notificationId = 1;
        createChannel(notificationManager);
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}