package son.nt.dota2.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Sonnt on 7/8/15.
 */
public class OttoBus {
    public static final Bus EVENT_BUS = new Bus(ThreadEnforcer.ANY);
    private static Handler handler = new Handler(Looper.getMainLooper());


    public static void register(Object Object) {
        EVENT_BUS.register(Object);
    }

    public static void unRegister(Object Object) {
        EVENT_BUS.unregister(Object);
    }

    public static void post(final Object Object) {
        Looper myLooper = Looper.myLooper();
        Looper mainLooper = Looper.getMainLooper();
        if (myLooper != null && myLooper.equals(mainLooper)) {
            EVENT_BUS.post(Object);
        } else {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    EVENT_BUS.post(Object);
                }
            });
        }
    }

}
