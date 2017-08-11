package com.nowist.android.moviesglobe.data.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.nowist.android.moviesglobe.data.network.models.MovieDetail;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;
import com.nowist.android.moviesglobe.data.network.retrofit.ApiUtils;
import com.nowist.android.moviesglobe.data.network.retrofit.RestApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mayankverma on 10/08/17.
 */

public class AppApiHelper implements ApiHelper {

    private Context mContext;

    public AppApiHelper(Context context) {
        this.mContext = context;
    }

    //mentioning the api key private to thid implementation
    private static final String API_KEY = "b66ffea8276ce576d60df52600822c88";

    /**
     * Implementation to fetch the popular movies data
     *
     * @param callback
     * @param pageNum
     */
    @Override
    public void getPopularMovies(@NonNull final LoadMoviesCallback callback, int pageNum) {

        RestApiService apiService = ApiUtils.getApiService(mContext);
        Call<MoviesData> call = apiService.getPopularMovies(API_KEY, String.valueOf(pageNum));
        call.enqueue(new Callback<MoviesData>() {
            @Override
            public void onResponse(Call<MoviesData> call, Response<MoviesData> response) {
                if (response != null && response.body() != null) {
                    MoviesData data = response.body();
                    if (data != null) {
                        callback.onMoviesLoadSuccess(data);
                    } else {
                        callback.onMoviesLoadFailed("Api returned empty data");
                    }
                } else {
                    callback.onMoviesLoadFailed("Api returned null response");
                }
            }

            @Override
            public void onFailure(Call<MoviesData> call, Throwable t) {
                if (t != null) {
                    t.printStackTrace();
                }
                callback.onMoviesLoadFailed("Request Failed");
            }
        });

    }

    @Override
    public void getMovieDetail(@NonNull final GetMovieDetailCallback callback, String movieId) {
        RestApiService apiService = ApiUtils.getApiService(mContext);
        Call<MovieDetail> call = apiService.getMovieDetail(movieId, API_KEY);
        call.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                if (response != null && response.body() != null) {
                    MovieDetail detail = response.body();
                    if (detail != null) {
                        callback.onMovieDetailLoadSuccess(detail);
                    }
                } else {
                    callback.onMoviesDetailLoadFailed("Api returned null response");
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                if (t != null) {
                    t.printStackTrace();
                }
                callback.onMoviesDetailLoadFailed("Request Failed");
            }
        });
    }
}
