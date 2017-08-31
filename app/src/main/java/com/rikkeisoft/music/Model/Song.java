package com.rikkeisoft.music.Model;

/**
 * Created by nguyenquanghung on 8/21/17.
 */

public class Song {
    private int thumbnail;
    private String name;
    private String path;
    private String artist;
    private String album;
    private String album_path;
    private String album_id;
    private int id;

    public Song() {
    }

    public Song(int id,String name, String artist,String path, String album, String album_id) {
        this.thumbnail = thumbnail;
        this.name = name;
        this.path = path;
        this.artist = artist;
        this.album = album;
//        this.album_path = album_path;
        this.album_id = album_id;
        this.id = id;
    }

    public Song(String name, String artist,String path, String album, String album_key) {
        this.name = name;
        this.path = path;
        this.artist = artist;
        this.album = album;
//        this.album_path = album_path;
        this.album_id = album_id;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum_path() {
        return album_path;
    }

    public void setAlbum_path(String album_path) {
        this.album_path = album_path;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_key(String album_id) {
        this.album_id = album_id;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }
}
