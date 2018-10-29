package com.vietdung.beautymusic.until;

import android.app.Activity;
import android.app.Application;
import android.app.Service;

public class AppController extends Application {
    private static AppController mInstance;
    private Service musicService;
    private Activity playMusicActivity;
    private Activity mainActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static AppController getInstance() {
        return mInstance;
    }

    public Service getMusicService() {
        return musicService;
    }

    public void setMusicService(Service musicService) {
        this.musicService = musicService;
    }

    public Activity getPlayMucsicActivity() {
        return playMusicActivity;
    }

    public void setPlayMusicActivity(Activity playMusicActivity) {
        this.playMusicActivity = playMusicActivity;
    }

    public Activity getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(Activity mainActivity) {
        this.mainActivity = mainActivity;
    }
}
