package com.vietdung.beautymusic.model;

public class Albums {
    private int id;
    private String nameAlbums;
    private String nameAuthor;
    private String pathArt;

    public Albums(int id, String nameAlbums, String nameAuthor,String pathArt) {
        this.id = id;
        this.nameAlbums = nameAlbums;
        this.nameAuthor = nameAuthor;
        this.pathArt = pathArt;
    }

    public String getPathArt() {
        return pathArt;
    }

    public void setPathArt(String pathArt) {
        this.pathArt = pathArt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameAlbums() {
        return nameAlbums;
    }

    public void setNameSong(String nameAlbums) {
        this.nameAlbums = nameAlbums;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public void setNameAuthor(String nameAuthor) {
        this.nameAuthor = nameAuthor;
    }
}
