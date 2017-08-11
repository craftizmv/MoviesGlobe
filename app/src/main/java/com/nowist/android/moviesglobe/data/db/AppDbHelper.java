package com.nowist.android.moviesglobe.data.db;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.db.sqlite.MoviesDbManager;

import java.util.List;

public class AppDbHelper implements DbHelper {

    private Context mContext;

    public AppDbHelper(Context context) {
        mContext = context;
    }

    @Override
    public List<MovieModel> getAllFavMovies() {
        MoviesDbManager dbManager = new MoviesDbManager(mContext);
        dbManager.open();
        return dbManager.getAllFavMoviesData();
    }

    @Override
    public MovieModel getMovie(@NonNull String movieId) {
        MoviesDbManager dbManager = new MoviesDbManager(mContext);
        dbManager.open();
        return dbManager.getMovieData(movieId);
    }

    @Override
    public Boolean saveMovie(@NonNull MovieModel movie) {
        MoviesDbManager dbManager = new MoviesDbManager(mContext);
        dbManager.open();
        return dbManager.insertMovieData(movie);
    }

    @Override
    public Boolean updateIsFav(int isFav, String movieId) {
        MoviesDbManager dbManager = new MoviesDbManager(mContext);
        dbManager.open();
        return dbManager.updateIsFavMovieData(isFav, movieId);
    }

    @Override
    public Boolean isFavoriteMovie(@NonNull String movieId) {
        MoviesDbManager dbManager = new MoviesDbManager(mContext);
        dbManager.open();
        return dbManager.isMovieFavourite(movieId);
    }
}
