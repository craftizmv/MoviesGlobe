package com.nowist.android.moviesglobe.ui.movies.favoritemovies;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.DbHelper;
import com.nowist.android.moviesglobe.data.db.models.MovieModel;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by mayankverma on 11/08/17.
 */

public class FavoriteMoviesPresenter implements FavouriteMoviesContract.UserActionsListener {

    private final FavouriteMoviesContract.View mFavouriteMoviesView;
    private final DbHelper mDbHelper;

    public FavoriteMoviesPresenter(@NonNull DbHelper dbHelper, @NonNull FavouriteMoviesContract.View moviesView) {
        mDbHelper = dbHelper;
        mFavouriteMoviesView = moviesView;
    }


    @Override
    public void loadAllFavoriteMovies() {
        mFavouriteMoviesView.setProgressIndicator(true);
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<MovieModel> data = mDbHelper.getAllFavMovies();
                mFavouriteMoviesView.showMovies(data);
            }
        }).start();
    }

    @Override
    public void openMovieDetails(@NonNull String movieId) {
        checkNotNull(movieId, "Movie Id can not be null");
        mFavouriteMoviesView.showMovieDetailUi(movieId);
    }
}
