package son.nt.dota2.utils;

import android.util.Log;

/**
 * Created by Sonnt on 4/26/15.
 */
public final class Logger {
    public static boolean DEBUG = true;

    public static void setDEBUG (boolean isDebug) {
        DEBUG = isDebug;
    }

    private Logger() {
        // enforcing singleton
        super();
    }

    /**
     * Convenience method.
     */
    public static void debug(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.d(tag, "" + msg);
        }
    }

    /**
     * Convenience method.
     */
    public static void info(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.i(tag, "" + msg);
        }
    }

    /**
     * Convenience method.
     */
    public static void warn(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.w(tag, "" + msg);
        }

    }

    /**
     * Convenience method.
     */
    public static void error(final String tag, final String msg) {
        if (Logger.DEBUG) {
            Log.e(tag, "" + msg);
        }

    }

    public static void logException(final Throwable throwable) {
        if (Logger.DEBUG) {
            throwable.printStackTrace();
        }

    }

}
