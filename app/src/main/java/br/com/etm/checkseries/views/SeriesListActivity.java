package br.com.etm.checkseries.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsListAdapter;
import br.com.etm.checkseries.daos.DAO_List;
import br.com.etm.checkseries.daos.DAO_ListSerie;
import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.ListSeriesFragment;
import br.com.etm.checkseries.utils.SlidingTabLayout;
import br.com.etm.checkseries.utils.UtilsEntitys;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class SeriesListActivity extends AppCompatActivity {
    private Toolbar tb_top;
    //
    List<ListOfUser> listOfUserList = new ArrayList<>();
    public TabsListAdapter mTabsAdapter;
    public ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    public Context context;
    public Picasso picasso;

    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_list);
        context = this;
        picasso = Picasso.with(this);
        listOfUserList = new DAO_List(this).findAll();

        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tb_top.setTitle(R.string.app_act_serieslist);
        tb_top.setSubtitle("");
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // NAVIGATION
//        this.getResult().setToolbar(this, tb_top);
//        this.getResult().setOnDrawerItemClickListener(navigation_click_item);


//        if (listOfUserList.isEmpty()) { // CRIANDO A PRIMEIRA TAB DEFAULT
//            createDefaulTitle();
//        }
        mTabsAdapter = new TabsListAdapter(getSupportFragmentManager(), this, listOfUserList);

        //TABS
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sld_tabs);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
        mSlidingTabLayout.setViewPager(mViewPager);
        mTabsAdapter.setViewPager(mViewPager);
