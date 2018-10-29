package com.vietdung.beautymusic.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vietdung.beautymusic.activity.MainActivity;
import com.vietdung.beautymusic.database.GetDataSdCard;

public class NotificationCloseBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
       MusicService musicService = MainActivity.musicService;
       MusicService musicService1 = (MusicService) AppController.getInstance().getMusicService();
       if(musicService!=null) {
           musicService.cancelNotification();
       }else{
           musicService1.cancelNotification();

       }
    }
}
