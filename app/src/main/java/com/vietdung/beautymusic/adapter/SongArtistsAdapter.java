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
import com.vietdung.beautymusic.model.Author;
import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class SongArtistsAdapter extends RecyclerView.Adapter<SongArtistsAdapter.RecyclerviewHolder> {
    private List<Songs> songsList;
    List<Author> authorList;
    Activity context;
    //MusicService musicService;
    public static String rq_itent_album = "123456";
//    public static String rq_itent_position="xyz";

    public SongArtistsAdapter(List<Songs> songsList, Activity context, int idArtist) {
        this.songsList = songsList;
        this.context = context;
        //this.authorList = albumsList;
        // this.idAlbums = idAlbums;
        // this.musicService =musicService;
    }

    @NonNull
    @Override
    public SongArtistsAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_songs, parent, false);
        return new SongArtistsAdapter.RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongArtistsAdapter.RecyclerviewHolder holder, final int position) {
        holder.tv_NameSong.setText(songsList.get(position).getNameSong());
        holder.tv_NameAuthor.setText(songsList.get(position).getNameAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                musicService.setSong(position);
//                musicService.playSong();
                Intent i = new Intent(context, PlayMussicActivity.class);
                i.putExtra(FragmentSongAdapter.rq_itent_id, songsList.get(position).getId());
                i.putExtra(FragmentSongAdapter.rq_itent_position, position);
                i.putExtra(rq_itent_album, songsList.get(position).getIdAlbums());
                i.putExtra(FragmentSongAdapter.rq_itent_screen, 321);
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
