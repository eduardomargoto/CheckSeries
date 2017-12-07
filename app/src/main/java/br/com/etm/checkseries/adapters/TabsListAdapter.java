package br.com.etm.checkseries.adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import java.util.List;

import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.daos.DAO_ListSerie;
import br.com.etm.checkseries.deprecated.domains.ListOfUser;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.fragments.ListSeriesFragment;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class TabsListAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<ListOfUser> myLists;
    private List<Serie> mySeries = null;
    private ViewPager mViewPager;

//    public TabsListAdapter(FragmentManager fm, Context context, List<ListOfUser> list, List<Serie> series) {
//        super(fm);
//        this.context = context;
//        myLists = list;
//        mySeries = series;
//    }

    public TabsListAdapter(FragmentManager fm, Context context, List<ListOfUser> list) {
        super(fm);
        this.context = context;
        myLists = list;
    }

    @Override
    public Fragment getItem(final int position) {
        Log.i("LOG-AMBISERIES", "TabsListAdapter - getItem");
        if (mySeries == null) {

            mySeries = new DAO_ListSerie(context).findAll(myLists.get(position));
            for (int i = 0; i < mySeries.size(); i++) {
//                mySeries.get(i).setEpisodeList(new DAO_Episode(context).findAll(mySeries.get(i)));
                mySeries.get(i).setNextEpisode(new DAO_Episode(context).findNextEpisode(String.valueOf(mySeries.get(i).getId())));
//                mySeries.get(i).setTotalEpisodes(new DAO_Episode(context).getCount(String.valueOf(mySeries.get(i).getId())));
//                    mySeries.get(i).setEpisodeList(new DAO_Episode(context).findAll(mySeries.get(i)));
            }
            Log.i("LOG-AMBISERIES", "TotalSeries: " + mySeries.size());

        }
        ListSeriesFragment frag = new ListSeriesFragment(this, mySeries, myLists);
        Bundle b = new Bundle();
        b.putInt("position", position);
        frag.setArguments(b);
        mySeries = null;
        return frag;
    }

    public List<Serie> getMySeries() {
        return mySeries;
    }

    public void setMySeries(List<Serie> series) {
        this.mySeries = series;
    }

    public void setViewPager(ViewPager viewPager) {
        this.mViewPager = viewPager;
    }

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    @Override
    public int getItemPosition(Object object) {
        for (int i = 0; i < myLists.size(); i++) {
            if (myLists.get(i).getName().toUpperCase().equals(object.toString().toUpperCase())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int getCount() {
        return myLists.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return myLists.get(position).getName().toUpperCase();
    }
}
