package br.com.etm.checkseries.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_ListSerie;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.ListOfUser_Serie;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.HttpConnection;
import br.com.etm.checkseries.utils.NotificationPublisher;
import br.com.etm.checkseries.utils.SerieComparator_Name;
import br.com.etm.checkseries.utils.SerieComparator_NextEpisode;
import br.com.etm.checkseries.utils.SerieComparator_Normal;
import br.com.etm.checkseries.utils.UtilsImages;
import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 30/10/2015.
 */
public class HelpFragment {

    public static void createContextMenu(Activity activity, Serie serie, ContextMenu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_options_serie, menu);
        if (serie.isHidden())
            menu.getItem(4).setTitle(R.string.app_it_serie_show);

        menu.setHeaderTitle(serie.getName());
    }

    public static void updateEpisodeMainActivity(final Episode episode, final int positionAdapter) {
 /*       new Thread() {
            @Override
            public void run() {

                mySeries.get(positionAdapter).setNextEpisode(episode);

                if (EnvironmentConfig.getInstance().isOrder_name()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                } else Collections.sort(MainActivity.mySeries, new SerieComparator_Normal());


            }
        }.start();*/
    }

    public static void updateEpisodeMainActivity(final Episode episode, final Serie serie) {
       /* new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < MainActivity.mySeries.size(); i++) {
                    if (MainActivity.mySeries.get(i).getId().equals(serie.getId())) {
                        MainActivity.mySeries.get(i).setNextEpisode(episode);
//                        int position = MainActivity.mySeries.get(i).getPosition(episode);
//                        if (position != -1 && MainActivity.mySeries.get(i).getEpisodeList().get(position).getId() == episode.getId()) {
//                            MainActivity.mySeries.get(i).getEpisodeList().set(position, episode);
//                        } else {
//                            Log.e("ERRO.. ", "NAO FOI POSSIVEL ATUALIZAR O EPISODIO DA SERIE");
//                        }
                        break;
                    }
                }
                if (EnvironmentConfig.getInstance().isOrder_name()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                } else Collections.sort(MainActivity.mySeries, new SerieComparator_Normal());

                Log.d("UPDATE", "ATUALIZO A MAIN");
            }
        }.start();*/
    }

    public static void updateSerieMainActivity(final Serie serie) {
/*        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < MainActivity.mySeries.size(); i++) {
                    if (MainActivity.mySeries.get(i).getId().equals(serie.getId())) {
                        MainActivity.mySeries.set(i, serie);
                        break;
                    }
                }

                if (EnvironmentConfig.getInstance().isOrder_name()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                } else {
                    Collections.sort(MainActivity.mySeries, new SerieComparator_Normal());
                }
            }
        }.start();*/
    }

    public static void createContextMenu(Activity activity, Serie serie, ContextMenu menu, int resId_menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(resId_menu, menu);
        List<ListOfUser_Serie> list = new DAO_ListSerie(activity).findAll(serie);

        if (list.isEmpty())
            menu.getItem(1).setVisible(false);

        menu.setHeaderTitle(serie.getName());
    }

    public static void inflaterContextMenu(Activity activity, Serie serie, ContextMenu menu, int resId_menu, String title) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(resId_menu, menu);

        menu.setHeaderTitle(title);
    }

    public static Serie updateSerie(Serie serie, Context context) throws ParseException, XmlPullParserException, IOException {
        Serie s = APITheTVDB.getSerieAndEpisodes(String.valueOf(serie.getId()));

        s.setName(serie.getName());
        s.setAlias_names(serie.getAlias_names());
        s.setFavorite(serie.isFavorite());
        s.setHidden(serie.isHidden());

        if (!s.getFanArt().equals(serie.getFanArt())) {
            if (!s.getFanArt().equals("")) {
                Bitmap bitmap = Picasso.with(context).load(APITheTVDB.PATH_BANNERS + s.getFanArt())
                        .stableKey(s.getFanArtFilenameCache())
                        .get();
                if (bitmap != null) {
                    UtilsImages.saveToInternalSorage(bitmap, s.getFanArtFilename(), context);
                    UtilsImages.removerImagem(context, serie.getFanArt().replaceAll("/", "-"), "", true);
                    bitmap.recycle();
                }
            }
        }

        boolean isNew = false;

        new DAO_Serie(context).edit(s);
        for (Episode ep : s.getEpisodeList()) {
            isNew = true;
            for (Episode ep1 : serie.getEpisodeList()) {
                if (ep.getId() == ep1.getId()) {
                    ep.setWatched(ep1.isWatched());
                    ep.setSkipped(ep1.isSkipped());
                    serie.getEpisodeList().remove(ep1);
                    new DAO_Episode(context).edit(ep);
                    isNew = false;
                    break;
                }
            }
            if (isNew) {
                new DAO_Episode(context).create(ep);
                Log.i("LOG-NEW-EPISODE", "Episose -> " + ep.getEpisodeFormatted());
            }
        }

        for (Episode ep : serie.getEpisodeList()) { // REMOVE TODOS OS QUE SOBRARAM NA LISTA, POIS NAO EXISTEM MAIS NA SERIE
            new DAO_Episode(context).remove(ep);
        }
        return s;
    }

    public static ArrayList<Serie> updateSeries(List<Serie> serieList, Context context) throws ParseException, XmlPullParserException, IOException {
        for (int i = 0; i < serieList.size(); i++) {
            Serie s = APITheTVDB.getSerieAndEpisodes(String.valueOf(serieList.get(i).getId()));
            s.setName(serieList.get(i).getName());
            s.setAlias_names(serieList.get(i).getAlias_names());
            s.setFavorite(serieList.get(i).isFavorite());
            s.setHidden(serieList.get(i).isHidden());
            Log.i("LOG-UPDATE", "SERIE -> " + serieList.get(i).getName());

            if (!s.getFanArt().equals(serieList.get(i).getFanArt())) {
                if (!s.getFanArt().equals("")) {
                    Bitmap bitmap = Picasso.with(context).load(APITheTVDB.PATH_BANNERS + s.getFanArt())
                            .stableKey(s.getFanArtFilenameCache())
                            .get();
                    if (bitmap != null) {
                        UtilsImages.saveToInternalSorage(bitmap, s.getFanArtFilename(), context);
                        UtilsImages.removerImagem(context, serieList.get(i).getFanArt().replaceAll("/", "-"), "", true);
                        bitmap.recycle();
                    }

                }
            }
            boolean isNew = false;

            new DAO_Serie(context).edit(s);
            for (Episode ep : s.getEpisodeList()) {
                isNew = true;
                for (Episode ep1 : serieList.get(i).getEpisodeList()) {
                    if (ep.getId() == ep1.getId()) {
                        ep.setWatched(ep1.isWatched());
                        ep.setSkipped(ep1.isSkipped());
                        serieList.get(i).getEpisodeList().remove(ep1);
                        new DAO_Episode(context).edit(ep);
                        isNew = false;
                        break;
                    }
                }
                if (isNew) {
                    new DAO_Episode(context).create(ep);
                    Log.i("LOG-NEW-EPISODE", "Episose -> " + ep.getEpisodeFormatted());
                }
            }
            for (Episode ep : serieList.get(i).getEpisodeList()) { // REMOVE TODOS OS QUE SOBRARAM NA LISTA, POIS NAO EXISTEM MAIS NA SERIE
                new DAO_Episode(context).remove(ep);
            }
            serieList.set(i, s);
        }
        return new ArrayList<>(serieList);
    }

    @Deprecated
    public static void updateSeries(final Context context) throws ParseException, XmlPullParserException, IOException {
        final List<Serie> serieList = new DAO_Serie(context).findAll();

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < serieList.size(); i++) {
                    Serie s = null;
                    try {
                        s = APITheTVDB.getSerieAndEpisodes(String.valueOf(serieList.get(i).getId()));
                        if (s != null) {
                            s.setName(serieList.get(i).getName());
                            s.setAlias_names(serieList.get(i).getAlias_names());
                            s.setFavorite(serieList.get(i).isFavorite());
                            s.setHidden(serieList.get(i).isHidden());
                            boolean isNew = false;

                            new DAO_Serie(context).edit(s);
                            for (Episode ep : s.getEpisodeList()) {
                                isNew = true;
                                for (Episode ep1 : serieList.get(i).getEpisodeList()) {
                                    if (ep.getId() == ep1.getId()) {
                                        ep.setWatched(ep1.isWatched());
                                        new DAO_Episode(context).edit(ep);
                                        isNew = false;
                                    }
                                }
                                if (isNew) {
                                    new DAO_Episode(context).create(ep);
                                    Log.i("LOG-NEW-EPISODE", "Episose -> " + ep.getEpisodeFormatted());
                                }
                            }
                            serieList.set(i, s);
                        }
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


}
