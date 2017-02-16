package br.com.etm.checkseries.adapters;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_ListSerie;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.domains.ListOfUser_Serie;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.ListSeriesFragment;
import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class ListOfUserBaseAdapter extends BaseAdapter {


    private List<ListOfUser> listOfUsers;
    private Activity activityContext;
    private List<Serie> serieList;
    private int serieId;

    public ListOfUserBaseAdapter(List<ListOfUser> list, List<Serie> serieList, Activity activity) {
        super();
        this.listOfUsers = list;
        this.activityContext = activity;
        this.serieList = serieList;
    }

    public ListOfUserBaseAdapter(List<ListOfUser> list, int serieId, Activity activity) {
        super();
        this.listOfUsers = list;
        this.activityContext = activity;
        this.serieList = null;
        this.serieId = serieId;
    }


    @Override
    public int getCount() {
        return listOfUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = activityContext.getLayoutInflater().inflate(R.layout.item_listseries, parent, false);
        try {
            ListOfUser listOfUser = listOfUsers.get(position);
            TextView et_name = (TextView) view.findViewById(R.id.tv_list_series);
            final CheckBox cb_addlist = (CheckBox) view.findViewById(R.id.cb_addlist);

            et_name.setText(listOfUser.getName().toUpperCase());
            Serie s = null;
            if (ListSeriesFragment.SEARCH_VIEW) {
                s = this.serieList.get(0);
            } else {
                if (serieList != null) {
                    s = this.serieList.get(ListSeriesAdapter.POSITION_SERIE_ACTIVE); // TAB DE LISTA
                } else {
                    s = new DAO_Serie(view.getContext()).find(String.valueOf(serieId));
                }
            }
            Log.i("log-serie", s.getName());
//            List<ListOfUser_Serie> list = new DAO_ListSerie(view.getContext()).findAll();
            ListOfUser_Serie listSerie = new DAO_ListSerie(view.getContext()).find(listOfUser, s);


            if (listSerie != null) {
                cb_addlist.setChecked(true);
            } else {
                cb_addlist.setChecked(false);
            }


            cb_addlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Serie s = null;
                    if (ListSeriesFragment.SEARCH_VIEW) {
                        s = serieList.get(0);
                    } else {
                        if (serieList != null)
                            s = serieList.get(ListSeriesAdapter.POSITION_SERIE_ACTIVE);
                        else
                            s = new DAO_Serie(v.getContext()).find(String.valueOf(serieId)); //s = MainActivity.mySeries.get(SerieAdapter.POSITION_SERIE_ACTIVE);
                    }
                    ListOfUser listOfUser = listOfUsers.get(position);
                    if (cb_addlist.isChecked()) {
                        ListOfUser_Serie listSerie = new ListOfUser_Serie();
                        listSerie.setSerie(s);
                        listSerie.setListOfUser(listOfUser);
                        new DAO_ListSerie(v.getContext()).create(listSerie);
                        Toast.makeText(v.getContext(), s.getName() + " adicionada em " + listOfUser.getName(), Toast.LENGTH_SHORT).show();
                    } else {
                        new DAO_ListSerie(v.getContext()).remove(listOfUser, s);
                        Toast.makeText(v.getContext(), s.getName() + " removida de " + listOfUser.getName(), Toast.LENGTH_SHORT).show();
                    }
//                    ((MainActivity) activityContext).updateTabs();
                }
            });
        } catch (Exception e) {
            return view;
        }
        return view;
    }
}
