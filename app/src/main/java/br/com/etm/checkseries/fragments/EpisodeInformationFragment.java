package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.activity.CommentActivity;
import br.com.etm.checkseries.activity.ListEpisodesSeasonActivity;
import br.com.etm.checkseries.activity.SerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 08/11/2015.
 */
public class EpisodeInformationFragment extends Fragment {

    private ImageView iv_banner, iv_watched, iv_next_skip, iv_comment, iv_share;
    private TextView tv_overview;
    private CardView rl_view_banner;
    private RelativeLayout rl_view_banner_img;
    private TextView tv_name_episode, tv_total_episode, tv_date_episode;
    private Serie mySerie = null;
    public static boolean UPDATE_FRAGMENT = false;
    public static boolean USING_TABS = false;
    Episode episode = null;

    Handler handler = new Handler();

    @SuppressLint("ValidFragment")
    public EpisodeInformationFragment() {
    }

    @SuppressLint("ValidFragment")
    public EpisodeInformationFragment(Serie serie) {
        mySerie = serie;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (episode == null) {
            if (savedInstanceState != null) {
                episode = (Episode) savedInstanceState.getSerializable("episode");
            }
            if (getArguments() != null) {
                episode = (Episode) getArguments().getSerializable("episode");
            }
        }
        if (episode == null) {
            episode = mySerie.getNextEpisode();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_episode_information, container, false);
        iv_watched = (ImageView) v.findViewById(R.id.iv_watched);
        iv_next_skip = (ImageView) v.findViewById(R.id.iv_next_skip);
        iv_comment = (ImageView) v.findViewById(R.id.iv_comment);
        iv_share = (ImageView) v.findViewById(R.id.iv_share);

        Log.i("LOG-AMBISERIES", "EpisodeInformationFragment - onCreateView");



        tv_name_episode = (TextView) v.findViewById(R.id.tv_name_episode);
        iv_banner = (ImageView) v.findViewById(R.id.iv_banner);
        tv_overview = (TextView) v.findViewById(R.id.tv_overview);
        tv_total_episode = (TextView) v.findViewById(R.id.tv_total_episode);
        tv_date_episode = (TextView) v.findViewById(R.id.tv_date_episode);
        rl_view_banner_img = (RelativeLayout) v.findViewById(R.id.rl_view_banner_img);
        rl_view_banner = (CardView) v.findViewById(R.id.rl_view_banner);

        rl_view_banner_img.setMinimumHeight(UtilsImages.getHeightImageDensity(getContext()));

        if (episode != null) {

            if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
                if (HttpConnection.isConnectionWifiOnline(getContext())) {
//                    Picasso.with(getContext()).load(APITheTVDB.PATH_BANNERS + episode.getFilename())
//                            .resize(UtilsImages.getWidthAllDensity(getContext()), UtilsImages.getHeightDensity(getContext()))
//                            .placeholder(R.drawable.loading_animation_black)
//                            .error(R.drawable.image_area_48dp)
//                            .into(iv_banner);
                    UtilsImages.darkenImagen(iv_banner);
                } else {
//                    Picasso.with(getContext()).load(R.drawable.image_area_48dp)
//                            .memoryPolicy(MemoryPolicy.NO_CACHE)
//                            .into(iv_banner);
                    UtilsImages.darkenImagen(iv_banner);
                }
            } else {
//                Picasso.with(getContext()).load(APITheTVDB.PATH_BANNERS + episode.getFilename())
//                        .resize(UtilsImages.getWidthAllDensity(getContext()), UtilsImages.getHeightDensity(getContext()))
//                        .placeholder(R.drawable.loading_animation_black)
//                        .error(R.drawable.image_area_48dp)
//                        .into(iv_banner);
                UtilsImages.darkenImagen(iv_banner);
            }


            tv_name_episode.setText(episode.getEpisodeFormatted());
            tv_total_episode.setText("(TOTAL " + episode.getTotalEpisodeNumber() + ")");
            tv_date_episode.setText(episode.getDateEpisodeFormatted(getContext()));
            tv_overview.setText(episode.getOverview());

            if (episode.isWatched()) {
//                Picasso.with(getContext()).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48)
//                        .into(iv_watched);
                iv_next_skip.setVisibility(View.INVISIBLE);
            } else {
//                Picasso.with(getContext()).load(R.drawable.ic_check).resize(48, 48)
//                        .into(iv_watched);
            }

            if (episode.isSkipped()) {
//                Picasso.with(getContext()).load(R.drawable.skip_next_marked_24dp).resize(48, 48)
//                        .into(iv_next_skip);
            } else {
//                Picasso.with(getContext()).load(R.drawable.skip_next_24dp).resize(48, 48)
//                        .into(iv_next_skip);
            }
        } else

