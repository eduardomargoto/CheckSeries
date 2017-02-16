package br.com.etm.checkseries.fragments;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
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
import br.com.etm.checkseries.adapters.SerieAdapter;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 30/10/2015.
 */
public class SearchSerieFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tv_msg;
    private LinearLayoutManager mLayoutManager;

    public ArrayList<Serie> series;

    @SuppressLint("ValidFragment")
    public SearchSerieFragment() {
    }

    @SuppressLint("ValidFragment")
    public SearchSerieFragment(List<Serie> series) {
        this.series = new ArrayList<>(series);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("LOG-AMBISERIES", "SearchSerieFragment - onCreateView");
        View v = inflater.inflate(R.layout.fragment_serie, container, false);



        tv_msg = (TextView) v.findViewById(R.id.tv_msg);
        if (MainActivity.mySeries.isEmpty())
            tv_msg.setText(R.string.app_myseries_empty);

        recyclerView = (RecyclerView) v.findViewById(R.id.rv_list_serie);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (series == null)
            series = new ArrayList<>(MainActivity.mySeries);
        SerieAdapter serieAdapter = new SerieAdapter(getActivity(), series);
        recyclerView.setAdapter(serieAdapter);
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
        // inflate menu
        int position = SerieAdapter.POSITION_SERIE_ACTIVE;
        Serie s = series.get(position);
        HelpFragment.createContextMenu(getActivity(), s, menu);
        s = null;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        final int position = SerieAdapter.POSITION_SERIE_ACTIVE;
        final Serie s = series.get(position);

        switch (item.getItemId()) {
            case R.id.it_serie_remove:
                try {
                    UtilsEntitys.createAlertDialog(recyclerView.getContext(),
                            "Remover Serie", "Deseja remover " + s.getName() + "?",
                            "REMOVER", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Log.i("LOG-POSITIONS", "position -> " + position);

                                    new DAO_Serie(recyclerView.getContext()).remove(s);
                                    Toast.makeText(recyclerView.getContext(), s.getName() + " removido.", Toast.LENGTH_SHORT).show();
                                    SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();
                                    serieAdapter.removeListItem(position);
                                    dialog.dismiss();
                                }
                            },
                            "CANCELAR").create().show();
                } catch (Exception e) {
                    Log.i("LOG-EXCEPTION", e.toString());
                }
                break;
            case R.id.it_serie_update:
                Toast.makeText(recyclerView.getContext(), "Atualiza -> " + s.getName(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.it_serie_hidden:
                try {
                    String title = "", message = "", positiveBtn = "";
                    if (s.isHidden()) {
                        positiveBtn = "EXIBIR";
                        title = "Exibir Serie";
                        message = "Deseja exibir";
                    } else {
                        positiveBtn = "OCULTAR";
                        title = "Ocultar Serie";
                        message = "Deseja ocultar";
                    }
                    UtilsEntitys.createAlertDialog(recyclerView.getContext(),
                            title, message + " " + s.getName() + "?",
                            positiveBtn, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    SerieAdapter serieAdapter = (SerieAdapter) recyclerView.getAdapter();
                                    if (s.isHidden()) {
                                        s.setHidden(false);
                                        Toast.makeText(recyclerView.getContext(), s.getName() + " visível novamente.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        s.setHidden(true);
                                        Toast.makeText(recyclerView.getContext(), s.getName() + " ocultado.", Toast.LENGTH_SHORT).show();
                                    }
                                    new DAO_Serie(recyclerView.getContext()).edit(s);
                                    serieAdapter.updateListItem(position);
                                    dialog.dismiss();
                                }
                            },
                            "CANCELAR").create().show();
                } catch (Exception e) {
                    Log.i("LOG-EXCEPTION", e.toString());
                }
                break;
            case R.id.it_serie_watched:
                Toast.makeText(recyclerView.getContext(), "Assistido", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }

}
