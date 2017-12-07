package br.com.etm.checkseries.views;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import java.util.List;
import br.com.etm.checkseries.R;
import br.com.etm.checkseries.deprecated.daos.DAO_EnvironmentConfig;
import br.com.etm.checkseries.deprecated.daos.DAO_Language;
import br.com.etm.checkseries.deprecated.domains.EnvironmentConfig;
import br.com.etm.checkseries.deprecated.domains.Language;

/**
 * Created by EDUARDO_MARGOTO on 05/11/2015.
 */
public class ConfigurationActivity extends AppCompatActivity {

//    private String[] formats_number = new String[]{"1x01", "S01E01", "s01e01"};

    private static int permsRequestCode = 200;

    boolean writeAccepted = false;
    boolean readAccepted = false;

    private Toolbar tb_top;
    private TextView tv_language_checked, tv_format, tv_timeoffset_msg, tv_enable;
    private Switch sw_only_wifi, sw_updates, sw_hidden_episode_specials, sw_notification, sw_ntf_only_favorite, sw_ntf_vibrate, sw_checkmain, sw_layout_serie;
    private RelativeLayout rl_format_number, rl_langague, rl_timeoffset;
    private RelativeLayout rl_when_notification, rl_favorite_notification, rl_vibrate_notification, rl_audio_notification;
    private Button sw_doBackup;
    private Button sw_doRestore;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuration);


        sw_doBackup = (Button) findViewById(R.id.sw_doBackup);
        sw_doRestore = (Button) findViewById(R.id.sw_doRestore);

   /*     sw_doBackup.setOnClickListener(v -> {
            String[] perms = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_EXTERNAL_STORAGE"};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                SharedPreferences sharedPref = v.getContext().getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                writeAccepted = sharedPref.getBoolean("writeAccepted", false);
                if (!writeAccepted)
                    requestPermissions(perms, permsRequestCode);

                if (writeAccepted) {
                    UtilsEntitys.exportDBLocal(MainActivity.DEFAULTPATHBACKUP, MainActivity.PACKAGE_NAME, BDCore.NOME_BD, v.getContext());
                    sw_doBackup.setEnabled(false);
                }

            } else {
                UtilsEntitys.exportDBLocal(MainActivity.DEFAULTPATHBACKUP, MainActivity.PACKAGE_NAME, BDCore.NOME_BD, v.getContext());
                sw_doBackup.setEnabled(false);
            }

        });

        sw_doRestore.setOnClickListener(v -> {
            final Context context = v.getContext();
            UtilsEntitys.createAlertDialog(v.getContext(), "Restaurar backup",
                    "Deseja restaurar o backup ? Você irá substituir o dados já existentes.",
                    "SIM", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UtilsEntitys.importDBLocal(MainActivity.DEFAULTPATHBACKUP, MainActivity.PACKAGE_NAME, BDCore.NOME_BD, context);

                            new AsyncTask() {
                                @Override
                                protected Object doInBackground(Object[] params) {
                                    new DAO_EnvironmentConfig(getApplicationContext()).bind();
                                    if (EnvironmentConfig.getInstance().getId() == null) {
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
                                            }
                                        }.start();
                                    } else {
                                        new DAO_EnvironmentConfig(getApplicationContext()).bind();
                                        if (EnvironmentConfig.getInstance().getLanguage() != null) {
                                            EnvironmentConfig.getInstance().setLanguage(new DAO_Language(getApplicationContext()).find(EnvironmentConfig.getInstance().getLanguage().getId().toString()));
                                        }
                                        if (EnvironmentConfig.getInstance().getNextUpdate() == -1)
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    if (EnvironmentConfig.getInstance().isUpdateAutomatic()) {
                                                        NotificationPublisher.scheduleUpdate(getApplicationContext());
                                                    }
                                                    NotificationPublisher.scheduleCommingSoonToday(getApplicationContext());
                                                }
                                            }.start();
                                    }

                                    MainActivity.mySeries = new DAO_Serie(context).findAll();
                                    for (int i = 0; i < MainActivity.mySeries.size(); i++) {
                                        MainActivity.mySeries.get(i).setEpisodeList(new DAO_Episode(context).findAll(MainActivity.mySeries.get(i)));
                                    }
                                    if (EnvironmentConfig.getInstance().isOrder_name()) {
                                        Collections.sort(MainActivity.mySeries, new SerieComparator_Name());
                                    } else if (EnvironmentConfig.getInstance().isOrder_nextEpisode()) {
                                        Collections.sort(MainActivity.mySeries, new SerieComparator_NextEpisode());
                                    } else
                                        Collections.sort(MainActivity.mySeries, new SerieComparator_Normal());
                                    return null;

                                }

                                @Override
                                protected void onPostExecute(Object o) {
                                    Toast.makeText(context.getApplicationContext(), "Importação realizada!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }.execute();
                        }
                    }, "NÃO").show();

        });*/


        tv_language_checked = (TextView) findViewById(R.id.tv_language_checked);
        tv_format = (TextView) findViewById(R.id.tv_format);
        tv_timeoffset_msg = (TextView) findViewById(R.id.tv_timeoffset_msg);
        tv_enable = (TextView) findViewById(R.id.tv_enable);

        sw_only_wifi = (Switch) findViewById(R.id.sw_only_wifi);
        sw_updates = (Switch) findViewById(R.id.sw_updates);
        sw_hidden_episode_specials = (Switch) findViewById(R.id.sw_hidden_episode_specials);
        sw_notification = (Switch) findViewById(R.id.sw_notification);
        sw_ntf_only_favorite = (Switch) findViewById(R.id.sw_ntf_only_favorite);
