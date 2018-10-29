package com.vietdung.beautymusic.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.FragmentSongAdapter;
import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class FragmentSongs extends Fragment  {
    RecyclerView rv_Songs;
    List<Songs> songsList;
    FragmentSongAdapter songAdapter;
    int numberofColumns = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        rv_Songs = view.findViewById(R.id.rvSongs);
        setHasOptionsMenu(true);
        songsList = new ArrayList<>();
        songAdapter = new FragmentSongAdapter(songsList, getActivity());
        getData();
        rv_Songs.setAdapter(songAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Songs.setLayoutManager(layoutManager);
        return view;

    }

    public void getData() {
        //songAdapter.notifyDataSetChanged();
        ContentResolver cr = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST);
            int albumsColums = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM_ID);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                int idALbums= musicCursor.getInt(albumsColums);
                songsList.add(new Songs(thisId, thisTitle, thisArtist,idALbums));

            }
            while (musicCursor.moveToNext());
        }


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main,menu);
        final MenuItem item = menu.findItem(R.id.mnSearch);
        final SearchView  searchView  = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(TextUtils.isEmpty(query)){
                   if (!searchView.isIconified()) {
                       searchView.setIconified(true);
                   }
                   item.collapseActionView();
                    return false;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String userInput = newText.toLowerCase();
                List<Songs> newsongList = new ArrayList<>();
                for(Songs song : songsList){
                    if(song.getNameSong().toLowerCase().contains(userInput)){
                        newsongList.add(song);
                    }
                }
                songAdapter.setfilter(newsongList);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mn_lvsongs:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_Songs.setLayoutManager(layoutManager);
                break;
            case R.id.mn_gvsongs:
                rv_Songs.setAdapter(songAdapter);
                rv_Songs.setLayoutManager(new GridLayoutManager(getActivity(), numberofColumns));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
