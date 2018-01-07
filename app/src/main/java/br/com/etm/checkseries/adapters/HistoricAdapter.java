package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsImages;

import static br.com.etm.checkseries.adapters.EpisodeAdapter.ITEM_EPISODE;
import static br.com.etm.checkseries.adapters.EpisodeAdapter.NATIVE_ADS;

/**
 * Created by EDUARDO_MARGOTO on 10/5/2016.
 */

public class HistoricAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final List<Episode> episodes;
    private final LayoutInflater layoutInflater;
    private final Context contextActivity;

    public HistoricAdapter(Context context, final List<Episode> episodes) {
        this.episodes = episodes;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contextActivity = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        RecyclerView.ViewHolder mvh = null;

        if (viewType == ITEM_EPISODE) {
            view = layoutInflater.inflate(R.layout.item_history, parent, false);
            mvh = new HistoricAdapter.MyViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_native_ads, parent, false);
            mvh = new HistoricAdapter.MyViewHolderNativeAds(view);
        }

        return  mvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {
        if (viewholder instanceof HistoricAdapter.MyViewHolder) {
            final HistoricAdapter.MyViewHolder holder = (HistoricAdapter.MyViewHolder) viewholder;
            holder.rl_view_banner_img.setMinimumHeight(UtilsImages.getHeightImageDensity(contextActivity));

            if (episodes.get(position) != null) {
                if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
                    if (HttpConnection.isConnectionWifiOnline(contextActivity)) {
//                        Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + episodes.get(position).getFilename())
//                                .resize(UtilsImages.getWidthAllDensity(contextActivity), UtilsImages.getHeightDensity(contextActivity))
//                                .placeholder(R.drawable.loading_animation_black)
//                                .error(R.drawable.image_area_48dp)
//                                .into(holder.iv_banner);
                        UtilsImages.darkenImagen(holder.iv_banner);
                    } else {
//                        Picasso.with(contextActivity).load(R.drawable.image_area_48dp)
//                                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                                .into(holder.iv_banner);
                        UtilsImages.darkenImagen(holder.iv_banner);
                    }
                } else {
//                    Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + episodes.get(position).getFilename())
//                            .resize(UtilsImages.getWidthAllDensity(contextActivity), UtilsImages.getHeightDensity(contextActivity))
//                            .placeholder(R.drawable.loading_animation_black)
//                            .error(R.drawable.image_area_48dp)
//                            .into(holder.iv_banner);
                    UtilsImages.darkenImagen(holder.iv_banner);
                }

                holder.tv_name_episode.setText(episodes.get(position).getEpisodeFormatted());
                String name_serie = new DAO_Serie(contextActivity).findName(episodes.get(position).getSerieId());
                holder.tv_name_serie.setText(name_serie);;
                holder.tv_date_episode.setText(episodes.get(position).getDateEpisodeFormatted(contextActivity));
                holder.tv_dateWatched_episode.setText(episodes.get(position).getDateWatchedFormatted(contextActivity));


            }

        }
       /* else {

            HistoricAdapter.MyViewHolderNativeAds mvhna = (HistoricAdapter.MyViewHolderNativeAds) viewholder;
            NativeAd mAd = null;

            if (!MainActivity.mAds.isEmpty()) {
                try {
                    for (NativeAd ad : MainActivity.mAds) {
                        if (ad.getTitle().equals(episodes.get(position).getEpisodeName())) {
                            mAd = ad;
                            break;
                        }
                    }

                    mvhna.tvTitle.setText(mAd.getTitle());
                    mvhna.tvDescription.setText(mAd.getDescription());
                    if (mAd.getRating() == 0) {
                        mvhna.ratingBar.setVisibility(View.INVISIBLE);
                    } else {
                        mvhna.ratingBar.setVisibility(View.VISIBLE);
                        mvhna.ratingBar.setRating(mAd.getRating());
                        mvhna.ratingBar.setStepSize(0.1f);
                    }

                    mvhna.ctaButton.setText(mAd.getCallToAction());

                    mvhna.imageView.setImageBitmap(mAd.getIcon());

                    View providerView = mAd.getProviderView(contextActivity);
                    if (providerView != null) {

                        mvhna.providerViewContainer.addView(providerView);
                    }

                    if (mAd.getAgeRestrictions() != null) {
                        mvhna.tvAgeRestrictions.setText(mAd.getAgeRestrictions());
                        mvhna.tvAgeRestrictions.setVisibility(View.VISIBLE);
                    } else {
                        mvhna.tvAgeRestrictions.setVisibility(View.GONE);
                    }

                    mAd.registerViewForInteraction(mvhna.itemView);
                    mvhna.itemView.setVisibility(View.VISIBLE);

                } catch (Exception e) {
                    Log.i("NATIVEADS", "EXCEPTION");
                }
            }
        }*/
    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (episodes.get(position).getId() == 0)
            return NATIVE_ADS;
        else return ITEM_EPISODE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_banner, ic_check_episode, iv_ic_history;
        public TextView tv_name_episode, tv_date_episode, tv_name_serie, tv_dateWatched_episode;
        public RelativeLayout rl_view_banner_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ic_check_episode = (ImageView) itemView.findViewById(R.id.ic_check_episode);

            iv_banner = (ImageView) itemView.findViewById(R.id.iv_banner);
            iv_ic_history = (ImageView) itemView.findViewById(R.id.iv_ic_history);
            tv_name_serie = (TextView) itemView.findViewById(R.id.tv_name_serie);
            rl_view_banner_img = (RelativeLayout) itemView.findViewById(R.id.rl_view_banner_img);
            tv_name_episode = (TextView) itemView.findViewById(R.id.tv_name_episode);
            tv_date_episode = (TextView) itemView.findViewById(R.id.tv_date_episode);
            tv_dateWatched_episode = (TextView) itemView.findViewById(R.id.tv_dateWatched_episode);
//
//            Picasso.with(contextActivity).load(R.drawable.ic_history)
//                    .resize(36, 36)
//                    .into(iv_ic_history);

        }
    }


    public class MyViewHolderNativeAds extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvAgeRestrictions;
        RatingBar ratingBar;
        Button ctaButton;
        ImageView imageView;
        FrameLayout providerViewContainer;

        public MyViewHolderNativeAds(View itemView) {
            super(itemView);

            providerViewContainer = (FrameLayout) itemView.findViewById(R.id.provider_view);
            imageView = (ImageView) itemView.findViewById(R.id.icon);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvDescription = (TextView) itemView.findViewById(R.id.tv_description);
            ratingBar = (RatingBar) itemView.findViewById(R.id.rb_rating);
            ctaButton = (Button) itemView.findViewById(R.id.b_cta);
            tvAgeRestrictions = (TextView) itemView.findViewById(R.id.tv_age_restriction);
        }

    }
}
