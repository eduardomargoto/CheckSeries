package br.com.etm.checkseries.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.TabsEpisodesSeasonAdapter;
import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.fragments.EpisodeInformationFragment;
import br.com.etm.checkseries.utils.SlidingTabLayout;

/**
 * Created by EDUARDO_MARGOTO on 03/01/2016.
 */
public class ListEpisodesSeasonActivity extends AppCompatActivity {

    private List<Episode> episodeList;
    private Serie serie;
    private Toolbar tb_top;
    private TabsEpisodesSeasonAdapter mTabsAdapter;
    private ViewPager mViewPager;
    private SlidingTabLayout mSlidingTabLayout;
    private Episode episodeMark;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_episodes_season);
        Intent it = getIntent();
        if (it != null) {
            serie = (Serie) it.getSerializableExtra("serie");
            episodeMark = (Episode) it.getSerializableExtra("episode");
        }

        episodeList = new DAO_Episode(this).findAll(serie, episodeMark.getSeasonNumber());

//        if (serie.getEpisodeList().isEmpty())
//            serie.setEpisodeList(new DAO_Episode(this).findAll(serie));
//
//        for (Episode ep : serie.getEpisodeList()) {
//            if (ep.getSeasonNumber() == episodeMark.getSeasonNumber()) {
//                episodeList.add(ep);
//            }
//            if (episodeList.size() > 1 && ep.getSeasonNumber() != episodeMark.getSeasonNumber()) {
//                break;
//            }
//        }

        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tb_top.setTitle(serie.getName());

        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        serie.setEpisodeList(null);
        mTabsAdapter = new TabsEpisodesSeasonAdapter(getSupportFragmentManager(), this, episodeList, serie);

        //TABS
        mViewPager = (ViewPager) findViewById(R.id.vp_tabs);
        mSlidingTabLayout = (SlidingTabLayout) findViewById(R.id.sld_tabs);
        mViewPager.setAdapter(mTabsAdapter);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        mSlidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.colorAccent));
        mSlidingTabLayout.setViewPager(mViewPager);
        mViewPager.setCurrentItem(episodeMark.getEpisodeNumber() - 1);
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        EpisodeInformationFragment.USING_TABS = false;
        EpisodeInformationFragment.UPDATE_FRAGMENT = true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i("LOG-AMBISERIES", "ListEpisodesSeasonActivity - onOptionsItemSelected");
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent it = new Intent();
                EpisodeInformationFragment.USING_TABS = false;
                EpisodeInformationFragment.UPDATE_FRAGMENT = true;
                setResult(Activity.RESULT_OK, it);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
