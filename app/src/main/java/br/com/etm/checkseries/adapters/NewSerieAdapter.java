package br.com.etm.checkseries.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import br.com.etm.checkseries.api.data.tracktv.ApiMediaObject;
import br.com.etm.checkseries.data.DbProvider;
import br.com.etm.checkseries.utils.LoadingImageListener;
import br.com.etm.checkseries.utils.UtilsImages;
import butterknife.BindView;
import butterknife.ButterKnife;


public class NewSerieAdapter extends RecyclerView.Adapter<NewSerieAdapter.MyViewHolder> {

    private List<ApiMediaObject> mediaObjects;
    private AdapterView.OnItemClickListener onItemClickListener;
    private LoadingImageListener onLoadingImageListener;

    public NewSerieAdapter(List<ApiMediaObject> mediaObjects) {
        this.mediaObjects = mediaObjects;
        if (this.mediaObjects == null) {
            this.mediaObjects = new ArrayList<>();
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_newserie, parent, false);
        return new MyViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bind(mediaObjects.get(position));
    }


    public void setOnLoadingImageListener(LoadingImageListener onLoadingImageListener) {
        this.onLoadingImageListener = onLoadingImageListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(MyViewHolder itemHolder) {
        if (onItemClickListener != null) {
            itemHolder.ivAddSerie.setImageDrawable(ContextCompat.
                    getDrawable(itemHolder.itemView.getContext(), R.drawable.loading_animation_white));

            onItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }

    public void setMediaObjects(List<ApiMediaObject> mediaObjects) {
        if (mediaObjects != null) {
            this.mediaObjects = mediaObjects;
            notifyDataSetChanged();
        }
    }

    public void onAddFinished(int position) {
        mediaObjects.get(position).setAdded(true);
        notifyItemChanged(position);
    }

    public void onLoadingImage(int position, ApiFanArtObject apiFanArtObject) {
        mediaObjects.get(position).setFanArtImages(apiFanArtObject);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mediaObjects.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_addserie)
        ImageView ivAddSerie;
        @BindView(R.id.iv_serie)
        ImageView ivSerie;
        @BindView(R.id.tv_name_serie)
        TextView tvNameSerie;
        @BindView(R.id.tv_ano_serie)
        TextView tvAnoSerie;
        @BindView(R.id.card_view)
        CardView cardView;

        public MyViewHolder(View itemView, final NewSerieAdapter adapter) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            ivAddSerie.setOnClickListener(view -> {
                adapter.onItemHolderClick(MyViewHolder.this);
            });
        }

        private void bind(ApiMediaObject mediaObject) {
            itemView.setTag(mediaObject);
            ViewCompat.setElevation(cardView, 4);
            tvNameSerie.setText(mediaObject.getTitle());
            tvAnoSerie.setText(String.valueOf(mediaObject.getYear()));

            if(mediaObject.isAdded()) {
                ivAddSerie.setImageDrawable(ContextCompat.
                        getDrawable(itemView.getContext(), R.drawable.ic_check_circle_white_24dp));
            } else {
                ivAddSerie.setImageDrawable(ContextCompat.
                        getDrawable(itemView.getContext(), R.drawable.ic_add_white_24dp));
            }

            if (onLoadingImageListener != null
                    && mediaObject.getFanArtImages() == null
                    && mediaObject.getIdToImage() != null) {
                onLoadingImageListener.onLoading(getAdapterPosition(), mediaObject.getIdToImage(), mediaObject.getType());
            }

            //TODO: create an algorithm to take the best image for the moment.
            if (mediaObject.getFanArtImages() != null && mediaObject.getFanArtImages().getTvBannerImages() != null) {
                ivSerie.setAdjustViewBounds(true);
                Picasso.with(itemView.getContext())
                        .load(mediaObject.getFanArtImages().getTvBannerImages().get(0).getUrl())
                        .error(R.drawable.ic_panorama_white)
                        .into(ivSerie);
            } else {
                ivSerie.setAdjustViewBounds(false);
                ivSerie.setImageDrawable(ContextCompat.
                        getDrawable(itemView.getContext(), R.drawable.ic_panorama_white));
            }
            UtilsImages.darkenImagen(ivSerie);
        }

    }
}
