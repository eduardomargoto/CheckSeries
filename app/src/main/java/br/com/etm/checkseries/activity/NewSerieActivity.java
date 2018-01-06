package br.com.etm.checkseries.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.fragments.NewSerieFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by EDUARDO_MARGOTO on 20/10/2015.
 */
public class NewSerieActivity extends AppCompatActivity{

    private static final String TAG = NewSerieActivity.class.getSimpleName();

    @BindView(R.id.tb_main)
    Toolbar tb_top;

    @BindView(R.id.rl_fragment_container)
    RelativeLayout rl_fragment;
;
    private Unbinder unbinder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_serie);
        unbinder = ButterKnife.bind(this);

        ViewCompat.setElevation(tb_top, 4);
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setFragmentInActivity();
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

    public void setFragmentInActivity() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_fragment_container, NewSerieFragment.newInstance(), "mainFrag");
        ft.commit();
    }
}