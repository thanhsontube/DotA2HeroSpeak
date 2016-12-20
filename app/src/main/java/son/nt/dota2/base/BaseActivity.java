package son.nt.dota2.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import butterknife.ButterKnife;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.customview.KenBurnsView2;
import timber.log.Timber;

/**
 * Created by sonnt on 11/6/16.
 */

public abstract class BaseActivity extends AppCompatActivity {

    KenBurnsView2 kenBurnsView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayoutResID());
        ButterKnife.bind(this);
        kenBurnsView = (KenBurnsView2) findViewById(R.id.home_kenburns);
        Timber.d(">>> onCreate:" + getClass().getSimpleName());
        updateKensburn();
        adMob();

    }

    /**
     * provide the the xml layout
     */
    protected abstract int provideLayoutResID();

    private void updateKensburn() {
        if (kenBurnsView == null) {
            Timber.e(">>>" + "\"This activity has not include KenBurn view\"");
            return;
        }
        if (ResourceManager.getInstance() == null) {
            kenBurnsView.setResourceUrl("http://cdn.dota2.com/apps/dota2/images/comics/comic_monkeyking/en/001.png", false);
            return;
        }

        if (ResourceManager.getInstance().listKenburns.size() > 0) {
            kenBurnsView.setResourceUrl(ResourceManager.getInstance().listKenburns);
            kenBurnsView.startLayoutAnimation();
        } else {
            kenBurnsView.setResourceUrl("http://cdn.dota2.com/apps/dota2/images/comics/comic_monkeyking/en/001.png", false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void adMob() {
        AdMobUtils.init(findViewById(R.id.ll_ads), R.id.adView);
    }
}
