package br.com.etm.checkseries.views;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.adapters.EpisodeAdapter;
import br.com.etm.checkseries.adapters.ListSeasonsAdapter;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;

/**
 * Created by EDUARDO_MARGOTO on 02/01/2016.
 */
public class SeasonActivity extends AppCompatActivity {

    private Serie serie;
    private Toolbar tb_top;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    List<Episode> episodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);
        Intent it = getIntent();
        if (it != null) {
            serie = (Serie) it.getSerializableExtra("serie");
            serie.setEpisodeList(new DAO_Episode(this).findAll(serie, ListSeasonsAdapter.POSITION_ACTIVE_SEASON));
        }

        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tb_top.setTitle(serie.getName());
//        if (ListSeasonsAdapter.POSITION_ACTIVE_SEASON != 0) {
//            mToolbar.setTitle(serie.getName() + " - " + ListSeasonsAdapter.POSITION_ACTIVE_SEASON + "ª " + getResources().getString(R.string.app_text_season));
//        } else {
//            mToolbar.setTitle(serie.getName() + " - " + getResources().getString(R.string.app_text_specials));
//        }
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.rv_list_serie);
        recyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        episodeList = new ArrayList<Episode>(serie.getEpisodeList());
        int positionAds = 0;
        /*if (!MainActivity.mAds.isEmpty()) {
            if (episodeList.size() >= 4) {
                for (int i = 3; i < episodeList.size(); i++) {
                    if ((i % 3) == 0) {
                        Episode e = new Episode();
                        e.setId(0);
                        e.setEpisodeName(MainActivity.mAds.get(positionAds).getTitle());
                        episodeList.add(i, e);
                        positionAds++;
                        if (positionAds == MainActivity.mAds.size())
                            positionAds = 0;
                    }
                }
            } else {
                Episode e = new Episode();
                e.setEpisodeName(MainActivity.mAds.get(positionAds).getTitle());
                episodeList.add(e);
            }
        }*/

//        for (Episode ep : serie.getEpisodeList()) {
//            if (ep.getSeasonNumber() == ListSeasonsAdapter.POSITION_ACTIVE_SEASON) {
//                episodeList.add(ep);
//            }
//            if (ep.getSeasonNumber() != ListSeasonsAdapter.POSITION_ACTIVE_SEASON && episodeList.size() > 1) {
//                break;
//            }
//        }


        EpisodeAdapter episodeAdapter = new EpisodeAdapter(this, episodeList);
        recyclerView.setAdapter(episodeAdapter);
    }



    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
//                Intent it = new Intent();
//                setResult(Activity.RESULT_OK, it);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
