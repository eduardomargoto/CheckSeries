package br.com.etm.checkseries.utils;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_Language;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Episode;
import br.com.etm.checkseries.domains.Language;
import br.com.etm.checkseries.domains.Serie;
import br.com.etm.checkseries.fragments.HelpFragment;
import br.com.etm.checkseries.views.MainActivity;
import br.com.etm.checkseries.views.SplashScreen;

/**
 * Created by EDUARDO_MARGOTO on 26/01/2016.
 */
public class NotificationPublisher extends BroadcastReceiver {

    public static String NOTIFICATION_UPDATE = "UPDATE";
    public static String NOTIFICATION_COMMINGSOON = "COMINGSOON";
    private static int NOTIFICATION_COUNT = 1;
    private static int ID_NOTIFICATION_UPDATE = 1;
    private static int ID_NOTIFICATION_NEW_SEASON = 2;
    private static int ID_NOTIFICATION_COMMINGSOON = 3;
    public static String KEY_NOTIFICATION = "ntfIntent";

    public static final int DELAY_DAY = 24 * 60 * 60 * 1000;
    public static final int DELAY_HALFDAY = 12 * 60 * 60 * 1000;
    public static final int DELAY_HOUR = 1 * 60 * 60 * 1000;
    public static final int DELAY_MINUTE = 1 * 60 * 1000;

    public void onReceive(final Context context, final Intent intent) {
        Log.i("LOG-AMBISERIES", "onReceive");
        new Thread() {
            @Override
            public void run() {
                String value = (String) intent.getExtras().get(KEY_NOTIFICATION);
                if (value.equals(NOTIFICATION_UPDATE)) {
                    try {
                        Log.d("LOG-AMBISERIES", "EXECUTO UPDATE");
                        List<Language> languageList = new DAO_Language(context).findAll();
                        for (Language l : languageList) {
                            if (l.getAbbreviation().equals(Locale.getDefault().getLanguage())) {
                                EnvironmentConfig.getInstance().setLanguage(l);
                                break;
                            }
                        }
                        if (HttpConnection.isOnline(context)) {
                            updateAutomaticSeries(context);

                            if (EnvironmentConfig.getInstance().isUpdateAutomatic()) {
                                Log.d("LOG-AMBISERIES", "ADD OUTRO UPDATE");
                                scheduleUpdate(context);
                            }
                        } else {
                            if (EnvironmentConfig.getInstance().isUpdateAutomatic()) {
                                Log.d("LOG-AMBISERIES", "ADD OUTRO UPDATE NEXT HOUR");
                                scheduleUpdateNextHour(context);
                            }

                        }

                    } catch (ParseException e) {
                        e.printStackTrace();
                    } catch (XmlPullParserException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else if (value.equals(NOTIFICATION_COMMINGSOON)) {
                    notificationEpisodesToday(context);
//                    scheduleCommingSoonToday(context);
                }
            }
        }.start();
    }

    public static void updateAutomaticSeries(Context context) throws ParseException, XmlPullParserException, IOException {
        if (HttpConnection.isOnline(context)) {
            List<Episode> episodesNewSeason = new ArrayList<>();
            NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
            String content = "";
            boolean firstUpdate = true;

            List<Serie> serieList = new DAO_Serie(context).findAll();
            for (Serie s : serieList) {
                s.setEpisodeList(new DAO_Episode(context).findAll(s));
            }
            for (int i = 0; i < serieList.subList(0,5).size(); i++) {
                Serie s = APITheTVDB.getSerieAndEpisodes(String.valueOf(serieList.get(i).getId()));
                s.setName(serieList.get(i).getName());
                s.setAlias_names(serieList.get(i).getAlias_names());
                s.setFavorite(serieList.get(i).isFavorite());
                s.setHidden(serieList.get(i).isHidden());

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
                boolean isNew;
                new DAO_Serie(context).edit(s);
                int qtdUpdates = 0;
                int qtdNovosUpdates = 0;
                for (Episode ep : s.getEpisodeList()) {
                    isNew = true; // novos episodios
                    for (Episode ep1 : serieList.get(i).getEpisodeList()) {
                        if (ep.getId() == ep1.getId()) {
                            ep.setWatched(ep1.isWatched());
                            ep.setSkipped(ep1.isSkipped());
                            new DAO_Episode(context).edit(ep);
                            serieList.get(i).getEpisodeList().remove(ep1);
                            if (Integer.parseInt(ep.getLastUpdated()) > Integer.parseInt(ep1.getLastUpdated())) {
                                qtdUpdates++;
                            }
                            isNew = false; // nao é um episodio novo
                            break;
                        }
                    }
                    if (isNew) {
                        if (ep.getEpisodeNumber() == 1)
                            episodesNewSeason.add(ep);
                        qtdNovosUpdates++;
                        new DAO_Episode(context).create(ep);
                    }
                }

                if (EnvironmentConfig.getInstance().isNotification()) {
                    if (EnvironmentConfig.getInstance().isNotification_only_favorite()) {
                        if (s.isFavorite()) {
                            if (qtdNovosUpdates > 0 && qtdUpdates > 0) {
                                if (firstUpdate) {
                                    content = s.getName() + ": " + qtdNovosUpdates + " novos episódios e " + qtdUpdates + " episódios foram atualizados.";
                                    firstUpdate = false;
                                }
                                style.addLine(s.getName() + ": " + qtdNovosUpdates + " novos episódios e " + qtdUpdates + " episódios foram atualizados.");
                            } else {
                                if (qtdNovosUpdates > 0) {
                                    if (firstUpdate) {
                                        content = s.getName() + ": " + qtdNovosUpdates + " novos episódios.";
                                        firstUpdate = false;
                                    }
                                    style.addLine(s.getName() + ": " + qtdNovosUpdates + " novos episódios.");
                                }
                                if (qtdUpdates > 0) {
                                    if (firstUpdate) {
                                        content = s.getName() + ": " + qtdUpdates + " episódios foram atualizados.";
                                        firstUpdate = false;
                                    }
                                    style.addLine(s.getName() + ": " + qtdUpdates + " episódios foram atualizados.");
                                }
                            }

                        }
                    } else {
                        if (firstUpdate) {
                            content = s.getName() + ": " + qtdNovosUpdates + " novos episódios e " + qtdUpdates + " episódios foram atualizados.";
                            firstUpdate = false;
                        }
                        style.addLine(s.getName() + ": " + qtdNovosUpdates + " novos episódios e " + qtdUpdates + " episódios foram atualizados.");
                    }
                }
                for (Episode ep1 : serieList.get(i).getEpisodeList()) {
                    new DAO_Episode(context).remove(ep1);
                }
                serieList.set(i, s);
            }
            if (!content.equals("")) {
                notificationNotify(context, ID_NOTIFICATION_UPDATE, getNotification("Séries atualizadas!", "Séries atualizadas!", content, context, style, null));
            }

            if (!episodesNewSeason.isEmpty()) { // Identificação de novas temporadas
                firstUpdate = true;
                style = new NotificationCompat.InboxStyle();
                for (Episode episode : episodesNewSeason) {
                    String serie_name = new DAO_Serie(context).findName(episode.getSerieId());
                    String body = "";
                    if (episode.getFirstAired() != null)
                        body = episode.getSeasonNumber() + "ª Temporada de " + serie_name + " anunciada para o dia " + episode.getFirstAiredFormatted(context) + ".";
                    else
                        body = episode.getSeasonNumber() + "ª Temporada de " + serie_name + " anunciada, " + episode.getFirstAiredFormatted(context);

                    style.addLine(body);
                    if (firstUpdate) {
                        content = body;
                        firstUpdate = false;
                    }
                }
                notificationNotify(context, ID_NOTIFICATION_NEW_SEASON, getNotification("Novas Temporadas!", "Novas Temporadas", content, context, style, null));
            }
        }
    }

    public static Notification getNotification(String ticker, String title, String content, Context context, NotificationCompat.InboxStyle style, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setTicker(ticker);
        builder.setContentTitle(title);

        if (style != null)
            builder.setStyle(style);

        builder.setContentText(content);
        builder.setSmallIcon(R.drawable.ic_playlist_add_check_black_24dp);
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_checkseries));

        if (intent != null)
            builder.setContentIntent(intent);
        Notification n = builder.build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;

        return n;
    }

