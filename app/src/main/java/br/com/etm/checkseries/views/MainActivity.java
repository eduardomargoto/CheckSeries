package br.com.etm.checkseries.views;

import android.accounts.Account;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.icu.text.LocaleDisplayNames;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
//import android.support.multidex.MultiDex;
import android.os.Looper;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NativeAd;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsAdapter;
import br.com.etm.checkseries.adapters.TabsListAdapter;
import br.com.etm.checkseries.daos.BDCore;
import br.com.etm.checkseries.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_Language;
import br.com.etm.checkseries.daos.DAO_List;
import br.com.etm.checkseries.daos.DAO_ListSerie;
import br.com.etm.checkseries.daos.DAO_Profile;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.daos.FirebaseCore;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Language;
import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.domains.Profile;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.HelpFragment;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.AppodealNativeCallBacks;
import br.com.etm.checkseries.utils.MyBackupAgent;
import br.com.etm.checkseries.utils.NotificationPublisher;
import br.com.etm.checkseries.utils.SerieComparator_Name;
import br.com.etm.checkseries.utils.SerieComparator_NextEpisode;
import br.com.etm.checkseries.utils.SlidingTabLayout;
import br.com.etm.checkseries.utils.UtilsEntitys;
import br.com.etm.checkseries.utils.UtilsImages;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 1972;
    public static int NEWSERIEACTIVITY_CODE = 1;
    public static boolean UPDATE_ONRESTART = true;
    public static String PACKAGE_NAME = "br.com.etm.checkseries";
    public static String DEFAULTPATHBACKUP = "/bkpCheckSeries/";

    private String CLIENTE_ID = "989127589714-9ctar51t4a3g68a4rhlibngoaj4plbtn.apps.googleusercontent.com";

    public static final String APPODEAL_KEY = "4729944a328755783c30d94625e292c166060b39a4d305e8";

    public static final String PREFS_NAME = "User";
    public static boolean SEARCHVIEW = false;
    public static List<NativeAd> mAds = new LinkedList<>();

    //    public static Person profile = null;
    public static ArrayList<Serie> mySeries = new ArrayList<>();
    public static final int SIGN_IN_CODE = 56465;
    public static GoogleApiClient googleApiClient;
    public static GoogleSignInOptions googleSignInOptions;
    public static GoogleSignInAccount googleSignInAccount;
    public static ConnectionResult connectionResult;

    private ProgressBar pbContainer;
    public Activity mActivityMain;
    public Toolbar tb_top;
    public TabsAdapter mTabsAdapter;
    public ViewPager mViewPager;
    public SlidingTabLayout mSlidingTabLayout;
    public Drawer nv_main = null;

    private Handler handler = new Handler();

    public ProgressBar pb_updates;
    public Context context;

