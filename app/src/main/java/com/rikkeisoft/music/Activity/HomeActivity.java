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
import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
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
    android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public static SlidingUpPanelLayout mLayoutPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();
        addEvents();
    }

    private void initView() {
        //Main
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FrHome frHome = new FrHome();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, frHome)
                .commit();
        FrPlayMusic frPlayMusic = new FrPlayMusic();
        fragmentManager.beginTransaction()
                .replace(R.id.content_Play, frPlayMusic)
                .commit();
        //Siding layout Play
        mLayoutPlay = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
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
                }
            }
        });
        mLayoutPlay.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {
        if(mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED){
            mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else {
            super.onBackPressed();
        }
    }
}
