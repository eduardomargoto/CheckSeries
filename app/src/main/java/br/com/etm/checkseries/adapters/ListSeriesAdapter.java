package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.activity.SerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class ListSeriesAdapter extends RecyclerView.Adapter<ListSeriesAdapter.MyViewHolder> {
    public static int POSITION_SERIE_ACTIVE = -1;
    public static int TAB_ACTIVE = -1;

    private List<Serie> serieList;
    private LayoutInflater layoutInflater;
    private MyViewHolder mvh = null;
    public Context contextActivity;
    private TabsListAdapter mTabListAdapter;

    public ListSeriesAdapter(TabsListAdapter tabsListAdapter, Context context, List<Serie> serieList) {
        this.serieList = new ArrayList<>(serieList);
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contextActivity = context;
        mTabListAdapter = tabsListAdapter;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View view = null;
        if (!EnvironmentConfig.getInstance().isLayoutCompat())
            view = layoutInflater.inflate(R.layout.item_serie_v2, parent, false);
        else
            view = layoutInflater.inflate(R.layout.item_serie_compat, parent, false);

        mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.i("LOG", "onBindViewHolder()");

        holder.tv_serieid.setText(serieList.get(position).getId().toString());
        holder.tv_serie_name.setText(serieList.get(position).getName());

        holder.tv_size_episodes.setVisibility(View.INVISIBLE);
        holder.pb_episodes.setVisibility(View.INVISIBLE);
        holder.ic_check_epsiode.setVisibility(View.INVISIBLE);

        if (serieList.get(position).isAllEpisodeWatched()) {
            holder.tv_nextepisode_serie.setText(contextActivity.getResources().getString(serieList.get(position).getStatusFormatted()));
            holder.tv_nextepisodetime_serie.setText("");
        } else {
            Episode ep = serieList.get(position).getNextEpisode();
            if (ep != null) {
                holder.tv_nextepisode_serie.setText(serieList.get(position).getNextEpisode().getEpisodeFormatted());
                holder.tv_nextepisodetime_serie.setText(serieList.get(position).getNextEpisode().getDateEpisodeFormatted(contextActivity));
            }
        }
        if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
        /* -- EXIBIÇÃO NETWORK E HORA QUE VAI AO AR -- */
            String text = serieList.get(position).getNetwork();
            if (!serieList.get(position).getAirs_DayOfWeekFormatted().equals(""))
                text = text + " / " + serieList.get(position).getAirs_DayOfWeekFormatted();
            if (!serieList.get(position).getAirs_Time().equals(""))
                text = text + " " + serieList.get(position).getAirs_Time();
            holder.tv_network_serie.setText(text);
        }


        /* -- AÇÃO AO ICONE FAVORITOS -- */
        holder.iv_favorite.setTag(1);
        if (serieList.get(position).isFavorite()) {
            holder.iv_favorite.setImageResource(R.drawable.star_24dp);
        } else {
            holder.iv_favorite.setImageResource(R.drawable.star_outline_24dp);
        }

         /* -- AÇÃO AO ICONE OPÇÕES DA SERIE -- */
        holder.iv_options_serie.setId(position);
        holder.iv_options_serie.setTag(2);


        if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
//            holder.rl_view.setMinimumHeight(UtilsImages.getHeightDensity(contextActivity));
            holder.rl_view.setMinimumHeight(350);

            final Serie serie = serieList.get(position);
            final MyViewHolder hold = holder;
            File file = UtilsImages.getFilesDirectory(contextActivity, serie.getFanArtFilename());
            if (!file.exists()) {

                new Thread() {
                    @Override
                    public void run() {
//                        Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                                .stableKey(serie.getFanArtFilenameCache())
//                                .resize(UtilsImages.getWidthAllDensity(contextActivity), 350)
//                                .centerCrop()
//                                .placeholder(R.drawable.loading_animation_white)
//                                .error(R.drawable.image_area_36dp)
//                                .into(hold.iv_serie);

                        UtilsImages.darkenImagen(hold.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
                        Bitmap bitmap = null;
//                        try {
//                            bitmap = Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                                    .get();
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }

                        UtilsImages.saveToInternalSorage(bitmap, serie.getFanArtFilename(), contextActivity);
                        bitmap.recycle();
                    }
                }.start();


            } else {
//                Picasso.with(contextActivity).load(UtilsImages.getFilesDirectory(contextActivity, serieList.get(position).getFanArtFilename()))
//                        .stableKey(serieList.get(position).getFanArtFilenameCache())
//                        .resize(UtilsImages.getWidthAllDensity(contextActivity), 350)
//                        .centerCrop()
//                        .placeholder(R.drawable.loading_animation_white)
//                        .error(R.drawable.image_area_36dp)
//                        .into(holder.iv_serie);
                UtilsImages.darkenImagen(holder.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
            }


            ;

        } else {
            int width = 0, height = 0;
            if (contextActivity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                width = UtilsImages.getWidthAllDensity(contextActivity) / 2;
                height = (UtilsImages.getHeightDensity(contextActivity) - 140) / 2;
            } else {
                width = UtilsImages.getWidthAllDensity(contextActivity);
                height = (UtilsImages.getHeightDensity(contextActivity) - 140);
            }
//            Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serieList.get(position).getPoster())
//                    .stableKey(serieList.get(position).getFanArtFilenameCache())
//                    .resize(width, height)
//                    .centerInside()
//                    .placeholder(R.drawable.loading_animation_white)
//                    .into(mvh.iv_serie);

        }

    }

    @Override
    public int getItemCount() {
        if (serieList == null)
            serieList = new ArrayList<>();
        return serieList.size();
    }

    public void updateListItem(int position) {
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

        public ImageView iv_serie, iv_favorite, iv_options_serie, ic_check_epsiode;
        public TextView tv_serie_name, tv_network_serie, tv_size_episodes;
        public TextView tv_serieid, tv_nextepisode_serie, tv_nextepisodetime_serie;
        public RelativeLayout rl_view;
        public ProgressBar pb_episodes;

        public MyViewHolder(View itemView) {
            super(itemView);

            ic_check_epsiode = (ImageView) itemView.findViewById(R.id.ic_check_episode);
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


            iv_options_serie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ListSeriesAdapter.POSITION_SERIE_ACTIVE = getAdapterPosition();
                    ListSeriesAdapter.TAB_ACTIVE = mTabListAdapter.getViewPager().getCurrentItem();
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
//                        Picasso.with(contextActivity).load(R.drawable.star_outline_24dp).into(iv_favorite);
                        str_action = v.getResources().getString(R.string.app_action_revfavorite_serie);
                        Toast.makeText(v.getContext(), serie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
                    } else {
                        serie.setFavorite(true);
//                        Picasso.with(contextActivity).load(R.drawable.star_24dp).into(iv_favorite);
                        str_action = v.getResources().getString(R.string.app_action_addfavorite_serie);
                        Toast.makeText(v.getContext(), serie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
                    }

                   /* //ALTERANDO MAIN LISTSERIES
                    for (Serie s : MainActivity.mySeries) {
                        if (s.getId().equals(serie.getId()))
                            s.setFavorite(serie.isFavorite());
                    }*/

                    new DAO_Serie(v.getContext()).edit(serie);
                    notifyItemChanged(getAdapterPosition());
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
                    Intent it = new Intent(v.getContext(), SerieActivity.class);
                    int position = -1;
                    Serie serie = serieList.get(getAdapterPosition());
                    it.putExtra("serie", serie);
                    contextActivity.startActivity(it);
                }
            }
        };

    }
}
