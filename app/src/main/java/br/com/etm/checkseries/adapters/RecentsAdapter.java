package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.deprecated.utils.UtilsCacheMemory;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 28/10/2015.
 */

/*Alterar para buscar apenas os mais recents*/
public class RecentsAdapter extends RecyclerView.Adapter<RecentsAdapter.MyViewHolder> {

    private List<Serie> serieList;
    private LayoutInflater layoutInflater;
    private float scale;
    private int width, height;
    private Context context;
    private Handler handler = new Handler();
    private MyViewHolder mvh = null;
    private UtilsImages utilsImages = new UtilsImages();

    public RecentsAdapter(Context context, final List<Serie> serieList) {
        this.serieList = serieList;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;

        scale = context.getResources().getDisplayMetrics().density;
        width = context.getResources().getDisplayMetrics().widthPixels - (int) (14 * scale + 0.5f);
        height = (width / 16) * 9;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View view = layoutInflater.inflate(R.layout.item_serie, parent, false);
//        View view = layoutInflater.inflate(R.layout.item_car_card, parent, false); // USANDO CARDVIEW
        mvh = new MyViewHolder(view);

        return mvh;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Log.i("LOG", "onBindViewHolder()");

        if (serieList.get(position).getBm_poster() != null)
            holder.iv_serie.setImageBitmap(serieList.get(position).getBm_poster());
        else
            holder.iv_serie.setImageResource(R.drawable.no_image_avaiable);

        holder.tv_serie_name.setText(serieList.get(position).getName());


        new Thread() {
            public void run() {
                if (serieList.get(position).getBm_poster() == null) {
                    try {
                        serieList.get(position).setBm_poster(HttpConnection.downloadImage(APITheTVDB.PATH_BANNERS + serieList.get(position).getPoster()));
                        if (serieList.get(position).getBm_poster() != null)
                            UtilsCacheMemory.getInstance().addBitmapToMemoryCache(serieList.get(position).getId().toString(), serieList.get(position).getBm_poster());
                    } catch (Exception e) {
                        Log.i("LOG-EXCEPTION", e.toString());
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //TRATAMENTO PARA ARREDONDAMENTO DE IMAGENS NO CARDVIEW
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (serieList.get(position).getBm_poster() != null) {
                                utilsImages.loadBitmap(serieList.get(position).getBm_poster(), holder.iv_serie, context, 75, 120, serieList.get(position).getId().toString());
                            } else {
                                utilsImages.loadBitmapResId(R.drawable.no_image_avaiable, holder.iv_serie, context, 75, 120);
                            }
                        } else {
                            Bitmap bitmap = null;
                            if (serieList.get(position).getBm_poster() != null) {
                                bitmap = serieList.get(position).getBm_poster();
                                UtilsCacheMemory.getInstance().addBitmapToMemoryCache(serieList.get(position).getId().toString(), bitmap);
                            } else
                                bitmap = UtilsImages.decodeSampledBitmapFromResource(context.getResources(), R.drawable.no_image_avaiable, 75, 120);

                            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
                            bitmap = UtilsImages.getRoundedCornerBitmap(context, bitmap, 4, width, height, false, false, false, false);
                            utilsImages.loadBitmap(bitmap, holder.iv_serie, context, 75, 120, serieList.get(position).getId().toString());
                        }
                    }
                });
            }
        }.start();

        //TRATAMENTO PARA ARREDONDAMENTO DE IMAGENS NO CARDVIEW
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (serieList.get(position).getBm_poster() != null)
                new UtilsImages().loadBitmap(serieList.get(position).getBm_poster(), holder.iv_serie, this.context, 75, 120, serieList.get(position).getId().toString());
//                holder.ivSerie.setImageBitmap(serieList.get(position).getBm_poster());
//            else
//                holder.ivSerie.setImageResource(R.drawable.no_image_avaiable);
        } else {
            Bitmap bitmap = null;
            if (serieList.get(position).getBm_poster() != null)
                bitmap = serieList.get(position).getBm_poster();
//            else
//                bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.no_image_avaiable);

            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
            bitmap = UtilsImages.getRoundedCornerBitmap(context, bitmap, 4, width, height, false, false, false, false);
            new UtilsImages().loadBitmap(bitmap, holder.iv_serie, this.context, 75, 120, serieList.get(position).getId().toString());
//            holder.ivSerie.setImageBitmap(bitmap);
        }


//        try {
//            YoYo.with(Techniques.FadeIn)
//                    .duration(700)
//                    .playOn(holder.itemView);
//        } catch (Exception e) {
//            Log.i("LOG", "ERROR ANIMATION");
//            System.out.print("ERROR ANIMATION");
//        }
    }

    @Override
    public int getItemCount() {
        if (serieList == null)
            serieList = new ArrayList<>();
        return serieList.size();
    }



    public void updateListItem(int position) {
        notifyItemChanged(position);
//        onBindViewHolder(mvh, position);
    }

    public void removeListItem(int position) {
        serieList.remove(position);
        notifyItemRemoved(position);
    }

    public void addListItem(Serie serie, int position) {
        serieList.add(serie);
        notifyItemInserted(position);
//        notifyItemChanged(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_serie;
        public TextView tv_serie_name;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_serie = (ImageView) itemView.findViewById(R.id.iv_serie);
            tv_serie_name = (TextView) itemView.findViewById(R.id.tv_name_serie);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }
}