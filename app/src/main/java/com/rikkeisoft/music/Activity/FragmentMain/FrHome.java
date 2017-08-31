package com.rikkeisoft.music.Activity.FragmentMain;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rikkeisoft.music.Adapter.PageAdapter;
import com.rikkeisoft.music.R;

/**
 * Created by nguyenquanghung on 8/23/17.
 */

public class FrHome extends Fragment {
    View view;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private PageAdapter pagerAdapter;
    private final static int LEVEL_PAUSE = 0;
    private final static int LEVEL_PLAY = 1;
    private int level = LEVEL_PAUSE;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        addControls();


        tabLayout.addTab(tabLayout.newTab().setText("SONGS"));
        tabLayout.addTab(tabLayout.newTab().setText("ALBUMS"));
        tabLayout.addTab(tabLayout.newTab().setText("ARTIST"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        pagerAdapter = new PageAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);

        addEvents();

        return view;
    }

    private void addEvents() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });// tablayout

        // button play/pause o bottom home.

    }

    private void addControls() {
        viewPager = (ViewPager) view.findViewById(R.id.page);
        tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
