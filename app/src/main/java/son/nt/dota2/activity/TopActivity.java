package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import son.nt.dota2.R;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.fragment.PlayListFragment;
import son.nt.dota2.fragment.TopFragment;
import son.nt.dota2.utils.TsGaTools;


public class TopActivity extends BaseFragmentActivity implements TopFragment.OnFragmentInteractionListener,
PlayListFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_top);
        adMob();
        TsGaTools.trackPages("/TopActivity");
        addTest();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_playlist:
                showFragment(new PlayListFragment(), true);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void addTest () {
        View view = findViewById(R.id.top_test);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(new PlayListFragment(), true);
            }
        });
    }
}
