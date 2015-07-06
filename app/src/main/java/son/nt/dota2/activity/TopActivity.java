package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;

import son.nt.dota2.BuildConfig;
import son.nt.dota2.R;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.fragment.PlayListFragment;
import son.nt.dota2.fragment.TopFragment;
import son.nt.dota2.utils.TsGaTools;


public class TopActivity extends BaseFragmentActivity implements TopFragment.OnFragmentInteractionListener,
        PlayListFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_top);
        adMob();
        TsGaTools.trackPages("/TopActivity");
    }

    private void adMob() {
        AdMobUtils.init(findViewById(R.id.ll_ads), R.id.adView);
        if (BuildConfig.DEBUG) {
            AdMobUtils.hide();
        } else {
            AdMobUtils.show();
        }

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

}