//        clearTabs();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tbtop_serieslist, menu);
        MenuItem searchItem = menu.findItem(R.id.it_search);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(final String newText) {
                Log.i("LOG-AMBISERIES", "SeriesListActivity - onQueryTextSubmit(" + newText + ")");
                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Serie> aux = null;
                        if (newText.length() > 0) {
                            ListSeriesFragment.SEARCH_VIEW = true;
                            aux = new ArrayList<Serie>(MainActivity.mySeries);
                            for (int i = aux.size() - 1; i >= 0; i--) {
                                if (aux.get(i).getName().toUpperCase().indexOf(newText.toUpperCase()) == -1) {
                                    aux.remove(i);
                                }
                            }
                            mTabsAdapter.setMySeries(aux);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateFragments(mTabsAdapter, mViewPager);
                                }
                            });
                        } else {
                            ListSeriesFragment.SEARCH_VIEW = false;
                            mTabsAdapter.setMySeries(null);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateFragments(mTabsAdapter, mViewPager);
                                }
                            });
                        }
                    }
                }.start();


                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.i("LOG-AMBISERIES", "SeriesListActivity - onQueryTextChange(" + newText + ")");
                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Serie> aux = null;
                        if (newText.length() > 0) {
                            ListSeriesFragment.SEARCH_VIEW = true;
                            aux = new ArrayList<Serie>(MainActivity.mySeries);
                            for (int i = aux.size() - 1; i >= 0; i--) {
                                if (aux.get(i).getName().toUpperCase().indexOf(newText.toUpperCase()) == -1) {
                                    aux.remove(i);
                                }
                            }
                            mTabsAdapter.setMySeries(aux);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateFragmentsSearch(mTabsAdapter, mViewPager);
                                }
                            });
                        } else {
                            ListSeriesFragment.SEARCH_VIEW = false;
                            mTabsAdapter.setMySeries(null);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateFragmentsSearch(mTabsAdapter, mViewPager);
                                }
                            });
                        }
                    }
                }.start();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        final View view_addlist = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_addlista, null);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SeriesListActivity.this);

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.it_addserielist:
                builder.setView(view_addlist)
                        .setTitle(getResources().getString(R.string.app_title_dialog_addlist))
                        .setPositiveButton(getResources().getString(R.string.app_dialog_addbutton), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                EditText new_list = (EditText) view_addlist.findViewById(R.id.et_name_list);
                                if (!new_list.getText().toString().equals("")) {
                                    if (listOfUserList == null)
                                        listOfUserList = new ArrayList<ListOfUser>();

                                    ListOfUser listOfUser = new ListOfUser(new_list.getText().toString());
                                    listOfUser.setWeight(listOfUserList.get(listOfUserList.size() - 1).getWeight() + 1);
                                    new DAO_List(context).create(listOfUser);
                                    listOfUserList.add(listOfUser);

                                    updateTabs();
                                }
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.app_dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        }).create().show();

                break;
            case R.id.it_options:
                PopupMenu popup = UtilsEntitys.inflaterPopupMenu(SeriesListActivity.this, tb_top, R.menu.menu_options_lists, Gravity.RIGHT);
                popup.setOnMenuItemClickListener(onMenuItemClick_Popup);
                break;
        }
        return true;
    }

    //
    public void createDefaulTitle() {
        listOfUserList = new ArrayList<>();
        ListOfUser listOfUser = new ListOfUser();
        String titleDefault = getResources().getString(R.string.app_serieslist_list_default);
        listOfUser.setName(titleDefault);
        listOfUser.setWeight(0);
        new DAO_List(this).create(listOfUser);
        listOfUserList.add(listOfUser);
    }

    PopupMenu.OnMenuItemClickListener onMenuItemClick_Popup = new PopupMenu.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final View view_addlist = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_addlista, null);
            final AlertDialog.Builder builder = new AlertDialog.Builder(SeriesListActivity.this);

            switch (item.getItemId()) {
                case R.id.it_options_manage:
                    final EditText new_list = (EditText) view_addlist.findViewById(R.id.et_name_list);
                    final int position = mViewPager.getCurrentItem();
                    final ListOfUser listOfUser = listOfUserList.get(position);
                    new_list.setText(listOfUser.getName());

                    builder.setView(view_addlist)
                            .setTitle(getResources().getString(R.string.app_title_dialog_addlist))
                            .setPositiveButton(getResources().getString(R.string.app_dialog_finalize), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    if (!new_list.getText().toString().equals("")) {
                                        if (listOfUserList == null)
                                            listOfUserList = new ArrayList<ListOfUser>();

                                        listOfUser.setName(new_list.getText().toString());
                                        new DAO_List(context).edit(listOfUser);
                                        updateTabs();
                                        mViewPager.setCurrentItem(listOfUserList.size() - 1);
                                    }
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(getResources().getString(R.string.app_dialog_removebutton), new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ListOfUser listOfUser = listOfUserList.get(mViewPager.getCurrentItem());
                                    new DAO_List(context).remove(listOfUser);
                                    new DAO_ListSerie(context).remove(listOfUser);
                                    listOfUserList.remove(listOfUser);
                                    if (listOfUserList.size() == 0) {
                                        createDefaulTitle();
                                    }
                                    updateTabs();
                                    dialog.dismiss();
                                }
                            }).create().show();
                    break;


            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        if (mTabsAdapter != null && mViewPager != null) {
            mTabsAdapter.notifyDataSetChanged();
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setAdapter(mTabsAdapter);
            mViewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateFragments(mTabsAdapter, mViewPager);
    }

    public static void updateFragments(TabsListAdapter mTabsAdapter, ViewPager mViewPager) {
        mTabsAdapter.notifyDataSetChanged();
        int currentItem = mViewPager.getCurrentItem();
        mViewPager.setAdapter(mTabsAdapter);
        mViewPager.setCurrentItem(currentItem);
    }

    public static void updateFragmentsSearch(TabsListAdapter mTabsAdapter, ViewPager mViewPager) {
        mTabsAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mTabsAdapter);
    }

//    public void clearTabs(List<Serie> series) {
//        List<ListOfUser> list = new ArrayList<>();
//        list.add(new ListOfUser(getResources().getString(R.string.app_serieslist_list_search)));
//        mTabsAdapter = new TabsListAdapter(getSupportFragmentManager(), this, list, series);
//        mViewPager.setAdapter(mTabsAdapter);
//        mSlidingTabLayout.setViewPager(mViewPager);
//    }

    public void updateTabs() {
        mTabsAdapter = new TabsListAdapter(getSupportFragmentManager(), this, listOfUserList);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout.setViewPager(mViewPager);
        mTabsAdapter.setViewPager(mViewPager);
    }

}


