package com.vietdung.beautymusic.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.PlayMussicActivity;
import com.vietdung.beautymusic.model.Songs;

import java.util.ArrayList;
import java.util.List;

public class FragmentSongAdapter extends RecyclerView.Adapter<FragmentSongAdapter.RecyclerviewHolder> {
    private List<Songs> songsList;
    FragmentActivity context;
    public static String rq_itent_id = "abc";
    public static String rq_itent_position = "xyz";
    public static String rq_itent_screen = "1996";
    //MusicService musicService;

    public FragmentSongAdapter(List<Songs> songsList, FragmentActivity context) {
        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_songs, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewHolder holder, final int position) {
        holder.tv_NameSong.setText(songsList.get(position).getNameSong());
        holder.tv_NameAuthor.setText(songsList.get(position).getNameAuthor());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayMussicActivity.class);
                intent.putExtra(rq_itent_id, songsList.get(position).getId());
                intent.putExtra(rq_itent_position, position);
                intent.putExtra(rq_itent_screen,111);
                context.startActivity(intent);

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

    public void setfilter(List<Songs> listSong)
    {
        songsList=new ArrayList<>();
        songsList.addAll(listSong);
        notifyDataSetChanged();
    }
}
