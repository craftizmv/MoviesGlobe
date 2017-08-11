package com.nowist.android.moviesglobe.data.db.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.utils.Utils;

import java.util.ArrayList;
import java.util.List;

//todo can be made singleton to return one instance
public class MoviesDbManager {

    private MoviesOpenHelper dbOpenHelper;
    private Context context;
    private SQLiteDatabase database;

    public MoviesDbManager(Context context) {
        this.context = context;
    }

    public MoviesDbManager open() throws SQLException {
        dbOpenHelper = new MoviesOpenHelper(context);
        database = dbOpenHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbOpenHelper.close();
    }

    public List<MovieModel> getAllFavMoviesData() {
        String selection = MoviesContract.MoviesEntry.COLUMN_IS_FAV + "=?";
        String[] selectionArgs = new String[]{String.valueOf(1)};
        Cursor cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        List<MovieModel> movieModelList = null;
        if (Utils.isCursorNotEmpty(cursor)) {
            movieModelList = new ArrayList<>();
            while (cursor.moveToNext()) {
                MovieModel movieModel = new MovieModel();
                movieModel.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME)));
                movieModel.setMovieId(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
                movieModel.setFav(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAV)));
                movieModel.setVoteAvg(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING)));
                movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH)));
                movieModelList.add(movieModel);
            }
        }
        cursor.close();
        return movieModelList;
    }

    public MovieModel getMovieData(String movieId) {
        String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{movieId};
        MovieModel movieModel = null;
        Cursor cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (Utils.isCursorNotEmpty(cursor)) {
            movieModel = new MovieModel();
            movieModel.setName(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME)));
            movieModel.setMovieId(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID)));
            movieModel.setFav(cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAV)));
            movieModel.setVoteAvg(cursor.getDouble(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RATING)));
            movieModel.setPosterPath(cursor.getString(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH)));
        }
        cursor.close();
        return movieModel;
    }

    public boolean insertMovieData(MovieModel model) {
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_NAME, model.getName());
        values.put(MoviesContract.MoviesEntry.COLUMN_MOVIE_ID, model.getMovieId());
        values.put(MoviesContract.MoviesEntry.COLUMN_IS_FAV, model.isFav());
        values.put(MoviesContract.MoviesEntry.COLUMN_RATING, model.getVoteAvg());
        values.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, model.getPosterPath());
        long rowId = database.insert(MoviesContract.MoviesEntry.TABLE_NAME, null, values);
        return rowId > 0 ? true : false;
    }

    public boolean updateIsFavMovieData(int isFav, String movieId) {
        String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{movieId};
        ContentValues values = new ContentValues();
        values.put(MoviesContract.MoviesEntry.COLUMN_IS_FAV, isFav);
        int numOfRowsAffected = database.update(MoviesContract.MoviesEntry.TABLE_NAME, values, selection, selectionArgs);
        return numOfRowsAffected > 0 ? true : false;
    }

    public boolean isMovieFavourite(String movieId) {
        boolean result = false;
        String selection = MoviesContract.MoviesEntry.COLUMN_MOVIE_ID + "=?";
        String[] selectionArgs = new String[]{movieId};
        Cursor cursor = database.query(MoviesContract.MoviesEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);
        if (Utils.isCursorNotEmpty(cursor)) {
            cursor.moveToFirst();
            if (cursor.getInt(cursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_IS_FAV)) == 1) {
                result = true;
            }
        }
        cursor.close();
        return result;
    }
}
