package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.fragments.NextEpisodeFragment;
import br.com.etm.checkseries.fragments.SerieFragment;

/**
 * Created by EDUARDO_MARGOTO on 27/10/2015.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = TabsAdapter.class.getSimpleName();
    private static final String PARAM_POSITION = "position";

    private String[] titles;
    private ArrayList<Serie> series;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        titles = new String[]{context.getResources().getString(R.string.app_title_myseries),
                context.getResources().getString(R.string.app_title_nextepisode)};
        this.series = new ArrayList<>();
    }

    @Deprecated
    public TabsAdapter(FragmentManager fm, Context context, ArrayList<Serie> series) {
        super(fm);
        titles = new String[]{context.getResources().getString(R.string.app_title_myseries),
                context.getResources().getString(R.string.app_title_nextepisode)};
        this.series = series;
    }

    @Override
    public Fragment getItem(int position) {
        Log.i(TAG, "getItem(" + position + ")");
        Fragment frag = null;
        if (position == 0) { //MYSERIES
            frag = SerieFragment.newInstance();
        } else if (position == 1) { // NEXTS
            frag = new NextEpisodeFragment();
        } else if (position == 2) { // NEXTS
            frag = new NextEpisodeFragment();
        }
        return frag;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        Log.i(TAG, "setSeries");
        this.series = new ArrayList<>(series);
    }
}
