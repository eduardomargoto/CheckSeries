package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
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


import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.ListOfUserBaseAdapter;
import br.com.etm.checkseries.adapters.SerieAdapter;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_List;
import br.com.etm.checkseries.daos.DAO_ListSerie;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.utils.SerieComparator_Name;
import br.com.etm.checkseries.utils.SerieComparator_NextEpisode;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.views.MainActivity;
import br.com.etm.checkseries.views.NewSerieActivity;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class SerieFragment extends Fragment  {
    public static boolean SHOW_SERIES_HIDDENS = false;

    public RecyclerView recyclerView;
    private TextView tv_msg;
    private LinearLayoutManager mLayoutManager;
    private FloatingActionButton bt_novo;

    public int position = -1;
    public Serie serie = null;
    List<Serie> seriesList = null;
    Handler handle = new Handler();



    private static final int HIDE_THRESHOLD = 20;
    private int scrolledDistance = 0;
    private boolean controlsVisible = true;

    @SuppressLint("ValidFragment")
    public SerieFragment() {
    }

    @SuppressLint("ValidFragment")
    public SerieFragment(List<Serie> series) {
        seriesList = new ArrayList<>(series);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LOG-AMBISERIES", "SerieFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_serie, container, false);

//        Appodeal.hide(MainActivity.mActivityMain, Appodeal.BANNER);

        tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        if (MainActivity.mySeries.isEmpty())
            tv_msg.setText(R.string.app_myseries_empty);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_serie);

        bt_novo = (FloatingActionButton) v.findViewById(R.id.btn_novo);

        // FLOATING BUTTON NOVO
        bt_novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(v.getContext(), NewSerieActivity.class);
                startActivityForResult(it, MainActivity.NEWSERIEACTIVITY_CODE);
            }
        });
        recyclerView.setHasFixedSize(true);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //show views if first item is first visible position and views are hidden

                if (dy > 0) { // descendo
                    bt_novo.hide(true);
                } else { // subindo
                    bt_novo.show(true);
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        if(EnvironmentConfig.getInstance().isLayoutCompat() && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            GridLayoutManager lm = new GridLayoutManager(getContext(), 2);
            recyclerView.setLayoutManager(lm);
        }


        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (seriesList == null)
            seriesList = new ArrayList<>(getListSeriesShow());

        int positionAds = 0;
        if (!MainActivity.mAds.isEmpty()) {
            if (seriesList.size() >= 4) {
                for (int i = 3; i < seriesList.size(); i++) {
                    if ((i % 3) == 0) {
                        Serie s = new Serie();
                        s.setName(MainActivity.mAds.get(positionAds).getTitle());
                        seriesList.add(i, s);
                        positionAds++;
                        if (positionAds == MainActivity.mAds.size())
                            positionAds = 0;
                    }
                }
            } else {
                Serie s = new Serie();
                s.setName(MainActivity.mAds.get(positionAds).getTitle());
                seriesList.add(s);
            }
        }


        SerieAdapter serieAdapter = new SerieAdapter(getActivity(), seriesList);
        recyclerView.setAdapter(serieAdapter);

        return v;
    }


    public ArrayList<Serie> getListSeriesShow() {
        EnvironmentConfig ec = EnvironmentConfig.getInstance();
        ArrayList<Serie> serieList = new ArrayList<>(MainActivity.mySeries);
        for (int i = serieList.size() - 1; i >= 0; i--) {
            boolean removed = false;
            if (ec.isFilter_hidden()) {
                if (!serieList.get(i).isHidden()) {
                    serieList.remove(i);
                    removed = true;
                }
            } else {
                if (serieList.get(i).isHidden()) {
                    serieList.remove(i);
                    removed = true;
                }
            }

            if (!removed) {
                if (ec.isFilter_notfinalized()) {
                    if (serieList.get(i).getNextEpisode() == null && serieList.get(i).getStatus().equals("Ended")) {
                        serieList.remove(i);
                        removed = true;
                    }
                }
            }
            if (!removed) {
                if (ec.isFilter_favorite()) {
                    if (!serieList.get(i).isFavorite()) {
                        serieList.remove(i);
                        removed = true;
                    }
                }
            }
//            if (!removed) {
//                if (ec.isFilter_comingsoon()) {
//                    if (!(serieList.get(i).getNextEpisode() != null && serieList.get(i).getNextEpisode().getFirstAired().after(Calendar.getInstance().getTime()))) {
//                        serieList.remove(i);
//                        removed = true;
//                    }
//                }
//            }
        }
        return serieList;
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
        // inflate menu

        int position = SerieAdapter.POSITION_SERIE_ACTIVE;

        Serie s = this.seriesList.get(position);
        HelpFragment.createContextMenu(getActivity(), s, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        position = SerieAdapter.POSITION_SERIE_ACTIVE;
        serie = seriesList.get(position);

        switch (item.getItemId()) {
            case R.id.it_serie_update:
                new Thread() {
                    @Override
                    public void run() {
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.getPb_updates().setVisibility(View.VISIBLE);
//                                MainActivity.pb_updates.setVisibility(View.VISIBLE);
                                Toast.makeText(recyclerView.getContext(), serie.getName() + " está sendo atualizada.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        try {
                            serie = HelpFragment.updateSerie(serie, getContext());
                        } catch (Exception e) {
                            Log.i("LOG-EXCEPTION", e.toString());
                        }
                        handle.post(new Runnable() {
                            @Override
                            public void run() {
                                SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();
                                serieAdapter.updateListItem(position, serie);
                                MainActivity mainActivity = (MainActivity) getActivity();
                                mainActivity.getPb_updates().setVisibility(View.INVISIBLE);
//                                MainActivity.pb_updates.setVisibility(View.INVISIBLE);
                                HelpFragment.updateSerieMainActivity(serie);
                                Toast.makeText(recyclerView.getContext(), serie.getName() + " foi atualizada.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }.start();

                break;
            case R.id.it_serie_remove:
                try {
                    UtilsEntitys.createAlertDialog(this.getContext(),
                            getResources().getString(R.string.app_dl_title_remove_serie), getResources().getString(R.string.app_text_wantremove) + " " + serie.getName() + "?",
                            getResources().getString(R.string.app_dialog_removebutton), new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, int whichButton) {
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            handle.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(recyclerView.getContext(), getResources().getString(R.string.app_text_removing) + " " + serie.getName(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                            new DAO_ListSerie(recyclerView.getContext()).remove(serie);
                                            new DAO_Serie(recyclerView.getContext()).remove(serie);
                                            for (Episode ep : serie.getEpisodeList()) {
                                                new DAO_Episode(recyclerView.getContext()).remove(ep);
                                            }

                                            handle.post(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(recyclerView.getContext(), serie.getName() + " " + getResources().getString(R.string.app_text_removed), Toast.LENGTH_SHORT).show();
                                                    SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();

                                                    seriesList.remove(position);
                                                    MainActivity.mySeries.remove(position);
                                                    serieAdapter.removeListItem(position);

                                                    serieAdapter.notifyDataSetChanged();
                                                    UtilsEntitys.updateWidgets(recyclerView.getContext().getApplicationContext());
                                                    dialog.dismiss();
                                                }
                                            });


                                        }
                                    }.start();

                                }
                            },
                            getResources().getString(R.string.app_dialog_cancel)).create().show();
                } catch (Exception e) {
                    Log.i("LOG-EXCEPTION", e.toString());
                }
                break;
            case R.id.it_serie_hidden:
                try {
                    String title = "", message = "", positiveBtn = "";
                    if (serie.isHidden()) {
                        positiveBtn = getResources().getString(R.string.app_show_positivebtn);
                        title = getResources().getString(R.string.app_text_show_serie);
                        message = getResources().getString(R.string.app_text_wantshow_serie);
                    } else {
                        positiveBtn = getResources().getString(R.string.app_hidden_positivebtn);
                        title = getResources().getString(R.string.app_text_hidden_serie);
                        message = getResources().getString(R.string.app_text_wanthidden_serie);
                        ;
                    }
                    UtilsEntitys.createAlertDialog(this.getContext(),
                            title, message + " " + serie.getName() + "?",
                            positiveBtn, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();

                                    if (serie.isHidden()) {
                                        serie.setHidden(false);
                                        Toast.makeText(recyclerView.getContext(), serie.getName() + " " + getResources().getString(R.string.app_text_showagain), Toast.LENGTH_SHORT).show();
                                        SerieFragment.SHOW_SERIES_HIDDENS = true;
                                        if (EnvironmentConfig.getInstance().isFilter_hidden()) {
                                            serieAdapter.removeListItem(position);
//                                            MainActivity.mySeries.remove(position);
                                            seriesList.remove(position);
                                        }


                                    } else {
                                        serie.setHidden(true);
                                        serieAdapter.removeListItem(position);
//                                        MainActivity.mySeries.remove(position);
                                        seriesList.remove(position);
                                        Toast.makeText(recyclerView.getContext(), serie.getName() + " " + getResources().getString(R.string.app_text_hidden), Toast.LENGTH_SHORT).show();
                                    }
                                    new DAO_Serie(recyclerView.getContext()).edit(serie);
                                    serieAdapter.notifyDataSetChanged();
                                    dialog.dismiss();
                                }
                            },
                            getResources().getString(R.string.app_dialog_cancel)).create().show();
                } catch (Exception e) {
                    Log.i("LOG-EXCEPTION", e.toString());
                }
                break;
            case R.id.it_serie_watched:
                Episode episode = serie.getNextEpisode();
                if (episode != null) {
                    if (episode.getFirstAired() != null && Calendar.getInstance().getTime().after(episode.getFirstAired())) {
                        episode.setWatched(true);
                        new DAO_Episode(getContext()).updateWatchedSkipped(episode);
                        SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();
                        serieAdapter.updateListItem(position, serie);
                        HelpFragment.updateEpisodeMainActivity(episode, serie);
                    } else {
                        Toast.makeText(recyclerView.getContext(), getResources().getString(R.string.app_msg_episode_withoutdate), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(recyclerView.getContext(), getResources().getString(R.string.app_msg_episode_haventmore), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.it_change_name:
                String names = serie.getName() + "|" + serie.getAlias_names();
                String[] aliasNames = names.split("\\|");

                AlertDialog.Builder dialogChangeTitleSerie = new AlertDialog.Builder((getActivity()));
                dialogChangeTitleSerie.setTitle(getResources().getString(R.string.app_text_titles));
                dialogChangeTitleSerie.setSingleChoiceItems(aliasNames, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String an = serie.getName() + "|" + serie.getAlias_names();
                        String[] aliasNames = an.split("\\|");
                        String newAliasNames = "";
                        String name = aliasNames[which];
                        for (int i = 0; i < aliasNames.length; i++) {
                            if (aliasNames[i].equals(name)) {
                                if (getActivity() != null)
                                    serie.setName(name);
                            } else {
                                if ((i + 1) == aliasNames.length)
                                    newAliasNames = newAliasNames + aliasNames[i];
                                else newAliasNames = newAliasNames + aliasNames[i] + "|";
                            }
                        }
                        if (getActivity() != null) {
                            serie.setAlias_names(newAliasNames);
                            new DAO_Serie(getContext()).edit(serie);
                        }

                    }
                }).setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();
                        serieAdapter.updateListItem(position);
                    }
                })
                        .create();
                dialogChangeTitleSerie.show();

                break;
            case R.id.it_manage_list:
                List<ListOfUser> listOfUsers = new DAO_List(getContext()).findAll();

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getResources().getString(R.string.app_it_manage_list))
                        .setAdapter(new ListOfUserBaseAdapter(listOfUsers, serie.getId(), getActivity()), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                break;
            default:
                if (EnvironmentConfig.getInstance().isOrder_name()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                }
                SerieAdapter.POSITION_SERIE_ACTIVE = -1;
                position = -1;
                serie = null;
        }
        return true;
    }

}

