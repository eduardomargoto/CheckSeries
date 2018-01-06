package br.com.etm.checkseries.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by eduardo on 06/01/18.
 */

public class Utils {

    public static void hideKeyboard(Context context, View view) {
        if (view != null && (context.getResources().getConfiguration().keyboardHidden == Configuration.KEYBOARDHIDDEN_NO)) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(Context context){
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
    }
}
