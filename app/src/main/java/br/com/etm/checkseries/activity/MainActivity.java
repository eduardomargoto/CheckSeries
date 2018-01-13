package br.com.etm.checkseries.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsAdapter;
import br.com.etm.checkseries.utils.UtilsEntitys;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends AppCompatActivity {

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

    private Unbinder unbinder;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);

        UtilsEntitys.setOrientationConfigDevice(this);

        ViewCompat.setElevation(mToolbar, 8);
        ViewCompat.setElevation(mTabLayout, 8);
        setSupportActionBar(mToolbar);

        pbContainer.setVisibility(View.VISIBLE);
        pb_updates.setVisibility(View.GONE);

        configureTabs();
    }

    private void configureTabs() {
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), MainActivity.this);
        mViewPager.setAdapter(tabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        pbContainer.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
