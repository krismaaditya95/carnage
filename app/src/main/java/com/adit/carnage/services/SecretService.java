package com.adit.carnage.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.adit.carnage.R;
import com.adit.carnage.classes.Camera2Utility;

public class SecretService extends Service {

    public static final String CHANNEL_ID = "00";
    public static final String CHANNEL_NAME = "myChannel";
    private Camera2Utility camera2Utility;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        startService();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext(), "Service Started", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        camera2Utility.stopRecording();
    }

    private Handler recordHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            return true;
        }
    });

    private Runnable recordingRun = new Runnable() {
        @Override
        public void run() {
            doRecording();
        }
    };

    private void removeCallback(){
        recordHandler.removeCallbacks(recordingRun);
    }

    private void startService(){
        recordHandler.removeCallbacks(recordingRun);
        recordHandler.post(recordingRun);
        camera2Utility = new Camera2Utility(this);
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//
//        }
        createChannelId();
    }

    private void createChannelId(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "channel0";
            String desc = "channel0";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(desc);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        showNotification();
    }

    private void showNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.carnage_logo)
                .setContentTitle("Service is started")
                .setContentText("Camera is recording...")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Camera is recording..."))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(0, builder.build());

    }

    private void doRecording(){
        // start recording shit
        camera2Utility.prepareRecording();
        camera2Utility.startRecording();
    }
}
