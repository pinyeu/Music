package com.rikkeisoft.music.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.music.Activity.FragmentMain.FrPlayMusic;
import com.rikkeisoft.music.Activity.PlayingQueue;
import com.rikkeisoft.music.Adapter.helper.ItemTouchHelperAdapter;
import com.rikkeisoft.music.Adapter.helper.ItemTouchHelperViewHolder;
import com.rikkeisoft.music.Adapter.helper.OnStartDragListener;
import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by nguyenquanghung on 9/7/17.
 */

public class PlayingQueueAdapter extends RecyclerView.Adapter<PlayingQueueAdapter.ViewHolder> implements ItemTouchHelperAdapter {
    ArrayList<Song> mList;
    Context context;
    private final OnStartDragListener mDragStartListener;

    public PlayingQueueAdapter(ArrayList<Song> mList, Context context, OnStartDragListener mDragStartListener) {
        this.mList = mList;
        this.context = context;
        this.mDragStartListener = mDragStartListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewitem = inflater.inflate(R.layout.item_playingqueue, parent, false);
        return new ViewHolder(viewitem);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textNameSong.setText(mList.get(position).getName());
        holder.textNameArtist.setText(mList.get(position).getArtist());
        holder.imageDrag.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, mList.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        TextView textNameSong, textNameArtist;
        ImageView imageDrag;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            textNameSong = (TextView) itemView.findViewById(R.id.textNameSong);
            textNameArtist = (TextView) itemView.findViewById(R.id.textNameArtist);
            imageDrag = (ImageView) itemView.findViewById(R.id.imageView);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.line1);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(0);
        }
    }

}
