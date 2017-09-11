package com.rikkeisoft.music.Activity.FragmentMain;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentTabLayout.FrAlbums;
import com.rikkeisoft.music.Activity.HomeActivity;
import com.rikkeisoft.music.Activity.PlayingQueue;
import com.rikkeisoft.music.Adapter.ListviewSongAdapter;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import me.tankery.lib.circularseekbar.CircularSeekBar;

import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by nguyenquanghung on 9/1/17.
 */

public class FrPlayMusic extends Fragment {
    View view;
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private static final int LEVEL_P = 1;
    private static final int LEVEL = 0;
    private int levelPlay = 1;
    private int levelRepeat = 0;
    private int levelShuffle = 0;
    byte[] dataImageDisc;
    android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    public static ArrayList<Song> mList = new ArrayList<>();
    private ArrayList<Song> mlistsong;
    public static int mIndex = 0;

    private ListView listViewSongs;
    private ListviewSongAdapter adapter;

    private ImageView imageCover;
    ImageButton btn_repeat, btn_shuffle, btnPausePS,btnPause, btnNext, btnPre;
    private TextView txtDuration, txtNameSongInPS, txtArstistInPS;
    private FloatingActionButton floatingActionButtonSort;
    private CircularSeekBar seekBar;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();

    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private RemoteViews remoteViews;
    private PendingIntent pendingIntent;
    public final static String NOTIF_PRE = "PRE";
    public final static String NOTIF_NEXT = "NEXT";
    public final static String NOTIF_PAUSE = "PAUSE";