//        sw_ntf_vibrate = (Switch) findViewById(R.id.sw_ntf_vibrate);
        sw_checkmain = (Switch) findViewById(R.id.sw_checkmain);
        sw_layout_serie = (Switch) findViewById(R.id.sw_layout_serie);

        rl_format_number = (RelativeLayout) findViewById(R.id.rl_format_number);
        rl_langague = (RelativeLayout) findViewById(R.id.rl_langague);
        rl_timeoffset = (RelativeLayout) findViewById(R.id.rl_timeoffset);
//        rl_when_notification = (RelativeLayout) findViewById(R.id.rl_when_notification);
        rl_favorite_notification = (RelativeLayout) findViewById(R.id.rl_favorite_notification);
//        rl_vibrate_notification = (RelativeLayout) findViewById(R.id.rl_vibrate_notification);
//        rl_audio_notification = (RelativeLayout) findViewById(R.id.rl_audio_notification);

        //TOOLBAR
        tb_top = (Toolbar) findViewById(R.id.tb_main);
        tb_top.setTitle(R.string.app_act_configuration);
        tb_top.setSubtitle("");
        setSupportActionBar(tb_top);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tv_language_checked.setText(EnvironmentConfig.getInstance().getLanguage().getLanguage());
        tv_format.setText(EnvironmentConfig.getInstance().getFormatNumber());
        tv_timeoffset_msg.setText(getResources().getString(R.string.app_confiact_timeoffset).replace(".hour.", EnvironmentConfig.getInstance().getTimeOffsetFormatted()));
        if (EnvironmentConfig.getInstance().isNotification()) {
            tv_enable.setText(getResources().getString(R.string.app_enable));
//            rl_when_notification.setEnabled(true);
            rl_favorite_notification.setEnabled(true);
//            rl_vibrate_notification.setEnabled(true);
//            rl_audio_notification.setEnabled(true);
        } else {
            tv_enable.setText(getResources().getString(R.string.app_unable));
//            rl_when_notification.setEnabled(false);
            rl_favorite_notification.setEnabled(false);
//            rl_vibrate_notification.setEnabled(false);
//            rl_audio_notification.setEnabled(false);
        }


        sw_layout_serie.setChecked(EnvironmentConfig.getInstance().isLayoutCompat());
        sw_layout_serie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isLayoutCompat()) {
                    EnvironmentConfig.getInstance().setLayoutCompat(false);
                } else {
                    EnvironmentConfig.getInstance().setLayoutCompat(true);
                }
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();


            }
        });


        sw_checkmain.setChecked(EnvironmentConfig.getInstance().isCheckmain());
        sw_checkmain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isCheckmain()) {
                    EnvironmentConfig.getInstance().setCheckmain(false);
                } else {
                    EnvironmentConfig.getInstance().setCheckmain(true);
                }
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();


            }
        });
        /*WI-FI CONNECTION*/
        sw_only_wifi.setChecked(EnvironmentConfig.getInstance().isImageOnlyWifi());
        sw_only_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isImageOnlyWifi()) {
                    EnvironmentConfig.getInstance().setImageOnlyWifi(false);
                } else {
                    EnvironmentConfig.getInstance().setImageOnlyWifi(true);
                }
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();


            }
        });

        /*UPDATES AUTOMATIC*/
        sw_updates.setChecked(EnvironmentConfig.getInstance().isUpdateAutomatic());
        sw_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isUpdateAutomatic())
                    EnvironmentConfig.getInstance().setUpdateAutomatic(false);
                else EnvironmentConfig.getInstance().setUpdateAutomatic(true);
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
            }
        });

        /*HIDDEN EPISODE SPECIALS*/
        sw_hidden_episode_specials.setChecked(EnvironmentConfig.getInstance().isHiddenEpisodesSpecials());
        sw_hidden_episode_specials.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isHiddenEpisodesSpecials())
                    EnvironmentConfig.getInstance().setHiddenEpisodesSpecials(false);
                else EnvironmentConfig.getInstance().setHiddenEpisodesSpecials(true);
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
            }
        });

        /*NOTIFICATIONS*/
        sw_notification.setChecked(EnvironmentConfig.getInstance().isNotification());
        sw_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isNotification()) {
                    EnvironmentConfig.getInstance().setNotification(false);
                    tv_enable.setText(getResources().getString(R.string.app_unable));
                    sw_ntf_only_favorite.setEnabled(false);
                    rl_favorite_notification.setEnabled(false);
                } else {
                    EnvironmentConfig.getInstance().setNotification(true);
                    tv_enable.setText(getResources().getString(R.string.app_enable));
                    rl_favorite_notification.setEnabled(true);

                }
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
            }
        });

        sw_ntf_only_favorite.setChecked(EnvironmentConfig.getInstance().isNotification_only_favorite());
        sw_ntf_only_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EnvironmentConfig.getInstance().isNotification()) {
                    if (EnvironmentConfig.getInstance().isNotification_only_favorite())
                        EnvironmentConfig.getInstance().setNotification_only_favorite(false);
                    else EnvironmentConfig.getInstance().setNotification_only_favorite(true);
                }
                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
            }
        });


