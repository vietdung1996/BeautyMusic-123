package com.vietdung.beautymusic.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vietdung.beautymusic.activity.FragmentAlbums;
import com.vietdung.beautymusic.activity.FragmentArtists;
import com.vietdung.beautymusic.activity.FragmentSongs;

public class PagerAdapter extends FragmentStatePagerAdapter {
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frag = null;
        switch (position) {
            case 0:
                frag = new FragmentSongs();
                break;
            case 1:
                frag = new FragmentAlbums();
                break;
            case 2:
                frag = new FragmentArtists();
                break;

        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "Songs";
                break;
            case 1:
                title = "Albums";
                break;
            case 2:
                title = "Artists";
                break;
        }
        return title;
    }
}
