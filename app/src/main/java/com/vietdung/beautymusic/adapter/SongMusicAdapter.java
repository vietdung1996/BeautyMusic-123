package com.vietdung.beautymusic.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.model.Songs;
import com.vietdung.beautymusic.until.MusicService;

import java.util.List;

public class SongMusicAdapter extends RecyclerView.Adapter<SongMusicAdapter.RecyclerviewHolder>{
    private List<Songs> songsList;
    FragmentActivity context;


    public interface OnItemClickListener{
        void onItemClick(View song, int position);
    }

    private OnItemClickListener listener;

    public SongMusicAdapter(List<Songs> songsList, FragmentActivity context) {
        this.songsList = songsList;
        this.context = context;
    }

    @NonNull
    @Override
    public SongMusicAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_songs, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongMusicAdapter.RecyclerviewHolder holder, final int position) {
        holder.bind(songsList.get(position), listener);

    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }
    public void setClickListener(OnItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iv_Beats;
        TextView tv_NameSong;
        TextView tv_NameAuthor;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Beats = itemView.findViewById(R.id.ivBeats);
            tv_NameAuthor = itemView.findViewById(R.id.tvAuthorName);
            tv_NameSong = itemView.findViewById(R.id.tvSongName);
        }

        public void bind(final Songs item, final OnItemClickListener listener) {
            tv_NameSong.setText(item.getNameSong());
            tv_NameAuthor.setText(item.getNameAuthor());
            itemView.setOnClickListener((View.OnClickListener) this);
        }
        @Override
        public void onClick(View view) {
            listener.onItemClick(view,getAdapterPosition());
        }
    }

}




