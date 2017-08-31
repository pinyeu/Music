package com.rikkeisoft.music.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.rikkeisoft.music.Model.Song;
import com.rikkeisoft.music.R;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class ListviewSongAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Song> mListSong;
    public ListviewSongAdapter(Context context, ArrayList<Song> mListSong) {
        this.context = context;
        this.mListSong = mListSong;
    }

    @Override
    public int getCount() {
        return mListSong.size();
    }

    @Override
    public Object getItem(int position) {
        return mListSong.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rootView = convertView;
        if(rootView == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rootView = inflater.inflate(R.layout.item_listsong,parent,false);
        }
        TextView txtNameSong = (TextView) rootView.findViewById(R.id.textNameSong);
        txtNameSong.setText(mListSong.get(position).getName());
        TextView txtArtist = (TextView) rootView.findViewById(R.id.textArtist);
        txtArtist.setText(mListSong.get(position).getArtist());
        return rootView;
    }
}
