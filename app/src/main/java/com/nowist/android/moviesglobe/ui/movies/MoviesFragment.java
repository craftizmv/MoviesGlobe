package com.nowist.android.moviesglobe.ui.movies;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.moviesglobe.R;
import com.nowist.android.moviesglobe.data.network.models.Movie;
import com.nowist.android.moviesglobe.data.network.models.MoviesData;
import com.nowist.android.moviesglobe.di.Injections;
import com.nowist.android.moviesglobe.ui.moviesdetail.MovieDetailActivity;
import com.nowist.android.moviesglobe.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MoviesFragment extends Fragment implements MoviesContract.View, MoviesListAdapter.MoviesAdapterOnClickListener {

    public static final String TAG = MoviesFragment.class.getSimpleName();
    private static final int DEFAULT_PAGE_NUM = 1;
    private static int mLastRequestedPageNum = 1;
    private static int totalPageCount = 0;
    private MoviesContract.UserActionsListener mActionsListener;
    private MoviesListAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private List<Movie> mDataList;
    private TextView mEmptyView;
    private Toolbar mToolbar;
//    private ProgressBar mProgressBar;

    public MoviesFragment() {

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
        mActionsListener = new MoviesPresenter(Injections.provideApiHelper(getContext().getApplicationContext()), this);
    }

    public static MoviesFragment newInstance() {
        return new MoviesFragment();
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
        mToolbar.setTitle(getString(R.string.popular_movies));
        if (Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
            mActionsListener.loadMovies(false, DEFAULT_PAGE_NUM);
        } else {
            Toast.makeText(getActivity(), getString(R.string.check_conn), Toast.LENGTH_SHORT).show();
            showEmptyView();
        }
    }

    private void initViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_movies_list);
        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        mAdapter = new MoviesListAdapter(mDataList, this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);

//        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);
        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);

        SwipeRefreshLayout swipeRefreshLayout =
                (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), R.color.colorPrimary),
                ContextCompat.getColor(getActivity(), R.color.colorAccent),
                ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.isNetworkAvailable(getActivity())) {
                    mActionsListener.loadMovies(true, DEFAULT_PAGE_NUM);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.check_conn), Toast.LENGTH_SHORT).show();
                    showEmptyView();
                }
            }
        });

        setOnLoadMoreListner();

    }

    private void setOnLoadMoreListner() {
        mAdapter.setOnLoadMoreListener(new MoviesListAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (++mLastRequestedPageNum < totalPageCount) {
                    if (Utils.isNetworkAvailable(getActivity())) {
                        mActionsListener.loadMovies(false, mLastRequestedPageNum);
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.check_conn), Toast.LENGTH_SHORT).show();
                        showEmptyView();
                    }
                }
            }
        });
    }


    @Override
    public void setProgressIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }

        final SwipeRefreshLayout srl =
                (SwipeRefreshLayout) getView().findViewById(R.id.refresh_layout);

        srl.post(new Runnable() {
            @Override
            public void run() {
                srl.setRefreshing(active);
            }
        });

        /*if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }*/
    }

    @Override
    public void showMovies(MoviesData movieData) {
        showRv();
        if (movieData.getMovies() != null) {
            totalPageCount = movieData.getTotalPages();
            mAdapter.appendData(movieData.getMovies());
            setMoreLoaded();
        }
    }

    @Override
    public void movieLoadFailed(String message) {
        setMoreLoaded();
        if (!TextUtils.isEmpty(message)) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
        showEmptyView();
    }

    private void showEmptyView() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showRv() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    public void showMovieDetailUi(String movieId) {
        Intent intent = new Intent(getContext(), MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.EXTRA_MOVIE_ID, movieId);
        startActivity(intent);
    }

    @Override
    public void setMoreLoaded() {
        mAdapter.setLoaded();
    }

    @Override
    public void onClick(String movieId) {
        mActionsListener.openMovieDetails(movieId);
    }
}
