package br.com.etm.checkseries.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;
import com.squareup.picasso.Picasso;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Language;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.NewSerieFragment;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.HttpConnection;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class NewSerieActivity extends AppCompatActivity {
    private static final String SERIESLIST = "seriesList";

    private Handler handler = new Handler();
    public ArrayList<Serie> series;
    private String last_search_value = "no_value";

    private EditText et_serie_name;
    private Toolbar tb_top;
    private TextView tv_msg;
    private RelativeLayout rl_fragment;
    private NewSerieFragment serieFragment = null;
    public Picasso picasso;
    ProgressDialog progressDialog = null;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            series = (ArrayList<Serie>) savedInstanceState.getSerializable(SERIESLIST);
//            series = savedInstanceState.getParcelableArrayList(SERIESLIST);
        }
        picasso = Picasso.with(this);
        final Context context = this;
        Appodeal.initialize(this, MainActivity.APPODEAL_KEY, Appodeal.BANNER);
//        Appodeal.show(this, Appodeal.BANNER);

        setContentView(R.layout.activity_new_serie);
        rl_fragment = (RelativeLayout) findViewById(R.id.rl_fragment_container);
        new Thread() {
            @Override
            public void run() {
                while (!Appodeal.isLoaded(Appodeal.BANNER)) {
                }
                final ViewGroup.MarginLayoutParams lpt = (ViewGroup.MarginLayoutParams) rl_fragment.getLayoutParams();
                lpt.setMargins(lpt.leftMargin, lpt.topMargin, lpt.rightMargin, UtilsImages.getMarginDensity(context, 60));
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        rl_fragment.setLayoutParams(lpt);
                        Appodeal.show(NewSerieActivity.this, Appodeal.BANNER);
                    }
                });

            }
        }.start();
        progressDialog = new ProgressDialog(NewSerieActivity.this);
        series = updateAddedSeries(series, MainActivity.mySeries);

        tb_top = (Toolbar) findViewById(R.id.tb_main);
        et_serie_name = (EditText) findViewById(R.id.et_name_serie);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tb_top = (Toolbar) findViewById(R.id.tb_main);

        tb_top.setTitle(R.string.app_act_newserie);
        tb_top.setSubtitle("");
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //RECYCLER VIEW - SERIRES
        setFragmentInActivity();
        if (!HttpConnection.isOnline(NewSerieActivity.this)) {
            tv_msg.setText(R.string.app_internet_off);
        }
        et_serie_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                progressDialog.setMessage(getResources().getString(R.string.app_searching));
                progressDialog.show();
                if (HttpConnection.isOnline(NewSerieActivity.this)) {
                    new Thread() {
                        public void run() {
                            try {
                                List<Language> languages = new ArrayList<Language>();
                                if (EnvironmentConfig.getInstance().getLanguage() == null)
                                    languages = APITheTVDB.getLanguages();
                                series = APITheTVDB.getSeries(et_serie_name.getText().toString());
                                series = updateAddedSeries(series, MainActivity.mySeries);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        setFragmentInActivity();
                                        if (series.isEmpty()) {
                                            tv_msg.setText(R.string.app_nofindnewseries);
                                        }
                                    }
                                });
                                last_search_value = et_serie_name.getText().toString();
                            } catch (UnknownHostException e) {
                                Log.i("LOG-EXCEPTION", e.toString());
                                e.printStackTrace();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        tv_msg.setText(R.string.app_internet_off);
                                    }
                                });
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.i("LOG-EXCEPTION", e.toString());
                            }
                            progressDialog.dismiss();

                        }
                    }.start();
                } else {
                    progressDialog.dismiss();
                    if (last_search_value.equalsIgnoreCase(et_serie_name.getText().toString())) {
                        Log.i("LOG-AMBISERIES", "getlast_search_value = true");
                        setFragmentInActivity();
                    } else {
                        serieFragment = new NewSerieFragment(new ArrayList<Serie>());

                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.rl_fragment_container, serieFragment, "mainFrag");
                        ft.commit();
                        tv_msg.setText(R.string.app_internet_off);
                    }
//                    Snackbar.make(v, R.string.app_internet_off, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return true;
            }
        });

        et_serie_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (et_serie_name.getCompoundDrawables()[2] == null)
                    return false;

                if (event.getX() > et_serie_name.getWidth() - et_serie_name.getPaddingRight() - et_serie_name.getCompoundDrawables()[2].getIntrinsicWidth()) {
                    et_serie_name.setText("");
                }
                return false;
            }
        });
    }

    public ArrayList<Serie> updateAddedSeries(ArrayList<Serie> serieList, ArrayList<Serie> mySeries) {
        if (serieList != null && mySeries != null) {
            for (int i = 0; i < serieList.size(); i++) {
                for (int j = 0; j < mySeries.size(); j++) {
                    if (serieList.get(i).getId().equals(mySeries.get(j).getId())) {
                        serieList.get(i).setAdded(true);
                    }
                }
            }
        }
        return serieList;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent();
                setResult(Activity.RESULT_OK, it);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        Log.i("LOG-AMBISERIES", "NewSerieActivity - onRestart()");
        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.i("LOG-AMBISERIES", "NewSerieActivity - onStop()");
        super.onStop();
    }

    @Override
    protected void onResume() {
        Log.i("LOG-AMBISERIES", "NewSerieActivity - onResume()");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i("LOG-AMBISERIES", "NewSerieActivity - onPause()");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("LOG-AMBISERIES", "NewSerieActivity - onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i("LOG-AMBISERIES", "onSaveInstanceState()");
//        outState.putParcelableArrayList(SERIESLIST, new ArrayList<Parcelable>(series));
        outState.putSerializable(SERIESLIST, new ArrayList<>(series));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setFragmentInActivity();
    }

    public void setFragmentInActivity() {
        Log.i("LOG-AMBISERIES", "setFragmentInActivity()");

        serieFragment = (NewSerieFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        tv_msg.setText("");
        if (series == null) series = new ArrayList<>();

        if (serieFragment == null) //{
            serieFragment = new NewSerieFragment(series);
        else
            serieFragment.updateView(series);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_fragment_container, serieFragment, "mainFrag");
        ft.commit();


//        }

    }
}