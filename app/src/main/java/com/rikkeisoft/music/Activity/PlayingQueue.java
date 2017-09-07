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
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
import com.rikkeisoft.music.Adapter.PlayingQueueAdapter;
import com.rikkeisoft.music.Adapter.helper.OnStartDragListener;
import com.rikkeisoft.music.Adapter.helper.SimpleItemTouchHelperCallback;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class PlayingQueue extends AppCompatActivity implements OnStartDragListener {
    private static final String TAG = "ActivityPlayingQueue";
    private RecyclerView recyclerView;
    private PlayingQueueAdapter adapter;
    private ArrayList<Song> mlist;

    private FragmentManager fragmentManager = getSupportFragmentManager();
    public SlidingUpPanelLayout mLayoutPlay;
    FrPlayMusic frPlayMusic = new FrPlayMusic();

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
//        mLayoutPlay.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
//            @Override
//            public void onPanelSlide(View panel, float slideOffset) {
//                Log.i(TAG, "onPanelSlide, slideoffset " + slideOffset);
//            }
//
//            @Override
//            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                Log.i(TAG, "onPanelStateChanged " + newState);
//                if (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
//                } else if (mLayoutPlay.getPanelState() == SlidingUpPanelLayout.PanelState.DRAGGING) {
////
//                }
//            }
//        });
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
        //
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.activity_playing_queue);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mlist = new ArrayList<>();
        mlist.addAll(FrPlayMusic.mList);
        Toast.makeText(this, mlist.size()+"", Toast.LENGTH_SHORT).show();
        recyclerView = (RecyclerView) findViewById(R.id.recycleViewSortListSong);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration divider = new DividerItemDecoration(this,layoutManager.getOrientation());
        recyclerView.addItemDecoration(divider);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new PlayingQueueAdapter(mlist,getApplicationContext(), this);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
