package com.nowist.android.moviesglobe.ui.moviesdetail;

import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.db.DbHelper;
import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.network.ApiHelper;
import com.nowist.android.moviesglobe.data.network.models.MovieDetail;

/**
 * Created by mayankverma on 11/08/17.
 */

public class MovieDetailPresenter implements MovieDetailContract.UserActionsListener {

    private final ApiHelper mApiHelper;
    private final DbHelper mDbHelper;
    private final MovieDetailContract.View mMovieDetailView;

    public MovieDetailPresenter(DbHelper dbHelper, ApiHelper apiHelper, MovieDetailContract.View movieDetailView) {
        mDbHelper = dbHelper;
        mApiHelper = apiHelper;
        mMovieDetailView = movieDetailView;
    }

    @Override
    public void loadMovieDetails(@NonNull String movieId) {
        mMovieDetailView.setProgressIndicator(true);
        mApiHelper.getMovieDetail(new ApiHelper.GetMovieDetailCallback() {
            @Override
            public void onMovieDetailLoadSuccess(MovieDetail movieDetail) {
                mMovieDetailView.setProgressIndicator(false);
                mMovieDetailView.showMovieDetails(movieDetail);
            }

            @Override
            public void onMoviesDetailLoadFailed(String failureMessage) {
                mMovieDetailView.movieDetailLoadFailed(failureMessage);
            }
        }, movieId);
    }

    @Override
    public void setMovieFavourite(@NonNull final MovieModel model) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean isSuccess = mDbHelper.updateIsFav(model.isFav(), model.getMovieId());
                if (!isSuccess) {
                    mDbHelper.saveMovie(model);
                }
                mMovieDetailView.updateFavMovie(model.isFav() == 1 ? true : false);
            }
        }).start();
    }

    @Override
    public void updateMovieFavStatus(@NonNull final String movieId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (mDbHelper.isFavoriteMovie(movieId)) {
                    mMovieDetailView.updateFavIcon(true);
                } else {
                    mMovieDetailView.updateFavIcon(false);
                }
            }
        }).start();
    }

}
