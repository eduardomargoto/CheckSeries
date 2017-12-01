package br.com.etm.checkseries.views;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.NewSerieFragment;
import br.com.etm.checkseries.utils.HttpConnection;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class NewSerieActivity extends AppCompatActivity {

    private static final String TAG = NewSerieActivity.class.getSimpleName();
    private static final String SERIESLIST = "seriesList";

    @BindView(R.id.et_name_serie)
    EditText et_serie_name;

    @BindView(R.id.tb_main)
    Toolbar tb_top;

    @BindView(R.id.tv_msg)
    TextView tv_msg;

    @BindView(R.id.rl_fragment_container)
    RelativeLayout rl_fragment;

    private String last_search_value = "no_value";
    private NewSerieFragment serieFragment = null;
    private ProgressDialog progressDialog = null;
    private ArrayList<Serie> series;
    private Unbinder unbinder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            series = (ArrayList<Serie>) savedInstanceState.getSerializable(SERIESLIST);
        }
        setContentView(R.layout.activity_new_serie);
        unbinder = ButterKnife.bind(this);
        progressDialog = new ProgressDialog(NewSerieActivity.this);

        //series = updateAddedSeries(series, MainActivity.mySeries);

        ViewCompat.setElevation(tb_top, 4);
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //RECYCLER VIEW - SERIRES
        setFragmentInActivity();
        if (!HttpConnection.isOnline(NewSerieActivity.this)) {
            tv_msg.setText(R.string.app_internet_off);
        }

        et_serie_name.setOnEditorActionListener((v, actionId, event) -> {
            progressDialog.setMessage(getResources().getString(R.string.app_searching));
            progressDialog.show();
            if (HttpConnection.isOnline(NewSerieActivity.this)) {
             /*   new Thread() {
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
                }.start();*/
            } else {
                progressDialog.dismiss();
                if (last_search_value.equalsIgnoreCase(et_serie_name.getText().toString())) {
                    Log.i(TAG, "getlast_search_value = true");
                    setFragmentInActivity();
                } else {
                    serieFragment = new NewSerieFragment(new ArrayList<>());

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container, serieFragment, "mainFrag");
                    ft.commit();
                    tv_msg.setText(R.string.app_internet_off);
                }
            }

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            return true;
        });

/*        et_serie_name.setOnTouchListener((v, event) -> {
            if (et_serie_name.getCompoundDrawables()[2] == null)
                return false;

            if (event.getX() > et_serie_name.getWidth() - et_serie_name.getPaddingRight() - et_serie_name.getCompoundDrawables()[2].getIntrinsicWidth()) {
                et_serie_name.setText("");
            }
            return false;
        });*/
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
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.i(TAG, "onSaveInstanceState()");
        outState.putSerializable(SERIESLIST, new ArrayList<>(series));

        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setFragmentInActivity();
    }

    public void setFragmentInActivity() {
        Log.i(TAG, "setFragmentInActivity()");

        serieFragment = (NewSerieFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        tv_msg.setText("");
        if (series == null) series = new ArrayList<>();

        if (serieFragment == null)
            serieFragment = new NewSerieFragment(series);
        else
            serieFragment.updateView(series);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_fragment_container, serieFragment, "mainFrag");
        ft.commit();
    }
}