    public static void notificationNotify(Context context, int id, Notification notification) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void scheduleUpdateNextHour(Context context) {
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureMills = SystemClock.elapsedRealtime() + DELAY_HOUR;
        EnvironmentConfig.getInstance().setNextUpdate(futureMills);
        new DAO_EnvironmentConfig(context).edit();

//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        Log.d("UPDATE_AUTOMATIC", "NEXT UPDATE: " + sdf.format(futureMills));
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMills, pendingIntent);
    }

    public static void scheduleUpdate(Context context) {
        Log.i("LOG-AMBISERIES", "scheduleUpdate");
        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(KEY_NOTIFICATION, NotificationPublisher.NOTIFICATION_UPDATE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureMills = SystemClock.elapsedRealtime() + DELAY_DAY;
//        Log.i("LOG-AMBISERIES", "scheduleUpdate - futureMills = " + futureMills);
        EnvironmentConfig.getInstance().setNextUpdate(futureMills);
        new DAO_EnvironmentConfig(context).edit();
//        Log.i("LOG-AMBISERIES", "scheduleUpdate - EnvironmentConfig.getInstance().getNextUpdate() = " + EnvironmentConfig.getInstance().getNextUpdate());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureMills, pendingIntent);
    }

    public static void scheduleCommingSoonToday(Context context) {
        Log.i("LOG-AMBISERIES", "scheduleCommingSoonToday");
        SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("doSheduleCommingSoonToday", false)) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("doSheduleCommingSoonToday", true);
            editor.commit();
        }

        Intent notificationIntent = new Intent(context, NotificationPublisher.class);
        notificationIntent.putExtra(KEY_NOTIFICATION, NotificationPublisher.NOTIFICATION_COMMINGSOON);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//        long futureMills = SystemClock.elapsedRealtime() + DELAY_HALFDAY;
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_HALF_DAY, AlarmManager.INTERVAL_HALF_DAY, pendingIntent);

    }

    public static void notificationEpisodesToday(Context context) {
        List<Episode> episodes = new DAO_Episode(context).findAllToday();


        String serie_name = "";
        String content = "";
        NotificationCompat.InboxStyle style = new NotificationCompat.InboxStyle();
        if (episodes.size() >= 1) {
            serie_name = new DAO_Serie(context).findName(episodes.get(0).getSerieId());
            content = serie_name + ": " + episodes.get(0).getEpisodeFormatted() + ".";
        }

        for (Episode episode : episodes) {
            serie_name = new DAO_Serie(context).findName(episode.getSerieId());
            style.addLine(serie_name + ": " + episode.getEpisodeFormatted() + ".");
        }


        if (!episodes.isEmpty()) {

            Intent intent = new Intent(context, SplashScreen.class);
            intent.putExtra(KEY_NOTIFICATION, NOTIFICATION_COMMINGSOON);
            PendingIntent p = PendingIntent.getActivity(context, 0, intent, 0);
            notificationNotify(context, ID_NOTIFICATION_COMMINGSOON, getNotification("Lançamento(s) de Hoje", "Lançamento(s) de Hoje", content, context, style, p));
        }
    }


}
