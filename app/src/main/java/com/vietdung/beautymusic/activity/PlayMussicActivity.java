package com.vietdung.beautymusic.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.FragmentSongAdapter;
import com.vietdung.beautymusic.adapter.SongAlbum1Adapter;
import com.vietdung.beautymusic.adapter.SongArtistsAdapter;
import com.vietdung.beautymusic.adapter.SongMusicAdapter;
import com.vietdung.beautymusic.database.GetDataSdCard;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.AppController;
import com.vietdung.beautymusic.until.MusicService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PlayMussicActivity extends AppCompatActivity implements SongMusicAdapter.OnItemClickListener {
    CircleImageView iv_CircleMussic;
    ImageView btn_Back;
    ImageView btn_Next;
    ImageButton iv_Pause;
    ImageView iv_Play;
    ObjectAnimator animator;
    TextView tv_Time, tv_TimeTotal;
    SeekBar seekBar;
    ImageView iv_Repeat, iv_Suffle;
    Toolbar tb_PlayMusic;
    List<Songs> songsList;
    SongMusicAdapter songAdapter;
    RecyclerView rv_Song;
    int id = 0;
    MusicService musicService;
    Intent playIntent;
    boolean musicBound = false;
    int position = 0;
    int screen;

    GetDataSdCard getDataSdCard;
    private SharedPreferences sharedPreferences;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";
    public final static String rq_notification = "2000";
    public final static String rq_screen = "screen";
    public final static String rq_screen_idalbums = "screen_idalbums";
    public final static String rq_screen_idartist = "screen_idartist";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_mussic);

        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
        initView();

        addEvents();
    }

    private ServiceConnection musicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicService = binder.getService();
            //pass list
            musicService.setList(songsList);
            songPicked();
            musicBound = true;
            setToolbar();
            if(musicService!=null&&musicBound){
                musicService.setRunBackground(false);

            }

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void addEvents() {
        getPosition();
        setButtonClick();
        updateTimeSong();
    }

    public void songPicked() {
        int rq_notifi=0;
        rq_notifi = getIntent().getIntExtra(rq_notification,0);
        if(rq_notifi!=3000){
            musicService.setSong(position);
            musicService.playSong();
        }else{
            if(musicService!=null && musicService.isPng()){
                iv_Play.setVisibility(View.INVISIBLE);
                iv_Pause.setVisibility(View.VISIBLE);
            }else{
                iv_Play.setVisibility(View.VISIBLE);
                iv_Pause.setVisibility(View.INVISIBLE);
            }
        }

    }

    private void setButtonClick() {
        iv_Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicService.isPng()){
                    musicService.pauseSong();
                    iv_Pause.setVisibility(View.INVISIBLE);
                    iv_Play.setVisibility(View.VISIBLE);
                    setPlayingMusic();
                    animator.pause();
                }

            }
        });
        iv_Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!musicService.isPng()){
                    musicService.pauseToPlaySong();
                    iv_Pause.setVisibility(View.VISIBLE);
                    iv_Play.setVisibility(View.INVISIBLE);
                    setPlayingMusic();
                    animator.resume();
                }

            }
        });
        btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.INVISIBLE);
                musicService.backSong();
                position = musicService.getPosition();
                setPlayingMusic();
                animator.start();
            }
        });
        btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iv_Pause.setVisibility(View.VISIBLE);
                iv_Play.setVisibility(View.INVISIBLE);
                musicService.nextSong();
                position = musicService.getPosition();
                setPlayingMusic();
                animator.start();
            }
        });
        iv_Repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean repeat=musicService.setRepeat();
                if(repeat==true){
                    iv_Repeat.setImageResource(R.drawable.repeatcolor);

                }else{
                    iv_Repeat.setImageResource(R.drawable.repeattrack);
                }
            }
        });
        iv_Suffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               boolean shuffle = musicService.setShuffle();
                if(shuffle){
                    iv_Suffle.setImageResource(R.drawable.shufflecolor);

                }else{
                    iv_Suffle.setImageResource(R.drawable.iconsuffle);
                }
            }
        });
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.seekTo(seekBar.getProgress());

            }
        });
    }

    private void getPosition() {
        if (position < songsList.size()) {
            id = getIntent().getIntExtra(FragmentSongAdapter.rq_itent_id, 0);
            for (int i = 0; i < songsList.size(); i++) {
                if (id == songsList.get(i).getId()) {
                    position = i;
                }
            }
        }

    }

    private void setToolbar() {
        setSupportActionBar(tb_PlayMusic);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setPlayingMusic();
        tb_PlayMusic.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });
    }

    private void setPlayingMusic() {
        for (int i = 0; i < songsList.size(); i++) {
            if (musicService.getId() == songsList.get(i).getId()) {
                // Log.d("idsong ", " " + musicService.getId() + " " + i);
                getSupportActionBar().setTitle(songsList.get(i).getNameSong());
            }
        }
        //getSupportActionBar().setTitle(songsList.get(musicService.getPosition()).getNameSong());
        SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
        if (musicService != null && musicBound && musicService.isPng()) {
            tv_TimeTotal.setText(dateFormat.format(musicService.getTimeTotal()));
            seekBar.setMax(musicService.getTimeTotal());
        }

    }
    // update seekbar when music is playing
    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");

                if (musicService != null && musicBound && musicService.isPng()) {
                    tv_Time.setText(dateFormat.format(musicService.getCurrentPosition()));
                    seekBar.setProgress(musicService.getCurrentPosition());
                    musicService.autoNextSong();
                    setPlayingMusic();
                }
                handler.postDelayed(this, 500);
            }
        }, 100);
    }
    //get list song from sd card
    private void getSonglist() {
        screen = getIntent().getIntExtra(FragmentSongAdapter.rq_itent_screen, -1);
        // getSong full
        if (screen == 111) {
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
                        (MediaStore.Audio.Media.ALBUM_ID);
                //add songs to list
                do {
                    int thisId = musicCursor.getInt(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    int idALbums = musicCursor.getInt(albumsColums);
                    songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));
                }
                while (musicCursor.moveToNext());
            }
            songAdapter.notifyDataSetChanged();
        }// getSong Album
        else if (screen == 123) {
            int idAlbums = getIntent().getIntExtra(SongAlbum1Adapter.rq_itent_album, -1);
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
                        (MediaStore.Audio.Media.ALBUM_ID);
                //add songs to list
                do {
                    int thisAlbums = musicCursor.getInt(albumsColums);
                    if (thisAlbums == idAlbums) {
                        int thisId = musicCursor.getInt(idColumn);
                        String thisTitle = musicCursor.getString(titleColumn);
                        String thisArtist = musicCursor.getString(artistColumn);
                        songsList.add(new Songs(thisId, thisTitle, thisArtist, idAlbums));
                    }
                }
                while (musicCursor.moveToNext());
            }

            songAdapter.notifyDataSetChanged();
        } else if(screen==321){
            int idArtist = getIntent().getIntExtra(SongArtistsAdapter.rq_itent_album, -1);
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
                        songsList.add(new Songs(thisId, thisTitle, thisArtist, idArtist));
                    }
                }
                while (musicCursor.moveToNext());
            }

            songAdapter.notifyDataSetChanged();
        } else {
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
                        (MediaStore.Audio.Media.ALBUM_ID);
                //add songs to list
                do {
                    int thisId = musicCursor.getInt(idColumn);
                    String thisTitle = musicCursor.getString(titleColumn);
                    String thisArtist = musicCursor.getString(artistColumn);
                    int idALbums = musicCursor.getInt(albumsColums);
                    songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));
                }
                while (musicCursor.moveToNext());
            }
            songAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_playmusic, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // start intent Playing Queue
            case R.id.mnPlayQueue:
                Intent intent = new Intent(this, PlayingQueueActivity.class);
                intent.putExtra(rq_screen, screen);
                if (screen == 123) {
                    int idAlbums = getIntent().getIntExtra(SongAlbum1Adapter.rq_itent_album, -1);
                    intent.putExtra(rq_screen_idalbums, idAlbums);
                } else if (screen == 321) {
                    int idArtist = getIntent().getIntExtra(SongArtistsAdapter.rq_itent_album, -1);
                    intent.putExtra(rq_screen_idartist, idArtist);
                }
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private List<Songs> getSampleData() {
        screen = getIntent().getIntExtra(FragmentSongAdapter.rq_itent_screen, -1);
        getDataSdCard = new GetDataSdCard(this, screen);
        //Get the song data
        List<Songs> songsList = getDataSdCard.getDataPlayMusic();
        //create an empty array to hold the list of sorted Customers
        List<Songs> sortedSongs = new ArrayList<Songs>();
        //get the JSON array of the ordered of sorted customers
        String jsonListOfSortedCustomerId = sharedPreferences.getString(LIST_OF_SORTED_DATA_ID, "");
        //check for null
        if (!jsonListOfSortedCustomerId.isEmpty()) {
            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<Long> listOfSortedCustomersId = gson.fromJson(jsonListOfSortedCustomerId, new TypeToken<List<Long>>() {
            }.getType());
            //build sorted list
            if (listOfSortedCustomersId != null && listOfSortedCustomersId.size() > 0) {
                for (Long id : listOfSortedCustomersId) {
                    for (Songs song : songsList) {
                        if (song.getId() == id) {
                            sortedSongs.add(song);
                            songsList.remove(song);
                            break;
                        }
                    }
                }
            }
            //if there are still songs that were not in the sorted list
            //maybe they were added after the last drag and drop
            //add them to the sorted list
            if (songsList.size() > 0) {
                sortedSongs.addAll(songsList);
            }
            return sortedSongs;
        } else {
            return songsList;
        }
    }


    private void initView() {
        iv_CircleMussic = findViewById(R.id.ivCircleMusic);
        btn_Back = findViewById(R.id.ibBack);
        btn_Next = findViewById(R.id.ibNext);
        tv_Time = findViewById(R.id.tvTime);
        tv_TimeTotal = findViewById(R.id.tvTimeTotal);
        iv_Suffle = findViewById(R.id.ivSuffle);
        iv_Repeat = findViewById(R.id.ivRepeat);
        iv_Pause = findViewById(R.id.ivPause);
        iv_Play = findViewById(R.id.ivStart);
        iv_Play.setVisibility(View.INVISIBLE);
        tb_PlayMusic = findViewById(R.id.tbPlayMussic);
        rv_Song = findViewById(R.id.rvMusicSong);
        seekBar = findViewById(R.id.seekbar);
        musicService = new MusicService();

        animator = ObjectAnimator.ofFloat(iv_CircleMussic, "rotation", 0f, 360f);
        animator.setDuration(10000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();

        sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);

        songsList = getSampleData();
        songAdapter = new SongMusicAdapter(songsList, this);
        songAdapter.setClickListener(this);
        rv_Song.setAdapter(songAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Song.setLayoutManager(layoutManager);
        AppController.getInstance().setPlayMusicActivity(this);
    }

    //click adapter
    @Override
    public void onItemClick(View song, int position) {
        musicService.setSong(position);
        musicService.playSong();
        if(musicService.isPng()){
            iv_Pause.setVisibility(View.VISIBLE);
            iv_Play.setVisibility(View.INVISIBLE);
            animator.start();
        }
    }

    public void updatePlayorPause() {
        if (musicService != null && musicService.isPng()) {
           // musicService.pauseSong();
            iv_Pause.setVisibility(View.VISIBLE);
            iv_Play.setVisibility(View.INVISIBLE);
            setPlayingMusic();
            animator.resume();
        }
        if (musicService != null && !musicService.isPng()) {
           // musicService.pauseToPlaySong();
            iv_Pause.setVisibility(View.INVISIBLE);
            iv_Play.setVisibility(View.VISIBLE);
            setPlayingMusic();
            animator.pause();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        songsList = getSampleData();
        songAdapter = new SongMusicAdapter(songsList, this);
        songAdapter.setClickListener(this);
        rv_Song.setAdapter(songAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Song.setLayoutManager(layoutManager);
        if (musicService != null) {
            musicService.setList(songsList);
        }
        MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
        if(musicService1!=null){
            if(musicService1.isPng()){
                animator.resume();
            }else{
                animator.pause();
            }
        }
    }

    @Override
    protected void onDestroy() {
        musicService=null;
        unbindService(musicConnection);
        super.onDestroy();

    }
}
