package com.vietdung.beautymusic.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.model.Albums;
import com.vietdung.beautymusic.model.Songs;

import java.util.List;

public class SongAlbum1Adapter extends RecyclerView.Adapter<SongAlbum1Adapter.RecyclerviewHolder> {
    private List<Songs> songsList;
    List<Albums>albumsList;
    Activity context;
    Activity getContext;


    //MusicService musicService;
    public static String rq_itent_album="123456";
//    public static String rq_itent_position="xyz";

    public SongAlbum1Adapter(List<Songs> songsList, Activity context, int idAlbums) {
        this.songsList = songsList;
        this.context = context;
        this.albumsList = albumsList;
       // this.idAlbums = idAlbums;
       // this.musicService =musicService;
    }

    @NonNull
    @Override
    public SongAlbum1Adapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_songs, parent, false);
        return new SongAlbum1Adapter.RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongAlbum1Adapter.RecyclerviewHolder holder, final int position) {
        holder.tv_NameSong.setText(songsList.get(position).getNameSong());
        holder.tv_NameAuthor.setText(songsList.get(position).getNameAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                musicService.setSong(position);
//                musicService.playSong();
                Intent i = new Intent(context,PlayMussicActivity.class);
                i.putExtra(FragmentSongAdapter.rq_itent_id,songsList.get(position).getId());
                i.putExtra(FragmentSongAdapter.rq_itent_position,position);
                i.putExtra(rq_itent_album,songsList.get(position).getIdAlbums());
                i.putExtra(FragmentSongAdapter.rq_itent_screen,123);
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Beats;
        TextView tv_NameSong;
        TextView tv_NameAuthor;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Beats = itemView.findViewById(R.id.ivBeats);
            tv_NameAuthor = itemView.findViewById(R.id.tvAuthorName);
            tv_NameSong = itemView.findViewById(R.id.tvSongName);
        }
    }
}
