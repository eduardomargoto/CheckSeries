package br.com.etm.checkseries.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class SerieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final String TAG = SerieAdapter.class.getSimpleName();

    public static int POSITION_SERIE_ACTIVE = -1;
    public static final int NATIVE_ADS = 0;
    public static final int ITEM_SERIE = 1;

    private List<ApiShow> apiShows;

    public SerieAdapter(List<ApiShow> apiShows) {
        if (apiShows != null) {
            this.apiShows = apiShows;
        } else {
            this.apiShows = new ArrayList<>();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder() viewType: " + viewType);

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_serie_v2, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i(TAG, "onBindViewHolder(" + position + ")");
        if (holder instanceof MyViewHolder) {
            ApiShow apiShow = apiShows.get(position);
            MyViewHolder myViewHolder = (MyViewHolder) holder;
            myViewHolder.bind(apiShow);
        }
    }

    @Override
    public int getItemCount() {
        if (apiShows == null)
            apiShows = new ArrayList<>();
        return apiShows.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (apiShows.get(position).getTraktId() != null)
            return NATIVE_ADS;
        else return ITEM_SERIE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ic_check_episode)
        ImageButton ibCheckEpisode;

        @BindView(R.id.iv_serie)
        ImageView ivSerie;

        @BindView(R.id.iv_favorite)
        ImageView ivFavourite;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_network_serie)
        TextView tvNetwork;

        @BindView(R.id.tv_size_episodes)
        TextView tvTotalEpisodes;

        @BindView(R.id.tv_nextepisode_serie)
        TextView tvNextEpisodeTitle;

        @BindView(R.id.tv_nextepisodetime_serie)
        TextView tvTimeNextEpisode;

        @BindView(R.id.pb_episodes)
        ProgressBar pbEpisodes;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(ApiShow apiShow) {
            tvTitle.setText(apiShow.getTitle());
            tvNetwork.setText(apiShow.getNetwork());
            tvTotalEpisodes.setText(App.getContext().getString(R.string.formatted_progress_show
                    , apiShow.getNextEpisode().getNumber()
                    , apiShow.getTotalEpisodes()));
            tvNextEpisodeTitle.setText(apiShow.getNextEpisode().getTitle());
            tvTimeNextEpisode.setText(apiShow.getNextEpisode().getDateFirstAiredFormatted(tvTimeNextEpisode.getContext()));

            Picasso.with(itemView.getContext())
                    .load(apiShow.getBackgroundUrl())
                    .error(R.drawable.ic_panorama_white)
                    .into(ivSerie);
        }
    }

    public class MyViewHolderNativeAds extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_description)
        TextView tvDescription;

        @BindView(R.id.tv_age_restriction)
        TextView tvAgeRestrictions;

        @BindView(R.id.rb_rating)
        RatingBar ratingBar;

        @BindView(R.id.b_cta)
        Button ctaButton;

        @BindView(R.id.icon)
        ImageView imageView;

        @BindView(R.id.provider_view)
        FrameLayout providerViewContainer;

        public MyViewHolderNativeAds(View itemView) {
            super(itemView);
            ButterKnife.bind(itemView);
        }

    }
}
