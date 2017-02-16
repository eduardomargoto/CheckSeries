package br.com.etm.checkseries.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import br.com.etm.checkseries.views.MainActivity;

/**
 * Created by EDUARDO_MARGOTO on 22/10/2015.
 */
public class HttpConnection {

    static HttpURLConnection urlConnection = null;

    public static Bitmap downloadImage(String path) {
        Log.i("LOG-AMBISERIES", "download(" + path + ")");

        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap image = null;
        InputStream in = null;
        try {
            url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
//            urlConnection.setConnectTimeout(10000); // 10 segundos
            urlConnection.setDoInput(true);
            urlConnection.connect();

            in = urlConnection.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
            image = BitmapFactory.decodeStream(bufferedInputStream);
            in = null;
//            image = UtilsImages.decodeSampledBitmapFromInputStream(in, 75, 120);

        } catch (Exception e) {
            Log.i("LOG-EXCEPTION", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return image;
    }

    public static Bitmap downloadImageCompressed(String path, Context context, int width, int height) {
        Log.i("LOG-AMBISERIES", "download(" + path + ")");

        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap image = null;
        InputStream in = null;
        try {
            url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setConnectTimeout(10000); // 10 segundos
            urlConnection.setDoInput(true);
            urlConnection.connect();

            in = urlConnection.getInputStream();
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
//            image = BitmapFactory.decodeStream(bufferedInputStream);

            image = UtilsImages.decodeSampledBitmapFromInputStream(in, width, height);
            in = null;
        } catch (Exception e) {
            Log.i("LOG-EXCEPTION", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return image;
    }

    public static Bitmap downloadImageCompressed(String path, Context context) {
        Log.i("LOG-AMBISERIES", "download(" + path + ")");

        URL url = null;
        HttpURLConnection urlConnection = null;
        Bitmap image = null;
        InputStream in = null;
        try {
            url = new URL(path);

            urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setConnectTimeout(10000); // 10 segundos
            urlConnection.setDoInput(true);
            urlConnection.connect();

            in = urlConnection.getInputStream();
//            BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
//            image = BitmapFactory.decodeStream(bufferedInputStream);

            image = UtilsImages.decodeSampledBitmapFromInputStream(in, UtilsImages.getWidthAllDensity(context), 400);
            in = null;
        } catch (Exception e) {
            Toast.makeText(context, "Ocorreu um erro de conexão com a internet.", Toast.LENGTH_SHORT).show();
            Log.i("LOG-EXCEPTION", e.toString());
        } finally {
            urlConnection.disconnect();
        }
        return image;
    }

    public static String getDataWeb(String path) throws Exception {
        Log.i("LOG-AMBISERIES", "getDataWeb(" + path + ")");

        URL url = null;
//        HttpURLConnection urlConnection = null;
        String str_page = "";

        url = new URL(path);

        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(10000); // 10 segundos
        urlConnection.connect();

        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));
        int value = 0;
        int countReader = 0;
        while ((value = rd.read()) != -1) {
            if (countReader >= 40)
                str_page += "<" + rd.readLine();

            countReader++;
        }

        return str_page;
    }

    public static InputStream getDataWebIS(String path) throws IOException {
        Log.i("LOG-AMBISERIES", "getDataWeb(" + path + ")");

        URL url = null;
        URLConnection url_connect = null;
        String str_page = "";
        InputStream in = null;

        url = new URL(path);

        url_connect = url.openConnection();
        url_connect.setConnectTimeout(10000); // 10 segundos
        url_connect.setDoInput(true);
        url_connect.connect();
        try {
            in = new BufferedInputStream(url_connect.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();

        }

        return in;
    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null)
            return netInfo.isConnectedOrConnecting();
        return false;
//        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static boolean isConnectionWifiOnline(Context context) {
        boolean haveConnectedWifi = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null) {
            if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
                if (netInfo.isConnected()) {
                    haveConnectedWifi = true;
                }
            }
        } else haveConnectedWifi = false;

        return haveConnectedWifi;
    }

}