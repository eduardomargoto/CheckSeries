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
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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
        if (apiShows != null) {this.apiShows = apiShows;
        } else {this.apiShows = new ArrayList<>();}
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
        if (apiShows.get(position).getApiIdentifiers() == null)
            return NATIVE_ADS;
        else return ITEM_SERIE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ic_check_episode)
        ImageButton ic_check_epsiode;

        @BindView(R.id.iv_serie)
        ImageView ivSerie;

        @BindView(R.id.iv_favorite)
        ImageView iv_favorite;

        @BindView(R.id.iv_options_serie)
        ImageView iv_options_serie;

        @BindView(R.id.tv_title)
        TextView tvTitle;

        @BindView(R.id.tv_network_serie)
        TextView tv_network_serie;

        @BindView(R.id.tv_size_episodes)
        TextView tv_size_episodes;

        @BindView(R.id.tv_nextepisode_serie)
        TextView tv_nextepisode_serie;

        @BindView(R.id.tv_nextepisodetime_serie)
        TextView tv_nextepisodetime_serie;

        @BindView(R.id.rl_view)
        RelativeLayout rl_view;

        @BindView(R.id.pb_episodes)
        ProgressBar pb_episodes;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        private void bind(ApiShow apiShow){
            tvTitle.setText(apiShow.getTitle());

            Picasso.with(itemView.getContext())
                    .load(apiShow.getFanArtImages().getShowBackgroundImages().get(0).getUrl())
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
