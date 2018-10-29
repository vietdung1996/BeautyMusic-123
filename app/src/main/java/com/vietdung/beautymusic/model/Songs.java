package com.vietdung.beautymusic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Songs implements Parcelable {
    private int id;
    private String nameSong;
    private String nameAuthor ;
    private String albums;
    private int idAlbums;

    public Songs(int id, String nameSong, String nameAuthor,int idAlbums) {
        this.id = id;
        this.nameSong = nameSong;
        this.nameAuthor = nameAuthor;
        this.idAlbums = idAlbums;
    }

    protected Songs(Parcel in) {
        id = in.readInt();
        nameSong = in.readString();
        nameAuthor = in.readString();
        albums = in.readString();
        idAlbums = in.readInt();
    }

    public static final Creator<Songs> CREATOR = new Creator<Songs>() {
        @Override
        public Songs createFromParcel(Parcel in) {
            return new Songs(in);
        }

        @Override
        public Songs[] newArray(int size) {
            return new Songs[size];
        }
    };

    public int getIdAlbums() {
        return idAlbums;
    }

    public void setIdAlbums(int idAlbums) {
        this.idAlbums = idAlbums;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }

    public String getAlbums() {
        return albums;
    }

    public void setAlbums(String albums) {
        this.albums = albums;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(nameSong);
        parcel.writeString(nameAuthor);
        parcel.writeString(albums);
        parcel.writeInt(idAlbums);

    }
}
