package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.ListOfUserBaseAdapter;
import br.com.etm.checkseries.adapters.ListSeriesAdapter;
import br.com.etm.checkseries.adapters.TabsListAdapter;
import br.com.etm.checkseries.deprecated.daos.DAO_List;
import br.com.etm.checkseries.deprecated.daos.DAO_ListSerie;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.ListOfUser;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.views.SeriesListActivity;

//import android.support.design.widget.FloatingActionButton;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */

public class ListSeriesFragment extends Fragment {
    public static boolean SEARCH_VIEW = false;


    private boolean hideFB = true;
    public RecyclerView recyclerView;
    private TextView tv_msg;
    private LinearLayoutManager mLayoutManager;
    public int position = -1;
    public Serie serie = null;
    List<Serie> seriesList = null;
    private List<ListOfUser> listOfUsers;
    private TabsListAdapter mTabListAdapter;
    private int positionTab = -1;


    public ListSeriesFragment() {
    }

    @SuppressLint("ValidFragment")
    public ListSeriesFragment(TabsListAdapter tabsListAdapter, List<Serie> series, List<ListOfUser> list) {
        seriesList = series;
        if (seriesList == null)
            seriesList = new ArrayList<>();
        listOfUsers = list;
        mTabListAdapter = tabsListAdapter;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LOG-AMBISERIES", "ListSeriesFragment - onCreate");

        if (getArguments() != null)
            this.positionTab = (int) getArguments().get("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LOG-AMBISERIES", "ListSeriesFragment - onCreateView");

        View v = inflater.inflate(R.layout.fragment_serie, container, false);
        tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        if (seriesList.isEmpty())
            tv_msg.setText(R.string.app_myseries_empty);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_serie);

        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);


        if (EnvironmentConfig.getInstance().isLayoutCompat() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(lm);
        }

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (seriesList == null)
            seriesList = new ArrayList<>();
//        int positionAds = 0;
//        if (!MainActivity.mAds.isEmpty()) {
//            if (seriesList.size() >= 4) {
//                for (int i = 3; i < seriesList.size(); i++) {
//                    if ((i % 3) == 0) {
//                        Serie s = new Serie();
//                        s.setName(MainActivity.mAds.get(positionAds).getTitle());
//                        seriesList.add(i, s);
//                        positionAds++;
//                        if (positionAds == MainActivity.mAds.size())
//                            positionAds = 0;
//                    }
//                }
//            } else {
//                Serie s = new Serie();
//                s.setName(MainActivity.mAds.get(positionAds).getTitle());
//                seriesList.add(s);
//            }
//        }

        ListSeriesAdapter listSerieAdapter = new ListSeriesAdapter(mTabListAdapter, getActivity(), seriesList);
        recyclerView.setAdapter(listSerieAdapter);

        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i("LOG", "onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_list_serie);
        registerForContextMenu(recyclerView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        Log.i("LOG", "onCreateContextMenu()");
        super.onCreateContextMenu(menu, v, menuInfo);
        position = ListSeriesAdapter.POSITION_SERIE_ACTIVE;
        serie = seriesList.get(position);

        HelpFragment.createContextMenu(getActivity(), serie, menu, R.menu.menu_options_lists_serie);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        Log.i("LOG", "onContextItemSelected()");
        listOfUsers = new DAO_List(getContext()).findAll();

        if (SEARCH_VIEW) {// UTILIZANDO O SEARCHVIEW - NA TELA DE LISTAS
            if (seriesList == null)
                seriesList = new ArrayList<>();

            if (serie != null)
                seriesList.add(serie);
            SEARCH_VIEW = false;
        } else {
            position = ListSeriesAdapter.POSITION_SERIE_ACTIVE;
            positionTab = ListSeriesAdapter.TAB_ACTIVE;

            if (positionTab != -1)
                seriesList = new DAO_ListSerie(getContext()).findAll(listOfUsers.get(positionTab));

//            seriesList.add(serie);
            if (!seriesList.isEmpty()) {
                serie = seriesList.get(position);
            }
//                List<ListOfUser_Serie> list = new DAO_ListSerie(getContext()).findAll(serie);
//            } else seriesList.add(serie);
        }
        switch (item.getItemId()) {
            case R.id.it_options_remove_list:
                Log.i("ListSeries", "Serie: " + serie.getName());
                Log.i("ListSeries", "positionTab: " + positionTab);
                Log.i("ListSeries", "lista: " + listOfUsers.get(positionTab).getName());

                new DAO_ListSerie(getContext()).remove(listOfUsers.get(positionTab), serie);
                Toast.makeText(getContext(), serie.getName() + " removida de " + listOfUsers.get(positionTab).getName(), Toast.LENGTH_SHORT).show();
//                Toast.makeText(getContext(), serie.getName() + "foi removida da(s) lista(s).", Toast.LENGTH_SHORT).show();

                SeriesListActivity.updateFragments(mTabListAdapter, mTabListAdapter.getViewPager());
                break;
            case R.id.it_options_manage:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.app_it_manage_list))
                        .setAdapter(new ListOfUserBaseAdapter(listOfUsers, seriesList, getActivity()), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        Log.i("LOG-DIMISS", "onDismiss()");
                        if (SEARCH_VIEW) SEARCH_VIEW = false;
                        else SeriesListActivity.updateFragments(mTabListAdapter, mTabListAdapter.getViewPager());

                    }
                });
                builder.create();
                builder.show();
                break;
            default:
                ListSeriesAdapter.POSITION_SERIE_ACTIVE = -1;
                position = -1;
                serie = null;
        }
        return true;
    }
}

