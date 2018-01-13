package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Calendar;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.ListSeasonsAdapter;
import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.activity.SerieActivity;
import br.com.etm.checkseries.utils.HelpFragment;

/**
 * Created by EDUARDO_MARGOTO on 08/11/2015.
 */
public class SeasonsFragment extends Fragment {


    private Serie mySerie = null;
    private int season;
    private ListView lv_seasons;
    ListSeasonsAdapter adapter;
    private Handler handler = new Handler();

    @SuppressLint("ValidFragment")
    public SeasonsFragment() {
    }

    @SuppressLint("ValidFragment")
    public SeasonsFragment(Serie serie) {
        mySerie = serie;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_seasons, container, false);
        Log.i("LOG-AMBISERIES", "SeasonsFragment - onCreateView");

        lv_seasons = (ListView) v.findViewById(R.id.lv_seasons);
        lv_seasons.setAdapter(new ListSeasonsAdapter(mySerie, getActivity()));
        adapter = (ListSeasonsAdapter) lv_seasons.getAdapter();

        registerForContextMenu(lv_seasons);
        return v;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.i("LOG-AMBISERIES", "SeasonsFragment - onCreateContextMenu");
        super.onCreateContextMenu(menu, v, menuInfo);
        season = ListSeasonsAdapter.POSITION_ACTIVE_SEASON;
        String title = "";
        if (season == 0)
            title = getResources().getString(R.string.app_text_specials);
        else title = season + "ª " + getResources().getString(R.string.app_text_season);

        HelpFragment.inflaterContextMenu(getActivity(), mySerie, menu, R.menu.menu_options_season, title);
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("LOG-AMBISERIES", "SeasonsFragment - onOptionsItemSelected");
        final int position = ListSeasonsAdapter.POSITION_ACTIVE_SEASON;
//        ListSeasonsAdapter adapter = (ListSeasonsAdapter) lv_seasons.getAdapter();
        final SerieActivity serieActivity = (SerieActivity) getActivity();

        switch (item.getItemId()) {
            case R.id.it_options_watchedallepsiodes:

                mySerie.setEpisodeList(new DAO_Episode(getContext()).findAll(mySerie, season));
                for (Episode episode : mySerie.getEpisodeList()) {
//                    Log.d("LOG-SPECIALS", "POSITION -> " + position);
//                    if (episode.getSeasonNumber() == position) {
                    if (episode.getFirstAired() != null) {
                        if (Calendar.getInstance().getTime().after(episode.getFirstAired())) {
                            episode.setWatched(true);
                            new DAO_Episode(getContext()).updateWatchedSkipped(episode);
                        }
                    } else {
                        episode.setWatched(true);
                        new DAO_Episode(getContext()).edit(episode);
                    }
//                    }
                }
                HelpFragment.updateSerieMainActivity(mySerie);
                serieActivity.mTabsAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                break;
            case R.id.it_options_watchednoneepsiodes:
//                if (mySerie.getEpisodeList().isEmpty())
                    mySerie.setEpisodeList(new DAO_Episode(getContext()).findAll(mySerie, season));
                for (Episode episode : mySerie.getEpisodeList()) {
//                    if (episode.getSeasonNumber() == position) {
                    episode.setWatched(false);
                    episode.setDateWatched(null);
                    new DAO_Episode(getContext()).edit(episode);
//                    }
                }
                HelpFragment.updateSerieMainActivity(mySerie);
                serieActivity.mTabsAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                break;
            case R.id.it_options_watchedallhereepsiodes:
//                if (mySerie.getEpisodeList().isEmpty())
                    mySerie.setEpisodeList(new DAO_Episode(getContext()).findAll(mySerie));

                for (Episode episode : mySerie.getEpisodeList()) {
                    if (episode.getSeasonNumber() < position) {
                        episode.setWatched(true);
                        new DAO_Episode(getContext()).updateWatchedSkipped(episode);

                    }
                }
                HelpFragment.updateSerieMainActivity(mySerie);
                serieActivity.mTabsAdapter.notifyDataSetChanged();
                adapter.notifyDataSetChanged();
                break;
        }
        return true;
    }


    @Override
    public void onResume() {
        Log.i("LOG-AMBISERIES", "SeasonsFragment - onResume");
        super.onResume();
//        ListSeasonsAdapter adapter = (ListSeasonsAdapter) lv_seasons.getAdapter();
        mySerie.setEpisodeList(new DAO_Episode(getContext()).findAll(mySerie));

        adapter.notifyDataSetChanged();
    }
}
