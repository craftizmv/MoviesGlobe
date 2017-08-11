package com.nowist.android.moviesglobe.ui.moviesdetail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nowist.android.moviesglobe.R;
import com.nowist.android.moviesglobe.data.db.models.MovieModel;
import com.nowist.android.moviesglobe.data.network.models.MovieDetail;
import com.nowist.android.moviesglobe.di.Injections;
import com.nowist.android.moviesglobe.utils.Constants;
import com.nowist.android.moviesglobe.utils.Utils;
import com.squareup.picasso.Picasso;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    private MovieDetailContract.UserActionsListener mActionsListener;
    private ImageView mIvMoviePoster;
    private TextView mOriginalTitleTv;
    private TextView mTvTagline;
    private TextView mTvOverview;
    private TextView mTvRating;
    private TextView mTvVoteCount;
    private TextView mTvPopularity;
    private ConstraintLayout mRootLayoutMovieDetail;

    private MovieDetail mMovieDetail;

    public static final String ARGUMENT_MOVIE_ID = "MOVIE_ID";
    private Toolbar mToolbar;
    private ImageView mIvLike;
    private TextView mTvTitleToolbar;
    private ProgressBar mProgressBar;
    private TextView mEmptyView;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initControl();
    }

    private void initControl() {
        mActionsListener =
                new MovieDetailPresenter(Injections.provideDbHelper
                        (getContext().getApplicationContext()), Injections.provideApiHelper(getContext().getApplicationContext())
                        , this);
    }

    public static MovieDetailFragment newInstance(String movieId) {
        Bundle arguments = new Bundle();
        arguments.putString(ARGUMENT_MOVIE_ID, movieId);
        MovieDetailFragment fragment = new MovieDetailFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        String movieId = getArguments().getString(ARGUMENT_MOVIE_ID);
        if (Utils.isNetworkAvailable(getActivity().getApplicationContext())) {
            showDetailView();
            mActionsListener.loadMovieDetails(movieId);
        } else {
            Toast.makeText(getActivity(), getString(R.string.check_conn), Toast.LENGTH_SHORT).show();
            showEmptyView();
        }
    }

    //get the reference of all the view here here
    //// TODO: 11/08/17 can be improved by using the data binding
    private void initViews(View view) {
        mRootLayoutMovieDetail = (ConstraintLayout) view.findViewById(R.id.root_layout_movie_detail);
        mIvMoviePoster = (ImageView) view.findViewById(R.id.iv_poster_detail);
        mOriginalTitleTv = (TextView) view.findViewById(R.id.tv_original_title);
        mTvTagline = (TextView) view.findViewById(R.id.tv_tagline);
        mTvOverview = (TextView) view.findViewById(R.id.tv_overview);
        mTvRating = (TextView) view.findViewById(R.id.tv_rating);
        mTvVoteCount = (TextView) view.findViewById(R.id.tv_vote_count);
        mTvPopularity = (TextView) view.findViewById(R.id.tv_popularity);
        mEmptyView = (TextView) view.findViewById(R.id.tv_empty_view);

        mToolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        mTvTitleToolbar = (TextView) getActivity().findViewById(R.id.title_toolbar);
        mTvTitleToolbar.setText(getString(R.string.movie_detail));
        mIvLike = (ImageView) mToolbar.findViewById(R.id.iv_like);
        mProgressBar = (ProgressBar) view.findViewById(R.id.pb_loading);

        mIvLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_like:
                        boolean isSelected = v.isSelected();
                        setFavouriteMovie(!isSelected);
//                        v.setSelected(!isSelected);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void showEmptyView() {
        mRootLayoutMovieDetail.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
    }

    private void showDetailView() {
        mRootLayoutMovieDetail.setVisibility(View.VISIBLE);
        mEmptyView.setVisibility(View.GONE);
    }

    private void setFavouriteMovie(boolean isFav) {
        if (mMovieDetail != null) {
            MovieModel model = new MovieModel();
            model.setMovieId(String.valueOf(mMovieDetail.getId()));
            model.setName(mMovieDetail.getOriginalTitle());
            model.setVoteAvg(mMovieDetail.getVoteAverage());
            model.setPosterPath(mMovieDetail.getPosterPath());
            if (isFav) {
                model.setFav(1);
            } else {
                model.setFav(0);
            }
            mActionsListener.setMovieFavourite(model);
        } else {
            Toast.makeText(getActivity(), getString(R.string.data_not_loaded), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (getView() == null) {
            return;
        }

        if (active) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMovieDetails(MovieDetail movieDetail) {
        if (movieDetail != null) {
            mMovieDetail = movieDetail;
            showDetailView();
            Picasso.with(getActivity().getApplicationContext()).load(Constants.IMAGE_PATH_SIZE_300 + movieDetail.getPosterPath()).placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher).into(mIvMoviePoster);
            mOriginalTitleTv.setText(movieDetail.getOriginalTitle());
            mTvTagline.setText(movieDetail.getTagline());
            mTvOverview.setText(movieDetail.getOverview());
            mTvRating.setText("Rating : " + movieDetail.getVoteAverage());
            mTvVoteCount.setText("Vote Count : " + movieDetail.getVoteCount());
            mTvPopularity.setText("Popularity : " + movieDetail.getPopularity());
            mActionsListener.updateMovieFavStatus(String.valueOf(movieDetail.getId()));
        }
    }

    @Override
    public void movieDetailLoadFailed(String errorMessage) {
        showEmptyView();
        Toast.makeText(getContext(), getString(R.string.check_conn), Toast.LENGTH_SHORT).show();
    }

    /**
     * this method is executed on different thread
     *
     * @param isSuccess
     */
    @Override
    public void updateFavMovie(final boolean isSuccess) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isSuccess) {
                    Toast.makeText(getActivity(), "Liked !!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Disliked !!", Toast.LENGTH_SHORT).show();
                }
                mIvLike.setSelected(isSuccess);
            }
        });
    }

    /**
     * ATTENTION !! - Method called from Non UI Thread
     *
     * @param isFav
     */
    @Override
    public void updateFavIcon(final boolean isFav) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIvLike.setSelected(isFav);
            }
        });
    }
}
