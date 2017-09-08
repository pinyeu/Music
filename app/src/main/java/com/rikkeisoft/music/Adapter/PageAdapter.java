package com.rikkeisoft.music.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.rikkeisoft.music.Activity.FragmentTabLayout.FrAlbums;
import com.rikkeisoft.music.Activity.FragmentTabLayout.FrArtists;
import com.rikkeisoft.music.Activity.FragmentTabLayout.FrSongs;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class PageAdapter extends FragmentStatePagerAdapter {
    private int numOfTab = 3;
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FrSongs tabSongs = new FrSongs();
                return tabSongs;
            case 1:
                FrAlbums tabAlbums = new FrAlbums();
                return tabAlbums;
            case 2:
                FrArtists tabArtists = new FrArtists();
                return tabArtists;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
