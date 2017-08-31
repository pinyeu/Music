package com.rikkeisoft.music.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrHome;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    FragmentManager fragmentManager = getSupportFragmentManager();
    public static MediaPlayer mediaPlayer = new MediaPlayer();
    private ImageView imageDisc, imageCover;
    byte[] dataImageDisc;
    android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();

    private static final int LEVEL_P = 1;
    private static final int LEVEL = 0;
    private int levelPlay = 1;
    private int levelRepeat = 0;
    private int levelShuffle = 0;

    public static ArrayList<Song> mList = new ArrayList<>();
    public static int mIndex = 0;

    ImageButton btn_repeat, btn_shuffle, btnPausePS, btnNext, btnPre;
    private TextView tvTitle, tvArtist, txtDuration, txtNameSongInPS, txtArstistInPS;
    private FloatingActionButton floatingActionButton;
    private SeekBar seekBar;
    private double timeElapsed = 0, finalTime = 0;
    private Handler durationHandler = new Handler();
    public static SlidingUpPanelLayout mLayoutPlay;
    public static RelativeLayout layoutPlayBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        addEvents();
    }

    private Runnable updateTimeSeekbar = new Runnable() {
        @Override
        public void run() {
            doUpdateTimeSeekbar();
        }
    };

    private void initView() {
        //Main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrHome frHome = new FrHome();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, frHome)
                .commit();
        //Siding layout Play
        layoutPlayBottom = (RelativeLayout) findViewById(R.id.LayoutPlayBottom);
        mLayoutPlay = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.fabPlay);
        tvArtist = (TextView) findViewById(R.id.txtArtist);
        tvTitle = (TextView) findViewById(R.id.txtTitle);
        txtDuration = (TextView) findViewById(R.id.txTimeDuration);
        btn_repeat = (ImageButton) findViewById(R.id.btn_repeat);
        btn_shuffle = (ImageButton) findViewById(R.id.btn_shuffle);
        btnNext = (ImageButton) findViewById(R.id.ButtonNext);
        btnPre = (ImageButton) findViewById(R.id.ButtonPre);
        imageDisc = (ImageView) findViewById(R.id.imageDisc);
        imageCover = (ImageView) findViewById(R.id.ImageCover);
        /// Panel State
        txtArstistInPS = (TextView) findViewById(R.id.textNameOfArtistPS);
        txtNameSongInPS = (TextView) findViewById(R.id.textNameOfSongPS);
        btnPausePS = (ImageButton) findViewById(R.id.btnPausePS);
    }

    private void addEvents() {

        mLayoutPlay.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, slideoffset " + slideOffset);
            }
            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
                if (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                } else if (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING) {
                    // update seekbar
                    updateViewPlay();
                    //update button play/pause
                    if (mediaPlayer.isPlaying()) {
                        floatingActionButton.setImageLevel(LEVEL);
                        btnPausePS.setImageLevel(LEVEL);
                        levelPlay = LEVEL_P;
                    }
                } else {
//                    layoutPlayBottom.setVisibility(View.GONE);
                }
            }
        });
        mLayoutPlay.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        //float btn play/pause
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewButtonPlay();
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
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (seekBar.getProgress() == seekBar.getMax() && !mediaPlayer.isLooping()) {
                    floatingActionButton.setImageLevel(LEVEL_P);
                    btnPausePS.setImageLevel(LEVEL_P);
                    levelPlay = LEVEL;
                    doNextSong();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
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
        durationHandler.postDelayed(updateTimeSeekbar,100);
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

    private void doPreSong() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateViewPlay();
        viewButtonPlay();
    }

    private void doNextSong() {
        try {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        updateViewPlay();
        viewButtonPlay();
    }

    private void updateViewPlay() {
        //seek bar
        finalTime = mediaPlayer.getDuration();
        seekBar.setMax((int) finalTime);
        timeElapsed = mediaPlayer.getCurrentPosition();
        seekBar.setProgress((int) timeElapsed);
        durationHandler.postDelayed(updateTimeSeekbar, 100);
        //update Textview Title, artist
        tvTitle.setText(mList.get(mIndex).getName());
        tvArtist.setText(mList.get(mIndex).getArtist());
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

    }

    private void viewButtonPlay() {
        if (levelPlay == LEVEL) {
            floatingActionButton.setImageLevel(LEVEL);
            btnPausePS.setImageLevel(LEVEL);
            mediaPlayer.start();
            levelPlay = LEVEL_P;
        } else {
            floatingActionButton.setImageLevel(LEVEL_P);
            btnPausePS.setImageLevel(LEVEL_P);
            mediaPlayer.pause();
            levelPlay = LEVEL;
        }
    }

    @Override
    public void onBackPressed() {
        if (mLayoutPlay != null &&
                (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            layoutPlayBottom.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    public void playSong(int i) throws IllegalArgumentException,
            IllegalStateException, IOException {
        mIndex = i;
        levelPlay = LEVEL;
        Log.d("playsong", ": " + i);
        mediaPlayer.reset();
        mediaPlayer.setDataSource(mList.get(mIndex).getPath());
        mediaPlayer.prepare();
        mediaPlayer.start();
    }

    @Override
    protected void onResume() {
        if (mediaPlayer.isPlaying()) {
            updateViewPlay();
            viewButtonPlay();
        }
        super.onResume();
    }
}
