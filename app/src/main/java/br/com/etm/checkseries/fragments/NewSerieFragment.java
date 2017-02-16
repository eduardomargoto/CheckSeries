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

import com.appodeal.ads.Appodeal;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.NewSerieAdapter;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.views.NewSerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 23/10/2015.
 */
public class NewSerieFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<Serie> serieList;
    private LinearLayoutManager mLayoutManager = null;

    @SuppressLint("ValidFragment")
    public NewSerieFragment(List<Serie> series) {
        super();
        this.serieList = series;
    }

    @SuppressLint("ValidFragment")
    public NewSerieFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_newserie, container, false);
        Log.i("LOG-AMBISERIES", "NewFragmentSerie - onCreateView()");

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_newserie);
        recyclerView.setHasFixedSize(true);


        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (serieList == null)
            serieList = new ArrayList<>();

        NewSerieAdapter serieAdapter = new NewSerieAdapter(getActivity(), serieList);
        recyclerView.setAdapter(serieAdapter);

        return v;
    }

    public void updateView(List<Serie> list) {
        serieList = list;
        NewSerieAdapter serieAdapter = new NewSerieAdapter(getActivity(), list);

        recyclerView.setAdapter(serieAdapter);

    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if(Appodeal.isLoaded(Appodeal.BANNER)){
//            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            lp.setMargins(0, 0, 0, 60);
//            recyclerView.setLayoutParams(lp);
//            Appodeal.hide(getActivity(), Appodeal.BANNER);
//        }
//    }
}