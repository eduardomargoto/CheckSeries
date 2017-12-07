package br.com.etm.checkseries.views;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.fragments.HistoricFragment;

/**
 * Created by EDUARDO_MARGOTO on 10/5/2016.
 */

public class HistoricActivity extends AppCompatActivity {
    private Toolbar tb_top;
    private TextView tv_msg;
    private HistoricFragment historicFragment;
    List<Episode> episodes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_historic);

        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        tb_top = (Toolbar) findViewById(R.id.tb_main);

        tb_top.setTitle(R.string.app_act_historic);
        tb_top.setSubtitle("");
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        episodes = new DAO_Episode(this).findAllHistoric();
//        for(Episode ep: episodes){
//            Log.i("EPISODE",ep.getSeasonNumber() + " - " + ep.getEpisodeNumber() + " / " + ep.getDateWatchedFormatted());
//        }
        /*int positionAds = 0;
        if (!MainActivity.mAds.isEmpty()) {
            if (episodes.size() >= 4) {
                for (int i = 3; i < episodes.size(); i++) {
                    if ((i % 3) == 0) {
                        Episode e = new Episode();
                        e.setId(0);
                        e.setEpisodeName(MainActivity.mAds.get(positionAds).getTitle());
                        episodes.add(i, e);
                        positionAds++;
                        if (positionAds == MainActivity.mAds.size())
                            positionAds = 0;
                    }
                }
            } else {
                Episode e = new Episode();
                e.setEpisodeName(MainActivity.mAds.get(positionAds).getTitle());
                episodes.add(e);
            }
        }*/
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

    public void setFragmentInActivity() {
        Log.i("LOG-AMBISERIES", "setFragmentInActivity()");
        historicFragment = (HistoricFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        tv_msg.setText("");
        if (episodes == null) episodes = new ArrayList<>();

        if (historicFragment == null)
            historicFragment = new HistoricFragment(episodes);
        else
            historicFragment.updateView(episodes);
        Bundle b = new Bundle();
        b.putString("nome", "Eduardo");
        historicFragment.setArguments(b);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.rl_fragment_container, historicFragment, "mainFrag");
        ft.commit();


//        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.i("LOG-AMBISERIES", "MainActivity - onConfigurationChanged()");
        super.onConfigurationChanged(newConfig);
      setFragmentInActivity();
    }
}
