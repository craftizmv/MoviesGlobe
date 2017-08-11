package com.nowist.android.moviesglobe.data.network;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.network.models.Movie;
import com.nowist.android.moviesglobe.data.network.models.MovieDetail;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;

import java.util.List;

/**
 * Created by mayankverma on 10/08/17.
 */

public interface ApiHelper {

    interface LoadMoviesCallback {
        void onMoviesLoadSuccess(MoviesData movieData);

        void onMoviesLoadFailed(String failureMessage);

    }

    void getPopularMovies(@NonNull LoadMoviesCallback callback, int pageNum);

    interface GetMovieDetailCallback {
        void onMovieDetailLoadSuccess(MovieDetail movieDetail);

        void onMoviesDetailLoadFailed(String failureMessage);
    }

    void getMovieDetail(@NonNull GetMovieDetailCallback callback, String movieId);
}
