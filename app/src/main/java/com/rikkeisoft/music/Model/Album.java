package com.rikkeisoft.music.Model;

/**
 * Created by nguyenquanghung on 8/22/17.
 */

public class Album {
    private String name;
    private int id;
    private int numSong;
    private String pathCoverArt;

    public Album(String name, int id, int numSong) {
        this.name = name;
        this.id = id;
        this.numSong = numSong;
    }

    public Album(String name, int id, int numSong, String pathCoverArt) {
        this.name = name;
        this.id = id;
        this.numSong = numSong;
        this.pathCoverArt = pathCoverArt;
    }

    public String getPathCoverArt() {
        return pathCoverArt;
    }

    public void setPathCoverArt(String pathCoverArt) {
        this.pathCoverArt = pathCoverArt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumSong() {
        return numSong;
    }

    public void setNumSong(int numSong) {
        this.numSong = numSong;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
