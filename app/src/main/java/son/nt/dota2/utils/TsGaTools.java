package son.nt.dota2.utils;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Google Analytics V4
 *
 * @author Sonnt
 */
public class TsGaTools {

    private static FirebaseAnalytics mFirebaseAnalytics;

    /**
     * the constructor, should put it in Application
     *
     * @param context
     */

    public static void createInstance(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);


    }


    public static void trackPages(String screenName) {
        if (mFirebaseAnalytics == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("ScreenName", screenName);
        mFirebaseAnalytics.logEvent("Dota2", bundle);
    }
    public static void trackNav(String screenName) {
        if (mFirebaseAnalytics == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("Navigation", screenName);
        mFirebaseAnalytics.logEvent("NAV", bundle);
    }

    public static void trackHero(String screenName) {
        if (mFirebaseAnalytics == null) {
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("HeroSelected", screenName);
        mFirebaseAnalytics.logEvent("HEROES", bundle);
    }
}
