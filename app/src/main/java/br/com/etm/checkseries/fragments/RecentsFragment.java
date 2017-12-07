package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.RecentsAdapter;
import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 28/10/2015.
 */
public class RecentsFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<Serie> mySeries;
    private LinearLayoutManager mLayoutManager;

    @SuppressLint("ValidFragment")
    public RecentsFragment(List<Serie> series) {
        super();
        this.mySeries = series;
    }

    @SuppressLint("ValidFragment")
    public RecentsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LOG-AMBISERIES", "RecentsFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_recents, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_serie);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) { // descendo
//                    if (mySeries.size() > 2) {
//                        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
//                    }
//
//                } else { // subindo
//                    ((AppCompatActivity) getActivity()).getSupportActionBar().show();
//                }
//            }
//        });
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mySeries = new ArrayList<>();
        RecentsAdapter serieAdapter = new RecentsAdapter(getActivity(), mySeries);
        recyclerView.setAdapter(serieAdapter);
        return v;
    }


}