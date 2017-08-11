package com.nowist.android.moviesglobe.ui.movies;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.network.models.Movie;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;

import java.util.List;

/**
 * Specifies contract between view and presenter
 */

public interface MoviesContract {
    interface View {

        void setProgressIndicator(boolean active);

        void showMovies(MoviesData movieData);

        void movieLoadFailed(String message);

        void showMovieDetailUi(String movieId);

        void setMoreLoaded();

    }

    interface UserActionsListener {
        void loadMovies(boolean forceUpdate, int pageNum);

        void openMovieDetails(@NonNull String movieId);
    }
}
