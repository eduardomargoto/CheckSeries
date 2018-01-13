package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appodeal.ads.NativeAd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.fragments.EpisodeInformationFragment;
import br.com.etm.checkseries.utils.HelpFragment;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.activity.ListEpisodesSeasonActivity;

/**
 * Created by EDUARDO_MARGOTO on 02/01/2016.
 */
public class EpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int NATIVE_ADS = 0;
    public static final int ITEM_EPISODE = 1;

    private final ArrayList<Episode> episodeList;
    private final LayoutInflater layoutInflater;
    private final Context contextActivity;

    public EpisodeAdapter(Context context, List<Episode> episodeList) {
        this.episodeList = new ArrayList<>(episodeList);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contextActivity = context;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
//        View view = layoutInflater.inflate(R.layout.item_episodes_season, parent, false);
//        mvh = new MyViewHolder(view);
//        return mvh;

        View view = null;
        RecyclerView.ViewHolder mvh = null;

        if (viewType == ITEM_EPISODE) {
            view = layoutInflater.inflate(R.layout.item_episodes_season, parent, false);
            mvh = new MyViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_native_ads, parent, false);
            mvh = new MyViewHolderNativeAds(view);
        }

        return mvh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewholder, final int position) {

        if (viewholder instanceof MyViewHolder) {
            final MyViewHolder holder = (MyViewHolder) viewholder;
            holder.rl_view_banner_img.setMinimumHeight(UtilsImages.getHeightImageDensity(contextActivity));
            holder.rl_view_banner_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("LOG-AMBISERIES", "episodeList.get(getAdapterPosition()) " + episodeList.get(position));
                    if (episodeList.get(position) != null) {
                        Intent it = new Intent(contextActivity, ListEpisodesSeasonActivity.class);
                        Serie serie = null;
                        try {
                            serie = new DAO_Serie(contextActivity).find(String.valueOf(episodeList.get(position).getSerieId()));
                            serie.setEpisodeList(episodeList);
                        } catch (Exception e) {
                            Toast.makeText(contextActivity, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        it.putExtra("serie", serie);
                        it.putExtra("episode", episodeList.get(position));

                        contextActivity.startActivity(it);
                    }
                }
            });
            if (episodeList.get(position) != null) {
                if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
                    if (HttpConnection.isConnectionWifiOnline(contextActivity)) {
//                        Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + episodeList.get(position).getFilename())
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
//                    Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + episodeList.get(position).getFilename())
//                            .resize(UtilsImages.getWidthAllDensity(contextActivity), UtilsImages.getHeightDensity(contextActivity))
//                            .placeholder(R.drawable.loading_animation_black)
//                            .error(R.drawable.image_area_48dp)
//                            .into(holder.iv_banner);
                    UtilsImages.darkenImagen(holder.iv_banner);
                }

                holder.tv_name_episode.setText(episodeList.get(position).getEpisodeFormatted());
                holder.tv_total_episode.setText("(TOTAL " + episodeList.get(position).getTotalEpisodeNumber() + ")");
                holder.tv_date_episode.setText(episodeList.get(position).getDateEpisodeFormatted(contextActivity));

//            holder.tv_name_episode.setText(episodeList.get(position).getEpisodeFormatted());
//            holder.tv_episodetime_serie.setText(episodeList.get(position).getDateEpisodeFormatted());


                Log.i("LOG", "onBindViewHolder() - " + episodeList.get(position).getEpisodeFormatted() + " isWatched() " + episodeList.get(position).isWatched());
                Log.i("LOG", "onBindViewHolder() - " + episodeList.get(position).getEpisodeFormatted() + " isSkipped() " + episodeList.get(position).isWatched());
                if (episodeList.get(position).isWatched()) {
//                    Picasso.with(contextActivity).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48)
//                            .into(holder.ic_skip_episode);
                    holder.ic_check_episode.setVisibility(View.INVISIBLE);

                } else if (episodeList.get(position).isSkipped()) {
//                    Picasso.with(contextActivity).load(R.drawable.skip_next_marked_24dp).resize(48, 48)
//                            .into(holder.ic_skip_episode);
                    holder.ic_check_episode.setVisibility(View.INVISIBLE);
                } else {
                    holder.ic_check_episode.setVisibility(View.VISIBLE);
                    holder.ic_skip_episode.setVisibility(View.VISIBLE);
//                    Picasso.with(contextActivity).load(R.drawable.ic_check).resize(48, 48)
//                            .into(holder.ic_check_episode);
//                    Picasso.with(contextActivity).load(R.drawable.skip_next_24dp).resize(48, 48)
//                            .into(holder.ic_skip_episode);
                }

            } else {
//                Picasso.with(contextActivity).load(R.drawable.image_area_48dp)
//                        .memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .into(holder.iv_banner);
                UtilsImages.darkenImagen(holder.iv_banner);
                holder.tv_name_episode.setText("Nenhum episódio encontrado");
            }


            holder.ic_check_episode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (episodeList.get(position).isWatched()) {
                        episodeList.get(position).setWatched(false);
//                        Picasso.with(contextActivity).load(R.drawable.ic_check).resize(48, 48)
//                                .into(holder.ic_check_episode);
//                        Picasso.with(contextActivity).load(R.drawable.skip_next_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.VISIBLE);
                        holder.ic_skip_episode.setVisibility(View.VISIBLE);

                    } else if (episodeList.get(position).isSkipped()) {
                        episodeList.get(position).setSkipped(false);
//                        Picasso.with(contextActivity).load(R.drawable.ic_check).resize(48, 48)
//                                .into(holder.ic_check_episode);
//                        Picasso.with(contextActivity).load(R.drawable.skip_next_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.VISIBLE);
                        holder.ic_skip_episode.setVisibility(View.VISIBLE);
                    } else {
                        episodeList.get(position).setWatched(true);
//                        Picasso.with(contextActivity).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.INVISIBLE);
                    }
                    Serie serie = null;
                    try {
                        serie = new DAO_Serie(contextActivity).find(String.valueOf(episodeList.get(position).getSerieId()));
                        HelpFragment.updateEpisodeMainActivity(episodeList.get(position), serie);
                    } catch (Exception e) {
                        Toast.makeText(contextActivity, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    EpisodeInformationFragment.UPDATE_FRAGMENT = true;


                    new DAO_Episode(contextActivity).updateWatchedSkipped(episodeList.get(position));
                }
            });

            holder.ic_skip_episode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (episodeList.get(position).isWatched()) {
                        episodeList.get(position).setWatched(false);
//                        Picasso.with(contextActivity).load(R.drawable.ic_check).resize(48, 48)
//                                .into(holder.ic_check_episode);
//                        Picasso.with(contextActivity).load(R.drawable.skip_next_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.VISIBLE);
                        holder.ic_skip_episode.setVisibility(View.VISIBLE);

                    } else if (episodeList.get(position).isSkipped()) {
                        episodeList.get(position).setSkipped(false);
//                        Picasso.with(contextActivity).load(R.drawable.ic_check).resize(48, 48)
//                                .into(holder.ic_check_episode);
//                        Picasso.with(contextActivity).load(R.drawable.skip_next_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.VISIBLE);
                        holder.ic_skip_episode.setVisibility(View.VISIBLE);
                    } else {
                        episodeList.get(position).setSkipped(true);
//                        Picasso.with(contextActivity).load(R.drawable.skip_next_marked_24dp).resize(48, 48)
//                                .into(holder.ic_skip_episode);
                        holder.ic_check_episode.setVisibility(View.INVISIBLE);
                    }
                    Serie serie = null;
                    try {
                        serie = new DAO_Serie(contextActivity).find(String.valueOf(episodeList.get(position).getSerieId()));
                        HelpFragment.updateEpisodeMainActivity(episodeList.get(position), serie);
                    } catch (Exception e) {
                        Toast.makeText(contextActivity, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    EpisodeInformationFragment.UPDATE_FRAGMENT = true;

                    episodeList.get(position).setDateWatched(new Date()); // adicionar a data de foi pulado ou assistido o episódio
                    new DAO_Episode(contextActivity).updateWatchedSkipped(episodeList.get(position));
                }
            });
        } else {

            MyViewHolderNativeAds mvhna = (MyViewHolderNativeAds) viewholder;

            NativeAd mAd = null;

            /*if (!MainActivity.mAds.isEmpty()) {
                try {
                    for (NativeAd ad : MainActivity.mAds) {
                        if (ad.getTitle().equals(episodeList.get(position).getEpisodeName())) {
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
            }*/
        }
    }

    @Override
    public int getItemCount() {
        return episodeList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (episodeList.get(position).getId() == 0)
            return NATIVE_ADS;
        else return ITEM_EPISODE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_banner, ic_check_episode, ic_skip_episode;
        public TextView tv_name_episode, tv_date_episode, tv_episodetime_serie, tv_total_episode;
        public RelativeLayout rl_view_banner_img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ic_check_episode = (ImageView) itemView.findViewById(R.id.ic_check_episode);

            iv_banner = (ImageView) itemView.findViewById(R.id.iv_banner);
            rl_view_banner_img = (RelativeLayout) itemView.findViewById(R.id.rl_view_banner_img);
            tv_name_episode = (TextView) itemView.findViewById(R.id.tv_name_episode);
            tv_date_episode = (TextView) itemView.findViewById(R.id.tv_date_episode);
            tv_total_episode = (TextView) itemView.findViewById(R.id.tv_total_episode);
            ic_skip_episode = (ImageView) itemView.findViewById(R.id.ic_skip_episode);
//            tv_episodetime_serie = (TextView) itemView.findViewById(R.id.tv_episodetime_serie);
//            itemView.setOnClickListener(this);
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
