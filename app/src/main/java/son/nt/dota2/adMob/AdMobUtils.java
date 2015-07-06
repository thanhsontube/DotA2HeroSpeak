package son.nt.dota2.adMob;

import android.view.View;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import son.nt.dota2.R;

/**
 * Created by Sonnt on 7/6/15.
 */
public class AdMobUtils {

    static View viewInstance = null;

    public static void init(View view, int adMobID) {
        //ad mob
        viewInstance = view;
        AdView mAdView = (AdView) view.findViewById(adMobID);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(view.getContext().getString(R.string.ad_mob_test_device)).build();
        mAdView.loadAd(adRequest);
    }

    public static void hide() {
        if (viewInstance != null) {
            viewInstance.setVisibility(View.GONE);
        }
    }

    public static void show() {
        if (viewInstance != null) {
            viewInstance.setVisibility(View.VISIBLE);

        }
    }
}
