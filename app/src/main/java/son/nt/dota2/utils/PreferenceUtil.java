package son.nt.dota2.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceUtil {

    private PreferenceUtil() {
    };

    public static String getPreference(Context context, String key, String defValue) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }
        if (defValue == null) {
            throw new IllegalArgumentException("'defValue' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, defValue);
    }

    public static int getPreference(Context context, String key, int defValue) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getInt(key, defValue);
    }

    public static boolean getPreference(Context context, String key, boolean defValue) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(key, defValue);
    }

    public static boolean setPreference(Context context, String key, String value) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }
        if (value == null) {
            throw new IllegalArgumentException("'value' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean setPreference(Context context, String key, int value) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public static boolean setPreference(Context context, String key, boolean value) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static void clearPreferences(Context context) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.clear().commit();
    }

    public static boolean setPreference(Context context, String key, long value) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor editor = preferences.edit();
        editor.putLong(key, value);
        return editor.commit();
    }

    public static long getPreference(Context context, String key, long defValue) {

        if (context == null) {
            throw new IllegalArgumentException("'context' must not be null.");
        }
        if (key == null) {
            throw new IllegalArgumentException("'key' must not be null.");
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getLong(key, defValue);
    }

    /*
     * remove shred pre with Key
     */
    public static Boolean remove(Context context, String key) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        Editor edit = sharedPreferences.edit();
        return edit.remove(key).commit();
    }

}
