package com.loetech.camerabeautify.camfilter;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.IBinder;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.loetech.camerabeautify.R;

import java.util.Objects;

import static android.content.ContentValues.TAG;

public class MyService extends Service {





    private MediaProjection mMediaProjection;
    private MediaProjectionManager mMediaProjectionManager;
    private int mResultCode;


    private static final String CHANNEL_ID = "MyNotificationChannelID";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        createNotificationChannel();

        //mResultCode = intent.getIntExtra("code", -1);
        //Parcelable   mResultData = intent.getParcelableExtra("data");
        //mResultData = intent.getSelector();

       // mMediaProjection = mMediaProjectionManager.getMediaProjection(mResultCode, (Intent) Objects.requireNonNull(mResultData));
        //mMediaProjection =  ((MediaProjectionManager) Objects.requireNonNull(getSystemService(Context.MEDIA_PROJECTION_SERVICE))).getMediaProjection(mResultCode, (Intent) mResultData);
        Log.e(TAG, "mMediaProjection created: " + mMediaProjection);

        return super.onStartCommand(intent, flags, startId);
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}