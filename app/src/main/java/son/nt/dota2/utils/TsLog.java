package son.nt.dota2.utils;

import android.util.Log;

public class TsLog {
    public static final int LOG_LEVEL_NONE = Integer.MAX_VALUE;
    public static final int LOG_LEVEL_DEBUG = 3;
    public static final int LOG_LEVEL_INFO = 4;
    public static final int LOG_LEVEL_VERBOSE = 2;
    public static final int LOG_LEVEL_WARN = 5;
    public static final int LOG_LEVEL_ERROR = 6;
//    public static int LOG_LEVEL = LOG_LEVEL_VERBOSE;
    public static int           LOG_LEVEL         = LOG_LEVEL_NONE;
    //    public static final int LOG_LEVEL = LOG_LEVEL_INFO; 

    private static final String LOG_DEFAULT_TAG = "";

    private String logTag = LOG_DEFAULT_TAG;

    public void setDismissLog() {
        LOG_LEVEL = LOG_LEVEL_NONE;
    }

    public TsLog(String logTag) {
        if (logTag != null) {
            this.logTag = logTag;
        }
    }

    public void d(String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_DEBUG) {
            if (msg == null) {
                msg = "null";
            }
            Log.d(logTag, msg);
        }
    }

    public void d(String msg, Throwable tr) {
        if (LOG_LEVEL <= LOG_LEVEL_DEBUG) {
            if (msg == null) {
                msg = "null";
            }
            if (tr == null) {
                d(msg);
                return;
            }
            Log.d(logTag, msg, tr);
        }
    }

    public void e(String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_ERROR) {
            if (msg == null) {
                msg = "null";
            }
            Log.e(logTag, msg);
        }
    }

    public void e(String msg, Throwable tr) {
        if (LOG_LEVEL <= LOG_LEVEL_ERROR) {
            if (msg == null) {
                msg = "null";
            }
            if (tr == null) {
                e(msg);
                return;
            }
            Log.e(logTag, msg, tr);
        }
    }

    public void i(String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_INFO) {
            if (msg == null) {
                msg = "null";
            }
            Log.i(logTag, msg);
        }
    }

    public void i(String msg, Throwable tr) {
        if (LOG_LEVEL <= LOG_LEVEL_INFO) {
            if (msg == null) {
                msg = "null";
            }
            if (tr == null) {
                i(msg);
                return;
            }
            Log.i(logTag, msg, tr);
        }
    }

    public void v(String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_VERBOSE) {
            if (msg == null) {
                msg = "null";
            }
            Log.v(logTag, msg);
        }
    }

    public void v(String msg, Throwable tr) {
        if (LOG_LEVEL <= LOG_LEVEL_VERBOSE) {
            if (msg == null) {
                msg = "null";
            }
            if (tr == null) {
                v(msg);
                return;
            }
            Log.v(logTag, msg, tr);
        }
    }

    public void w(String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_WARN) {
            if (msg == null) {
                msg = "null";
            }
            Log.w(logTag, msg);
        }
    }

    public void w(Throwable tr) {
        if (tr == null) {
            return;
        }
        if (LOG_LEVEL <= LOG_LEVEL_WARN) {
            Log.w(logTag, tr);
        }
    }

    public void w(String msg, Throwable tr) {
        if (LOG_LEVEL <= LOG_LEVEL_WARN) {
            if (msg == null) {
                msg = "null";
            }
            if (tr == null) {
                w(msg);
                return;
            }
            Log.w(logTag, msg, tr);
        }
    }

    //son 2012.09.11
    public void d(String tag, String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_DEBUG) {
            if (msg == null) {
                msg = "null";
            }
            Log.d(tag, msg);
        }
    }

    public void v(String tag, String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_ERROR) {
            if (msg == null) {
                msg = "null";
            }
            Log.v(tag, msg);
        }
    }

    public void e(String tag, String msg) {
        if (LOG_LEVEL <= LOG_LEVEL_ERROR) {
            if (msg == null) {
                msg = "null";
            }
            Log.e(tag, msg);
        }
    }

}