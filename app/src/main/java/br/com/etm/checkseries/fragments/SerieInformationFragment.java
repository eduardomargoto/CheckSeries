package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.daos.DAO_ListSerie;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.utils.HelpFragment;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.activity.CommentActivity;
import br.com.etm.checkseries.activity.SerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 08/11/2015.
 */
public class SerieInformationFragment extends Fragment {

    private static final int HEIGHT = 350;

    private ImageView iv_banner;
    private TextView tv_overview;
    private CardView rl_view_banner;
    private ImageView iv_background;
    private ImageView iv_favorite;
    private ImageView iv_share;
    private ImageView iv_remove;
    private ImageView iv_allwatched;
    private ImageView iv_comment;
    private TextView tv_genre;
    private TextView tv_year;
    private TextView tv_network;
    private TextView tv_status;
    private TextView tv_duration;
    private TextView tv_rating;
    private Calendar d;

    private Serie mySerie = null;
    private int positionTab = -1;

    @SuppressLint("ValidFragment")
    public SerieInformationFragment() {
    }

    @SuppressLint("ValidFragment")
    public SerieInformationFragment(Serie serie) {
        mySerie = serie;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            positionTab = b.getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_serie_information, container, false);
        Log.i("LOG-AMBISERIES", "SerieInformationFragment - onCreateView");
        iv_banner = (ImageView) v.findViewById(R.id.iv_banner);

        iv_background = (ImageView) v.findViewById(R.id.iv_background_banner);
        iv_favorite = (ImageView) v.findViewById(R.id.iv_favorite);
        iv_share = (ImageView) v.findViewById(R.id.iv_share);
        iv_remove = (ImageView) v.findViewById(R.id.iv_remove);
        iv_allwatched = (ImageView) v.findViewById(R.id.iv_allwatched);
        iv_comment = (ImageView) v.findViewById(R.id.iv_comment);
        tv_overview = (TextView) v.findViewById(R.id.tv_overview);
        tv_genre = (TextView) v.findViewById(R.id.tv_genre);
        tv_year = (TextView) v.findViewById(R.id.tv_year);
        tv_duration = (TextView) v.findViewById(R.id.tv_duration);
        tv_network = (TextView) v.findViewById(R.id.tv_network);
        tv_status = (TextView) v.findViewById(R.id.tv_status);
//        tv_rating = (TextView) v.findViewById(R.id.tv_rating);
        rl_view_banner = (CardView) v.findViewById(R.id.rl_view_banner);

        d = Calendar.getInstance();
        d.setTime(mySerie.getFirst_aired());

//        Picasso.with(getActivity()).load(APITheTVDB.PATH_BANNERS + mySerie.getFanArt())
//                .stableKey(mySerie.getFanArtFilenameCache())
//                .resize(UtilsImages.getWidthAllDensity(getActivity()), UtilsImages.getHeightDensity(getContext()))
//                .placeholder(R.drawable.loading_animation_white)
//                .error(R.drawable.image_area_48dp)
//                .into(iv_background);
        UtilsImages.darkenImagen(iv_background); // escurecer as imagens para evitar problemas ao visualizar os icones

        if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
            if (HttpConnection.isConnectionWifiOnline(getContext())) {
//                Picasso.with(getContext()).load(APITheTVDB.PATH_BANNERS + mySerie.getPoster())
//                        .resize(UtilsImages.getWidthAllDensity(getContext()), UtilsImages.getHeightDensity(getContext()) - 30)
//                        .centerInside()
//                        .placeholder(R.drawable.loading_animation_black)
//                        .error(R.drawable.image_area_48dp)
//                        .into(iv_banner);
            } else {
//                Picasso.with(getContext()).load(R.drawable.image_area_48dp)
//                        .memoryPolicy(MemoryPolicy.NO_CACHE)
//                        .into(iv_banner);
                UtilsImages.darkenImagen(iv_banner);
            }
        } else {
//            Picasso.with(getContext()).load(APITheTVDB.PATH_BANNERS + mySerie.getPoster())
//                    .resize(UtilsImages.getWidthAllDensity(getContext()), UtilsImages.getHeightDensity(getContext()) - 30)
//                    .centerInside()
//                    .placeholder(R.drawable.loading_animation_black)
//                    .error(R.drawable.image_area_48dp)
//                    .into(iv_banner);
        }

