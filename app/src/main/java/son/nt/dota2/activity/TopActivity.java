package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import son.nt.dota2.R;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.fragment.TopFragment;
import son.nt.dota2.utils.TsGaTools;


public class TopActivity extends BaseFragmentActivity implements TopFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_top);
        adMob();
        TsGaTools.trackPages("/TopActivity");
    }


    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return TopFragment.newInstance("", "");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.ll_main;
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    private void adMob() {
        //ad mob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("C5C5650D2E6510CF583E2D3D94B69B57")
//                .addTestDevice("224370EA280CB464C7C922F369F77C69")
                .build();

        //my s3
        mAdView.loadAd(adRequest);
    }
}
