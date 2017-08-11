package com.nowist.android.moviesglobe.data.db.models;

public class MovieModel {

    private String name;
    private String movieId;
    private int isFav;
    private Double voteAvg;
    private String posterPath;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int isFav() {
        return isFav;
    }

    public void setFav(int fav) {
        isFav = fav;
    }

    public Double getVoteAvg() {
        return voteAvg;
    }

    public void setVoteAvg(Double voteAvg) {
        this.voteAvg = voteAvg;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }
}
