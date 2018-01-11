package br.com.etm.checkseries.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.SerieAdapter;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.activity.NewSerieActivity;
import br.com.etm.checkseries.di.components.DaggerSerieComponent;
import br.com.etm.checkseries.di.modules.SerieModule;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.views.SerieView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;


/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class SerieFragment extends Fragment implements SerieView {

    private static final String TAG = SerieFragment.class.getSimpleName();
    private static final String PARAM_SERIES = "series_param";

    public static boolean SHOW_SERIES_HIDDENS = false;

    private static int RELOAD_SERIES = 101;

    @BindView(R.id.rv_list_serie)
    RecyclerView recyclerView;

    @BindView(R.id.tv_msg)
    TextView tvMessage;

    @BindView(R.id.btn_novo)
    FloatingActionButton btnNew;

    private Unbinder unbinder;
    private ArrayList<Serie> seriesList = null;
    private SerieAdapter serieAdapter;

    @Inject
    SeriePresenter presenter;

    public static SerieFragment newInstance() {
        return new SerieFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSerieComponent.builder()
                .serieModule(new SerieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie, container, false);
        unbinder = ButterKnife.bind(this, view);

        presenter.onCreate();
        return view;
    }

    @OnClick(R.id.btn_novo)
    public void onClickAddSerie() {
        Intent intent = new Intent(getActivity(), NewSerieActivity.class);
        startActivityForResult(intent, RELOAD_SERIES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == RELOAD_SERIES) {
                presenter.retrieveShows();
            }
        }
    }

    private void configureRecyclerView(List<ApiShow> apiShows) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lm);
        serieAdapter = new SerieAdapter(apiShows);

        serieAdapter.setOnShowListener(new SerieAdapter.OnShowListener() {
            @Override
            public void onFavouriteShow(ApiShow apiShow) {
                presenter.updateShow(apiShow);
            }

            @Override
            public void onNextEpisode(ApiShow apiShow, int position) {
                presenter.nextEpisode(apiShow, position);
            }
        });
        recyclerView.setAdapter(serieAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // inflate menu
        int position = SerieAdapter.POSITION_SERIE_ACTIVE;
        Serie s = this.seriesList.get(position);
        HelpFragment.createContextMenu(getActivity(), s, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void configureView(List<ApiShow> apiShows) {
        if (seriesList != null && !seriesList.isEmpty()) {
            tvMessage.setVisibility(View.VISIBLE);
        }

        configureRecyclerView(apiShows);
    }

    @Override
    public void notifyDataChanged(ApiShow apiShow, int position) {
        if(position != -1) {
            serieAdapter.notifyItemChanged(apiShow, position);
        }
    }

}

