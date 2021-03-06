package br.com.etm.checkseries.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import br.com.etm.checkseries.App;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.NewSerieAdapter;
import br.com.etm.checkseries.api.data.fanart.ApiFanArtObject;
import br.com.etm.checkseries.api.data.trakTv.ApiMediaObject;
import br.com.etm.checkseries.di.components.DaggerNewSerieComponent;
import br.com.etm.checkseries.di.modules.NewSerieModule;
import br.com.etm.checkseries.presenters.NewSeriePresenter;
import br.com.etm.checkseries.deprecated.utils.HttpConnection;
import br.com.etm.checkseries.utils.Utils;
import br.com.etm.checkseries.views.NewSerieView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private ProgressDialog progressDialog = null;
    private NewSerieAdapter serieAdapter;

    public static NewSerieFragment newInstance() {
        return new NewSerieFragment();
    }

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

        presenter.onCreate();
        return view;
    }

    @OnClick(R.id.iv_clear_search)
    public void onClickClearSearch() {
        etSearch.setText("");
        Utils.showKeyboard(getContext());
    }

    @OnEditorAction(R.id.et_name_serie)
    protected boolean onSearch() {
        presenter.searchSerie(getContext(), etSearch.getText().toString());
        Utils.hideKeyboard(getContext(), etSearch);
        return true;
    }

    public void updateView(List<ApiMediaObject> apiMediaObjects) {
        if (serieAdapter == null) {
            serieAdapter = new NewSerieAdapter(apiMediaObjects);
            recyclerView.setAdapter(serieAdapter);
        } else {
            serieAdapter.setMediaObjects(apiMediaObjects);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.onDestroy();
    }

    @Override
    public void returnImage(int position, ApiFanArtObject apiFanArtObject) {
        serieAdapter.onLoadingImage(position, apiFanArtObject);
    }

    @Override
    public void configureView() {
        if (!HttpConnection.isOnline(getActivity())) {
            tvMessage.setVisibility(View.VISIBLE);
            tvMessage.setText(R.string.app_internet_off);
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getResources().getString(R.string.app_searching));

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        serieAdapter = new NewSerieAdapter(null);
        serieAdapter.setOnItemClickListener((adapterView, view, position, l) -> {
            ApiMediaObject mediaObject = (ApiMediaObject) view.getTag();
            presenter.insert(position, mediaObject);
        });

        serieAdapter.setOnLoadingImageListener((position, id, type) -> presenter.retrieveImages(position, id, type));

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

    @Override
    public void onSerieAdded(int position) {
        if (serieAdapter != null) {
            serieAdapter.onAddFinished(position);
        }
    }

}