//            tv_rating.setText("Avaliações: " + mySerie.get);
        tv_status.setText(getResources().getString(R.string.app_word_status) + ": " + getResources().getString(mySerie.getStatusFormatted()));
        if (!mySerie.getAirs_DayOfWeekFormatted().equals("")) {
            tv_network.setText(getResources().getString(R.string.app_word_network) + ": " + mySerie.getNetwork() + " (" + mySerie.getAirs_DayOfWeekFormatted() + " " + mySerie.getAirs_Time() + ")");
        } else
            tv_network.setText(getResources().getString(R.string.app_word_network) + ": " + mySerie.getNetwork());

        tv_year.setText(getResources().getString(R.string.app_word_year) + ": " + d.get(Calendar.YEAR));
        tv_duration.setText(getResources().getString(R.string.app_word_duration) + ": " + mySerie.getRuntime() + " min");
        tv_overview.setText(mySerie.getOverview());
        tv_genre.setText(mySerie.getGenreFormatted());

        if (mySerie.isFavorite()) {
            iv_favorite.setImageResource(R.drawable.star_24dp);
        } else {
            iv_favorite.setImageResource(R.drawable.star_outline_24dp);
        }

        iv_comment.setOnClickListener(click_comment);
        iv_remove.setOnClickListener(click_remove);
        iv_favorite.setOnClickListener(click_favorite);
        iv_allwatched.setOnClickListener(click_allwatched);
        iv_share.setOnClickListener(click_share);
        return v;
    }

    View.OnClickListener click_share = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://thetvdb.com/?tab=series&id=" + mySerie.getId());
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://trakt.tv/search/imdb/" + mySerie.getImdb_id());
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    };

    View.OnClickListener click_favorite = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String str_action = "";
            if (mySerie.isFavorite()) {
                mySerie.setFavorite(false);
                iv_favorite.setImageResource(R.drawable.star_outline_24dp);
                str_action = v.getResources().getString(R.string.app_action_revfavorite_serie);
                Toast.makeText(v.getContext(), mySerie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
            } else {
                mySerie.setFavorite(true);
                iv_favorite.setImageResource(R.drawable.star_24dp);
                str_action = v.getResources().getString(R.string.app_action_addfavorite_serie);
                Toast.makeText(v.getContext(), mySerie.getName() + " " + str_action, Toast.LENGTH_SHORT).show();
            }
            HelpFragment.updateSerieMainActivity(mySerie);
            new DAO_Serie(v.getContext()).edit(mySerie);
        }
    };

    View.OnClickListener click_comment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (HttpConnection.isOnline(getActivity())) {
                    Intent it = new Intent(getActivity(), CommentActivity.class);
                    Serie s = mySerie.clone();
                    s.setEpisodeList(null);
                    s.setBm_poster(null);
                    it.putExtra("serie", s);
                    startActivity(it);
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.app_internet_off), Toast.LENGTH_SHORT).show();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    };

    View.OnClickListener click_allwatched = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                UtilsEntitys.createAlertDialog(v.getContext(),
                        getResources().getString(R.string.app_dl_title_all_watched), getResources().getString(R.string.app_text_mark_allwatched) + "?",
                        getResources().getString(R.string.app_dialog_ok), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                for (Episode ep : mySerie.getEpisodeList()) {
                                    if (ep.getFirstAired() != null) {
                                        if (ep.getFirstAired().before(new Date())) {
                                            ep.setWatched(true);
                                            new DAO_Episode(getContext()).edit(ep);
                                        }
                                    }
                                }
                                HelpFragment.updateSerieMainActivity(mySerie);
                                EpisodeInformationFragment.UPDATE_FRAGMENT = true;
                                SerieActivity serieActivity = (SerieActivity) getActivity();
                                serieActivity.mTabsAdapter.notifyDataSetChanged();


                                dialog.dismiss();

//                                ALTERAR ICONE AO TERMINAR OPERACAO
                            }
                        },
                        getResources().getString(R.string.app_dialog_cancel)).create().show();
            } catch (Exception e) {
                Log.i("LOG-EXCEPTION", e.toString());
            }
        }
    };

    View.OnClickListener click_remove = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                UtilsEntitys.createAlertDialog(v.getContext(),
                        getResources().getString(R.string.app_dl_title_remove_serie), getResources().getString(R.string.app_text_wantremove) + " " + mySerie.getName() + "?",
                        getResources().getString(R.string.app_dialog_removebutton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                new DAO_ListSerie(getContext()).remove(mySerie);
                                new DAO_Serie(getContext()).remove(mySerie);
                                for (Episode ep : mySerie.getEpisodeList()) {
                                    new DAO_Episode(getContext()).remove(ep);
                                }
                                Toast.makeText(getContext(), mySerie.getName() + " " + getResources().getString(R.string.app_text_removed), Toast.LENGTH_SHORT).show();

//                                for (int i = 0; i < MainActivity.mySeries.size(); i++) {
//                                    if (MainActivity.mySeries.get(i).getId().equals(mySerie.getId())) {
//                                        MainActivity.mySeries.remove(i);
//                                        break;
//                                    }
//                                }
                                dialog.dismiss();

                                Intent it = new Intent();
                                getActivity().setResult(Activity.RESULT_OK, it);
                                getActivity().finish();
                            }
                        },
                        getResources().getString(R.string.app_dialog_cancel)).create().show();
            } catch (Exception e) {
                Log.i("LOG-EXCEPTION", e.toString());
            }
        }

    };


}
