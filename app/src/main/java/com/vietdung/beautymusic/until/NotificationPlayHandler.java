package com.vietdung.beautymusic.until;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.activity.PlayMussicActivity;

public class NotificationPlayHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MusicService musicService = MainActivity.musicService;
        MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
        PlayMussicActivity playMussicActivity = (PlayMussicActivity) AppController.getInstance().getPlayMucsicActivity();
        MainActivity mainActivity = (MainActivity) AppController.getInstance().getMainActivity();
        if(musicService!=null){
            if(musicService.isPng()){
                musicService.pauseSong();
            }else{
                musicService.pauseToPlaySong();
            }

        }else{
            if(musicService1.isPng()){
                musicService1.pauseSong();
            }else{
                musicService1.pauseToPlaySong();
            }
        }
        if(playMussicActivity!=null){
            playMussicActivity.updatePlayorPause();
        }
        if(mainActivity!=null){
            mainActivity.updateBottomControlls();
        }
    }
}