//        sw_ntf_vibrate.setChecked(false);
        /*DIALOG PARA IDIOMA PREFERIDO*/
        rl_langague.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<Language> languageList = new DAO_Language(v.getContext()).findAll();
                final Language[] language = {null};
                String[] languages = new String[languageList.size()];
                int position = -1;
                for (int i = 0; i < languageList.size(); i++) {
                    if (languageList.get(i).getId().equals(EnvironmentConfig.getInstance().getLanguage().getId()))
                        position = i;

                    languages[i] = languageList.get(i).getLanguage();
                }
                AlertDialog.Builder dl_language = new AlertDialog.Builder(v.getContext())
                        .setTitle(getResources().getString(R.string.app_dl_title_languages))
                        .setSingleChoiceItems(languages, position, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                language[0] = languageList.get(which);
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.app_text_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EnvironmentConfig.getInstance().setLanguage(language[0]);
                                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
                                tv_language_checked.setText(EnvironmentConfig.getInstance().getLanguage().getLanguage());
//                                Toast.makeText(ConfigurationActivity.this, "Finaliza alteração", Toast.LENGTH_SHORT).show();
                            }
                        });
                dl_language.create().show();
            }
        });
        /*DIALOG PARA FORMA OS NUMEROS DAS SERIES*/
        rl_format_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = -1;
                final String[] newFormat = {""};
                for (int i = 0; i < EnvironmentConfig.typesFormat.length; i++) {
                    if (EnvironmentConfig.typesFormat[i].equals(EnvironmentConfig.getInstance().getFormatNumber())) {
                        position = i;
                        break;
                    }
                }
                AlertDialog.Builder dl_format_number = new AlertDialog.Builder(v.getContext())
                        .setTitle(getResources().getString(R.string.app_dl_title_format_numbers))
                        .setSingleChoiceItems(EnvironmentConfig.typesFormat, position, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                newFormat[0] = EnvironmentConfig.typesFormat[which];
//                                Toast.makeText(ConfigurationActivity.this, "Altera p/ " + EnvironmentConfig.typesFormat[which], Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.app_text_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EnvironmentConfig.getInstance().setFormatNumber(newFormat[0]);
                                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
                                tv_format.setText(EnvironmentConfig.getInstance().getFormatNumber());
//                                Toast.makeText(ConfigurationActivity.this, "Finaliza alteração", Toast.LENGTH_SHORT).show();
                            }
                        });
                dl_format_number.create().show();
            }

        });
        rl_timeoffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] timesOffset = new String[EnvironmentConfig.timesOffset.length];
                final int[] position = {-1};

                for (int i = 0; i < EnvironmentConfig.timesOffset.length; i++) {
                    if (EnvironmentConfig.timesOffset[i] == EnvironmentConfig.getInstance().getTimeOffset())
                        position[0] = i;

                    if (EnvironmentConfig.timesOffset[i] > 0)
                        timesOffset[i] = "+" + EnvironmentConfig.timesOffset[i];
                    else
                        timesOffset[i] = String.valueOf(EnvironmentConfig.timesOffset[i]);
                }

                AlertDialog.Builder dl_timeoffset = new AlertDialog.Builder(v.getContext())
                        .setTitle(getResources().getString(R.string.app_dl_title_format_numbers))
                        .setSingleChoiceItems(timesOffset, position[0], new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                position[0] = which;
                            }
                        })
                        .setPositiveButton(getResources().getString(R.string.app_text_ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EnvironmentConfig.getInstance().setTimeOffset(EnvironmentConfig.timesOffset[position[0]]);
                                new DAO_EnvironmentConfig(ConfigurationActivity.this).edit();
                                tv_timeoffset_msg.setText(getResources().getString(R.string.app_confiact_timeoffset).replace(".hour.", EnvironmentConfig.getInstance().getTimeOffsetFormatted()));
                            }
                        });
                dl_timeoffset.create().show();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("LOG-AMBISERIES", "ConfigurationActivity - onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

      /*  switch (permsRequestCode) {
            case 200:
                SharedPreferences sharedPref = this.getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                writeAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                readAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                editor.putBoolean("writeAccepted", writeAccepted);
                editor.putBoolean("readAccepted", readAccepted);
                editor.commit();
                break;
        }*/

    }


    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    public Action getIndexApiAction() {
//        Thing object = new Thing.Builder()
//                .setName("Configuration Page") // TODO: Define a title for the content shown.
//                // TODO: Make sure this auto-generated URL is correct.
//                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
//                .build();
//        return new Action.Builder(Action.TYPE_VIEW)
//                .setObject(object)
//                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
//                .build();
//    }
//
//
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }
}
