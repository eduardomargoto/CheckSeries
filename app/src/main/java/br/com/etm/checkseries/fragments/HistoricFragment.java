package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.HistoricAdapter;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.utils.DividerItemDecoration;

/**
 * Created by EDUARDO_MARGOTO on 10/5/2016.
 */

public class HistoricFragment extends Fragment {

    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private List<Episode> episodes;

    @SuppressLint("ValidFragment")
    public HistoricFragment(List<Episode> episodes) {
        this.episodes = episodes;
    }
    @SuppressLint("ValidFragment")
    public HistoricFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = getArguments();
        Log.i("BUNDLE", b.getString("nome"));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_historic, container, false);
        Log.i("LOG-AMBISERIES", "HistoricFragment - onCreateView()");

        Log.i("BUNDLE-2", getArguments().getString("nome"));
        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_historic);
        recyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
        if (episodes == null)
            episodes = new ArrayList<>();

        HistoricAdapter episodeAdapter = new HistoricAdapter(getActivity(), episodes);
        recyclerView.setAdapter(episodeAdapter);

        return v;
    }


    public void updateView(List<Episode> list) {
        episodes = list;
        HistoricAdapter episodeAdapter = new HistoricAdapter(getActivity(), list);

        recyclerView.setAdapter(episodeAdapter);

    }
}
