package com.rikkeisoft.music.Activity;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
import com.rikkeisoft.music.Adapter.PlayingQueueAdapter;
import com.rikkeisoft.music.Adapter.helper.OnStartDragListener;
import com.rikkeisoft.music.Adapter.helper.SimpleItemTouchHelperCallback;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.IOException;
import java.util.ArrayList;

public class PlayingQueue extends AppCompatActivity implements OnStartDragListener {
    private static final String TAG = "ActivityPlayingQueue";
    public Song songCurrent;
    private RecyclerView recyclerView;
    private PlayingQueueAdapter adapter;
    private ArrayList<Song> mlist;

    public TextView textNameSong, textNameArtist;
    private ImageButton btnPause, btnBack;

    private ItemTouchHelper mItemTouchHelper;

    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);
        db = new DatabaseHandler(this);
        initView();
        addEvents();
    }

    private void addEvents() {
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PlayingQueue.this, mlist.get(0).getName() + "", Toast.LENGTH_SHORT).show();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        mlist = new ArrayList<>();
        mlist.addAll(FrPlayMusic.mList);
        Toast.makeText(this, mlist.size() + "", Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewSortListSong);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        DividerItemDecoration divider = new DividerItemDecoration(this, layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayingQueueAdapter(mlist, this, this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
        songCurrent = mlist.get(FrPlayMusic.mIndex);
        textNameSong = (TextView) findViewById(R.id.textNameSong);
        textNameSong.setText(mlist.get(FrPlayMusic.mIndex).getName());
        textNameArtist = (TextView) findViewById(R.id.textNameArtist);
        textNameArtist.setText(mlist.get(FrPlayMusic.mIndex).getArtist());
        btnPause = (ImageButton) findViewById(R.id.btnPause);
        btnBack = (ImageButton) findViewById(R.id.btnBack);
    }

    public void test(int postion){
        textNameSong.setText(mlist.get(postion).getName());
        textNameArtist.setText(mlist.get(postion).getArtist());
        Toast.makeText(this, "sss"+postion, Toast.LENGTH_SHORT).show();
        FrPlayMusic.mIndex = postion;
        try {
            FrPlayMusic.mediaPlayer.reset();
            FrPlayMusic.mediaPlayer.setDataSource(mlist.get(FrPlayMusic.mIndex).getPath());
            FrPlayMusic.mediaPlayer.prepare();
            FrPlayMusic.mediaPlayer.start();
        } catch (IllegalArgumentException iae) {
        } catch (IllegalStateException ise) {
        } catch (IOException ex) {
        }
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void finish() {
        FrPlayMusic.mList = mlist;
        setResult(FrPlayMusic.OPEN_PLAYINGQUEUE);
        super.finish();
    }
}
