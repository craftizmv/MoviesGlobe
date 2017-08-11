package com.nowist.android.moviesglobe.ui.movies;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nowist.android.moviesglobe.R;
import com.nowist.android.moviesglobe.data.network.models.Movie;
import com.nowist.android.moviesglobe.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MoviesItemViewHolder> {

    private List<Movie> mMovieList;
    private final MoviesAdapterOnClickListener mClickListener;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerView mRecyclerView;

    public interface MoviesAdapterOnClickListener {
        void onClick(String movieId);
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public MoviesListAdapter(List<Movie> movies, MoviesAdapterOnClickListener clickListener, RecyclerView recyclerView) {
        mMovieList = movies;
        mClickListener = clickListener;
        mRecyclerView = recyclerView;


        if (mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading
                            && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void replaceData(List<Movie> movieList) {
        mMovieList = checkNotNull(movieList);
        notifyDataSetChanged();
    }

    public void appendData(List<Movie> movieList) {
        checkNotNull(movieList);
        for (Movie movie : movieList) {
            mMovieList.add(movie);
        }
        notifyDataSetChanged();
    }

    @Override
    public MoviesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.movie_list_item_layout, parent, false);
        return new MoviesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesItemViewHolder holder, int position) {
        if (mMovieList.get(position) != null) {
            Movie movie = mMovieList.get(position);
            String posterPath = movie.getPosterPath();
            if (!TextUtils.isEmpty(posterPath)) {
                Picasso.with(holder.context).load(Constants.IMAGE_PATH_SIZE_185 + posterPath).placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .resize(64, 64)
                        .centerCrop().into(holder.posterIv);
            }
            if (!TextUtils.isEmpty(movie.getTitle())) {
                holder.titleTv.setText(movie.getTitle());
            }
            if (!TextUtils.isEmpty(String.valueOf(movie.getVoteAverage()))) {
                holder.ratingValTv.setText(String.valueOf(movie.getVoteAverage()));
                holder.ratingTv.setVisibility(View.VISIBLE);
            } else {
                holder.ratingTv.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return (mMovieList != null && mMovieList.size() > 0) ? mMovieList.size() : 0;
    }


    public class MoviesItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView posterIv;
        final TextView titleTv, ratingValTv, ratingTv;
        final Context context;

        public MoviesItemViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            posterIv = (ImageView) itemView.findViewById(R.id.iv_poster);
            titleTv = (TextView) itemView.findViewById(R.id.tv_title);
            ratingValTv = (TextView) itemView.findViewById(R.id.tv_rating_val);
            ratingTv = (TextView) itemView.findViewById(R.id.tv_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            int movieId = mMovieList.get(adapterPosition).getId();
            mClickListener.onClick(String.valueOf(movieId));
        }
    }

    public void setLoaded() {
        loading = false;
    }
}
