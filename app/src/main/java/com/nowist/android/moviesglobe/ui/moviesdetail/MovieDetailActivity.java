package com.nowist.android.moviesglobe.ui.moviesdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.nowist.android.moviesglobe.R;

/**
 * Displays movie detail screen
 */
public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE_ID = "MOVIE_ID";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        //setup the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        String movieId = getIntent().getStringExtra(EXTRA_MOVIE_ID);

        if (null == savedInstanceState) {
            initFragment(MovieDetailFragment.newInstance(movieId));
        }
    }

    private void initFragment(MovieDetailFragment movieDetailFragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.contentFrame, movieDetailFragment);
        transaction.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
