package com.onjyb.util;

import android.content.Context;
import android.content.SharedPreferences.Editor;

public class Preference {
    private static Context context;
    private static String prefFile = "onjyb_pref";

    public static String getSharedPref(String key, String defaultValue) {
        return context.getSharedPreferences(prefFile, 0).getString(key, defaultValue);
    }

    public static void setSharedPref(String key, String value) {
        Editor editor = context.getSharedPreferences(prefFile, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void clearSharedPrefFile() {
        Editor editor = context.getSharedPreferences(prefFile, 0).edit();
        editor.clear();
        editor.commit();
    }

    public static void setAppContext(Context context1) {
        context = context1;
    }
}