    public final static int OPEN_PLAYINGQUEUE = 101;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_play_siding, container, false);
        initView();
        addEvents();
        return view;
    }

    private Runnable updateTimeSeekbar = new Runnable() {
        @Override
        public void run() {
            doUpdateTimeSeekbar();
        }
    };

    private void initView() {
        mlistsong = new ArrayList<>();
        mlistsong.addAll(mList);
        listViewSongs = (ListView) view.findViewById(R.id.listViewSongs);
        adapter = new ListviewSongAdapter(getActivity(), mlistsong);
        listViewSongs.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        ///
        seekBar = (CircularSeekBar) view.findViewById(R.id.seekBar2);
        floatingActionButtonSort = (FloatingActionButton) view.findViewById(R.id.fabSort);
        txtDuration = (TextView) view.findViewById(R.id.txTimeDuration);
        btn_repeat = (ImageButton) view.findViewById(R.id.btn_repeat);
        btn_shuffle = (ImageButton) view.findViewById(R.id.btn_shuffle);
        btnNext = (ImageButton) view.findViewById(R.id.ButtonNext);
        btnPre = (ImageButton) view.findViewById(R.id.ButtonPre);
        btnPause = (ImageButton) view.findViewById(R.id.btn_pause);

        imageCover = (ImageView) view.findViewById(R.id.ImageCover);
        /// Panel State
        txtArstistInPS = (TextView) view.findViewById(R.id.textNameOfArtistPS);
        txtNameSongInPS = (TextView) view.findViewById(R.id.textNameOfSongPS);
        btnPausePS = (ImageButton) view.findViewById(R.id.btnPausePS);
        //notification
        notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        remoteViews = new RemoteViews(getActivity().getPackageName(), R.layout.notification);
        addEventsNotif();
        Intent resultIntent = new Intent(getActivity(), HomeActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getActivity());
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(HomeActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        pendingIntent = PendingIntent.getActivity(getActivity(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder = new NotificationCompat.Builder(getActivity());
        //play from activity album
        if (mlistsong.size() > 0) {
            updateViewPlay();
        }
    }

    private void addEventsNotif() {
        //btn Pre on Notif
        Intent btnpre_intent = new Intent(NOTIF_PRE);
        btnpre_intent.putExtra(NOTIF_PRE, mIndex);
        PendingIntent p_btnPre_Intent = PendingIntent.getBroadcast(getActivity(), 11, btnpre_intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btnPreNotif, p_btnPre_Intent);
        //btn next on Notif
        Intent btnnext_intent = new Intent(NOTIF_NEXT);
        btnnext_intent.putExtra(NOTIF_NEXT, mIndex);
        PendingIntent p_btnNext_Intent = PendingIntent.getBroadcast(getActivity(), 12, btnnext_intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btnNextNotif, p_btnNext_Intent);
        //btn pause on Notif
        Intent btnpause_intent = new Intent(NOTIF_PAUSE);
        btnpause_intent.putExtra(NOTIF_PAUSE, mIndex);
        PendingIntent p_btnPause_Intent = PendingIntent.getBroadcast(getActivity(), 13, btnpause_intent, 0);
        remoteViews.setOnClickPendingIntent(R.id.btnPauseNotif, p_btnPause_Intent);
    }

    private void doUpdateTimeSeekbar() {
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekBar.setProgress((int) timeElapsed);

        // set time remaing
        double timeRemaining = finalTime - timeElapsed;
        txtDuration.setText(String.format(
                "%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining),
                TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining)
                        - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS
                        .toMinutes((long) timeRemaining))));

        // repeat yourself that again in 100 miliseconds
        durationHandler.postDelayed(updateTimeSeekbar, 100);
        if(!mediaPlayer.isPlaying()){
            levelPlay = LEVEL_P;
            viewButtonPlay();
        }
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                doNextSong();
            }
        });
    }

    private void addEvents() {
        //click item listview musicplayer
        listViewSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                playSong(position);
            }
        });
        //float btn play/pause
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewButtonPlay();
            }
        });
        floatingActionButtonSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PlayingQueue.class);
                startActivityForResult(intent, OPEN_PLAYINGQUEUE);
            }
        });
        //btn pause in panel state
        btnPausePS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewButtonPlay();
            }
        });
        // Lap lai bai hat
        btn_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doRepeat();
            }
        });
        // Phat ngau nhien
        btn_shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doShuffle();
            }
        });
        // Next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doNextSong();
            }
        });
        //pre
        btnPre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doPreSong();
            }
        });
        seekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {

            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {
                mediaPlayer.seekTo((int) seekBar.getProgress());
            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });
    }

    private void doRepeat() {
        if (levelRepeat == LEVEL) {
            btn_repeat.setImageLevel(LEVEL_P);
            levelRepeat = LEVEL_P;
            mediaPlayer.setLooping(true);
        } else {
            btn_repeat.setImageLevel(LEVEL);
            levelRepeat = LEVEL;
            mediaPlayer.setLooping(false);
        }
    }

    private void doShuffle() {
        if (levelShuffle == LEVEL) {
            btn_shuffle.setImageLevel(LEVEL_P);
            levelShuffle = LEVEL_P;
        } else {
            btn_shuffle.setImageLevel(LEVEL);
            levelShuffle = LEVEL;
        }
    }

    public void doPreSong() {
        if (levelShuffle == LEVEL_P) {
            mIndex = new Random().nextInt(mList.size());
            playSong(mIndex);
        } else {
            if (mIndex != 0) {
                playSong(mIndex - 1);
            } else {
                mIndex = mList.size() - 1;
                playSong(mIndex);
            }
        }
    }

    public void doNextSong() {
        if (levelShuffle == LEVEL_P) {
            mIndex = new Random().nextInt(mList.size());
            playSong(mIndex);
        } else {
            if (mIndex < mList.size() - 1) {
                playSong(mIndex + 1);
            } else {
                mIndex = 0;
                playSong(mIndex);
            }
        }
    }

    private void updateNotification() {
        remoteViews.setImageViewResource(R.drawable.ic_album_black_24dp, R.drawable.ic_album_black_24dp);
        remoteViews.setTextViewText(R.id.textNameSong, mList.get(mIndex).getName() + "");
        remoteViews.setTextViewText(R.id.textNameArtist, mList.get(mIndex).getArtist() + "");
        builder.setSmallIcon(R.drawable.ic_album_black_24dp)
                .setCustomBigContentView(remoteViews)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());
    }

    public void updateViewPlay() {
        //seek bar
        finalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) finalTime);
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekBar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateTimeSeekbar, 100);
        //update Textview Title, artist
        txtNameSongInPS.setText(mList.get(mIndex).getName());
        txtArstistInPS.setText(mList.get(mIndex).getArtist());
        //update image coverArt
        mmr.setDataSource(mList.get(mIndex).getPath());
        dataImageDisc = mmr.getEmbeddedPicture();
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            imageCover.setImageBitmap(bitmap);
        } else {
            imageCover.setImageResource(R.drawable.disc_default);
        }
        adapter.notifyDataSetChanged();
        updateNotification();
    }

    private void viewButtonPlay() {
        if (levelPlay == LEVEL) {
            btnPause.setImageLevel(LEVEL);
            btnPausePS.setImageLevel(LEVEL);
            mediaPlayer.start();
            levelPlay = LEVEL_P;
        } else {
            btnPause.setImageLevel(LEVEL_P);
            btnPausePS.setImageLevel(LEVEL_P);
            mediaPlayer.pause();
            levelPlay = LEVEL;
        }
    }

    public void playSong(int i) {
        mIndex = i;
        levelPlay = LEVEL;
        Log.d("playsong", ": " + i);
        Log.d("playsong m", ": " + mList.size());
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(mList.get(mIndex).getPath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IllegalArgumentException iae) {
        } catch (IllegalStateException ise) {
        } catch (IOException ex) {
        }
        updateViewPlay();
        viewButtonPlay();
    }

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        if (mediaPlayer.isPlaying()) {
//            updateViewPlay();
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == OPEN_PLAYINGQUEUE) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }else if (requestCode== FrAlbums.OPEN_ALBUM){

        }
    }
}
