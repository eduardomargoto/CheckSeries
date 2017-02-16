package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.util.Log;

import com.appodeal.ads.Appodeal;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.NextEpisodeFragment;
import br.com.etm.checkseries.fragments.SerieFragment;
import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 27/10/2015.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {
    private Drawable myDrawable = null;
    private SpannableStringBuilder sb = null;
    private ImageSpan span = null;

    private Context context;
    private String[] titles;
    private List<Serie> series;

    public TabsAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        titles = new String[]{context.getResources().getString(R.string.app_title_myseries),
                context.getResources().getString(R.string.app_title_nextepisode)};
        this.series = null;

    }

    @Deprecated
    public TabsAdapter(FragmentManager fm, Context context, List<Serie> series) {
        super(fm);
        this.context = context;

        titles = new String[]{context.getResources().getString(R.string.app_title_myseries),
                context.getResources().getString(R.string.app_title_nextepisode)};
        this.series = series;

    }

    @Override
    public Fragment getItem(int position) {
        Log.i("LOG-AMBISERIES", "TabsAdapter - getItem(" + position + ")");
        Fragment frag = null;
        if (position == 0) { //MYSERIES
            if (MainActivity.SEARCHVIEW) {
                frag = new SerieFragment(series);
            } else frag = new SerieFragment();

        } else if (position == 1) { // NEXTS
            frag = new NextEpisodeFragment();
        } else if (position == 2) { // NEXTS
            frag = new NextEpisodeFragment();
        }
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
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
        Log.i("LOG-AMBISERIES", "TabsAdapter - setSeries");
        this.series = new ArrayList<>(series);
    }
}
