package com.rikkeisoft.music.Activity.FragmentTabLayout;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rikkeisoft.music.Adapter.ListviewSongAdapter;
import com.rikkeisoft.music.Activity.HomeActivity;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class FrSongs extends Fragment {
    View view;
    private ListviewSongAdapter adapter;
    private ListView lv;
    TextView textViewNoti;
    DatabaseHandler db;
    private Button btnReload;
    private ArrayList<Song> mListSong = new ArrayList<>();
    private int LOADDB = 0;
    private LinearLayout lineDataNull;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_songs, container, false);
        db = new DatabaseHandler(getActivity());
        initView();
        new loadListSongs().execute(LOADDB);
        addEvents();
        return view;
    }

    private void addEvents() {
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    HomeActivity homeActivity = new HomeActivity();
                    homeActivity.mIndex = position;
                    homeActivity.playSong(position);
                    HomeActivity.mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new loadListSongs().execute(LOADDB);
            }
        });
    }

    private void initView() {
        textViewNoti = (TextView) view.findViewById(R.id.txtDataNull);
        btnReload = (Button) view.findViewById(R.id.btnReloadSongs);
        lv = (ListView) view.findViewById(R.id.listViewSongs);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        lineDataNull = (LinearLayout) view.findViewById(R.id.lineDataNull);
        if (HomeActivity.mediaPlayer.isPlaying() == false) {
            HomeActivity.mLayoutPlay.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        }
    }

    public ArrayList<Song> findSongs() {
        ContentResolver musicResolver = getActivity().getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        final Cursor mCursor = musicResolver.query(uri,
                new String[]{MediaStore.Audio.AudioColumns.TITLE,
                        MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AlbumColumns.ARTIST, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.AudioColumns.ALBUM_ID}, selection, null, null);

        ArrayList<Song> mList = new ArrayList<Song>();
        try {
            if (mCursor.moveToFirst()) {
                do {
                    String name = mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
                    //// Lay ten bai hat////
                    String path = mCursor.getString(mCursor
                            .getColumnIndex(MediaStore.Audio.Media.DATA));
                    //// lay duong dan bai hat ////
                    String artist = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    // Nghe si//
                    String album = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM));
                    // Ten Album//
                    String album_id = mCursor.getString(mCursor.getColumnIndex(MediaStore.Audio.AudioColumns.ALBUM_ID));

                    Song a = new Song(name, artist, path, album, album_id);
                    db.addSong(a);
                    mCursor.moveToNext();
                } while (!mCursor.isAfterLast());
            }
            mCursor.close();
            mList = db.getListSong();
        } catch (Exception ex){
        }
        return mList;
    }

    class loadListSongs extends AsyncTask<Integer, Void, Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            lineDataNull.setVisibility(View.GONE);
        }

        @Override
        protected Integer doInBackground(Integer... params) {
            mListSong.addAll(findSongs());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            HomeActivity.mList = mListSong;
            LOADDB = 1;
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (LOADDB == 1) {
                if (mListSong.size() != 0) {
                    adapter = new ListviewSongAdapter(getActivity(), mListSong);
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    lv.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                } else {
                    lv.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    lineDataNull.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
