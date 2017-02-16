package br.com.etm.checkseries.utils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import br.com.etm.checkseries.R;

/**
 * Created by EDUARDO_MARGOTO on 10/25/2016.
 */

public class WidgetProviderComingSoon extends AppWidgetProvider {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("WIDGET", "onReceive");
        String strAction = intent.getAction();
        if (AppWidgetManager.ACTION_APPWIDGET_UPDATE.equals(strAction)) {
            int[] ids = (int[]) intent.getExtras().get(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            onUpdate(context, AppWidgetManager.getInstance(context), ids);
        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.i("WIDGET", "onUpdate: " + appWidgetIds.length);

        for (int i = 0; i < appWidgetIds.length; ++i) {
            int idWidget = appWidgetIds[i];
            Intent serviceIntent = new Intent(context, WidgetService.class);
            serviceIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            serviceIntent.setData(Uri.parse(serviceIntent.toUri(Intent.URI_INTENT_SCHEME)));


            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_comingsoon);
            rv.setRemoteAdapter(R.id.rv_episodes, serviceIntent);

            rv.setEmptyView(R.id.rv_episodes, R.id.tv_empty_message);

//            Intent intent = new Intent(context, WidgetProviderComingSoon.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//            rv.setOnClickPendingIntent(R.id.ib_update, pendingIntent);

            appWidgetManager.updateAppWidget(idWidget, rv);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
