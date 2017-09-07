package com.rikkeisoft.music.Activity.FragmentTabLayout;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rikkeisoft.music.Activity.AlbumArtist_Activity;
import com.rikkeisoft.music.Adapter.GridviewAlbumAdapter;
import com.rikkeisoft.music.Model.Album;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class FrAlbums extends Fragment{
    private View view;
    private ProgressBar progressBar;
    private TextView txtDataNull;
    private LinearLayout lineDataNull;
    private GridviewAlbumAdapter adapter;
    private ArrayList<Album> mList= new ArrayList<>();;
    private GridView gridViewAlbum;
    private DatabaseHandler db;
    private int LOADDB = 0;
    private final static int OPEN_ALBUM=100;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =inflater.inflate(R.layout.fragment_albums,container,false);
        db = new DatabaseHandler(getActivity());
        initView();
        new loadListAlbums().execute(LOADDB);
        addEvents();
        return view;
    }

    private void initView() {
        gridViewAlbum = (GridView) view.findViewById(R.id.gvAlbum);
        txtDataNull = (TextView) view.findViewById(R.id.txtDataNull);
        lineDataNull = (LinearLayout) view.findViewById(R.id.lineDataNull);
        progressBar= (ProgressBar) view.findViewById(R.id.progressBar);
    }
    private void addEvents() {
        gridViewAlbum.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AlbumArtist_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ALBUM_NAME",mList.get(position).getName());
                bundle.putString("ALBUM_PATH",mList.get(position).getPathCoverArt());
                intent.putExtra("ALBUM",bundle);
                startActivityForResult(intent,OPEN_ALBUM);
            }
        });
    }

    class loadListAlbums extends AsyncTask<Integer,Void,Integer>{

        @Override
        protected Integer doInBackground(Integer... params) {
            mList.addAll(db.getListAlbums());
            LOADDB = 1;
            return LOADDB;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if(LOADDB==1){
                if(mList.size()!=0){
                    adapter = new GridviewAlbumAdapter(getActivity(),mList);
                    gridViewAlbum.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    gridViewAlbum.setVisibility(View.VISIBLE);
                }else {
                    gridViewAlbum.setVisibility(View.GONE);
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
