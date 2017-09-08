package com.rikkeisoft.music.Activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
import com.rikkeisoft.music.Activity.FragmentTabLayout.FrAlbums;
import com.rikkeisoft.music.Adapter.ListviewSongAdapter;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;

public class AlbumArtist_Activity extends AppCompatActivity {

    private static final String TAG = "AlbumActivity";
    private Toolbar toolbar;
    private ImageView imageAlbum;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayout lineDataNull;
    private ProgressBar progressBar;
    private DatabaseHandler db;
    private String nameTitle = "toolbar", path;

    private ArrayList<Song> mList = new ArrayList();
    private ListView lv;
    private ListviewSongAdapter adapter;
    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    private int mIndex; // 0 = album, 1 = artist
    private FragmentManager fragmentManager = getSupportFragmentManager();
    public static SlidingUpPanelLayout mLayoutPlay;
    FrPlayMusic frPlayMusic = new FrPlayMusic();
    private boolean checkPlayListofAblbum = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_artist);
        db = new DatabaseHandler(this);
        initView();
        addEvents();
    }

    private void addEvents() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (checkPlayListofAblbum == false) {
                    FrPlayMusic.mList = mList;
                    checkPlayListofAblbum = true;
                }
                frPlayMusic.playSong(position);
                getSupportFragmentManager().beginTransaction().detach(frPlayMusic).attach(frPlayMusic).commit();
                mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
            }
        });
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
//                    // update seekbar
//                    updateViewPlay();
//                    //update button play/pause
//                    if (mediaPlayer.isPlaying()) {
//                        floatingActionButton.setImageLevel(LEVEL);
//                        btnPausePS.setImageLevel(LEVEL);
//                        levelPlay = LEVEL_P;
//                    }
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

    private void initView() {
        fragmentManager.beginTransaction()
                .add(R.id.content_Play, frPlayMusic)
                .commit();
        //Siding layout Play
        mLayoutPlay = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        if (FrPlayMusic.mediaPlayer.isPlaying() == false) {
            mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
        Intent intent = getIntent();
        if (intent.hasExtra("ALBUM")) {
            mIndex = 0;
            Bundle bundle = intent.getBundleExtra("ALBUM");
            nameTitle = bundle.getString("ALBUM_NAME");
            path = bundle.getString("ALBUM_PATH");
        } else if (intent.hasExtra("ARTIST")) {
            mIndex = 1;
            Bundle bundle = intent.getBundleExtra("ARTIST");
            nameTitle = bundle.getString("ARTIST_NAME");
            path = bundle.getString("ARTIST_PATH");
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(nameTitle);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(nameTitle);
        imageAlbum = (ImageView) findViewById(R.id.ImageThumnail);
        lv = (ListView) findViewById(R.id.listViewSongs);
        lineDataNull = (LinearLayout) findViewById(R.id.lineDataNull);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    class loadListSongsOfAlbum extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            mList.clear();
            if (mIndex == 0) {
                mList.addAll(db.getListSongsOfAlbum(params[0]));
//                FrPlayMusic.mList = mList;
            } else {
                mList.addAll(db.getListSongsOfArtist(params[0]));
//                FrPlayMusic.mList = mList;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            //
            if (mList.size() != 0) {
                mmr.setDataSource(path);
                byte[] dataImageDisc = mmr.getEmbeddedPicture();
                if (dataImageDisc != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
                    imageAlbum.setImageBitmap(bitmap);
                } else {
                    imageAlbum.setImageResource(R.drawable.disc_default);
                }
                adapter = new ListviewSongAdapter(AlbumArtist_Activity.this, mList);
                lv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                lv.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                lineDataNull.setVisibility(View.GONE);
            } else {
                lineDataNull.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                lv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onResume() {
        new loadListSongsOfAlbum().execute(nameTitle);
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        if (checkPlayListofAblbum == true) {
            FrPlayMusic.mList = mList;
        }
        setResult(FrAlbums.OPEN_ALBUM);
        super.finish();
    }

    @Override
    public void onBackPressed() {
        if (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        } else {
            super.onBackPressed();
        }
    }
}