        {
//            Picasso.with(getContext()).load(R.drawable.image_area_48dp)
//                    .memoryPolicy(MemoryPolicy.NO_CACHE)
//                    .into(iv_banner);
            UtilsImages.darkenImagen(iv_banner);
            tv_name_episode.setText("Nenhum episódio encontrado");

            iv_watched.setVisibility(View.INVISIBLE);
            iv_next_skip.setVisibility(View.INVISIBLE);
            iv_comment.setVisibility(View.INVISIBLE);
            iv_share.setVisibility(View.INVISIBLE);
        }

        iv_banner.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             if (!USING_TABS) {
                                                 if (mySerie.getNextEpisode() != null) {
                                                     Intent it = new Intent(getActivity(), ListEpisodesSeasonActivity.class);
                                                     Serie s = null;
                                                     try {
                                                         s = new DAO_Serie(getContext()).find(mySerie.getId().toString());
                                                     } catch (Exception e) {
                                                         Toast.makeText(getContext(), "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                         e.printStackTrace();
                                                     }

                                                     it.putExtra("serie", s);
                                                     it.putExtra("episode", episode);

                                                     startActivity(it);
                                                     USING_TABS = true;
                                                 }
                                             }
                                         }
                                     }

        );

        iv_comment.setOnClickListener(click_comment);
        iv_watched.setOnClickListener(click_watched);
        iv_next_skip.setOnClickListener(click_skipped);
        iv_share.setOnClickListener(click_share);


        return v;
    }

    View.OnClickListener click_comment = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (HttpConnection.isOnline(getActivity())) {
                    Intent it = new Intent(getActivity(), CommentActivity.class);
                    Serie serie = mySerie.clone();
                    serie.setEpisodeList(null);
                    serie.setBm_poster(null);
                    it.putExtra("serie", serie);
                    it.putExtra("episode", episode);
                    startActivity(it);
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.app_internet_off), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    View.OnClickListener click_share = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            Episode episode = mySerie.getNextEpisode();
            sendIntent.putExtra(Intent.EXTRA_TEXT, "http://thetvdb.com/?tab=episode&seriesid=" + mySerie.getId() + "&seasonid=" + episode.getSeasonId() + "&id=" + episode.getId());
//            sendIntent.putExtra(Intent.EXTRA_TEXT, "https://trakt.tv/search/imdb/" + mySerie.getNextEpisode().getImdb_id() + "?id_type=episode");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        }
    };

    View.OnClickListener click_skipped = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (episode.isSkipped()) {
                episode.setSkipped(false);
//                Picasso.with(getContext()).load(R.drawable.skip_next_24dp).resize(48, 48)
//                        .into(iv_next_skip);

            } else {
                episode.setSkipped(true);
//                Picasso.with(getContext()).load(R.drawable.skip_next_marked_24dp).resize(48, 48)
//                        .into(iv_next_skip);
            }

            if (!USING_TABS)
                UPDATE_FRAGMENT = true;
            else UPDATE_FRAGMENT = false;

            new DAO_Episode(getContext()).updateWatchedSkipped(episode);
            if (mySerie != null && !USING_TABS) {
                SerieActivity serieActivity = (SerieActivity) getActivity();
                serieActivity.mTabsAdapter.notifyDataSetChanged();
            } else {
                onResume();
            }

            HelpFragment.updateEpisodeMainActivity(episode, mySerie);
        }
    };

    View.OnClickListener click_watched = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (episode.isWatched()) {
                episode.setWatched(false);
                episode.setSkipped(false);
//                Picasso.with(getContext()).load(R.drawable.ic_check).resize(48, 48)
//                        .into(iv_watched);

            } else {
                episode.setWatched(true);
                episode.setSkipped(false);
//                Picasso.with(getContext()).load(R.drawable.ic_check_circle_white_24dp).resize(48, 48)
//                        .into(iv_watched);
                iv_next_skip.setVisibility(View.INVISIBLE);
            }
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    if (!USING_TABS)
                        UPDATE_FRAGMENT = true;
                    else UPDATE_FRAGMENT = false;

                    new DAO_Episode(getContext()).updateWatchedSkipped(episode);
                    if (mySerie != null && !USING_TABS) {
                        final SerieActivity serieActivity = (SerieActivity) getActivity();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                serieActivity.mTabsAdapter.notifyDataSetChanged();
                            }
                        });
                    } else {
                        onResume();
                    }

                    if (episode != null)
                        HelpFragment.updateEpisodeMainActivity(episode, mySerie);
                    else HelpFragment.updateSerieMainActivity(mySerie);
                }
            }.start();

        }
    };

    @Override
    public void onResume() {
        Log.i("LOG-AMBISERIES", "EpisodeInformationFragment - onResume");
        super.onResume();


        if (UPDATE_FRAGMENT) {
            if (mySerie != null) {
                mySerie.setEpisodeList(new DAO_Episode(getContext()).findAll(mySerie));
                episode = mySerie.getNextEpisode();
            }
            getFragmentManager().beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
            UPDATE_FRAGMENT = false;
        }
    }

}