//    List<ListOfUser> listOfUserList = new ArrayList<>();

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(getApplication());
        setContentView(R.layout.activity_main);
        UtilsEntitys.setOrientationConfigDevice(this);


        mActivityMain = this;
        pbContainer = (ProgressBar) findViewById(R.id.pbContainer);
        pbContainer.setVisibility(View.VISIBLE);
        pb_updates = (ProgressBar) findViewById(R.id.pb_updates);

        context = this;

        pb_updates.setVisibility(View.INVISIBLE);


        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(CLIENTE_ID)
                .requestEmail()
                .requestProfile()
                .requestScopes(new Scope(Scopes.PROFILE))
                .requestScopes(new Scope(Scopes.PLUS_ME))
                .requestScopes(new Scope(Scopes.PLUS_LOGIN))
                .build();
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(MainActivity.this, MainActivity.this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .addApi(Plus.API)
                .build();


        new Thread() {
            @Override
            public void run() {
                try {
                    initAPPODEAL();

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            listOfUserList = new DAO_List(MainActivity.this).findAll();
//                            mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this, listOfUserList);
                            mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this);

                            //TABS
                            mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
                            mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sld_tabs);
                            mViewPager.setAdapter(mTabsAdapter);
                            mSlidingTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                }

                                @Override
                                public void onPageSelected(final int position) {
                                    if (position == 0) { // SERIES
                                        final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
                                        lpt.setMargins(lpt.leftMargin, lpt.topMargin, lpt.rightMargin, (lpt.bottomMargin - UtilsImages.getMarginDensity(context, 60)));
                                        mViewPager.setLayoutParams(lpt);
                                        Appodeal.hide(mActivityMain, Appodeal.BANNER);
                                    }
                                    if (position == 1) { // NEXT
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                while (!Appodeal.isLoaded(Appodeal.BANNER)) {}
                                                final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams) mViewPager.getLayoutParams();
                                                lpt.setMargins(lpt.leftMargin, lpt.topMargin, lpt.rightMargin, UtilsImages.getMarginDensity(context, 60));
                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mViewPager.setLayoutParams(lpt);
                                                        if (position == 1)
                                                            Appodeal.show(MainActivity.this, Appodeal.BANNER);
                                                    }
                                                });

                                            }
                                        }.start();
                                    }
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {}
                            });
                            mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                            mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.accent));
                            mSlidingTabLayout.setDistributeEvenly(true);
                            mSlidingTabLayout.setViewPager(mViewPager);
                            mSlidingTabLayout.setActivity(MainActivity.this);


                            if (getIntent().getExtras() != null) {
                                Object obj = getIntent().getExtras().get(NotificationPublisher.KEY_NOTIFICATION);
                                if (obj != null) {
                                    if (obj.toString().equals(NotificationPublisher.NOTIFICATION_COMMINGSOON)) {
                                        mViewPager.setCurrentItem(1);
                                        UPDATE_ONRESTART = false;
                                    }

                                }
                            }
                            pbContainer.setVisibility(View.INVISIBLE);
                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "ERROR: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.i("LOG-EXCEPTIONS", e.toString());
                }
            }
        }.start();

        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tb_top.setTitle(R.string.app_name_serie);
        tb_top.setSubtitle("");
        setSupportActionBar(tb_top);

        new DAO_Profile(this).bind();

        if (!Profile.getInstance().getId().equals("")) { // MANTER CONEXAO
            Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(MainActivity.googleApiClient);
            this.startActivityForResult(signInIntent, MainActivity.SIGN_IN_CODE);
        } else {
            createNavigationDrawer();
        }

        //NAVIGATION DRAWER

    }

    public static boolean checkPlayServices(Context context) {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(context);
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog((Activity) context, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            return false;
        }
        return true;
    }

    private void createNavigationDrawer() {
        nv_main = new DrawerBuilder().withActivity(this)
                .withAccountHeader(UtilsEntitys.createAccountHeader(this))
                .withToolbar(tb_top)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.app_series).withSetSelected(true).withIcon(R.drawable.ic_television_guide),
                        new PrimaryDrawerItem().withName(R.string.app_list).withIcon(R.drawable.ic_list_bulleted),
                        new PrimaryDrawerItem().withName(R.string.app_search).withIcon(R.drawable.magnify),
                        new PrimaryDrawerItem().withName(R.string.app_historic).withIcon(R.drawable.history),
//                        new PrimaryDrawerItem().withName(R.string.app_statistic).withIcon(R.drawable.ic_statistic),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.app_settings).withIcon(R.drawable.ic_settings)
                ).withOnDrawerItemClickListener(navigation_click_item).build();
        nv_main.addStickyFooterItem(new PrimaryDrawerItem().withName("Powered thetvdb.com"));
    }

    Drawer.OnDrawerItemClickListener navigation_click_item = new Drawer.OnDrawerItemClickListener() {
        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            Intent it = null;
            Log.d("NAV", "POSITION: " + position);
            switch (position) {
                case 2:
                    nv_main.closeDrawer();
                    it = new Intent(MainActivity.this, SeriesListActivity.class);
                    startActivityForResult(it, NEWSERIEACTIVITY_CODE);
                    break;
                case 3:
                    nv_main.closeDrawer();
                    it = new Intent(MainActivity.this, NewSerieActivity.class);
                    startActivityForResult(it, NEWSERIEACTIVITY_CODE);
                    break;
                case 4:
                    nv_main.closeDrawer();
                    it = new Intent(MainActivity.this, HistoricActivity.class);
                    startActivityForResult(it, NEWSERIEACTIVITY_CODE);
                    break;
                case 6:
                    nv_main.closeDrawer();
                    it = new Intent(MainActivity.this, ConfigurationActivity.class);
                    startActivity(it);
                    break;
            }
            return true;

        }
    };

    PopupMenu.OnMenuItemClickListener onMenuItemClick_Popup = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                /*MENUS*/
                case R.id.it_options_update:
                    new Thread() {
                        @Override
                        public void run() {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb_updates.setVisibility(View.VISIBLE);
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.app_text_updating_series), Toast.LENGTH_SHORT).show();
                                }
                            });
                            try {
                                mySeries = HelpFragment.updateSeries(mySeries, context);
                            } catch (Exception e) {
                                Log.i("LOG-EXCEPTION", e.toString());
                            }
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    updateFragments(mTabsAdapter, mViewPager);
                                    pb_updates.setVisibility(View.INVISIBLE);
                                    Toast.makeText(MainActivity.this, getResources().getString(R.string.app_text_updating_series_finish), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }.start();

