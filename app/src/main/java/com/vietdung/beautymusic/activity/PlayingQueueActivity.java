package com.vietdung.beautymusic.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.adapter.SongPlayingQueueAdapter;
import com.vietdung.beautymusic.database.GetDataSdCard;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.presenter.ItemTouchListenner;
import com.vietdung.beautymusic.presenter.SimpleItemTouchHelperCallback;
import com.vietdung.beautymusic.presenter.called_listener.OnCustomerListChangedListener;

import java.util.ArrayList;
import java.util.List;

public class PlayingQueueActivity extends AppCompatActivity implements OnCustomerListChangedListener {
    Toolbar tb_Playing;
    List<Songs> songsList;
    SongPlayingQueueAdapter songPlayingQueueAdapter;
    RecyclerView rv_Playing;

    public final static String rq_newList = "newlist";
    int screen = 0;
    GetDataSdCard getDataSdCard;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public static final String LIST_OF_SORTED_DATA_ID = "json_list_sorted_data_id";
    public final static String PREFERENCE_FILE = "preference_file";



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playing_queue);


        initView();
        addEvents();
    }

    private void addEvents() {
        setToolBar();
        addItemTouchCallback(rv_Playing);
        //getData();
//        songsList = getSampleData();
//        songPlayingQueueAdapter.notifyDataSetChanged();
//        rv_Playing.setAdapter(songPlayingQueueAdapter);
        Log.d("listsong", "null "+songsList);

    }

    private void getData() {

        getDataSdCard.getData();
        Log.d("songlistplaying", " " + songsList);
        songPlayingQueueAdapter.notifyDataSetChanged();

    }

    private void addItemTouchCallback(RecyclerView recyclerView) {
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(new ItemTouchListenner() {
            @Override
            public void onMode(int oldPositoin, int newPosition) {
                songPlayingQueueAdapter.onMove(oldPositoin, newPosition);
            }


        });
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void setToolBar() {
        setSupportActionBar(tb_Playing);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // Intent intent = new Intent();
    }

    private void initView() {
        tb_Playing = findViewById(R.id.tbPlayingQueue);
        rv_Playing = findViewById(R.id.rvPlaying);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_Playing.setLayoutManager(layoutManager);
        sharedPreferences = this.getApplicationContext().getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE);
        editor =sharedPreferences.edit();
        songsList = getSampleData();
        songPlayingQueueAdapter = new SongPlayingQueueAdapter(songsList, this,this);
        rv_Playing.setAdapter(songPlayingQueueAdapter);

    }

    @Override
    public void onNoteListChanged(List<Songs> songsList) {
        List<Long> listOfSortedSongId = new ArrayList<Long>();
        for (Songs song: songsList){
            listOfSortedSongId.add((long) song.getId());
        }
        //convert the List of Longs to a JSON string
        Gson gson = new Gson();
        String jsonListOfSortedCustomerIds = gson.toJson(listOfSortedSongId);
        //save to SharedPreference
        editor.putString(LIST_OF_SORTED_DATA_ID, jsonListOfSortedCustomerIds).commit();
        editor.commit();

    }

    private List<Songs> getSampleData(){
        int screen = getIntent().getIntExtra(PlayMussicActivity.rq_screen, 0);
        getDataSdCard = new GetDataSdCard(this, screen);
        //Get the song data
        List<Songs> songsList = getDataSdCard.getData() ;
        //create an empty array to hold the list of sorted Customers
        List<Songs> sortedSongs= new ArrayList<Songs>();
        //get the JSON array of the ordered of sorted customers
        String jsonListOfSortedCustomerId = sharedPreferences.getString(LIST_OF_SORTED_DATA_ID, "");


        //check for null
        if (!jsonListOfSortedCustomerId.isEmpty()){

            //convert JSON array into a List<Long>
            Gson gson = new Gson();
            List<Long> listOfSortedCustomersId = gson.fromJson(jsonListOfSortedCustomerId, new TypeToken<List<Long>>(){}.getType());

            //build sorted list
            if (listOfSortedCustomersId != null && listOfSortedCustomersId.size() > 0){
                for (Long id: listOfSortedCustomersId){
                    for (Songs song: songsList){
                        if (song.getId()==id){
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
            if (songsList.size() > 0){
                sortedSongs.addAll(songsList);
            }
            return sortedSongs;
        }else {
            return songsList;
        }
    }
}
