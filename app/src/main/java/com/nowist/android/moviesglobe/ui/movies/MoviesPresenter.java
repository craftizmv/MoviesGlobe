package com.nowist.android.moviesglobe.ui.movies;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.network.ApiHelper;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;
import com.nowist.android.moviesglobe.ui.movies.MoviesContract.UserActionsListener;

import static com.google.common.base.Preconditions.checkNotNull;


public class MoviesPresenter implements UserActionsListener {

    private final ApiHelper mApiHelper;
    private final MoviesContract.View mMoviesView;

    public MoviesPresenter(@NonNull ApiHelper apiHelper, @NonNull MoviesContract.View moviesView) {
        mApiHelper = apiHelper;
        mMoviesView = moviesView;
    }

    @Override
    public void loadMovies(boolean forceUpdate, int pageNum) {
        mMoviesView.setProgressIndicator(true);
        mApiHelper.getPopularMovies(new ApiHelper.LoadMoviesCallback() {
            @Override
            public void onMoviesLoadSuccess(MoviesData movieData) {
                mMoviesView.setProgressIndicator(false);
                mMoviesView.showMovies(movieData);
            }

            @Override
            public void onMoviesLoadFailed(String failureMessage) {
                mMoviesView.movieLoadFailed(failureMessage);
            }
        }, pageNum);
    }

    @Override
    public void openMovieDetails(@NonNull String movieId) {
        checkNotNull(movieId, "Movie Id can not be null");
        mMoviesView.showMovieDetailUi(movieId);
    }

}
