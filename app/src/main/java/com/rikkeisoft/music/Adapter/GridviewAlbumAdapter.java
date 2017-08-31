package com.rikkeisoft.music.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rikkeisoft.music.Model.Album;
import com.rikkeisoft.music.R;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/22/17.
 */

public class GridviewAlbumAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Album> mList;

    private android.media.MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    public GridviewAlbumAdapter(Context context, ArrayList<Album> mList) {
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
        if(rootView ==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            rootView = inflater.inflate(R.layout.item_gridalbum,parent,false);
        }
        TextView txtNameAlbum = (TextView) rootView.findViewById(R.id.textNameAlbum);
        txtNameAlbum.setText(mList.get(position).getName());
        TextView textNumSong = (TextView) rootView.findViewById(R.id.textNumSong);
        textNumSong.setText(mList.get(position).getNumSong()+" songs");
        mmr.setDataSource(mList.get(position).getPathCoverArt());
        byte[] dataImageDisc = mmr.getEmbeddedPicture();
        ImageView imageAlbum = (ImageView) rootView.findViewById(R.id.thumbnailAlbum);
        if (dataImageDisc != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(dataImageDisc, 0, dataImageDisc.length);
            imageAlbum.setImageBitmap(bitmap);
        } else {
            imageAlbum.setImageResource(R.drawable.disc_default);
        }
        return rootView;
    }
}
