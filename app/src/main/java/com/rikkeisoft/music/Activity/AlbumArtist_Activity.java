package com.rikkeisoft.music.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.rikkeisoft.music.Adapter.ListviewSongAdapter;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;

import java.util.ArrayList;

public class AlbumArtist_Activity extends AppCompatActivity {
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
    private int mIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_artist);
        db = new DatabaseHandler(this);
        initView();
        new loadListSongsOfAlbum().execute(nameTitle);
    }

    private void initView() {
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
            if (mIndex == 0) {
                mList.addAll(db.getListSongsOfAlbum(params[0]));
            } else {
                mList.addAll(db.getListSongsOfArtist(params[0]));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
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
}
