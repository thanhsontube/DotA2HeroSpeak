package son.nt.dota2.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import son.nt.dota2.R;

/**
 * Google Analytics V4
 * 
 * @author Sonnt
 *
 */
public class TsGaTools {

	static TsGaTools tsGaToolsInstance = null;
	static Tracker instance = null;
	public Context context;

	/**
	 * the constructor, should put it in Application
	 * 
	 * @param context
	 */

	public static void createInstance(Context context) {
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(context);
		instance = analytics.newTracker(context.getString(R.string.ga_id));
		instance.enableAdvertisingIdCollection(true);
		tsGaToolsInstance = new TsGaTools(context);
	}

	public TsGaTools(Context context) {
		this.context = context;
	}

	public static Tracker getInstance() {
		return instance;
	}

	public static void trackPages(String screenName) {
		if (getInstance() == null) {
			Log.e("ERROR",
					">>>ERROR TsGaTools : you do not create a constructor in Application");
			return;
		}
		getInstance().setScreenName(screenName);
		getInstance().send(new HitBuilders.AppViewBuilder().build());
	}
}
