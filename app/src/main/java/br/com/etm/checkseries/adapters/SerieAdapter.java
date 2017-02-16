package br.com.etm.checkseries.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
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
import android.widget.Toast;


import com.appodeal.ads.NativeAd;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.HelpFragment;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.views.MainActivity;
import br.com.etm.checkseries.views.SerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class SerieAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static int POSITION_SERIE_ACTIVE = -1;
    public static final int NATIVE_ADS = 0;
    public static final int ITEM_SERIE = 1;

    private List<Serie> serieList;
    private LayoutInflater layoutInflater;
    public Context contextActivity;
    public Activity mActivity;

    public SerieAdapter(Activity activity, List<Serie> serieList) {
        mActivity = activity;
        this.serieList = new ArrayList<>(serieList);

        this.contextActivity = (Context) activity;
        this.layoutInflater = (LayoutInflater) contextActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder() viewType: " + viewType);
        View view = null;
        RecyclerView.ViewHolder mvh = null;
        if (viewType == ITEM_SERIE) {

            if (!EnvironmentConfig.getInstance().isLayoutCompat())
                view = layoutInflater.inflate(R.layout.item_serie_v2, parent, false);
            else
                view = layoutInflater.inflate(R.layout.item_serie_compat, parent, false);
            mvh = new MyViewHolder(view);
        } else {
            view = layoutInflater.inflate(R.layout.item_native_ads, parent, false);
            mvh = new MyViewHolderNativeAds(view);
        }

        return mvh;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("LOG", "onBindViewHolder(" + position + ")");
        if (holder instanceof MyViewHolder) {
            Serie s = serieList.get(position);

            Episode episode = s.getNextEpisode();

            MyViewHolder mvh = (MyViewHolder) holder;
            if (EnvironmentConfig.getInstance().isCheckmain()) {
                mvh.tv_size_episodes.setVisibility(View.VISIBLE);
                mvh.pb_episodes.setVisibility(View.VISIBLE);
                mvh.ic_check_epsiode.setVisibility(View.VISIBLE);
            } else {
                mvh.tv_size_episodes.setVisibility(View.INVISIBLE);
                mvh.pb_episodes.setVisibility(View.INVISIBLE);
                mvh.ic_check_epsiode.setVisibility(View.INVISIBLE);
            }

            mvh.tv_serieid.setText(s.getId().toString());
            mvh.tv_serie_name.setText(s.getName());

            mvh.tv_size_episodes.setText(s.getSizeWatchedEpisode() + "/" + s.getSizeEpisodes());
//            mvh.tv_size_episodes.setText(s.getSizeWatchedEpisode() + "/" + s.getTotalEpisodes());
//            mvh.pb_episodes.setMax(s.getSizeEpisodes());
            mvh.pb_episodes.setMax(s.getTotalEpisodes());
            mvh.pb_episodes.setProgress(s.getSizeWatchedEpisode());

            if (s.isAllEpisodeWatched()) {
                mvh.tv_nextepisode_serie.setText(contextActivity.getResources().getString(s.getStatusFormatted()));
                mvh.tv_nextepisodetime_serie.setText("");

                mvh.tv_size_episodes.setVisibility(View.INVISIBLE);
                mvh.pb_episodes.setVisibility(View.INVISIBLE);
                mvh.ic_check_epsiode.setVisibility(View.INVISIBLE);
            } else {

                if (episode != null) {
                    if (episode.getFirstAired() == null)
                        mvh.ic_check_epsiode.setVisibility(View.INVISIBLE);
                    else {
                        if (Calendar.getInstance().getTime().before(episode.getFirstAired()))
                            mvh.ic_check_epsiode.setVisibility(View.INVISIBLE);
                    }
                    mvh.tv_nextepisode_serie.setText(episode.getEpisodeFormatted());
                    mvh.tv_nextepisodetime_serie.setText(episode.getDateEpisodeFormatted(contextActivity));
                }
            }


        /* -- EXIBIÇÃO NETWORK E HORA QUE VAI AO AR -- */

            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
                String text = s.getNetwork();

                if (!s.getAirs_DayOfWeekFormatted().equals(""))
                    text = text + " / " + s.getAirs_DayOfWeekFormatted();
                if (!s.getAirs_Time().equals(""))
                    text = text + " " + s.getAirs_Time();

                mvh.tv_network_serie.setText(text);
            }
        /* -- AÇÃO AO ICONE FAVORITOS -- */
            mvh.iv_favorite.setTag(1);
            if (s.isFavorite()) {
                mvh.iv_favorite.setImageResource(R.drawable.star_24dp);
            } else {
                mvh.iv_favorite.setImageResource(R.drawable.star_outline_24dp);
            }

         /* -- AÇÃO AO ICONE OPÇÕES DA SERIE -- */
            mvh.iv_options_serie.setId(position);
            mvh.iv_options_serie.setTag(2);

            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
//                mvh.rl_view.setMinimumHeight(UtilsImages.getHeightDensity(contextActivity));
                final Serie serie = s;
                final MyViewHolder hold = mvh;
                File file = UtilsImages.getFilesDirectory(contextActivity, serie.getFanArtFilename());

                if (!file.exists()) {
                    Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
                            .stableKey(serie.getFanArtFilenameCache())
//                            .resize(UtilsImages.getWidthAllDensity(contextActivity), UtilsImages.getHeightDensity(contextActivity))
//                            .placeholder(R.drawable.loading_animation_black)
                            .error(R.drawable.image_area_48dp)
                            .into(hold.iv_serie);

                    UtilsImages.darkenImagen(hold.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
                    new Thread() {
                        @Override
                        public void run() {
                            Bitmap bitmap = null;
                            try {
                                bitmap = Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serie.getFanArt()).get();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            UtilsImages.saveToInternalSorage(bitmap, serie.getFanArtFilename(), contextActivity);
                            bitmap.recycle();
                        }
                    }.start();

                } else {
                    Picasso.with(contextActivity).load(UtilsImages.getFilesDirectory(contextActivity, serie.getFanArtFilename()))
                            .stableKey(serie.getFanArtFilenameCache())
//                            .resize(UtilsImages.getWidthAllDensity(contextActivity), UtilsImages.getHeightDensity(contextActivity))
//                            .placeholder(R.drawable.loading_animation_black)
                            .error(R.drawable.image_area_48dp)
                            .into(hold.iv_serie);

                    UtilsImages.darkenImagen(hold.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
                }
            } else {
                int width = 0, height = 0;
                if (contextActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    width = UtilsImages.getWidthAllDensity(contextActivity) / 2;
                    height = (UtilsImages.getHeightDensity(contextActivity) - 140) / 2;
                } else {
                    width = UtilsImages.getWidthAllDensity(contextActivity);
                    height = (UtilsImages.getHeightDensity(contextActivity) - 140);
                }
                mvh.rl_view.setMinimumHeight(height);
                if (HttpConnection.isOnline(contextActivity)) {
                    Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + s.getPoster())
                            .stableKey(s.getPosterFilenameCache())
                            .resize(width, height)
                            .centerInside()
                            .placeholder(R.drawable.loading_animation_black)
                            .error(R.drawable.image_area_36dp)
                            .into(mvh.iv_serie);
                } else {
                    Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + s.getPoster())
                            .stableKey(s.getPosterFilenameCache())
                            .resize(width, height)
                            .centerInside()
                            .error(R.drawable.image_area_36dp)
                            .into(mvh.iv_serie);
                    UtilsImages.darkenImagen(mvh.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
                }
            }

        } else {

            MyViewHolderNativeAds mvhna = (MyViewHolderNativeAds) holder;

            NativeAd mAd = null;

            if (!MainActivity.mAds.isEmpty()) {
                try {
                    for (NativeAd ad : MainActivity.mAds) {
                        if (ad.getTitle().equals(serieList.get(position).getName())) {
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

        }
    }

    @Override
    public int getItemCount() {
        if (serieList == null)
            serieList = new ArrayList<>();
        return serieList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (serieList.get(position).getId() == null)
            return NATIVE_ADS;
        else return ITEM_SERIE;
    }

    public void updateListItem(int position) {
        notifyItemChanged(position);
    }


    public void updateListItem(int position, Serie serie) {
        serieList.set(position, serie);
        notifyItemChanged(position);
    }


    public void removeListItem(int position) {
        serieList.remove(position);
        notifyItemRemoved(position);
    }

    public void addListItem(Serie serie, int position) {
        serieList.add(serie);
        notifyItemInserted(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

//        public Transformation trans1 = new ContrastFilterTransformation(contextActivity, 1.5f);
//        public Transformation trans2 = new BrightnessFilterTransformation(contextActivity, 0.2f);

        public ImageView iv_serie, iv_favorite, iv_options_serie;
        public TextView tv_serie_name, tv_network_serie, tv_size_episodes;
        public TextView tv_serieid, tv_nextepisode_serie, tv_nextepisodetime_serie;
        public RelativeLayout rl_view;

        public ProgressBar pb_episodes;
        public ImageButton ic_check_epsiode;


        public MyViewHolder(View itemView) {
            super(itemView);

            ic_check_epsiode = (ImageButton) itemView.findViewById(R.id.ic_check_episode);
            pb_episodes = (ProgressBar) itemView.findViewById(R.id.pb_episodes);
            tv_size_episodes = (TextView) itemView.findViewById(R.id.tv_size_episodes);

            iv_serie = (ImageView) itemView.findViewById(R.id.iv_serie);
            rl_view = (RelativeLayout) itemView.findViewById(R.id.rl_view);
            tv_serie_name = (TextView) itemView.findViewById(R.id.tv_name_serie);
            tv_serieid = (TextView) itemView.findViewById(R.id.tv_serieid);
            iv_favorite = (ImageView) itemView.findViewById(R.id.iv_favorite);
            iv_favorite.setTag(1);
            iv_options_serie = (ImageView) itemView.findViewById(R.id.iv_options_serie);
            iv_options_serie.setTag(2);
            tv_nextepisode_serie = (TextView) itemView.findViewById(R.id.tv_nextepisode_serie);
            tv_nextepisodetime_serie = (TextView) itemView.findViewById(R.id.tv_nextepisodetime_serie);
            tv_network_serie = (TextView) itemView.findViewById(R.id.tv_network_serie);


            ic_check_epsiode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Serie serie = serieList.get(getAdapterPosition());
                    Episode episode = serie.getNextEpisode();
                    episode.setWatched(true);
                    new DAO_Episode(v.getContext()).updateWatchedSkipped(episode);
//                    episode = new DAO_Episode(v.getContext()).findNextEpisode(serie.getId().toString());

//                    MainActivity.mySeries.get(getAdapterPosition()).
//                    serieList.get(getAdapterPosition()).setNextEpisode(episode);
                    //HelpFragment.updateEpisodeMainActivity(episode, getAdapterPosition());

                    notifyItemChanged(getAdapterPosition());

//                    MainActivity.updateFragments();
                }
            });

            iv_options_serie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SerieAdapter.POSITION_SERIE_ACTIVE = getAdapterPosition();
                    v.showContextMenu();
                }
            });

            iv_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Serie serie = serieList.get(getAdapterPosition());
                    String str_action = "";
                    if (serie.isFavorite()) {
                        serie.setFavorite(false);
                        Picasso.with(contextActivity).load(R.drawable.star_outline_24dp)
                                .into(iv_favorite);
                        str_action = v.getResources().getString(R.string.app_action_revfavorite_serie);
                        Toast.makeText(v.getContext(), serie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
                    } else {
                        serie.setFavorite(true);
                        Picasso.with(contextActivity).load(R.drawable.star_24dp)
                                .into(iv_favorite);
                        str_action = v.getResources().getString(R.string.app_action_addfavorite_serie);
                        Toast.makeText(v.getContext(), serie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
                    }

                    new DAO_Serie(v.getContext()).edit(serie);
                    notifyItemChanged(getAdapterPosition());
                    Log.i("LOG-FAVORITE-SERIE", "FAVORIE -> " + serie.isFavorite());
                    if (EnvironmentConfig.getInstance().isFilter_favorite() && !serie.isFavorite()) {
                        notifyItemRemoved(getAdapterPosition());
                        serieList.remove(serie);
                    }
                    serie = null;
                    str_action = null;
                }

            });

            if (!EnvironmentConfig.getInstance().isLayoutCompat())
                itemView.setOnClickListener(view_click);
            else rl_view.setOnClickListener(view_click);
        }


        View.OnClickListener view_click = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getTag() == null) {
                    Serie s = null;
                    try {
                        s = new DAO_Serie(contextActivity).find(serieList.get(getAdapterPosition()).getId().toString());
                        if (serieList.get(getAdapterPosition()).getNextEpisode() != null)
                            s.setEpisodeList(new DAO_Episode(contextActivity).findAll(serieList.get(getAdapterPosition()), serieList.get(getAdapterPosition()).getNextEpisode().getSeasonNumber()));
                    } catch (Exception e) {
                        Toast.makeText(contextActivity, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                    Intent it = new Intent(contextActivity, SerieActivity.class);
                    it.putExtra("serie", s);
                    contextActivity.startActivity(it);
                }
            }
        };
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
