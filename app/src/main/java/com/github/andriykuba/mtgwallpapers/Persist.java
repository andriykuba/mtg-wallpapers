package com.github.andriykuba.mtgwallpapers;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Date;

public class Persist {
    private final static String CURRENT_WALLPAPER_NAME = "current_wallpaper_name";
    private final static String CURRENT_WALLPAPER_SERIES = "current_wallpaper_series";
    private final static String CURRENT_WALLPAPER_DATE = "current_wallpaper_date";
    private final static String CURRENT_WALLPAPER_AUTHOR = "current_wallpaper_author";
    private final static String CURRENT_WALLPAPER_URL = "current_wallpaper_url";

    private static String SWITCH_BLUR = "switch_blur";
    private static String SWITCH_AUTOMATICALLY = "switch_automatically";

    final private Context context;

    public Persist(final Context context) {
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return context.getSharedPreferences("persist", Context.MODE_PRIVATE);
    }

    public void saveCurrent(final MetaData data) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putString(CURRENT_WALLPAPER_NAME, data.name);
        editor.putString(CURRENT_WALLPAPER_SERIES, data.series);
        editor.putLong(CURRENT_WALLPAPER_DATE, (data.date == null) ? 0 : data.date.getTime());
        editor.putString(CURRENT_WALLPAPER_AUTHOR, data.author);
        editor.putString(CURRENT_WALLPAPER_URL, data.url);
        editor.apply();
    }

    public MetaData getCurrentWallpaperMetadata() {
        final SharedPreferences preferences = getPreferences();

        final MetaData data = new MetaData();
        data.name = preferences.getString(CURRENT_WALLPAPER_NAME, "");
        data.series = preferences.getString(CURRENT_WALLPAPER_SERIES, "");
        data.date = new Date(preferences.getLong(CURRENT_WALLPAPER_DATE, 0));
        data.author = preferences.getString(CURRENT_WALLPAPER_AUTHOR, "");
        data.url = preferences.getString(CURRENT_WALLPAPER_URL, "");

        return data;
    }

    void saveSwitchBlur(final boolean value) {
        saveSwitch(SWITCH_BLUR, value);
    }

    public boolean isBlur() {
        return getSwitch(SWITCH_BLUR);
    }

    void saveSwitchAutomatically(final boolean value) {
        saveSwitch(SWITCH_AUTOMATICALLY, value);
    }

    public boolean isUpdateAutomatically() {
        return getSwitch(SWITCH_AUTOMATICALLY);
    }

    private void saveSwitch(final String id, final boolean value) {
        final SharedPreferences.Editor editor = getPreferences().edit();
        editor.putBoolean(id, value);
        editor.apply();
    }

    private boolean getSwitch(final String id) {
        return getPreferences().getBoolean(id, false);
    }
}
