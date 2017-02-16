package br.com.etm.checkseries.views;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.daos.DAO_Episode;
import br.com.etm.checkseries.daos.DAO_Language;
import br.com.etm.checkseries.daos.DAO_List;
import br.com.etm.checkseries.daos.DAO_Serie;
import br.com.etm.checkseries.domains.EnvironmentConfig;
import br.com.etm.checkseries.domains.Language;
import br.com.etm.checkseries.domains.ListOfUser;
import br.com.etm.checkseries.utils.APITheTVDB;
import br.com.etm.checkseries.utils.NotificationPublisher;
import br.com.etm.checkseries.utils.SerieComparator_Name;
import br.com.etm.checkseries.utils.SerieComparator_NextEpisode;
import br.com.etm.checkseries.utils.SerieComparator_Normal;


/**
 * Created by EDUARDO_MARGOTO on 19/08/2016.
 */
public class SplashScreen extends Activity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_SECONDS = 3;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(final Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        final Context context = this;


        new Thread() {
            @Override
            public void run() {
                new DAO_EnvironmentConfig(getApplicationContext()).bind();
                if (EnvironmentConfig.getInstance().getId() == null) {
//                    Log.i("LOG-AMBISERIES", "EnvironmentConfig.getInstance().getId() == null");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                List<Language> languages = APITheTVDB.getLanguages();
                                for (Language l : languages) {
                                    new DAO_Language(getApplicationContext()).create(l);
                                    if (l.getAbbreviation().equals(Locale.getDefault().getLanguage())) {
                                        EnvironmentConfig.getInstance().setLanguage(l);
                                    }
                                }
                                new DAO_EnvironmentConfig(getApplicationContext()).create();


                            } catch (XmlPullParserException e) {
                                Log.i("LOG-ERROR", e.toString());
                            } catch (IOException e) {
                                Log.i("LOG-ERROR", e.toString());
                            } catch (ParseException e) {
                                Log.i("LOG-ERROR", e.toString());
                            }
                            new Thread() {
                                @Override
                                public void run() {
                                    if (EnvironmentConfig.getInstance().isUpdateAutomatic())
                                        NotificationPublisher.scheduleUpdate(getApplicationContext());
                                }
                            }.start();
                            new Thread() {
                                @Override
                                public void run() {
                                    SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                                    if (!sharedPreferences.getBoolean("doSheduleCommingSoonToday", false))
                                        NotificationPublisher.scheduleCommingSoonToday(getApplicationContext());
                                }
                            }.start();

                        }
                    }.start();

                } else {
//                    Log.i("LOG-AMBISERIES", "EnvironmentConfig.getInstance().getId() != null");
                    new DAO_EnvironmentConfig(getApplicationContext()).bind();
                    if (EnvironmentConfig.getInstance().getLanguage() != null) {

                        EnvironmentConfig.getInstance().setLanguage(new DAO_Language(getApplicationContext()).find(EnvironmentConfig.getInstance().getLanguage().getId().toString()));
                    }
//                    Log.i("LOG-AMBISERIES", "EnvironmentConfig.getInstance().getNextUpdate() = " + EnvironmentConfig.getInstance().getNextUpdate());
                    if (EnvironmentConfig.getInstance().getNextUpdate() == -1) {
                        new Thread() {
                            @Override
                            public void run() {
                                if (EnvironmentConfig.getInstance().isUpdateAutomatic()) {
                                    NotificationPublisher.scheduleUpdate(getApplicationContext());
                                }

                            }
                        }.start();
                    }
                    new Thread() {
                        @Override
                        public void run() {
//                            SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
//                            if (!sharedPreferences.getBoolean("doSheduleCommingSoonToday", false))
                                NotificationPublisher.scheduleCommingSoonToday(getApplicationContext());
                        }
                    }.start();
                }

                // caso nao exista nenhum lista de series, criará a tab default
                List<ListOfUser> listOfUsers = new DAO_List(getApplicationContext()).findAll();
                if (listOfUsers.isEmpty()) {
                    List<ListOfUser> listOfUserList = new ArrayList<>();
                    ListOfUser listOfUser = new ListOfUser();
                    String titleDefault = getResources().getString(R.string.app_serieslist_list_default);
                    listOfUser.setName(titleDefault);
                    listOfUser.setWeight(0);
                    new DAO_List(getApplicationContext()).create(listOfUser);
                    listOfUserList.add(listOfUser);
                }
                listOfUsers = null;

                if (MainActivity.mySeries.isEmpty() || MainActivity.mySeries == null) {
                    MainActivity.mySeries = new DAO_Serie(SplashScreen.this).findAll();
//                MainActivity.mySeries = new DAO_Serie(SplashScreen.this).findAll(EnvironmentConfig.getInstance().isOrder_name(), EnvironmentConfig.getInstance().isOrder_nextEpisode());
                    for (int i = 0; i < MainActivity.mySeries.size(); i++) {
                        MainActivity.mySeries.get(i).setEpisodeList(new DAO_Episode(SplashScreen.this).findAll(MainActivity.mySeries.get(i)));
//                        MainActivity.mySeries.get(i).setNextEpisode(new DAO_Episode(SplashScreen.this).findNextEpisode(String.valueOf(MainActivity.mySeries.get(i).getId())));

                    }
                    if (EnvironmentConfig.getInstance().isOrder_name()) {
                        Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                    } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                        Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                    } else Collections.sort(MainActivity.mySeries, new SerieComparator_Normal());
                }
                // --
                Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                if (getIntent().getExtras() != null) {
                    Object obj = getIntent().getExtras().get(NotificationPublisher.KEY_NOTIFICATION);
                    if (obj != null) {
                        if (obj.toString().equals(NotificationPublisher.NOTIFICATION_COMMINGSOON)) {
                            mainIntent.putExtra(NotificationPublisher.KEY_NOTIFICATION, NotificationPublisher.NOTIFICATION_COMMINGSOON);
                        }

                    }
                }
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }.start();

        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
//        new Handler().postDelayed(new Runnable(){
//            @Override
//            public void run() {
//                /* Create an Intent that will start the Menu-Activity. */
////                MainActivity.mySeries = new DAO_Serie(SplashScreen.this).findAll();
////                for (int i = 0; i < MainActivity.mySeries.size(); i++) {
////                    MainActivity.mySeries.get(i).setEpisodeList(new DAO_Episode(SplashScreen.this).findAll(MainActivity.mySeries.get(i)));
////                }
////                if (EnvironmentConfig.getInstance().isOrder_name()) {
////                    Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
////                } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
////                    Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
////                }
//                Intent mainIntent = new Intent(SplashScreen.this,MainActivity.class);
//                SplashScreen.this.startActivity(mainIntent);
//                SplashScreen.this.finish();
//            }
//        }, SPLASH_DISPLAY_SECONDS * 1000);
    }
}
