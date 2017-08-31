package com.rikkeisoft.music.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.rikkeisoft.music.Model.Album;
import com.rikkeisoft.music.Model.Artist;
import com.rikkeisoft.music.Model.Song;

import java.util.ArrayList;

/**
 * Created by nguyenquanghung on 8/25/17.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "db_music";
    //table name
    private static final String TABLE_NAME = "tbl_listsong";
    //columns
    private static final String KEY_ID = "ls_id";
    private static final String KEY_NAME = "ls_name";
    private static final String KEY_ARTIST = "ls_artist";
    private static final String KEY_PATH = "ls_path";
    private static final String KEY_ALBUM = "ls_album";
    private static final String KEY_ALBUM_ART = "ls_album_art";
    private static final String KEY_ALBUM_ID = "ls_album_key";

    Context context;

    public DatabaseHandler(Context contexteDatabase) {
        super(contexteDatabase, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = contexteDatabase;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_table = "Create table " + TABLE_NAME + " (" +
                KEY_ID + " Integer primary key autoincrement," +
                KEY_NAME + " text not null," +
                KEY_ARTIST + " text not null," +
                KEY_PATH + " text not null unique," +
                KEY_ALBUM + " text," +
                KEY_ALBUM_ID +" text)";
        db.execSQL(create_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public long addSong (Song song){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME,song.getName());
        values.put(KEY_ARTIST,song.getArtist());
        values.put(KEY_PATH,song.getPath());
        values.put(KEY_ALBUM,song.getAlbum());
//        values.put(KEY_ALBUM_ART,song.getAlbum_path());
        values.put(KEY_ALBUM_ID,song.getAlbum_id());
        long kq = db.insert(TABLE_NAME,null,values);
        db.close();
        return kq;
    }
    public ArrayList<Song> getListSong(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> mList = new ArrayList<>();
        String sql = "Select "+KEY_ID + "," + KEY_NAME + ","+KEY_ARTIST+","+KEY_PATH+","+KEY_ALBUM+","+KEY_ALBUM_ID
                + " From "+ TABLE_NAME+" ORDER BY "+KEY_NAME +" ASC";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            mList.add(new Song(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
        }
        db.close();
        return mList;
    }
    public ArrayList<Album> getListAlbums(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Album> mList = new ArrayList<>();
        String sql = "Select "+KEY_ALBUM+","+ KEY_ALBUM_ID + ",Count("+KEY_NAME+"),"+KEY_PATH
                + " From "+ TABLE_NAME+" GROUP BY "+KEY_ALBUM;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            mList.add(new Album(cursor.getString(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(3)));
        }
        db.close();
        return mList;
    }
    public ArrayList<Artist> getListArtist(){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Artist> mList = new ArrayList<>();
        String sql = "Select "+ KEY_ARTIST +",Count("+ KEY_ALBUM +"),Count("+ KEY_NAME
                + "),"+KEY_PATH+" From "+ TABLE_NAME+" GROUP BY "+KEY_ARTIST;
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            mList.add(new Artist(cursor.getString(0),cursor.getInt(1),cursor.getInt(2),cursor.getString(3)));
        }
        db.close();
        return mList;
    }
    public ArrayList<Song> getListSongsOfAlbum(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> mList = new ArrayList<>();
        String sql = "Select "+KEY_ID + "," + KEY_NAME + ","+KEY_ARTIST+","+KEY_PATH+","+KEY_ALBUM+","+KEY_ALBUM_ID
                + " From "+ TABLE_NAME+" WHERE "+KEY_ALBUM+"='"+name+"' ORDER BY "+KEY_NAME +" ASC";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            mList.add(new Song(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
        }
        db.close();
        return mList;
    }
    public ArrayList<Song> getListSongsOfArtist(String name){
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Song> mList = new ArrayList<>();
        String sql = "Select "+KEY_ID + "," + KEY_NAME + ","+KEY_ARTIST+","+KEY_PATH+","+KEY_ALBUM+","+KEY_ALBUM_ID
                + " From "+ TABLE_NAME+" WHERE "+KEY_ARTIST+"='"+name+"' ORDER BY "+KEY_NAME +" ASC";
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()){
            mList.add(new Song(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5)));
        }
        db.close();
        return mList;
    }
}