//                    Toast.makeText(MainActivity.this, "Update Action", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.it_options_order:
                    inflaterPopupMenu(tb_top, R.menu.menu_order, Gravity.RIGHT);
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
//                case R.id.it_filter_comingsoon:
//                    EnvironmentConfig.getInstance().alterFilterComingsoon();
//                    new DAO_EnvironmentConfig(MainActivity.this).edit();
//                    updateFragments();
//                    break;
                case R.id.it_filter_removeall:
                    EnvironmentConfig.getInstance().removeAllFilters();
                    new DAO_EnvironmentConfig(MainActivity.this).edit();
                    updateFragments(mTabsAdapter, mViewPager);
                    break;
            }
            return false;
        }
    };


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
                Log.i("LOG-AMBISERIES", "MainActivity - onQueryTextSubmit(" + newText + ")");

                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Serie> aux = null;
                        SEARCHVIEW = false;
                        if (newText.length() > 0) {
                            aux = new ArrayList<Serie>(mySeries);
                            SEARCHVIEW = true;
                            for (int i = mySeries.size() - 1; i >= 0; i--) {
                                if (mySeries.get(i).getName().toUpperCase().indexOf(newText.toUpperCase()) == -1) {
                                    mySeries.remove(i);
                                }
                            }
                        }
