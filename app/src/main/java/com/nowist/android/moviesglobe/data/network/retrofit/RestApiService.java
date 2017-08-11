package com.nowist.android.moviesglobe.data.network.retrofit;

import com.nowist.android.moviesglobe.data.network.models.MovieDetail;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestApiService {

    @GET("3/movie/popular")
    Call<MoviesData> getPopularMovies(@Query("api_key") String apiKey, @Query("page") String pageNum);

    @GET("3/movie/{movie_id}")
    Call<MovieDetail> getMovieDetail(@Path("movie_id") String movieId, @Query("api_key") String apiKey);
}
