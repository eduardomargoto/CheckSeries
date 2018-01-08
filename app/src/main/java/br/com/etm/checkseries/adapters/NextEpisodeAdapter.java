package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 28/10/2015.
 */

public class NextEpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private int totalHeaders = 1;
    private List<Episode> episodeList;
    private LayoutInflater layoutInflater;
    private Context context;
    Episode episode = null;
    Calendar before = null;
    SparseIntArray headers_itens;

    public NextEpisodeAdapter(Context context, final List<Episode> episodeList) {
        this.episodeList = episodeList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        before = Calendar.getInstance();
        before.set(Calendar.DAY_OF_MONTH, before.get(Calendar.DAY_OF_MONTH) + 5);
        headers_itens = new SparseIntArray();
        if (!episodeList.isEmpty()) {
            Episode ep = null;
            for (int i = 0; i < episodeList.size(); i++) {
                if (i == 0)
                    ep = episodeList.get(i);
                else ep = episodeList.get(i - 1);

                if (!episodeList.get(i).getFirstAired().equals(ep.getFirstAired())) {
                    totalHeaders++;
                }
//                Log.i("LOG", "NextEpisodeAdapter - construct headers_itens.put(" + (i + totalHeaders) + ", " + totalHeaders + ")");
                headers_itens.put(i + totalHeaders, totalHeaders);
            }
//            Log.i("LOG", "NextEpisodeAdapter - construct totalHeaders: " + totalHeaders);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "NextEpisodeAdapter - onCreateViewHolder()");
        View view = null;
        RecyclerView.ViewHolder mvh = null;

        if (viewType == TYPE_HEADER) {
            view = layoutInflater.inflate(R.layout.header_item_nextepisodes, parent, false);
            mvh = new MyViewHolderHeader(view);
        } else {
            if (EnvironmentConfig.getInstance().isLayoutCompat())
                view = layoutInflater.inflate(R.layout.item_serie_compat, parent, false);
            else
                view = layoutInflater.inflate(R.layout.item_serie_v2, parent, false);
            mvh = new MyViewHolder(view);
        }
        return mvh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        Log.i("LOG", "NextEpisodeAdapter - onBindViewHolder(" + position + ")");

        if (holder instanceof MyViewHolderHeader && !episodeList.isEmpty()) {
            if (position == 0)
                episode = episodeList.get(position);
            else episode = episodeList.get((position + 1) - headers_itens.get(position + 1));
            MyViewHolderHeader mvholderHeader = (MyViewHolderHeader) holder;
            Calendar dateEpisode = Calendar.getInstance();
            dateEpisode.setTime(episode.getFirstAired());
            dateEpisode.set(Calendar.HOUR_OF_DAY, 00);
            dateEpisode.set(Calendar.MINUTE, 00);
            dateEpisode.set(Calendar.SECOND, 00);
            dateEpisode.set(Calendar.MILLISECOND, 00);

            Calendar today = Calendar.getInstance();
            today.set(Calendar.HOUR_OF_DAY, 00);
            today.set(Calendar.MINUTE, 00);
            today.set(Calendar.SECOND, 00);
            today.set(Calendar.MILLISECOND, 00);

            long differenceDays = (int) ((dateEpisode.getTimeInMillis() - today.getTimeInMillis()) / (24 * 60 * 60 * 1000));
            if (differenceDays == 0) {
                mvholderHeader.tv_header.setText(context.getResources().getString(R.string.today));
            } else if (differenceDays == 1) {
                mvholderHeader.tv_header.setText(context.getResources().getString(R.string.tomorrow));
            } else if (differenceDays > 1 && differenceDays <= 3) {
                mvholderHeader.tv_header.setText(context.getResources().getString(R.string.at_days).replace("/days", "" + (differenceDays)));
            } else {
                mvholderHeader.tv_header.setText(episode.getDateEpisodeFormatted(context));
            }

        } else if (holder instanceof MyViewHolder && !episodeList.isEmpty()) {

            int header = headers_itens.get(position);
            int newposition = position - header;

            MyViewHolder mvholder = (MyViewHolder) holder;
            episode = episodeList.get(newposition);
            Serie serie = null;
            try {
                serie = new DAO_Serie(context).find(String.valueOf(episode.getSerieId()));
            } catch (Exception e) {
                Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            mvholder.iv_favorite.setVisibility(View.INVISIBLE);
            mvholder.iv_options_serie.setVisibility(View.INVISIBLE);
            mvholder.tv_size_episodes.setVisibility(View.INVISIBLE);
            mvholder.pb_episodes.setVisibility(View.INVISIBLE);
            mvholder.ic_check_epsiode.setVisibility(View.INVISIBLE);

            mvholder.tv_serieid.setText("" + episode.getSerieId());
            mvholder.tv_serie_name.setText(serie.getName());

            if (episode != null) {

                mvholder.tv_nextepisode_serie.setText(episode.getEpisodeFormatted());
                mvholder.tv_nextepisodetime_serie.setText(episode.getDateEpisodeFormatted(context));
            }
        /* -- EXIBIÇÃO NETWORK E HORA QUE VAI AO AR -- */
            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
                String text = serie.getNetwork();
                if (!serie.getAirs_DayOfWeekFormatted().equals(""))
                    text = text + " / " + serie.getAirs_DayOfWeekFormatted();
                if (!serie.getAirs_Time().equals(""))
                    text = text + " " + serie.getAirs_Time();
                mvholder.tv_network_serie.setText(text);
            }


            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
                mvholder.rl_view.setMinimumHeight(UtilsImages.getHeightDensity(context));
                File file = UtilsImages.getFilesDirectory(context, serie.getFanArtFilename());
                if (file == null) {
                    Bitmap bitmap = null;
//                    try {
//                        bitmap = Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                                .stableKey(serie.getFanArtFilenameCache())
//                                .get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                    UtilsImages.saveToInternalSorage(bitmap, serie.getFanArtFilename(), context);
                    bitmap.recycle();
                }
//                Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                        .stableKey(serie.getFanArtFilenameCache())
//                        .resize(UtilsImages.getWidthAllDensity(context), UtilsImages.getHeightDensity(context))
//                        .placeholder(R.drawable.loading_animation_black)
//                        .error(R.drawable.image_area_48dp)
//                        .into(mvholder.ivSerie);

                UtilsImages.darkenImagen(mvholder.iv_serie); // escurecer as imagens para evitar problemas ao visualizar os icones
            } else {
                int width = 0, height = 0;
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    width = UtilsImages.getWidthAllDensity(context) / 2;
                    height = (UtilsImages.getHeightDensity(context) - 140) / 2;
                } else {
                    width = UtilsImages.getWidthAllDensity(context);
                    height = (UtilsImages.getHeightDensity(context) - 140);
                }
                mvholder.rl_view.setMinimumHeight(height);
//
//                Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getPoster())
//                        .stableKey(serie.getFanArtFilenameCache())
//                        .resize(width, height)
//                        .centerInside()
//                        .placeholder(R.drawable.loading_animation_black)
//                        .error(R.drawable.image_area_48dp)
//                        .into(mvholder.ivSerie);
            }

//            Picasso.with(context).load(UtilsImages.getFilesDirectory(context, serie.getFanArtFilename()))
//                    .stableKey(serie.getFanArtFilenameCache())
//                    .resize(UtilsImages.getWidthAllDensity(context), UtilsImages.getHeightDensity(context))
//                    .centerCrop()
//                    .placeholder(R.drawable.loading_animation_white)
//                    .into(mvholder.ivSerie);
//            UtilsImages.darkenImagen(mvholder.ivSerie); // escurecer as imagens para evitar problemas ao visualizar os icones
        }
    }

    @Override
    public int getItemCount() {
        if (episodeList == null)
            episodeList = new ArrayList<>();
        return episodeList.size() + totalHeaders;
    }

    public int getNumHeader(int position) {
        int header = -1;
        try {
            header = headers_itens.get(position);
            if(header == 0)
                return -1;
        } catch (NullPointerException e) {
            return -1;
        }

        return header;
    }

    public boolean isHeader(int position) {

        int header = getNumHeader(position);
        if (position == 0)
            return true;
        else if (header < 0)
            return true;
        else if (episodeList.get(position - header) != null) {
            return false;
        } else return false;
    }

    @Override
    public int getItemViewType(int position) {
        if (isHeader(position)) {
//            Log.i("LOG", "NextEpisodeAdapter - getItemViewType(" + position + ") return HEADER");
            return TYPE_HEADER;
        } else {
//            Log.i("LOG", "NextEpisodeAdapter - getItemViewType(" + position + ") return ITEM");
            return TYPE_ITEM;
        }
    }

    public class MyViewHolderHeader extends RecyclerView.ViewHolder {
        public TextView tv_header;

        public MyViewHolderHeader(View itemView) {
            super(itemView);
            tv_header = (TextView) itemView.findViewById(R.id.tv_header);
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

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


//            ic_check_episode.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Serie serie = serieList.get(getAdapterPosition());
//                    Episode episode = serie.getNextEpisode();
//                    episode.setWatched(true);
//                    new DAO_Episode(v.getContext()).edit(episode);
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });

//            itemView.setOnClickListener(this);
        }

//        @Override
//        public void onClick(View v) {
//            if (v.getTag() == null) {
//                /*DIRECIONAR PARA A EXIBICAO DA LISTA DOS EPISÓDIO (SLIDINGTBABS)*/
//                Intent it = new Intent(context, SerieActivity.class);
//                it.putExtra("serie", serieList.get(getAdapterPosition()));
//                context.startActivity(it);
//            }
//
//        }
    }
}