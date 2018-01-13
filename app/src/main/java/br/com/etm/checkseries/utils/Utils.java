package br.com.etm.checkseries.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import br.com.etm.checkseries.R;
import br.com.etm.checkseries.api.data.tracktv.ApiShow;

/**
 * Created by eduardo on 06/01/18.
 */

public class Utils {

    public static void createContextMenu(Activity activity, ApiShow apiShow, ContextMenu menu) {
        MenuInflater inflater = activity.getMenuInflater();
        inflater.inflate(R.menu.menu_options_serie, menu);

        menu.setHeaderTitle(apiShow.getTitle());
        if (apiShow.isHidden())
            menu.getItem(3).setTitle(R.string.app_it_serie_show);

    }

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
