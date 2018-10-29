package com.vietdung.beautymusic.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.activity.PlayMussicActivity;

public class NotificationPrevBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        MusicService musicService = MainActivity.musicService;
        MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
        MainActivity mainActivity = (MainActivity) AppController.getInstance().getMainActivity();
        if(musicService!=null){
            musicService.backSong();
        }else{
            musicService1.backSong();
        }
        if(mainActivity!=null){
            mainActivity.updateBottomControlls();
        }

    }
}
