package com.nowist.android.moviesglobe.data.db;


import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.network.models.Movie;

import java.util.List;

/**
 * Entry point to access movie data from Local Db
 */
public interface DbHelper {

    List<MovieModel> getAllFavMovies();

    MovieModel getMovie(@NonNull String movieId);

    Boolean saveMovie(@NonNull MovieModel movie);

    Boolean updateIsFav(int isFav, @NonNull String movieId);

    Boolean isFavoriteMovie(@NonNull String movieId);
}
