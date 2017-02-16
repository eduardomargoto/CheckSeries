package br.com.etm.checkseries.fragments;


import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.NextEpisodeAdapter;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.views.MainActivity;


/**
 * Created by EDUARDO_MARGOTO on 28/10/2015.
 */
public class NextEpisodeFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Serie> mySeries;
    private LinearLayoutManager mLayoutManager;
    private TextView tv_msg;

    @SuppressLint("ValidFragment")
    public NextEpisodeFragment(List<Serie> series) {
        super();
        this.mySeries = series;
    }

    @SuppressLint("ValidFragment")
    public NextEpisodeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LOG-AMBISERIES", "NextEpisodeFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_next_episodes, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_serie);
        tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

//        if (EnvironmentConfig.getInstance().isLayoutCompat() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            GridLayoutManager lm = new GridLayoutManager(MainActivity.context, 2);
//            recyclerView.setLayoutManager(lm);
//        }
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        List<Episode> episodeList = new DAO_Episode(getActivity()).findAllRelease();
        if (episodeList.isEmpty())
            tv_msg.setText(R.string.app_nextserie_empty);

        NextEpisodeAdapter serieAdapter = new NextEpisodeAdapter(getActivity(), episodeList);
        recyclerView.setAdapter(serieAdapter);
        return v;
    }

}