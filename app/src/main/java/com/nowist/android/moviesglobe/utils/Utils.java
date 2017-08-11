package com.nowist.android.moviesglobe.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by mayankverma on 10/08/17.
 */

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static boolean isCursorNotEmpty(Cursor cursor) {
        boolean result = false;
        if (cursor != null && cursor.getCount() > 0) {
            result = true;
        }
        return result;
    }

}
