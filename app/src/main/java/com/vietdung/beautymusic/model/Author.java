package com.vietdung.beautymusic.model;

public class Author {
    private int id;
    private String numberAlbum;
    private String nameAuthor;
    private String numberSong;

    public Author(int id, String nameAuthor, String numberAlbum,String numberSong) {
        this.id = id;
        this.nameAuthor = nameAuthor;
        this.numberAlbum = numberAlbum;
        this.numberSong = numberSong;
    }

    public String getNumberSong() {
        return numberSong;
    }

    public void setNumberSong(String numberSong) {
        this.numberSong = numberSong;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumberAlbum() {
        return numberAlbum;
    }

    public void setNumberAlbum(String numberAlbum) {
        this.numberAlbum = numberAlbum;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }
}


