package com.nowist.android.moviesglobe.data.db.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nowist.android.moviesglobe.data.db.sqlite.MoviesContract.MoviesEntry;

public class MoviesOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;

    public MoviesOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_STATEMENT_MOVIES_TABLE = "CREATE TABLE " + MoviesEntry.TABLE_NAME
                + " ("
                + MoviesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + MoviesEntry.COLUMN_MOVIE_NAME + " TEXT NOT NULL, "
                + MoviesEntry.COLUMN_MOVIE_ID + " VARCHAR NOT NULL, "
                + MoviesEntry.COLUMN_IS_FAV + "  INT NOT NULL DEFAULT 0, "
                + MoviesEntry.COLUMN_RATING + " FLOAT NOT NULL, "
                + MoviesEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL"
                + ");";

        db.execSQL(CREATE_STATEMENT_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MoviesEntry.TABLE_NAME);
        onCreate(db);
    }
}
