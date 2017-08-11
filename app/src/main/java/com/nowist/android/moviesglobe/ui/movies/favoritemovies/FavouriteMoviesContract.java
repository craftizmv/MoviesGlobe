package com.nowist.android.moviesglobe.ui.movies.favoritemovies;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.models.MovieModel;

import java.util.List;

public interface FavouriteMoviesContract {
    interface View {

        void setProgressIndicator(boolean active);

        void showMovies(List<MovieModel> movieData);

        void movieLoadFailed(String message);

        void showMovieDetailUi(String movieId);

    }

    interface UserActionsListener {
        void loadAllFavoriteMovies();

        void openMovieDetails(@NonNull String movieId);
    }
}
