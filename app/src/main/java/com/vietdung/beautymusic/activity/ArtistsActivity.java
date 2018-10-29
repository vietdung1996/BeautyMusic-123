package com.vietdung.beautymusic.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.FragmentArtirstsAdapter;
import com.vietdung.beautymusic.adapter.SongArtistsAdapter;
import com.vietdung.beautymusic.model.Albums;
import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class ArtistsActivity extends AppCompatActivity {
    Toolbar tb_Albums;
    ImageView iv_Albums;
    CollapsingToolbarLayout collapsingToolbarLayout;
    RecyclerView rv_Albums;
    SeekBar seekBarBottom;
    TextView tv_SongBottom, tv_ArtistBottom;
    ImageView iv_Pause;
    List<Songs> songsList;
    List<Albums> albumsList;
    SongArtistsAdapter songArtistsAdapter;

    int idArtist = 0;
    String thisTitle = "";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_albums);

        initView();
        addEvents();
    }

    private void addEvents() {

        getAlbums();
        getSongAlbums();
        setToolbar();
        updateTimeSong();
    }

    // getAlbum from sdcard
    private void getAlbums() {
        idArtist = getIntent().getIntExtra(FragmentArtirstsAdapter.rq_request_idArtist, 0);
        ContentResolver cr = getApplication().getContentResolver();
        Uri musicUri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.ARTIST);
            int idColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists._ID);
            int numberSong = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_TRACKS);
            Log.d("numberSong", " " + numberSong);
            int numberAlbum = musicCursor.getColumnIndex
                    (MediaStore.Audio.Artists.NUMBER_OF_ALBUMS);
            //add songs to list
            do {

                int thisId = musicCursor.getInt(idColumn);
                if (idArtist == thisId) {
                    thisTitle = musicCursor.getString(titleColumn);
                }

                //albumsList.add(new Albums(thisId, thisTitle, thisArtist,thisArt));
            }
            while (musicCursor.moveToNext());
        }
    }

    // getSong on Album
    private void getSongAlbums() {
        ContentResolver cr = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = cr.query(musicUri, null, null, null, null);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumsColums = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ARTIST_ID);
            //add songs to list
            do {
                int thisArtistID = musicCursor.getInt(albumsColums);
                if (thisArtistID == idArtist) {
                    int thisId = musicCursor.getInt(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    songsList.add(new Songs(thisId, thisTitle, thisArtist,idArtist));
                }
            }
            while (musicCursor.moveToNext());
        }

        songArtistsAdapter.notifyDataSetChanged();

    }

    private void setToolbar() {
        setSupportActionBar(tb_Albums);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        collapsingToolbarLayout.setTitle(thisTitle);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (MainActivity.musicService != null) {
            setDisplayMusicBottom();
        }
    }
    public void setDisplayMusicBottom() {
        //int position = musicService.getPosition();
        //Log.d("Namesong", " "+songsList.get(position).getNameSong());
        // Log.d("NameAritis", " "+musicService.getNameArtist());
        if (MainActivity.musicService.isPng()) {
            tv_SongBottom.setText(MainActivity.musicService.getNameSong());
            tv_ArtistBottom.setText(MainActivity.musicService.getNameArtist());
            seekBarBottom.setMax(MainActivity.musicService.getTimeTotal());
            seekBarBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MainActivity.musicService.seekTo(seekBar.getProgress());

                }

            });
            iv_Pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(MainActivity.musicService.isPng()){
                        MainActivity.musicService.pauseSong();
                        iv_Pause.setImageResource(R.drawable.play);
                    }else{
                        MainActivity.musicService.pauseToPlaySong();
                        iv_Pause.setImageResource(R.drawable.pause);
                    }
                }
            });

        }
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (MainActivity.musicService != null && MainActivity.musicService.isPng()) {
                    seekBarBottom.setProgress(MainActivity.musicService.getCurrentPosition());
                   // MainActivity.musicService.autoNextSong();
                    tv_SongBottom.setText(MainActivity.musicService.getNameSong());
                    tv_ArtistBottom.setText(MainActivity.musicService.getNameArtist());
                }
                handler.postDelayed(this, 500);
            }
        }, 100);
    }



    private void initView() {
        tb_Albums = findViewById(R.id.tbAlbums);
        iv_Albums = findViewById(R.id.ivActivityAlbums);
        collapsingToolbarLayout = findViewById(R.id.collToolBar);
        rv_Albums = findViewById(R.id.rvAlbums);

        seekBarBottom = findViewById(R.id.seekbarBottomAlbum);
        tv_SongBottom = findViewById(R.id.tvSongsAlbum);
        tv_ArtistBottom = findViewById(R.id.tvArtistAlbum);
        iv_Pause = findViewById(R.id.ivPauseBottomAlbum);
        songsList = new ArrayList<>();
        albumsList = new ArrayList<>();
        songArtistsAdapter = new SongArtistsAdapter(songsList, this, idArtist);
        songArtistsAdapter.notifyDataSetChanged();
        rv_Albums.setAdapter(songArtistsAdapter);
        rv_Albums.setLayoutManager(new LinearLayoutManager(this));
    }

}
