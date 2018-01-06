package br.com.etm.checkseries.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appodeal.ads.Appodeal;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsSerieAdapter;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.SlidingTabLayout;
import br.com.etm.checkseries.utils.UtilsImages;

/**
 * Created by EDUARDO_MARGOTO on 31/10/2015.
 */
public class SerieActivity extends AppCompatActivity {

    private Toolbar tb_top;
    private TextView tv_tb_title;
    private ViewPager vp_tabs;
    private Serie serie;
    public TabsSerieAdapter mTabsAdapter;
    public ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serie);
        vp_tabs = (ViewPager) findViewById(R.id.vp_tabs);
        final Context context = this;
        new Thread(){
            @Override
            public void run() {
                while(!Appodeal.isLoaded(Appodeal.BANNER)){}
                final ViewGroup.MarginLayoutParams lpt =(ViewGroup.MarginLayoutParams)vp_tabs.getLayoutParams();
                lpt.setMargins(lpt.leftMargin,lpt.topMargin,lpt.rightMargin, UtilsImages.getMarginDensity(context, 60));
                handler.post(new Runnable() {
                        @Override
                        public void run() {
                        vp_tabs.setLayoutParams(lpt);
                        Appodeal.show(SerieActivity.this, Appodeal.BANNER);
                    }
                });

            }
        }.start();

        Intent it = getIntent();
        if (it != null) {
            serie = (Serie) it.getSerializableExtra("serie");
        }
//        System.out.println("SerieActivity episodios: " + serie.getEpisodeList().size());
        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);


        tb_top.setTitle(serie.getName());
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mTabsAdapter = new TabsSerieAdapter(getSupportFragmentManager(), this, serie);

        //TABS
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sld_tabs);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mSlidingTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(1);

//        Appodeal.initialize(this, MainActivity.APPODEAL_KEY, Appodeal.INTERSTITIAL | Appodeal.BANNER | Appodeal.MREC | Appodeal.NATIVE);



    }

    public void updateFragments() {
        mTabsAdapter.notifyDataSetChanged();
        mViewPager.setAdapter(mTabsAdapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        updateFragments();
    }

//    private boolean gettingOut = false;
//
//    @Override
//    public void onBackPressed() {
//
//        if (gettingOut) {
//            super.onBackPressed();
//            finish();
//        } else {
//            gettingOut = true;
////            Appodeal.setLogging(true);
////            Appodeal.show(this, Appodeal.MREC);
//        }
//
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("SerieActivity", "onOptionsItemSelected - home");
                Intent it = new Intent();
                setResult(Activity.RESULT_OK, it);
                serie = null;
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
