package com.rikkeisoft.music.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rikkeisoft.music.Model.Artist;
import com.rikkeisoft.music.R;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/22/17.
 */

public class GridviewArtistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Artist> mList;
    public GridviewArtistAdapter(Context context, ArrayList<Artist> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            rootView = inflater.inflate(R.layout.item_gridartist,parent,false);
        }
        TextView txtName = (TextView) rootView.findViewById(R.id.textNameArtist);
        txtName.setText(mList.get(position).getName());
        TextView txtNumAlbum = (TextView) rootView.findViewById(R.id.textNumAlbum);
        txtNumAlbum.setText(mList.get(position).getNumAlbum()+ " albums");
        TextView txtNumSong = (TextView) rootView.findViewById(R.id.textNumSong);
        txtNumSong.setText(mList.get(position).getNumSong()+" songs");


        return rootView;
    }
}
