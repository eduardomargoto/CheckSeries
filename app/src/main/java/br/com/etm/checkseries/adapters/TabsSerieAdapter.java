package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.EpisodeInformationFragment;
import br.com.etm.checkseries.fragments.SeasonsFragment;
import br.com.etm.checkseries.fragments.SerieInformationFragment;

/**
 * Created by EDUARDO_MARGOTO on 27/10/2015.
 */
public class TabsSerieAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private String[] titles;
    private Serie serie;


    public TabsSerieAdapter(FragmentManager fm, Context context, Serie serie) {
        super(fm);
        this.context = context;
        titles = new String[]{context.getResources().getString(R.string.app_title_serie), context.getResources().getString(R.string.app_title_episode), context.getResources().getString(R.string.app_title_seasons)};
        this.serie = serie;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i("LOG-AMBISERIES", "TabsSerieAdapter - getItem -> " + position);
        Fragment frag = null;
        if (position == 0) { //SERIE
            frag = new SerieInformationFragment(serie);
        } else if (position == 1) { // EPISODE
            frag = new EpisodeInformationFragment(serie);
        } else if (position == 2) { // SEASONS
            frag = new SeasonsFragment(serie);
        }
        Bundle b = new Bundle();
        b.putInt("position", position);
        b.putSerializable("episode", serie.getNextEpisode());
        frag.setArguments(b);
        return frag;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
//        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
