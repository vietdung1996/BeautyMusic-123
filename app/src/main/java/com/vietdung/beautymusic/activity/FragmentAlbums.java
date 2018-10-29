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
import com.vietdung.beautymusic.adapter.FragmentAlbumsAdapter;
import com.vietdung.beautymusic.model.Albums;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlbums extends Fragment {
    RecyclerView rv_Albums;
    List<Albums> albumsList;
    FragmentAlbumsAdapter albumsAdapter;
    int numberofColumns = 2;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        rv_Albums = view.findViewById(R.id.rvAlbums);
        setHasOptionsMenu(true);

        albumsList = new ArrayList<>();
        albumsAdapter = new FragmentAlbumsAdapter(albumsList, getActivity());
        getData();
        rv_Albums.setAdapter(albumsAdapter);
        rv_Albums.setLayoutManager(new GridLayoutManager(getActivity(), numberofColumns));
        return view;
    }

    private void getData() {


        ContentResolver cr = getActivity().getContentResolver();
        Uri musicUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ARTIST);
            int artMusic = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM_ART);
            //add songs to list
            do {
                int thisId = musicCursor.getInt(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisArt = musicCursor.getString(artMusic);
                albumsList.add(new Albums(thisId, thisTitle, thisArtist, thisArt));
            }
            while (musicCursor.moveToNext());
        }
        albumsAdapter.notifyDataSetChanged();

    }

    //Listerner search view
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_main, menu);
        final MenuItem item = menu.findItem(R.id.mnSearch);
        final SearchView searchView = (SearchView) item.getActionView();
        MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW | MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
        MenuItemCompat.setActionView(item, searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)) {
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
                List<Albums> newAlbumList = new ArrayList<>();
                for (Albums albums : albumsList) {
                    if (albums.getNameAlbums().toLowerCase().contains(userInput)) {
                        newAlbumList.add(albums);
                    }
                }
                albumsAdapter.setfilter(newAlbumList);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.mn_gvsongs:
                rv_Albums.setLayoutManager(new GridLayoutManager(getActivity(), numberofColumns));
                break;
            case R.id.mn_lvsongs:
                LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                rv_Albums.setLayoutManager(layoutManager);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
