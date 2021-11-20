package com.example.cream.note2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoteDb extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "notes";
    public static final String CONTENT = "content";
    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String AUTHOR = "author";
    public static final String TITLE = "title";
    private Context mContext;

    public NoteDb(Context context) {
        super(context, "notes", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + " ( "
                + ID + " integer primary key AUTOINCREMENT,"
                + CONTENT + " TEXT,"
                + TIME + " TEXT,"
                + AUTHOR + " TEXT,"
                + TITLE + " TEXT)";
        db.execSQL(sql);
        sql = "create table image(id integer primary key AUTOINCREMENT,content BLOB)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}