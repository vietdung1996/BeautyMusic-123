package com.vietdung.beautymusic.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.presenter.called_listener.OnCustomerListChangedListener;

import java.util.Collections;
import java.util.List;

public class SongPlayingQueueAdapter extends RecyclerView.Adapter<SongPlayingQueueAdapter.RecyclerviewHolder> {
    List<Songs> songsList;
    Activity context;
    private OnCustomerListChangedListener onCustomerListChangedListener;

    public SongPlayingQueueAdapter(List<Songs> songsList, Activity context,OnCustomerListChangedListener onCustomerListChangedListener) {
        this.songsList = songsList;
        this.context = context;
        this.onCustomerListChangedListener = onCustomerListChangedListener;
    }

    @NonNull
    @Override
    public RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_song_playing_queue, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewHolder holder, final int position) {
        holder.tv_NameSong.setText(songsList.get(position).getNameSong());
        holder.tv_NameAuthor.setText(songsList.get(position).getNameAuthor());
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(songsList, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(songsList, i, i - 1);
            }
        }
        onCustomerListChangedListener.onNoteListChanged(songsList);
        notifyItemMoved(fromPosition, toPosition);
    }


    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Beats;
        TextView tv_NameSong;
        TextView tv_NameAuthor;
        ImageView iv_Corner;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Beats = itemView.findViewById(R.id.ivBeatsPlaying);
            tv_NameAuthor = itemView.findViewById(R.id.tvAuthorNamePlaying);
            tv_NameSong = itemView.findViewById(R.id.tvSongNamePlaying);
            //iv_Corner = itemView.findViewById(R.id.ivCornerPlaying);
        }


    }
}
