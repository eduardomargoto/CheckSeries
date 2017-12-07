package br.com.etm.checkseries.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.NewSerieAdapter;
import br.com.etm.checkseries.api.data.ApiMediaObject;
import br.com.etm.checkseries.api.data.ApiSearchObject;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.di.DaggerNewSerieComponent;
import br.com.etm.checkseries.di.NewSerieModule;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.utils.HttpConnection;
import br.com.etm.checkseries.views.NewSerieView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.Unbinder;

/**
 * Created by EDUARDO_MARGOTO on 23/10/2015.
 */
public class NewSerieFragment extends Fragment implements NewSerieView {

    private final static String TAG = NewSerieFragment.class.getSimpleName();

    @BindView(R.id.rv_list_newserie)
    RecyclerView recyclerView;

    @BindView(R.id.et_name_serie)
    EditText etSearch;

    @BindView(R.id.tv_msg)
    TextView tvMessage;

    @Inject
    NewSeriePresenter presenter;

    private Unbinder unbinder;
    private List<Serie> serieList;
    private ProgressDialog progressDialog = null;

    public static NewSerieFragment newInstance() {
        return new NewSerieFragment();
    }

     /*et_serie_name.setOnEditorActionListener((v, actionId, event) -> {
            progressDialog.setMessage(getResources().getString(R.string.app_searching));
            progressDialog.show();
            if (HttpConnection.isOnline(NewSerieActivity.this)) {
             *//*   new Thread() {
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
                }.start();*//*
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
        });*/

/*        et_serie_name.setOnTouchListener((v, event) -> {
            if (et_serie_name.getCompoundDrawables()[2] == null)
                return false;

            if (event.getX() > et_serie_name.getWidth() - et_serie_name.getPaddingRight() - et_serie_name.getCompoundDrawables()[2].getIntrinsicWidth()) {
                et_serie_name.setText("");
            }
            return false;
        });*/

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerNewSerieComponent.builder()
                .appComponent(App.getAppComponent())
                .newSerieModule(new NewSerieModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newserie, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (!HttpConnection.isOnline(getActivity())) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(R.string.app_internet_off);
        }

        presenter.onCreate();
        return view;
    }

    @OnEditorAction(R.id.et_name_serie)
    protected boolean onSearch() {
        presenter.searchSerie(etSearch.getText().toString());
        return true;
    }

    public void updateView(List<ApiSearchObject> apiSearchObjects) {
        Log.i(TAG, "updateView");
//        NewSerieAdapter serieAdapter = new NewSerieAdapter(getActivity(), apiMediaObjectList);
//        recyclerView.setAdapter(serieAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroy();
    }

    @Override
    public void configureView() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.app_searching));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (serieList == null)
            serieList = new ArrayList<>();

        NewSerieAdapter serieAdapter = new NewSerieAdapter(getActivity(), serieList);
        recyclerView.setAdapter(serieAdapter);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void dismissProgress() {
        progressDialog.dismiss();
    }

}