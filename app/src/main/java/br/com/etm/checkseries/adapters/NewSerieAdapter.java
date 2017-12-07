package br.com.etm.checkseries.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsImages;


/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class NewSerieAdapter extends RecyclerView.Adapter<NewSerieAdapter.MyViewHolder> {

    private final static int WIDTH_IMAGES = 70;
    private final static int HEIGHT_IMAGES = 200;


    private List<Serie> serieList;
    private LayoutInflater layoutInflater;

    private Context contextActivity;
    private MyViewHolder mvh = null;

    public NewSerieAdapter(Context context, final List<Serie> serieList) {
        this.serieList = serieList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.contextActivity = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View view = layoutInflater.inflate(R.layout.item_newserie, parent, false);
        mvh = new MyViewHolder(view);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.i("LOG", "onBindViewHolder()");
//        holder.rl_newserie_view.setMinimumHeight(HEIGHT_IMAGES);

        holder.tv_serie_name.setText(serieList.get(position).getName());
        holder.tv_serieid.setText(serieList.get(position).getId().toString());
        if (serieList.get(position).getFirst_aired() != null) {
            Calendar c = Calendar.getInstance();
            c.setTime(serieList.get(position).getFirst_aired());
            holder.tv_serie_ano.setText("" + c.get(Calendar.YEAR));
        }

        if (serieList.get(position).isAdded()) {

            Picasso.with(contextActivity).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48)
                    .into(holder.iv_addserie);
//            utilsImages.loadBitmapResId(R.drawable.ic_checkboxmarked, holder.iv_addserie, this.contextActivity, 24, 24);
        } else {
            Picasso.with(contextActivity).load(R.drawable.ic_add_white_24dp).resize(48, 48)
                    .into(holder.iv_addserie);
//            utilsImages.loadBitmapResId(R.drawable.ic_plus_24, holder.iv_addserie, this.contextActivity, 24, 24);
        }

        if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
            if (HttpConnection.isConnectionWifiOnline(contextActivity)) {
                Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serieList.get(position).getBanner())
                        .resize(UtilsImages.getWidthAllDensity(contextActivity), HEIGHT_IMAGES)
                        .placeholder(R.drawable.loading_animation_black)
//                .error(android.R.color.transparent)
                        .error(R.drawable.image_area_36dp)
                        .into(holder.iv_serie);
                UtilsImages.darkenImagen(holder.iv_serie);
            } else {
                Picasso.with(contextActivity).load(R.drawable.image_area_36dp)
                        .memoryPolicy(MemoryPolicy.NO_CACHE)
                        .into(holder.iv_serie);
                UtilsImages.darkenImagen(holder.iv_serie);
            }
        } else {
            Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serieList.get(position).getBanner())
                    .resize(UtilsImages.getWidthAllDensity(contextActivity), HEIGHT_IMAGES)
                    .centerCrop()
                    .placeholder(R.drawable.loading_animation_black)
//                .error(android.R.color.transparent)
                    .error(R.drawable.image_area_36dp)
                    .into(holder.iv_serie);
            UtilsImages.darkenImagen(holder.iv_serie);
        }


    }

    @Override
    public int getItemCount() {
        return serieList.size();
    }


