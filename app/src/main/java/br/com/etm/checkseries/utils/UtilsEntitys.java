package br.com.etm.checkseries.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.daos.BDCore;
import br.com.etm.checkseries.daos.DAO_Profile;
import br.com.etm.checkseries.daos.FirebaseCore;
import br.com.etm.checkseries.domains.Profile;
import br.com.etm.checkseries.views.MainActivity;
import br.com.etm.checkseries.views.SplashScreen;


/**
 * Created by EDUARDO_MARGOTO on 23/10/2015.
 */
public class UtilsEntitys {

    public static AlertDialog.Builder myDialog = null;

    public static void updateWidgets(Context context) {
//        Intent intent = new Intent(context.getApplicationContext(), WidgetProviderComingSoon.class);
//        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);

        // Use an array and EXTRA_APPWIDGET_IDS instead of AppWidgetManager.EXTRA_APPWIDGET_ID,
        // since it seems the onUpdate() is only fired on that:
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(context);
        int[] ids = widgetManager.getAppWidgetIds(new ComponentName(context, WidgetProviderComingSoon.class));

//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
        widgetManager.notifyAppWidgetViewDataChanged(ids, R.id.rv_episodes);

//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
//            context.sendBroadcast(intent);
    }


    public static void importDBLocal(String pathBackup, String packagename, String dbname, Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packagename
                        + "//databases//" + dbname;
                String backupDBPath = pathBackup; // No SDCard
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context.getApplicationContext(), "Importando dados...",
                        Toast.LENGTH_SHORT).show();

//                MainActivity.mySeries = new ArrayList<>();
//                Intent it = new Intent(context, SplashScreen.class);
//                context.startActivity(it);
            }
        } catch (Exception e) {

            Toast.makeText(context.getApplicationContext(), "Importação Falhou!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void exportDBLocal(String pathBackup, String packagename, String dbname, Context context) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

//            if (sd.canWrite()) {
                String currentDBPath = "//data//" + packagename + "//databases//" + dbname;
                String backupDBPath = pathBackup;

                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(context.getApplicationContext(), "Backup com sucesso!",
                        Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(context.getApplicationContext(), "Sem permissão para escrever no disco!",
//                        Toast.LENGTH_SHORT).show();
//            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context.getApplicationContext(), "Backup Falhou!", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public static void setOrientationConfigDevice(Activity context) {
        if (android.provider.Settings.System.getInt(context.getContentResolver(),
                Settings.System.ACCELEROMETER_ROTATION, 0) == 1) {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        } else {
            context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static byte[] streamToBytes(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, len);
        }
        output.close();
        inputStream.close();
        return output.toByteArray();

    }

    public static PopupMenu inflaterPopupMenu(Context context, View anchor, int idMenu, int gravity) {
        PopupMenu popup = new PopupMenu(context, anchor);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(idMenu, popup.getMenu());
        popup.setGravity(gravity);
        popup.show();
//        popup.setOnMenuItemClickListener(onMenuItemClick_Popup);
        return popup;
    }

    public static AccountHeader createAccountHeader(final Activity activity) {
        final AccountHeaderBuilder accountHeaderBuilder = new AccountHeaderBuilder();
        accountHeaderBuilder.withActivity(activity)
                .withHeaderBackground(R.drawable.back_navigation);
        if (MainActivity.googleSignInAccount != null) {
            if (MainActivity.googleSignInAccount.getPhotoUrl() != null) {
                DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                    @Override
                    public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                        Picasso.with(imageView.getContext()).load("https://lh6.googleusercontent.com/" + MainActivity.googleSignInAccount.getPhotoUrl().getPath()).into(imageView);
                    }
                });
            }
            accountHeaderBuilder.addProfiles(
                    new ProfileDrawerItem().withName(MainActivity.googleSignInAccount.getDisplayName())
                            .withEmail(Profile.getInstance().getEmail())
                            .withIcon("https://lh6.googleusercontent.com/" + Profile.getInstance().getImageUrl())
            ).withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                @Override
                public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                    if (MainActivity.googleSignInAccount != null) {
                        UtilsEntitys.createAlertDialog(activity,
                                "Desconectar", "",
                                "Sim", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        if (MainActivity.googleApiClient.isConnected()) {
                                            MainActivity.googleSignInAccount = null;
                                            new DAO_Profile(activity).remove();
//                                            MainActivity.googleApiClient.clearDefaultAccountAndReconnect();

                                            FirebaseCore.logOut();
                                            Auth.GoogleSignInApi.signOut(MainActivity.googleApiClient);
//                                            MainActivity.updateFragments();
                                            MainActivity mainActivity = (MainActivity) activity;
                                            mainActivity.onResume();
//                                            ((MainActivity) MainActivity.context).onResume();
                                        }
                                        dialog.dismiss();
                                    }
                                },
                                "Não").create().show();
                    }
                    return false;
                }
            });


        } else {
            DrawerImageLoader.init(new AbstractDrawerImageLoader() {
                @Override
                public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                    Picasso.with(imageView.getContext()).load(R.drawable.no_user_48).into(imageView);
                }
            });
            accountHeaderBuilder.addProfiles(
                    new ProfileDrawerItem().withEmail("Conecte-se")
                            .withIcon(R.drawable.no_user))
                    .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                        @Override
                        public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                            if (!MainActivity.checkPlayServices(view.getContext())) {
                                Toast.makeText(activity, "Google Play Services indisponível.", Toast.LENGTH_SHORT).show();
                            } else if (HttpConnection.isOnline(activity)) {
                                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(MainActivity.googleApiClient);
                                activity.startActivityForResult(signInIntent, MainActivity.SIGN_IN_CODE);
//                                ((MainActivity) MainActivity.context).onResume();
                            } else {
                                Toast.makeText(activity, activity.getResources().getString(R.string.app_internet_off), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    });
        }


        return accountHeaderBuilder.build();
    }

    public static AlertDialog.Builder createAlertDialog(Context context, String title, String message, String textPositiveButton, DialogInterface.OnClickListener positiveButton,
                                                        String textNegativeButton, DialogInterface.OnClickListener negativeButton) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textPositiveButton, positiveButton)
                .setNegativeButton(textNegativeButton, negativeButton);

    }

    public static AlertDialog.Builder createAlertDialog(Context context, String title, String message, String textPositiveButton, DialogInterface.OnClickListener positiveButton,
                                                        String textNegativeButton) {
        return new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(textPositiveButton, positiveButton)
                .setNegativeButton(textNegativeButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

    }


}

