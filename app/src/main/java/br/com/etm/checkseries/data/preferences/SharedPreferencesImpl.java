package br.com.etm.checkseries.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import br.com.etm.checkseries.App;

/**
 * Created by eduardo on 13/01/18.
 */

public class SharedPreferencesImpl implements Preferences {

    private static final String KEY_FILTER_NOT_FINISHED = "filter_not_finished";
    private static final String KEY_FILTER_FAVOURITE = "filter_favourite";
    private static final String KEY_FILTER_HIDDEN = "filter_hidden";
    private static final String KEY_FILTER_NEXT_EPISODES = "filter_next_episodes";

    private static final String KEY_ORDER_NAME = "order_name";
    private static final String KEY_ORDER_NEXT_EPISODE = "order_next_episode";


    private void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            Log.d("Error getFromPrefs %s", e.getMessage());
            return defaultValue;
        }
    }

    private void saveBooleanToPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    private boolean getBooleanFromPrefs(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            Log.d("getBooleanFromPrefs %s", e.getMessage());
            return defaultValue;
        }
    }


    @Override
    public void setFilterUnfinished(boolean notFinished) {
        saveBooleanToPrefs(App.getContext(), KEY_FILTER_NOT_FINISHED, notFinished);
    }

    @Override
    public boolean isFilterUnfinished() {
        return getBooleanFromPrefs(App.getContext(), KEY_FILTER_NOT_FINISHED, false);
    }

    @Override
    public void setFilterFavourite(boolean favourite) {
        saveBooleanToPrefs(App.getContext(), KEY_FILTER_FAVOURITE, favourite);
    }

    @Override
    public boolean isFilterFavourite() {
        return getBooleanFromPrefs(App.getContext(), KEY_FILTER_FAVOURITE, false);
    }

    @Override
    public void setFilterHidden(boolean hidden) {
        saveBooleanToPrefs(App.getContext(), KEY_FILTER_HIDDEN, hidden);
    }

    @Override
    public boolean isFilterHidden() {
        return getBooleanFromPrefs(App.getContext(), KEY_FILTER_HIDDEN, false);
    }

    @Override
    public void setFilterNextEpisodes(boolean nextEpisodes) {
        saveBooleanToPrefs(App.getContext(), KEY_FILTER_NEXT_EPISODES, nextEpisodes);
    }

    @Override
    public boolean isFilterNextEpisodes() {
        return getBooleanFromPrefs(App.getContext(), KEY_FILTER_NEXT_EPISODES, false);
    }

    @Override
    public void setOrderName(boolean orderName) {
        saveBooleanToPrefs(App.getContext(), KEY_ORDER_NEXT_EPISODE, false);
        saveBooleanToPrefs(App.getContext(), KEY_ORDER_NAME, orderName);
    }

    @Override
    public void setOrderNextEpisode(boolean orderNextEpisode) {
        saveBooleanToPrefs(App.getContext(), KEY_ORDER_NAME, false);
        saveBooleanToPrefs(App.getContext(), KEY_ORDER_NEXT_EPISODE, orderNextEpisode);
    }

    @Override
    public boolean isOrderName() {
        return getBooleanFromPrefs(App.getContext(), KEY_ORDER_NAME, false);
    }

    @Override
    public boolean isOrderNextEpisode() {
        return getBooleanFromPrefs(App.getContext(), KEY_ORDER_NEXT_EPISODE, false);
    }

    @Override
    public void clearFilter() {
        setFilterHidden(false);
        setFilterFavourite(false);
        setFilterUnfinished(false);
        setFilterNextEpisodes(false);
    }
}
