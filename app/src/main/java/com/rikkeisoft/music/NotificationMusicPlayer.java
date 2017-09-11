package com.rikkeisoft.music;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
import com.rikkeisoft.music.Activity.HomeActivity;

import java.io.IOException;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by nguyenquanghung on 8/31/17.
 */

public class NotificationMusicPlayer extends BroadcastReceiver {
    public final static String NOTIF_PRE = "PRE";
    public final static String NOTIF_NEXT = "NEXT";
    public final static String NOTIF_PAUSE = "PAUSE";

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;
    private PendingIntent pendingIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        manager.cancel(intent.getExtras().getInt("id"));
        //
        if (intent.hasExtra(NOTIF_NEXT)) {
        }
        //
        if (intent.hasExtra(NOTIF_PRE)) {
        }
        //
        if (intent.hasExtra(NOTIF_PAUSE)) {

        }
    }
}
