package br.com.etm.checkseries.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.views.SeasonActivity;

/**
 * Created by EDUARDO_MARGOTO on 08/11/2015.
 */
public class ListSeasonsAdapter extends BaseAdapter {
    private Serie mySerie = null;
    private Activity activityContext;
    public static int POSITION_ACTIVE_SEASON = -1;

    public ListSeasonsAdapter(Serie serie, Activity activity) {
        this.mySerie = serie;
        this.activityContext = activity;
    }

    @Override
    public int getCount() {
        if (mySerie.haveSpecialSeason())
            return mySerie.getSizeSeasons();
        else return mySerie.getSizeSeasons() + 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = activityContext.getLayoutInflater().inflate(R.layout.item_season, parent, false);
        int size_season = mySerie.getSizeSeasons();

        int size_episode_notdisplayed = mySerie.getSizeEpisodesNotDisplayed(position);

        TextView tv_season_name = (TextView) view.findViewById(R.id.tv_season_name);
        TextView tv_size_not_displayed = (TextView) view.findViewById(R.id.tv_size_not_displayed);
        TextView tv_size = (TextView) view.findViewById(R.id.tv_size);
        ImageView iv_options_season = (ImageView) view.findViewById(R.id.iv_options_season);
        ProgressBar pb_episodes_watched = (ProgressBar) view.findViewById(R.id.pb_episodes_watched);

        iv_options_season.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POSITION_ACTIVE_SEASON = position;
                v.showContextMenu();
            }
        });

        if (position == 0)
            tv_season_name.setText(view.getResources().getString(R.string.app_text_specials));
        else
            tv_season_name.setText(position + "ª " + view.getResources().getString(R.string.app_text_season));

        if (size_episode_notdisplayed > 0)
            tv_size_not_displayed.setText(" (" + mySerie.getSizeEpisodesNotDisplayed(position) + " " + view.getResources().getString(R.string.app_text_not_displayed) + ")");
        else
            tv_size_not_displayed.setText("");
        tv_size.setText(mySerie.getSizeEpisodesWatched(position) + "/" + mySerie.getSizeEpisodes(position));

        pb_episodes_watched.setMax(mySerie.getSizeEpisodes(position));
        pb_episodes_watched.setProgress(mySerie.getSizeEpisodesWatched(position));

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                POSITION_ACTIVE_SEASON = position;
                Intent it = new Intent(activityContext, SeasonActivity.class);
                Serie s = mySerie;
                s.setEpisodeList(null);
                it.putExtra("serie", s);
                activityContext.startActivity(it);

            }
        });

        return view;
    }
}
