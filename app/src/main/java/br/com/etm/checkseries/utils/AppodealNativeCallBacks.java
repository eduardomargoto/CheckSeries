package br.com.etm.checkseries.utils;

/**
 * Created by EDUARDO_MARGOTO on 17/05/2016.
 */

import android.app.Activity;
import android.util.Log;

import com.appodeal.ads.NativeAd;
import com.appodeal.ads.NativeCallbacks;

import java.util.List;

public class AppodealNativeCallBacks implements NativeCallbacks {
    private final Activity mActivity;


    public AppodealNativeCallBacks(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onNativeLoaded(List<NativeAd> nativeAds) {
        Log.i("NATIVEADS", "onNativeLoaded - " + nativeAds.size());
        boolean existe = false;
        /*for (NativeAd nativeAd : nativeAds) {
            for (NativeAd ad : MainActivity.mAds) {
                if (nativeAd.getTitle().equals(ad.getTitle())) {
                    existe = true;
                }
            }
            if (!existe) {
                MainActivity.mAds.add(nativeAd);
                existe = false;
            }


        }
        MainActivity mainActivity = (MainActivity) mActivity;
        MainActivity.updateFragments(mainActivity.getTabsAdapter(), mainActivity.getViewPager());
        Log.i("NATIVEADS", "mAds - " + MainActivity.mAds.size());*/
    }

    @Override
    public void onNativeFailedToLoad() {
        Log.i("NATIVEADS", "onNativeFailedToLoad");
//        Utils.showToast(mActivity, "onNativeFailedToLoad");
    }

    @Override
    public void onNativeShown(NativeAd nativeAd) {
        Log.i("NATIVEADS", "onNativeShown");
//        Utils.showToast(mActivity, "onNativeShown");
    }

    @Override
    public void onNativeClicked(NativeAd nativeAd) {
        Log.i("NATIVEADS", "onNativeClicked");
    }

}
