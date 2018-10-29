package com.vietdung.beautymusic.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.AppController;
import com.vietdung.beautymusic.until.MusicService;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;
    Toolbar tb_main;
    SeekBar seekBarBottom;
    TextView tv_SongBottom, tv_ArtistBottom;
    ImageView iv_Pause;
    public static MusicService musicService;
    List<Songs> songsList;
    public static Intent playIntent;
    boolean musicBound = false;
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    MusicService musicService1;



    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            if(musicService==null){
                musicService = binder.getService();
            }
            //pass list
            AppController.getInstance().setMusicService(musicService);
            musicService.setList(songsList);
            musicBound = true;
            if(musicService!=null&&musicBound){
                if(musicService.isRunBackground()==true){
                    musicService.setRunBackground(false);
                    musicService.setCancelMain(false);
                }
                if (musicService.isCancelPlayMusic() == true) {
                    musicService.setCancelPlayMusic(false);
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!checkPermissions()) {

            return;
        }
        initView();
        addEvents();
    }

    @Override
    public void onStart() {

        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
            //Log.d("service onstart", "onStart: ");
        }else if(playIntent!=null){
           // Log.d("playintent", "onStart: ");
        }
        if (musicService1 != null) {
            Log.d("service onstart", "onStart: ");
            setDisplayMusicBottom();

        }

        updateBottomControlls();
        //Log.d("service onstart 1", "onStart: ");
    }

    private void addEvents() {
        getData();
        setToolbar();
        updateTimeSong();
    }

    private void setToolbar() {
        setSupportActionBar(tb_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void initView() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tb_main = findViewById(R.id.tbMain);
        seekBarBottom = findViewById(R.id.seekbarBottom);
        tv_SongBottom = findViewById(R.id.tvSongs);
        tv_ArtistBottom = findViewById(R.id.tvArtist);
        iv_Pause = findViewById(R.id.ivPauseBottom);
        songsList = new ArrayList<>();

        FragmentManager manager = getSupportFragmentManager();
        PagerAdapter adapter = new com.vietdung.beautymusic.adapter.PagerAdapter(manager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setTabsFromPagerAdapter(adapter);//deprecated
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        AppController.getInstance().setMainActivity(this);
        musicService1 = (MusicService) AppController.getInstance().getMusicService();



    }

    @Override
    protected void onDestroy() {
        if(musicService!=null){
            musicService.setRunBackground(true);
            musicService.setCancelPlayMusic(true);
            musicService.setCancelMain(true);
           // Log.d("chay vao day main", "onDestroy: ");
        }else{
            MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
            musicService1.setRunBackground(true);
            musicService1.setCancelPlayMusic(true);
            musicService1.setCancelMain(true);
        }
        //Log.d("service max ngu", "onDestroy: ");
        musicService=null;
        if (musicBound) {
            unbindService(musicConnection);
            musicBound = false;
        }
        playIntent=null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (musicService != null&&musicBound) {
            setDisplayMusicBottom();
        }
        super.onResume();
        updateTimeSong();
    }


    public void setDisplayMusicBottom() {
        //final MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
        if (musicService != null) {
            if (musicService.isPng()) {
                tv_SongBottom.setText(musicService.getNameSong());
                tv_ArtistBottom.setText(musicService.getNameArtist());
                seekBarBottom.setMax(musicService.getTimeTotal());
                seekBarBottom.setProgress(musicService.getCurrentPosition());
                iv_Pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (musicService.isPng()) {
                            musicService.pauseSong();
                            iv_Pause.setImageResource(R.drawable.play);
                        } else {
                            musicService.pauseToPlaySong();
                            iv_Pause.setImageResource(R.drawable.pause);
                        }
                    }
                });
                seekBarBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
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
        } else if (musicService1 != null && musicService1.isPng()) {
            tv_SongBottom.setText(musicService1.getNameSong());
            tv_ArtistBottom.setText(musicService1.getNameArtist());
            seekBarBottom.setMax(musicService1.getTimeTotal());
            iv_Pause.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (musicService1.isPng()) {
                        musicService1.pauseSong();
                        iv_Pause.setImageResource(R.drawable.play);
                    }else{
                        musicService1.pauseToPlaySong();
                        iv_Pause.setImageResource(R.drawable.pause);
                    }
                }
            });
            seekBarBottom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    musicService1.seekTo(seekBar.getProgress());
                }

            });
        }
    }

    private void updateTimeSong() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                if (musicService != null && musicBound && musicService.isPng()) {
                    seekBarBottom.setProgress(musicService.getCurrentPosition());
                    tv_SongBottom.setText(musicService.getNameSong());
                    tv_ArtistBottom.setText(musicService.getNameArtist());
                    musicService.autoNextSong();
                    Log.d("chay vao day", "run:"+musicService.getNameSong());
                }
                handler.postDelayed(this, 500);
            }
        }, 100);
    }

    public void getData() {
        //songAdapter.notifyDataSetChanged();
        ContentResolver cr = getApplicationContext().getContentResolver();
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
                int idALbums = musicCursor.getInt(albumsColums);
                songsList.add(new Songs(thisId, thisTitle, thisArtist, idALbums));

            }
            while (musicCursor.moveToNext());
        }


    }

    // permisson android >=6.0
    private boolean checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String check : permissions) {
                int status = checkSelfPermission(check);
                if (status == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(permissions, 0);
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (checkPermissions()) {
            initView();
            addEvents();

        } else {
            finish();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //update iv_Pause on BottomDisPlay
    public void updateBottomControlls() {

        if (musicService != null && musicBound) {
            if (musicService.isPng()) {
                iv_Pause.setImageResource(R.drawable.pause);

            } else {
                iv_Pause.setImageResource(R.drawable.play);
            }
        }
    }
}
