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
import com.rikkeisoft.music.Adapter.GridviewArtistAdapter;
import com.rikkeisoft.music.Model.Artist;
import com.rikkeisoft.music.R;
import com.rikkeisoft.music.db.DatabaseHandler;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class FrArtists extends Fragment {
    View view;
    GridView gridViewArtist;
    GridviewArtistAdapter adapter;
    ArrayList<Artist> mList = new ArrayList<>();
    private ProgressBar progressBar;
    private TextView txtDataNull;
    private LinearLayout lineDataNull;
    private DatabaseHandler db;
    private int LOADDB = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_artists, container, false);
        gridViewArtist = (GridView) view.findViewById(R.id.gvArtist);
        db = new DatabaseHandler(getActivity());
        txtDataNull = (TextView) view.findViewById(R.id.txtDataNull);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        lineDataNull = (LinearLayout) view.findViewById(R.id.lineDataNull);
        new loadListArtist().execute(LOADDB);
        addEvents();
        return view;
    }

    private void addEvents() {
        gridViewArtist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), AlbumArtist_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("ARTIST_NAME",mList.get(position).getName());
                bundle.putString("ARTIST_PATH",mList.get(position).getPath());
                intent.putExtra("ARTIST",bundle);
                startActivity(intent);
            }
        });
    }

    class loadListArtist extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            mList.addAll(db.getListArtist());
            LOADDB = 1;
            return LOADDB;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (LOADDB == 1) {
                if (mList.size() != 0) {
                    adapter = new GridviewArtistAdapter(getActivity(), mList);
                    gridViewArtist.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    gridViewArtist.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                    lineDataNull.setVisibility(View.GONE);
                } else {
                    gridViewArtist.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    lineDataNull.setVisibility(View.VISIBLE);
                }
            }
        }
    }
}