//                        mTabsAdapter.setMySeries(mySeries);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateFragments(mTabsAdapter, mViewPager);
                            }
                        });
                        if (aux != null) {
                            mySeries = new ArrayList<Serie>(aux);
                            aux.clear();
                        }
                    }
                }.start();

                return true;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                Log.i("LOG-AMBISERIES", "MainActivity - onQueryTextChange(" + newText + ")");
                new Thread() {
                    @Override
                    public void run() {
                        ArrayList<Serie> aux = null;
                        SEARCHVIEW = false;
                        if (newText.length() > 0) {
                            aux = new ArrayList<Serie>(mySeries);
                            SEARCHVIEW = true;
                            for (int i = mySeries.size() - 1; i >= 0; i--) {
                                if (mySeries.get(i).getName().toUpperCase().indexOf(newText.toUpperCase()) == -1) {
                                    mySeries.remove(i);
                                }
                            }
                        }
//                        mTabsAdapter.setMySeries(mySeries);
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                updateFragments(mTabsAdapter, mViewPager);
                            }
                        });
                        if (aux != null) {
                            mySeries = new ArrayList<Serie>(aux);
                            aux.clear();
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

        switch (item.getItemId()) {
            case R.id.it_options:
                inflaterPopupMenu(tb_top, R.menu.menu_options, Gravity.RIGHT);
                break;
            case R.id.it_filter:
                EnvironmentConfig ec = EnvironmentConfig.getInstance();
                PopupMenu popupMenu = inflaterPopupMenu(tb_top, R.menu.menu_filter, Gravity.RIGHT);
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
        popup.setOnMenuItemClickListener(onMenuItemClick_Popup);
        return popup;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("GOOGLE_CONNECTED", "MainActivity - onActivityResult() - requestCode: " + requestCode + " - resultCode(" + resultCode + ")");
        if (requestCode == SIGN_IN_CODE && resultCode == Activity.RESULT_OK) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
            handler.post(new Runnable() { //NAVIGATION DRAWER
                @Override
                public void run() {
                    createNavigationDrawer();
                }
            });
        }

        if (resultCode == Activity.RESULT_OK) {
            updateFragments(mTabsAdapter, mViewPager);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("GOOGLE_CONNECTED", "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            googleSignInAccount = result.getSignInAccount();
            getDataProfile();
        }
    }

    public ProgressBar getPb_updates() {
        return this.pb_updates;
    }

    public static void updateFragments(TabsAdapter mTabsAdapter, ViewPager mViewPager) {
        if (mTabsAdapter != null && mViewPager != null) {
            mTabsAdapter.notifyDataSetChanged();
            int currentItem = mViewPager.getCurrentItem();
            mViewPager.setAdapter(mTabsAdapter);
            mViewPager.setCurrentItem(currentItem);
        }
    }

    public TabsAdapter getTabsAdapter() {
        return mTabsAdapter;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("LOG-AMBISERIES", "MainActivity - onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
        updateFragments(mTabsAdapter, mViewPager);
    }


    @Override
    protected void onStart() {
        Log.i("LOG-AMBISERIES", "MainActivity - onStart()");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        Log.i("LOG-AMBISERIES", "MainActivity - onRestart()");
        super.onRestart();


        if (UPDATE_ONRESTART)
            updateFragments(mTabsAdapter, mViewPager);
        else UPDATE_ONRESTART = true;
    }

    @Override
    protected void onStop() {
        Log.i("LOG-AMBISERIES", "MainActivity - onStop()");
        super.onStop();

        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    private boolean gettingOut = false;

    @Override
    public void onBackPressed() {

        if (gettingOut) {
            super.onBackPressed();
            finish();
        } else {
            gettingOut = true;
            Appodeal.show(this, Appodeal.INTERSTITIAL);
        }

        Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
            @Override
            public void onInterstitialLoaded(boolean b) {

            }

            @Override
            public void onInterstitialFailedToLoad() {

            }

            @Override
            public void onInterstitialShown() {

            }

            @Override
            public void onInterstitialClicked() {

            }

            @Override
            public void onInterstitialClosed() {
                onBackPressed();
            }
        });
    }

    @Override
    public void onResume() {
        Log.i("LOG-AMBISERIES", "MainActivity - onResume()");
        super.onResume();
        if (googleSignInAccount == null)
            createNavigationDrawer();
    }

    @Override
    protected void onPause() {
        Log.i("LOG-AMBISERIES", "MainActivity - onPause()");
        super.onPause();
    }


    public void getDataProfile() {

        try {
            new DAO_Profile(MainActivity.this).bind();// IF EXIST DO NOT CREATE

            Profile.getInstance().setId(googleSignInAccount.getId());
            Profile.getInstance().setName(googleSignInAccount.getDisplayName());
            Profile.getInstance().setEmail(googleSignInAccount.getEmail());
            if (googleSignInAccount.getPhotoUrl() != null)
                Profile.getInstance().setImageUrl(googleSignInAccount.getPhotoUrl().getPath());


            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    String acc_name = googleSignInAccount.getEmail();
                    String scope = String.format("oauth2:%s", Scopes.PLUS_LOGIN);

                    try {
                        Profile.getInstance().setTokenFB(GoogleAuthUtil.getToken(getApplicationContext(), acc_name, scope));
                        new DAO_Profile(MainActivity.this).create();

                        FirebaseCore.logIn_Google(MainActivity.this);
                    } catch (IOException e) {
                        Profile.getInstance().clear();
                        e.printStackTrace();
                    } catch (GoogleAuthException e) {
                        Profile.getInstance().clear();
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute();
        } catch (Exception e) {
            Log.e("GOOGLEAPI-ERROR", e.getMessage());
        }
    }

    //    @Override
//    public void onConnected(Bundle bundle) {
//        Log.i("GOOGLE_CONNECTED", "MainActivity - onConnected()");
//
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i("GOOGLE_CONNECTED", "MainActivity - onConnectionSuspended()");
//        googleApiClient.connect();
//    }
//
//    //
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i("GOOGLE_CONNECTED", "MainActivity - onConnectionFailed()");
//        profile = null;
        connectionResult = result;
        //NAVIGATION DRAWER
        createNavigationDrawer();

        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), MainActivity.this, 0).show();
            return;
        }
        connectionResult = result;

    }


    public void initAPPODEAL() {
        Appodeal.disableLocationPermissionCheck();
        Appodeal.initialize(this, APPODEAL_KEY, Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.MREC | Appodeal.NATIVE);
        Appodeal.setNativeCallbacks(new AppodealNativeCallBacks(this));
        Appodeal.cache(this, Appodeal.NATIVE, 3);
    }


}
