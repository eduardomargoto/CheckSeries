package br.com.etm.checkseries.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.Collections;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsAdapter;
import br.com.etm.checkseries.deprecated.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.utils.SerieComparator_Name;
import br.com.etm.checkseries.utils.SerieComparator_NextEpisode;
import br.com.etm.checkseries.utils.UtilsEntitys;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.pbContainer)
    ProgressBar pbContainer;

    @BindView(R.id.tb_main)
    Toolbar mToolbar;

    @BindView(R.id.pb_updates)
    ProgressBar pb_updates;

    @BindView(R.id.vp_tabs)
    ViewPager mViewPager;

    @BindView(R.id.sld_tabs)
    TabLayout mTabLayout;

    private TabsAdapter mTabsAdapter;
    private ArrayList<Serie> mySeries = new ArrayList<>();
    private Unbinder unbinder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        UtilsEntitys.setOrientationConfigDevice(this);

        ViewCompat.setElevation(mToolbar, 4);
        ViewCompat.setElevation(mTabLayout, 4);
        setSupportActionBar(mToolbar);

        pbContainer.setVisibility(View.VISIBLE);
        pb_updates.setVisibility(View.GONE);

        configureTabs();
    }

    private void configureTabs() {
        mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        pbContainer.setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tbtop, menu);
        MenuItem searchItem = menu.findItem(R.id.it_search);
        SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String newText) {
                Log.i(TAG, "onQueryTextSubmit(" + newText + ")");
                Observable.just(mySeries)
                        .flatMapIterable(serie -> serie)
                        .filter(serie -> serie.getName().toLowerCase().contains(newText.toLowerCase()))
                        .toList()
                        .subscribe(series -> {
                            updateFragments(mTabsAdapter, mViewPager);
                        }, throwable -> {
                            Log.e(TAG, "onQueryTextSubmit", throwable);
                        });
                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.i(TAG, "onQueryTextChange(" + newText + ")");
                Observable.just(mySeries)
                        .flatMapIterable(serie -> serie)
                        .filter(serie -> serie.getName().toLowerCase().contains(newText.toLowerCase()))
                        .toList()
                        .subscribe(series -> {
                            updateFragments(mTabsAdapter, mViewPager);
                        }, throwable -> {
                            Log.e(TAG, "onQueryTextChange", throwable);
                        });

                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.it_options:
                inflaterPopupMenu(mToolbar, R.menu.menu_options, Gravity.RIGHT);
                break;
            case R.id.it_filter:
                EnvironmentConfig ec = EnvironmentConfig.getInstance();
                PopupMenu popupMenu = inflaterPopupMenu(mToolbar, R.menu.menu_filter, Gravity.RIGHT);
                popupMenu.getMenu().getItem(0).setChecked(ec.isFilter_notfinalized()); // unattended
                popupMenu.getMenu().getItem(1).setChecked(ec.isFilter_favorite()); // favorite
                popupMenu.getMenu().getItem(2).setChecked(ec.isFilter_hidden()); // hidden
                popupMenu.getMenu().getItem(3).setChecked(ec.isFilter_comingsoon()); // coming soon
                break;
        }
        return true;
    }

    public PopupMenu inflaterPopupMenu(View anchor, int idMenu, int gravity) {
        PopupMenu popup = new PopupMenu(this, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(idMenu, popup.getMenu());
        popup.setGravity(gravity);
        popup.show();
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
            /*MENUS*/
                case R.id.it_options_update:
                    /*new Thread() {
                        @Override
                        public void run() {
                            handler.post(() -> {
                                pb_updates.setVisibility(View.VISIBLE);
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.app_text_updating_series), Toast.LENGTH_SHORT).show();
                            });
                            try {
                                // busca do BD local
                                mySeries = HelpFragment.updateSeries(mySeries, context);
                            } catch (Exception e) {
                                Log.i("LOG-EXCEPTION", e.toString());
                            }
                            handler.post(() -> {
                                updateFragments(mTabsAdapter, mViewPager);
                                pb_updates.setVisibility(View.INVISIBLE);
                                Toast.makeText(MainActivity.this, getResources().getString(R.string.app_text_updating_series_finish), Toast.LENGTH_SHORT).show();
                            });
                        }
                    }.start();*/
                    break;
                case R.id.it_options_order:
                    inflaterPopupMenu(mToolbar, R.menu.menu_order, Gravity.RIGHT);
                    break;

            /*ORDER BY*/
                case R.id.it_order_name:
                    EnvironmentConfig.getInstance().setOrder_nextEpisode(false);
                    EnvironmentConfig.getInstance().setOrder_name(true);
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    Collections.sort(mySeries, new SerieComparator_Name());
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
                case R.id.it_order_nextepisode:
                    EnvironmentConfig.getInstance().setOrder_name(false);
                    EnvironmentConfig.getInstance().setOrder_nextEpisode(true);
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    Collections.sort(mySeries, new SerieComparator_NextEpisode());
                    updateFragments(mTabsAdapter, mViewPager);
                    break;

            /*FILTROS*/
                case R.id.it_filter_notfinalized:
                    EnvironmentConfig.getInstance().alterFilterNotFinalized();
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
                case R.id.it_filter_stars:
                    EnvironmentConfig.getInstance().alterFilterFavorite();
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
                case R.id.it_filter_hiddens:
                    EnvironmentConfig.getInstance().alterFilterHidden();
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
                case R.id.it_filter_removeall:
                    EnvironmentConfig.getInstance().removeAllFilters();
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
            }
            return false;
        });

        return popup;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult() - requestCode: " + requestCode + " - resultCode(" + resultCode + ")");
        if (resultCode == Activity.RESULT_OK) {
            updateFragments(mTabsAdapter, mViewPager);
        }
    }

    public static void updateFragments(TabsAdapter mTabsAdapter, ViewPager mViewPager) {
        if (mTabsAdapter != null && mViewPager != null) {
            mTabsAdapter.notifyDataSetChanged();
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setAdapter(mTabsAdapter);
            mViewPager.setCurrentItem(currentItem);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i(TAG, "onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        updateFragments(mTabsAdapter, mViewPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
