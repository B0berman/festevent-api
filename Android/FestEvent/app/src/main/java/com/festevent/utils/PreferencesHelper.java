package com.festevent.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by walbecq on 14/02/17.
 */

public class PreferencesHelper {

    public static final String LANGUAGE_PREF = "lang_pref";

    public static void savePreferences(String id, String param, Context ctx) throws IOException {
        SharedPreferences sharedPref = ctx.getSharedPreferences("shared_pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(id, param);
        editor.commit();
    }

    public static String getPreferences(Context ctx, String pref) throws IOException, ClassNotFoundException {
        SharedPreferences sharedPref = ctx.getSharedPreferences("shared_pref", MODE_PRIVATE);
        String toget = sharedPref.getString(pref, "default");

        if (toget.compareTo("default") == 0) {
            return null;
        }
        else
            return toget;
    }

}
