package com.vietdung.beautymusic.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vietdung.beautymusic.R;
import com.vietdung.beautymusic.activity.AlbumsActivity;
import com.vietdung.beautymusic.model.Albums;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlbumsAdapter extends RecyclerView.Adapter<FragmentAlbumsAdapter.RecyclerviewHolder> {
    private List<Albums> albumsList;
    FragmentActivity context;
    public final static String idALbums = "1234";

    public FragmentAlbumsAdapter(List<Albums> albumsList, FragmentActivity context) {
        this.albumsList = albumsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_albums,parent,false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerviewHolder holder, final int position) {
        holder.tvAlbumsSong.setText(albumsList.get(position).getNameAlbums());
        holder.tvAlbumsAuthorName.setText(albumsList.get(position).getNameAuthor());
        Bitmap bitmap = BitmapFactory.decodeFile(albumsList.get(position).getPathArt());
        if (bitmap != null) {
            holder.iv_Albums.setImageBitmap(bitmap);
        } else {
            holder.iv_Albums.setImageResource(R.drawable.beatslogo);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AlbumsActivity.class);
                intent.putExtra(idALbums,albumsList.get(position).getId());
                intent.putExtra(FragmentSongAdapter.rq_itent_screen,123);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumsList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Albums;
        TextView tvAlbumsAuthorName;
        TextView tvAlbumsSong;
        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Albums = itemView.findViewById(R.id.ivAlbums);
            tvAlbumsAuthorName = itemView.findViewById(R.id.tvAlbumsAuthorName);
            tvAlbumsSong = itemView.findViewById(R.id.tvAlbumsSong);
        }
    }

    public void setfilter(List<Albums> listAlbums) {
        albumsList = new ArrayList<>();
        albumsList.addAll(listAlbums);
        notifyDataSetChanged();
    }
}
