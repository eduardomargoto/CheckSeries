package br.com.etm.checkseries.utils;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;



import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import br.com.etm.checkseries.R;

import br.com.etm.checkseries.deprecated.daos.DAO_Episode;
import br.com.etm.checkseries.deprecated.daos.DAO_Serie;
import br.com.etm.checkseries.deprecated.domains.Episode;
import br.com.etm.checkseries.deprecated.domains.Serie;
import br.com.etm.checkseries.deprecated.utils.APITheTVDB;

/**
 * Created by EDUARDO_MARGOTO on 10/25/2016.
 */

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        Log.i("WIDGET", "onGetViewFactory");
        RemoteViewsFactory listProvidder = new CallsListRemoteViewsFactory(this.getApplicationContext(), intent);
        return listProvidder;
    }
}

class CallsListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private int appWidgetId;
    private List<Episode> episodeList;
    private Context context = null;

    public CallsListRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public int getCount() {
        return episodeList.size();
    }

    @Override
    public long getItemId(int position) {
        return episodeList.get(position).getId();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Log.i("WIDGET", "getViewAt(" + position + ")");
        RemoteViews row = new RemoteViews(context.getPackageName(), R.layout.widget_item);

        Episode episode = episodeList.get(position);
        Serie serie = new DAO_Serie(context).find(String.valueOf(episode.getSerieId()));

        row.setTextViewText(R.id.tv_name_serie, serie.getName());
        row.setTextViewText(R.id.tv_nextepisode_serie, episode.getEpisodeFormatted());

        Calendar dateEpisode = Calendar.getInstance();
        dateEpisode.setTime(episode.getFirstAired());
        dateEpisode.set(Calendar.HOUR_OF_DAY, 00);
        dateEpisode.set(Calendar.MINUTE, 00);
        dateEpisode.set(Calendar.SECOND, 00);
        dateEpisode.set(Calendar.MILLISECOND, 00);

        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 00);
        today.set(Calendar.MINUTE, 00);
        today.set(Calendar.SECOND, 00);
        today.set(Calendar.MILLISECOND, 00);

        long differenceDays = (int) ((dateEpisode.getTimeInMillis() - today.getTimeInMillis()) / (24 * 60 * 60 * 1000));
        if (differenceDays == 0) {
            row.setTextViewText(R.id.tv_nextepisodetime_serie, context.getResources().getString(R.string.today) + " às " + serie.getAirs_Time() + "(" + serie.getNetwork() + ")");
        } else if (differenceDays == 1) {
            row.setTextViewText(R.id.tv_nextepisodetime_serie, context.getResources().getString(R.string.tomorrow) + " às " + serie.getAirs_Time() + "(" + serie.getNetwork() + ")");
        } else if (differenceDays > 1 && differenceDays <= 3) {
            row.setTextViewText(R.id.tv_nextepisodetime_serie, context.getResources().getString(R.string.at_days).replace("/days", "" + (differenceDays)) + " às " + serie.getAirs_Time() + "(" + serie.getNetwork() + ")");
        } else {
            row.setTextViewText(R.id.tv_nextepisodetime_serie, episode.getDateEpisodeShortFormatted(context) + " às " + serie.getAirs_Time() + "(" + serie.getNetwork() + ")");
        }


//        try {
//            row.setImageViewBitmap(R.id.iv_serie,
//                    Picasso.with(context).load(APITheTVDB.PATH_BANNERS + serie.getPoster())
//                            .resize(70, 100)
//                            .stableKey(serie.getPosterFilenameCache())
//                            .placeholder(R.drawable.loading_animation_black)
//                            .error(R.drawable.image_area_36dp)
//                            .get()

//            );
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        return row;
    }

    @Override
    public RemoteViews getLoadingView() {
        Log.i("WIDGET", "getLoadingView");
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public void onCreate() {
        Log.i("WIDGET", "onCreate");
        episodeList = new DAO_Episode(context).findAllRelease();
    }

    @Override
    public void onDataSetChanged() {
        Log.i("WIDGET", "onDataSetChanged");
        episodeList = new DAO_Episode(context).findAllRelease();
    }

    @Override
    public void onDestroy() {
    }
}


