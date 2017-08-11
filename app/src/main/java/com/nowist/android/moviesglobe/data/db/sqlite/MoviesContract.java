package com.nowist.android.moviesglobe.data.db.sqlite;

import android.provider.BaseColumns;

/**
 * Created by mayankverma on 11/08/17.
 */

public class MoviesContract {

    public static final class MoviesEntry implements BaseColumns {
        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_NAME = "name";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_IS_FAV = "is_fav";
        public static final String COLUMN_RATING = "vote_avg";
        public static final String COLUMN_POSTER_PATH = "poster_path";
    }

}
