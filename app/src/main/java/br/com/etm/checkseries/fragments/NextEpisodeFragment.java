package br.com.etm.checkseries.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.NextEpisodeAdapter;
import br.com.etm.checkseries.api.data.tracktv.ApiEpisode;
import br.com.etm.checkseries.di.components.DaggerNextEpisodeComponent;
import br.com.etm.checkseries.di.components.DaggerSerieComponent;
import br.com.etm.checkseries.di.modules.NextEpisodeModule;
import br.com.etm.checkseries.di.modules.SerieModule;
import br.com.etm.checkseries.presenters.NextEpisodePresenter;
import br.com.etm.checkseries.presenters.impl.NextEpisodePresenterImpl;
import br.com.etm.checkseries.views.NextEpisodeView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NextEpisodeFragment extends Fragment implements NextEpisodeView {

    @BindView(R.id.rv_list_serie)
    RecyclerView recyclerView;
    @BindView(R.id.tv_msg)
    TextView tvMessageEmpty;

    @Inject
    NextEpisodePresenter presenter;

    private Unbinder unbinder;

    public static NextEpisodeFragment newInstance() {
        return new NextEpisodeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerNextEpisodeComponent.builder()
                .appComponent(App.getAppComponent())
                .nextEpisodeModule(new NextEpisodeModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_next_episodes, container, false);
        ButterKnife.bind(this, view);

        presenter.onCreate();
        return view;
    }

    @Override
    public void configureView(List<ApiEpisode> episodeList) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (episodeList != null && episodeList.isEmpty())
            tvMessageEmpty.setVisibility(View.VISIBLE);

        NextEpisodeAdapter serieAdapter = new NextEpisodeAdapter(getContext(), episodeList);
        serieAdapter.setOnItemClickListener((adapterView, view, i, l) -> {
//                Intent it = new Intent(context, SerieActivity.class);
//                it.putExtra("serie", serieList.get(getAdapterPosition()));
//                context.startActivity(it);
        });

        serieAdapter.setOnCheckItemClickListener((adapterView, view, i, l) -> {
//                Serie serie = serieList.get(getAdapterPosition());
//                Episode episode = serie.getNextEpisode();
//                episode.setWatched(true);
//                new DAO_Episode(v.getContext()).edit(episode);
//                notifyItemChanged(getAdapterPosition());
        });
        recyclerView.setAdapter(serieAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}