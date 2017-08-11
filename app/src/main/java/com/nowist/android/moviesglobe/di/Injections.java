package com.nowist.android.moviesglobe.di;

import android.content.Context;

import com.nowist.android.moviesglobe.data.db.AppDbHelper;
import com.nowist.android.moviesglobe.data.db.DbHelper;
import com.nowist.android.moviesglobe.data.network.ApiHelper;
import com.nowist.android.moviesglobe.data.network.AppApiHelper;

public class Injections {

    public static ApiHelper provideApiHelper(Context context) {
        return new AppApiHelper(context);
    }

    public static DbHelper provideDbHelper(Context context) {
        return new AppDbHelper(context);
    }
}
