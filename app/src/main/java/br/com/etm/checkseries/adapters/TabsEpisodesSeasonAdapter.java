package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.fragments.EpisodeInformationFragment;

/**
 * Created by EDUARDO_MARGOTO on 03/01/2016.
 */
public class TabsEpisodesSeasonAdapter extends FragmentStatePagerAdapter {


    private final String[] titles;
    private List<Episode> episodeList;
    private Episode episode;
    private Serie serie;
    private Context context;

    public TabsEpisodesSeasonAdapter(FragmentManager fm, Context context, List<Episode> episodeList, Serie serie) {
        super(fm);
        this.episodeList = new ArrayList<>(episodeList);
        this.context = context;
        this.serie = serie;

        this.titles = new String[episodeList.size()];
        if (!episodeList.isEmpty()) {
            for (int i = 0; i < episodeList.size(); i++) {
                titles[i] = episodeList.get(i).getNumEpisodeFormatted();
//                if (episodeList.get(i).getEpisodeNumber() < 10)
//                    titles[i] = "E0" + String.valueOf(episodeList.get(i).getEpisodeNumber());
//                else
//                    titles[i] = "E" + String.valueOf(episodeList.get(i).getEpisodeNumber());
            }
        }

    }

    @Override
    public Fragment getItem(int position) {
        Log.i("LOG-AMBISERIES", "TabsAdapter - getItem(" + position + ")");
        Fragment frag = new EpisodeInformationFragment(serie);
        Bundle b = new Bundle();
        b.putSerializable("episode", episodeList.get(position));
        frag.setArguments(b);
        return frag;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }
}
