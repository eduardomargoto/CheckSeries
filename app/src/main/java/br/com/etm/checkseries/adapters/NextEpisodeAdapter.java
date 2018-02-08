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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.utils.UtilsImages;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by EDUARDO_MARGOTO on 28/10/2015.
 */

public class NextEpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemClickListener onCheckItemClickListener;
    private int totalHeaders = 1;
    private List<ApiEpisode> episodeList;
    private LayoutInflater layoutInflater;
    private Context context;
    ApiEpisode episode = null;
    Calendar before = null;
    SparseIntArray headers_itens;

    public NextEpisodeAdapter(Context context, final List<ApiEpisode> episodeList) {
        this.episodeList = episodeList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        before = Calendar.getInstance();
        before.set(Calendar.DAY_OF_MONTH, before.get(Calendar.DAY_OF_MONTH) + 5);
        headers_itens = new SparseIntArray();
        if (!episodeList.isEmpty()) {
            ApiEpisode ep;
            for (int i = 0; i < episodeList.size(); i++) {
                if (i == 0) ep = episodeList.get(i);
                else ep = episodeList.get(i - 1);

                if (!episodeList.get(i).getDateFirstAired().equals(ep.getDateFirstAired())) {
                    totalHeaders++;
                }
                headers_itens.put(i + totalHeaders, totalHeaders);
            }
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        if (viewType == TYPE_HEADER) {
            view = layoutInflater.inflate(R.layout.header_item_nextepisodes, parent, false);
            return new MyViewHolderHeader(view);
        } else {
            if (EnvironmentConfig.getInstance().isLayoutCompat())
                view = layoutInflater.inflate(R.layout.item_serie_compat, parent, false);
            else
                view = layoutInflater.inflate(R.layout.item_serie_v2, parent, false);
            return new MyViewHolder(view, this);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("LOG", "NextEpisodeAdapter - onBindViewHolder(" + position + ")");

        if (holder instanceof MyViewHolderHeader && !episodeList.isEmpty()) {
            if (position == 0)
                episode = episodeList.get(position);
            else episode = episodeList.get((position + 1) - headers_itens.get(position + 1));
            MyViewHolderHeader holderHeader = (MyViewHolderHeader) holder;
            holderHeader.bind(episode);

        } else if (holder instanceof MyViewHolder && !episodeList.isEmpty()) {

            int header = headers_itens.get(position);
            int newposition = position - header;

            MyViewHolder mvholder = (MyViewHolder) holder;
            episode = episodeList.get(newposition);
//            Serie serie = null;
//            try {
//                serie = new DAO_Serie(context).find(String.valueOf(episode.getSerieId()));
//            } catch (Exception e) {
//                Toast.makeText(context, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                e.printStackTrace();
//            }

            // mvholder.tvName.setText(serie.getName());

            if (episode != null) {

                mvholder.tvNextEpisode.setText(episode.getTitleFormatted());
                mvholder.tvNextEpisodeTime.setText(episode.getDateFirstAiredFormatted(context));
            }
        /* -- EXIBIÇÃO NETWORK E HORA QUE VAI AO AR -- */
//            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
//                String text = serie.getNetwork();
//                if (!serie.getAirs_DayOfWeekFormatted().equals(""))
//                    text = text + " / " + serie.getAirs_DayOfWeekFormatted();
//                if (!serie.getAirs_Time().equals(""))
//                    text = text + " " + serie.getAirs_Time();
//                mvholder.tvNetwork.setText(text);
//            }


            if (!EnvironmentConfig.getInstance().isLayoutCompat()) {
                mvholder.rlView.setMinimumHeight(UtilsImages.getHeightDensity(context));
                //          File file = UtilsImages.getFilesDirectory(context, serie.getFanArtFilename());
                // if (file == null) {
                //      Bitmap bitmap = null;
//                    try {
//                        bitmap = Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                                .stableKey(serie.getFanArtFilenameCache())
//                                .get();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

                //    UtilsImages.saveToInternalSorage(bitmap, serie.getFanArtFilename(), context);
                //    bitmap.recycle();
                //   }
//                Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getFanArt())
//                        .stableKey(serie.getFanArtFilenameCache())
//                        .resize(UtilsImages.getWidthAllDensity(context), UtilsImages.getHeightDensity(context))
//                        .placeholder(R.drawable.loading_animation_black)
//                        .error(R.drawable.image_area_48dp)
//                        .into(mvholder.ivSerie);

                UtilsImages.darkenImagen(mvholder.ivSerie); // escurecer as imagens para evitar problemas ao visualizar os icones
            } else {
                int width = 0, height = 0;
                if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    width = UtilsImages.getWidthAllDensity(context) / 2;
                    height = (UtilsImages.getHeightDensity(context) - 140) / 2;
                } else {
                    width = UtilsImages.getWidthAllDensity(context);
                    height = (UtilsImages.getHeightDensity(context) - 140);
                }
                mvholder.rlView.setMinimumHeight(height);
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

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnCheckItemClickListener(AdapterView.OnItemClickListener onCheckItemClickListener) {
        this.onCheckItemClickListener = onCheckItemClickListener;
    }

    private void onCheckItemHolderClick(MyViewHolder itemHolder) {
        if (onCheckItemClickListener != null) {
            onCheckItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    private void onItemHolderClick(MyViewHolder itemHolder) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
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
            if (header == 0)
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
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public class MyViewHolderHeader extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_header)
        TextView tvHeader;

        public MyViewHolderHeader(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(ApiEpisode episode) {
            Calendar dateEpisode = Calendar.getInstance();
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            try {
                dateEpisode.setTime(formatter.parse(episode.getDateFirstAired()));
                Calendar today = Calendar.getInstance();
                today.set(Calendar.HOUR_OF_DAY, 00);
                today.set(Calendar.MINUTE, 00);
                today.set(Calendar.SECOND, 00);
                today.set(Calendar.MILLISECOND, 00);

                long differenceDays = (int) ((dateEpisode.getTimeInMillis() - today.getTimeInMillis()) / (24 * 60 * 60 * 1000));
                if (differenceDays == 0) {
                    tvHeader.setText(context.getResources().getString(R.string.today));
                } else if (differenceDays == 1) {
                    tvHeader.setText(context.getResources().getString(R.string.tomorrow));
                } else if (differenceDays > 1 && differenceDays <= 3) {
                    tvHeader.setText(context.getResources().getString(R.string.at_days).replace("/days", "" + (differenceDays)));
                } else {
                    tvHeader.setText(episode.getDateFirstAiredFormatted(context));
                }
            } catch (ParseException e) {
                Log.e("Presenter", "NextEpisode", e);
                tvHeader.setText(episode.getDateFirstAiredFormatted(context));
            }
        }
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ic_check_episode)
        ImageButton icCheckEpisode;
        @BindView(R.id.tv_title)
        TextView tvName;
        @BindView(R.id.tv_nextepisode_serie)
        TextView tvNextEpisode;
        @BindView(R.id.tv_network_serie)
        TextView tvNetwork;
        @BindView(R.id.tv_nextepisodetime_serie)
        TextView tvNextEpisodeTime;

        @BindView(R.id.iv_serie)
        ImageView ivSerie;
        @BindView(R.id.rl_view)
        RelativeLayout rlView;


        public MyViewHolder(View itemView, NextEpisodeAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                adapter.onItemHolderClick(this);
            });
            icCheckEpisode.setOnClickListener(view -> {
                adapter.onCheckItemHolderClick(this);
            });
        }

    }
}