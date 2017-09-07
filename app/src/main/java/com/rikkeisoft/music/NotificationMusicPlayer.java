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
    FrPlayMusic frPlayMusic = new FrPlayMusic();
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
            if (FrPlayMusic.mIndex < FrPlayMusic.mList.size() - 1) {
//                frPlayMusic.playSong(FrPlayMusic.mIndex + 1);
            } else {
                FrPlayMusic.mIndex = 0;
//                frPlayMusic.playSong(FrPlayMusic.mIndex);
            }
            updateNotification(context);
        }
        //
        if (intent.hasExtra(NOTIF_PRE)) {
            if (FrPlayMusic.mIndex != 0) {
//                frPlayMusic.playSong(FrPlayMusic.mIndex - 1);
            } else {
                FrPlayMusic.mIndex = FrPlayMusic.mList.size() - 1;
//                frPlayMusic.playSong(FrPlayMusic.mIndex);
            }
            updateNotification(context);
        }
        //
        if (intent.hasExtra(NOTIF_PAUSE)) {
            Toast.makeText(context, "pause", Toast.LENGTH_SHORT).show();
            if(FrPlayMusic.mediaPlayer.isPlaying()){
                FrPlayMusic.mediaPlayer.pause();
            }else {
                FrPlayMusic.mediaPlayer.start();
            }
        }
    }
    public void updateNotification(Context context) {
        //notification
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(context.getPackageName(),R.layout.notification);
        addEventsNotif(context);
        Intent notification_intent = new Intent(context,HomeActivity.class);
        notification_intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(context,0,notification_intent,PendingIntent.FLAG_ONE_SHOT);
        builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ic_album_black_24dp)
                .setAutoCancel(true)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);
        remoteViews.setImageViewResource(R.drawable.ic_album_black_24dp,R.drawable.ic_album_black_24dp);
        remoteViews.setTextViewText(R.id.textNameSong,FrPlayMusic.mList.get(FrPlayMusic.mIndex).getName()+"");
        remoteViews.setTextViewText(R.id.textNameArtist,FrPlayMusic.mList.get(FrPlayMusic.mIndex).getArtist()+"");
        builder.setSmallIcon(R.drawable.ic_album_black_24dp)
                .setCustomBigContentView(remoteViews)
                .setContentIntent(pendingIntent);
        notificationManager.notify(1,builder.build());
    }
    private void addEventsNotif(Context context) {
        //btn Pre on Notif
        Intent btnpre_intent = new Intent(NOTIF_PRE);
        btnpre_intent.putExtra(NOTIF_PRE,1);
        PendingIntent p_btnPre_Intent = PendingIntent.getBroadcast(context,11,btnpre_intent,0);
        remoteViews.setOnClickPendingIntent(R.id.btnPreNotif,p_btnPre_Intent);
        //btn next on Notif
        Intent btnnext_intent = new Intent(NOTIF_NEXT);
        btnnext_intent.putExtra(NOTIF_NEXT,2);
        PendingIntent p_btnNext_Intent = PendingIntent.getBroadcast(context,12,btnnext_intent,0);
        remoteViews.setOnClickPendingIntent(R.id.btnNextNotif,p_btnNext_Intent);
        //btn pause on Notif
        Intent btnpause_intent = new Intent(NOTIF_PAUSE);
        btnpause_intent.putExtra(NOTIF_PAUSE,3);
        PendingIntent p_btnPause_Intent = PendingIntent.getBroadcast(context,13,btnpause_intent,0);
        remoteViews.setOnClickPendingIntent(R.id.btnPauseNotif,p_btnPause_Intent);
    }
}
