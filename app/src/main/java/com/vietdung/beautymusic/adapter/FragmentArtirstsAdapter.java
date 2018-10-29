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
import com.vietdung.beautymusic.activity.ArtistsActivity;
import com.vietdung.beautymusic.model.Author;

import java.util.ArrayList;
import java.util.List;

public class FragmentArtirstsAdapter extends RecyclerView.Adapter<FragmentArtirstsAdapter.RecyclerviewHolder> {
    List<Author> authorList;
    Activity context;

    public final static String rq_request_idArtist = "idArtist";

    public FragmentArtirstsAdapter(List<Author> authorList, Activity context) {
        this.authorList = authorList;
        this.context = context;
    }

    @NonNull
    @Override
    public FragmentArtirstsAdapter.RecyclerviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.custom_albums, parent, false);
        return new RecyclerviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentArtirstsAdapter.RecyclerviewHolder holder, final int position) {
        holder.tvAlbumsAuthorName.setText(authorList.get(position).getNameAuthor());
        holder.tvNumberSong.setText(authorList.get(position).getNumberAlbum()+" album |"+authorList.get(position).getNumberSong()+" song");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ArtistsActivity.class);
                intent.putExtra(rq_request_idArtist,authorList.get(position).getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return authorList.size();
    }

    public class RecyclerviewHolder extends RecyclerView.ViewHolder {
        ImageView iv_Albums;
        TextView tvAlbumsAuthorName;
        TextView tvNumberSong;

        public RecyclerviewHolder(View itemView) {
            super(itemView);
            iv_Albums = itemView.findViewById(R.id.ivAlbums);
            tvNumberSong= itemView.findViewById(R.id.tvAlbumsAuthorName);
            tvAlbumsAuthorName = itemView.findViewById(R.id.tvAlbumsSong);
        }
    }

    public void setfilter(List<Author> listAuthor) {
        authorList = new ArrayList<>();
        authorList.addAll(listAuthor);
        notifyDataSetChanged();
    }
}
