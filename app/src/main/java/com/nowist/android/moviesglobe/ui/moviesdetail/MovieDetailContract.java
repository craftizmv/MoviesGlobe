package com.nowist.android.moviesglobe.ui.moviesdetail;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.network.models.MovieDetail;

/**
 * Created by mayankverma on 11/08/17.
 */

public interface MovieDetailContract {
    interface View {
        void setProgressIndicator(boolean active);

        void showMovieDetails(MovieDetail movieDetail);

        void movieDetailLoadFailed(String errorMessage);

        void updateFavMovie(boolean isSuccess);

        void updateFavIcon(boolean isFav);
    }

    interface UserActionsListener {
        void loadMovieDetails(@NonNull String movieId);

        void setMovieFavourite(@NonNull MovieModel model);

        void updateMovieFavStatus(@NonNull String movieId);
    }
}
