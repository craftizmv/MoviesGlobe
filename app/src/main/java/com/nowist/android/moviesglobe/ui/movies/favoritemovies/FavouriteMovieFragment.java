package com.nowist.android.moviesglobe.ui.movies.favoritemovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.moviesglobe.R;
import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.network.models.Movie;
import com.nowist.android.moviesglobe.di.Injections;
import com.nowist.android.moviesglobe.ui.movies.MoviesListAdapter;
import com.nowist.android.moviesglobe.ui.moviesdetail.MovieDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovieFragment extends Fragment implements FavouriteMoviesContract.View, MoviesListAdapter.MoviesAdapterOnClickListener {

    public static final String TAG = FavouriteMovieFragment.class.getSimpleName();
    private ArrayList<Movie> mDataList;
    private FavoriteMoviesPresenter mActionsListener;
    private RecyclerView mRecyclerView;
    private TextView mEmptyView;
    private MoviesListAdapter mAdapter;
    private Toolbar mToolbar;

    public FavouriteMovieFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControl();
    }

    private void initControl() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mActionsListener = new FavoriteMoviesPresenter(Injections.provideDbHelper(getContext().getApplicationContext()), this);
    }

    public static FavouriteMovieFragment newInstance() {
        return new FavouriteMovieFragment();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mActionsListener.loadAllFavoriteMovies();
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_movies_list);
        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new MoviesListAdapter(mDataList, this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mToolbar.setTitle(getString(R.string.favourite_movies));
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (getView() == null) {
            return;
        }
    }

    /**
     * ATTENTION !! - this is called from seperate thread
     *
     * @param movieData
     */
    @Override
    public void showMovies(final List<MovieModel> movieData) {
        //here we will alter the data to resuse the MoviesAdapter
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (movieData != null && !movieData.isEmpty()) {
                    for (MovieModel movieModel : movieData) {
                        Movie movie = new Movie();
                        movie.setId(Integer.valueOf(movieModel.getMovieId()));
                        movie.setPosterPath(movieModel.getPosterPath());
                        movie.setTitle(movieModel.getName());
                        movie.setVoteAverage(movieModel.getVoteAvg());
                        mDataList.add(movie);
                    }
                    if (!mDataList.isEmpty()) {
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    @Override
    public void movieLoadFailed(String message) {
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    /**
     * ATTENTION !! - This method is called from a different thread
     *
     * @param movieId
     */
    @Override
    public void showMovieDetailUi(final String movieId) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getContext(), MovieDetailActivity.class);
                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId);
                startActivity(intent);
            }
        });
    }


    @Override
    public void onClick(String movieId) {
        mActionsListener.openMovieDetails(movieId);
    }
}