//    public void updateListItem(int position) {
//        notifyItemChanged(position);
//    }
//
//    public void removeListItem(int position) {
//        serieList.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    public void addListItem(Serie serie, int position) {
//        serieList.add(serie);
//        notifyItemInserted(position);
//    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_serie, iv_addserie;
        public TextView tv_serie_name, tv_serie_ano, tv_serieid;
        public RelativeLayout rl_newserie_view;
        private Handler handler = new Handler();

        public MyViewHolder(View itemView) {
            super(itemView);

            rl_newserie_view = (RelativeLayout) itemView.findViewById(R.id.rl_newserie_view);
            iv_addserie = (ImageView) itemView.findViewById(R.id.iv_addserie);
            iv_serie = (ImageView) itemView.findViewById(R.id.iv_serie);
            tv_serie_name = (TextView) itemView.findViewById(R.id.tv_name_serie);
            tv_serie_ano = (TextView) itemView.findViewById(R.id.tv_ano_serie);
            tv_serieid = (TextView) itemView.findViewById(R.id.tv_serieid);

            iv_addserie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    iv_addserie.setEnabled(false);

//                    iv_addserie.setImageResource(R.drawable.loading_black);
                    String aliasNames = "";
                    final Serie[] serie = {new Serie()};
                    boolean isAdded = false;
                    final Integer serieID = Integer.parseInt(tv_serieid.getText().toString());
                    for (Serie s : serieList) {
                        if (s.getId().equals(serieID)) {
                            if (s.isAdded()) {
                                isAdded = true;
                                break;
                            }
                        }
                    }

                    if (!isAdded) {
                        Picasso.with(contextActivity).load(R.drawable.loading_animation_black)
                                .placeholder(R.drawable.loading_animation_black).resize(24, 24)
                                .into(iv_addserie);

                        final ProgressDialog progressDialog = new ProgressDialog(contextActivity);
                        progressDialog.setMessage("Aguarde enquanto atualizamos a serie.");
                        progressDialog.show();

                    /*    new Thread() {
                            public void run() {
                                try {
                                    serie[0] = APITheTVDB.getSerieAndEpisodes(tv_serieid.getText().toString());
                                    for (Serie s : serieList) { // ATUALIZA O ALIAS NAME
                                        if (s.getId().equals(serie[0].getId()))
                                            serie[0].setAlias_names(s.getAlias_names());
                                    }

                                    if (!serie[0].getFanArt().equals("")) {
                                        Bitmap bitmap = Picasso.with(contextActivity).load(APITheTVDB.PATH_BANNERS + serie[0].getFanArt())
                                                .stableKey(serie[0].getFanArtFilenameCache())
                                                .get();

                                        UtilsImages.saveToInternalSorage(bitmap, serie[0].getFanArtFilename(), contextActivity);
                                        bitmap.recycle();
                                    }

                                    List<ListOfUser> listOfUserLit = new DAO_List(v.getContext()).findAll();
                                    if (!listOfUserLit.isEmpty()) {
                                        ListOfUser_Serie listOfUser_serie = new ListOfUser_Serie();
                                        listOfUser_serie.setSerie(serie[0]);
                                        listOfUser_serie.setListOfUser(listOfUserLit.get(0));

                                        new DAO_ListSerie(v.getContext()).create(listOfUser_serie);
                                    }
                                    new DAO_Serie(v.getContext()).create(serie[0]);
                                    for (Episode ep : serie[0].getEpisodeList()) {
                                        new DAO_Episode(v.getContext()).create(ep);
                                    }


                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
//                                            utilsImages.loadBitmapResId(R.drawable.ic_checkboxmarked, iv_addserie, v.getContext(), 24, 24);
                                            Picasso.with(contextActivity).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48).into(iv_addserie);
                                            progressDialog.dismiss();
                                            MainActivity.mySeries.add(serie[0]);
                                            HelpFragment.updateSerieMainActivity(serie[0]);
//                                            updateWidgets(context);
                                            UtilsEntitys.updateWidgets(v.getContext().getApplicationContext());
                                            iv_addserie.setEnabled(true);
                                            Toast.makeText(v.getContext(), "Serie " + serie[0].getName() + " foi adicionada.", Toast.LENGTH_SHORT).show();

                                        }
                                    });
                                } catch (Exception e) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressDialog.dismiss();
                                            Picasso.with(contextActivity).load(R.drawable.ic_add_white_24dp).resize(48, 48)
                                                    .into(iv_addserie);
//                                            utilsImages.loadBitmapResId(R.drawable.ic_plus_24, iv_addserie, v.getContext(), 24, 24);
                                            iv_addserie.setEnabled(true);
                                            Toast.makeText(v.getContext(), "Ocorreu um erro de conexão, tente novamente.", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    Log.i("LOG-EXCEPTION", e.toString());
                                }


                            }
                        }.start();*/
                    }
                }
            });
        }


    }
}
