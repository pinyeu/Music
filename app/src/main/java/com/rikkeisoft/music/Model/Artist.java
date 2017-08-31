package com.rikkeisoft.music.Model;

/**
 * Created by nguyenquanghung on 8/22/17.
 */

public class Artist {
    private String name;
    private int numAlbum;
    private int numSong;
    private String path;

    public Artist(String name, int numAlbum, int numSong, String path) {
        this.name = name;
        this.numAlbum = numAlbum;
        this.numSong = numSong;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumAlbum() {
        return numAlbum;
    }

    public void setNumAlbum(int numAlbum) {
        this.numAlbum = numAlbum;
    }

    public int getNumSong() {
        return numSong;
    }

    public void setNumSong(int numSong) {
        this.numSong = numSong;
    }
}
