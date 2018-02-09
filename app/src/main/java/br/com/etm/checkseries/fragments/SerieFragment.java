package br.com.etm.checkseries.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import br.com.etm.checkseries.App;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.SerieAdapter;
import br.com.etm.checkseries.api.data.trakTv.ApiAliases;
import br.com.etm.checkseries.api.data.trakTv.ApiShow;
import br.com.etm.checkseries.data.preferences.Preferences;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.activity.NewSerieActivity;
import br.com.etm.checkseries.di.components.DaggerSerieComponent;
import br.com.etm.checkseries.di.modules.SerieModule;
import br.com.etm.checkseries.presenters.SeriePresenter;
import br.com.etm.checkseries.utils.Utils;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.views.SerieView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.app.Activity.RESULT_OK;

public class SerieFragment extends Fragment implements SerieView {

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
    private Toolbar toolbar;

    @Inject
    SeriePresenter presenter;
    @Inject
    Preferences preferences;

    public static SerieFragment newInstance() {
        return new SerieFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerSerieComponent.builder()
                .appComponent(App.getAppComponent())
                .serieModule(new SerieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_serie, container, false);
        unbinder = ButterKnife.bind(this, view);
        toolbar = getActivity().findViewById(R.id.tb_main);
        setHasOptionsMenu(true);
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
        if (resultCode == RESULT_OK && requestCode == RELOAD_SERIES) {
            presenter.retrieveShows();
        }
    }

    private void configureRecyclerView(List<ApiShow> apiShows) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        ApiShow apiShow = (ApiShow) serieAdapter.getMyViewHolder().itemView.getTag();
        Utils.createContextMenu(getActivity(), apiShow, menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_tbtop, menu);
        MenuItem searchItem = menu.findItem(R.id.it_search);
        SearchManager manager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(manager.getSearchableInfo(getActivity().getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String newText) {
                presenter.filter(newText);
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                presenter.filter(newText);
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.it_options:
                inflaterPopupMenu(toolbar, R.menu.menu_options, Gravity.END);
                break;
            case R.id.it_filter:
                PopupMenu popupMenu = inflaterPopupMenu(toolbar, R.menu.menu_filter, Gravity.END);
                popupMenu.getMenu().getItem(0).setChecked(preferences.isFilterUnfinished());
                popupMenu.getMenu().getItem(1).setChecked(preferences.isFilterFavourite());
                popupMenu.getMenu().getItem(2).setChecked(preferences.isFilterHidden());
                break;
        }
        return true;
    }

    private PopupMenu inflaterPopupMenu(View anchor, int idMenu, int gravity) {
        PopupMenu popupMenu = new PopupMenu(getContext(), anchor);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(idMenu, popupMenu.getMenu());
        popupMenu.setGravity(gravity);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
            /* MENU */
                case R.id.it_options_update:
                    //TODO: updated all shows added and visibles (unhidden).
                    // presenter.updateShows();
                    break;
                case R.id.it_options_order:
                    inflaterPopupMenu(toolbar, R.menu.menu_order, Gravity.END);
                    break;

            /*ORDER BY*/
                case R.id.it_order_name:
                case R.id.it_order_nextepisode:
                    presenter.filter();
                    break;

            /*FILTERS*/
                case R.id.it_filter_notfinalized:
                    preferences.setFilterUnfinished(!preferences.isFilterUnfinished());
                    presenter.filter();
                    break;
                case R.id.it_filter_stars:
                    preferences.setFilterFavourite(!preferences.isFilterFavourite());
                    presenter.filter();
                    break;
                case R.id.it_filter_hiddens:
                    preferences.setFilterHidden(!preferences.isFilterHidden());
                    presenter.filter();
                    break;
                case R.id.it_filter_removeall:
                    preferences.clearFilter();
                    presenter.filter();
                    break;

            }
            return false;
        });
        return popupMenu;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ApiShow apiShow = (ApiShow) serieAdapter.getMyViewHolder().itemView.getTag();
        switch (item.getItemId()) {
            case R.id.it_serie_update:
                //TODO: sync with service trakt case logged.
                presenter.syncShowWithService(apiShow);
                break;
            case R.id.it_serie_remove:
                //TODO: remove in database and sync with service trakt case logged
                UtilsEntitys.createAlertDialog(getContext(), "",
                        getString(R.string.app_dialog_serie_remove_confirmation, apiShow.getTitle()),
                        "Remover", (dialogInterface, i) -> {
                            serieAdapter.removeItem(apiShow);
                            presenter.removeShow(apiShow);
                        }, "Cancelar", (dialogInterface, i) -> {
                            dialogInterface.dismiss();
                        }).show();
                break;
            case R.id.it_serie_hidden:
                //TODO: hidden apiShow for not appear in main list
                apiShow.setHidden(!apiShow.isHidden());
                presenter.updateShow(apiShow);
                serieAdapter.removeItem(apiShow);
                break;
            case R.id.it_change_name:
                presenter.retrieveAliases(apiShow);
                // TODO: change title name, verify possibility with service
                break;
            case R.id.it_manage_list:
                // TODO: (2) managed list, (1) make map in database
                break;
        }
        return true;
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
        if (position != -1) {
            serieAdapter.notifyItemChanged(apiShow, position);
        }
    }

    @Override
    public void updateRecyclerView(List<ApiShow> apiShows) {
        serieAdapter.updateList(apiShows);
    }

    @Override
    public void showProgress() {
        UtilsEntitys.showProgress(getActivity());
    }

    @Override
    public void dismissProgress() {
        UtilsEntitys.dismissProgress();
    }

    @Override
    public void configureDialogAliases(String[] aliases, int checkItem, ApiShow apiShow) {
        if(aliases.length != 0) {
            AlertDialog.Builder dialogChangeTitleSerie = new AlertDialog.Builder(getActivity());
            dialogChangeTitleSerie.setTitle(getString(R.string.app_text_titles));
            dialogChangeTitleSerie.setSingleChoiceItems(aliases, checkItem, (dialog, which) -> {
                apiShow.setTitle(aliases[which]);
            }).setOnDismissListener(dialog -> {
                presenter.updateShow(apiShow);
                serieAdapter.notifyItemChanged(apiShow);
            }).create();
            dialogChangeTitleSerie.show();
        } else {
            Toast.makeText(getContext(), "Não há outros títulos para serem exibidos.", Toast.LENGTH_SHORT).show();
        }
    }

}

