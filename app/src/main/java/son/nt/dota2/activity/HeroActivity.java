package son.nt.dota2.activity;

import com.squareup.otto.Subscribe;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.adapter.AdapterPagerHero;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.base.HeroTabFragment;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.CircleFeatureDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.hero.AdapterCircleFeature;
import son.nt.dota2.hero.HeroActivityPresenter;
import son.nt.dota2.hero.HeroContract;
import son.nt.dota2.ottobus_entry.GoCircle;
import son.nt.dota2.service.PlayService2;
import son.nt.dota2.utils.OttoBus;

/**
 * * Get HeroBasicDto from heroID -> update kenburns and get heroGroup,
 * Then get List <HeroBasicDto> based on heroGroup (for A pager with full hero in a group)
 */
public class HeroActivity extends BaseActivity implements HeroContract.View {

    HeroContract.Presenter mPresenter;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.home_kenburns)
    KenBurnsView2 mKenBurnsView;

    @BindView(R.id.search_view)
    EditText mSearchView;

    /**
     * the recyclerView contains : response / hero skill / comments / bio ....
     */
    @BindView(R.id.rcv_feature)
    RecyclerView mRecyclerViewFeature;

    private AdapterCircleFeature mAdapterCircleFeature;

    private AdapterPagerHero mAdapter;

    public String tab = "Sound";

    PlayService2 mPlayService;

    ServiceConnection serviceConnectionMedia = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService2.LocalBinder binder = (PlayService2.LocalBinder) service;
            mPlayService = binder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mPlayService.releaseMediaPlayer();
            mPlayService = null;
        }
    };

    public static void startActivity(Context context, String selectedHero) {
        Intent intent = new Intent(context, HeroActivity.class);
        intent.putExtra("data", selectedHero);
        context.startActivity(intent);
    }

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_hero;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        mPresenter = new HeroActivityPresenter(this, new HeroRepository());
        bindService(PlayService2.getIntentService(this), serviceConnectionMedia, Service.BIND_AUTO_CREATE);

        mAdapter = new AdapterPagerHero(getSafeFragmentManager(), Collections.emptyList());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);

        mViewPager.addOnPageChangeListener(mChangeListener);

        //circle
        mAdapterCircleFeature = new AdapterCircleFeature(this, createListCircle());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewFeature.setLayoutManager(linearLayoutManager);
        mRecyclerViewFeature.setHasFixedSize(true);
        mRecyclerViewFeature.setAdapter(mAdapterCircleFeature);

        mSearchView.addTextChangedListener(mTextWatcher );

        final String selectedHero = getIntent().getStringExtra("data");
        mPresenter.setSelectedHeroId(selectedHero);
        mPresenter.getDataToUpdateView();

        OttoBus.register(this);
        adMob();
//        isAddMob();
    }

    private void setupToolbar() {
        setSupportActionBar(ButterKnife.findById(this, R.id.home_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            searchSound(s.toString());

        }
    };

    private void searchSound (String keyword) {
        final int selected = mViewPager.getCurrentItem();
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + selected);
        if (fragment != null) {

            ((HeroTabFragment) fragment).searchSound (keyword);
        }
    }



    private List<CircleFeatureDto> createListCircle() {
        List<CircleFeatureDto> featureDtos = new ArrayList<>();
        featureDtos.add(new CircleFeatureDto("Sound", R.drawable.ic_sound, true));
        featureDtos.add(new CircleFeatureDto("Skill", R.drawable.ic_abi, false));
//        featureDtos.add(new CircleFeatureDto("Bio", R.drawable.ability_icon, false));
//        featureDtos.add(new CircleFeatureDto("Comments", R.drawable.ability_icon, false));
        return featureDtos;
    }


    public void isAddMob() {
        AdMobUtils.show();
    }

    private void adMob() {
        AdMobUtils.init(findViewById(R.id.ll_ads), R.id.adView);
        AdMobUtils.hide();
    }

    @Override
    protected void onDestroy() {
        if (mPlayService != null) {
            unbindService(serviceConnectionMedia);
            mPlayService = null;
        }
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hero, menu);
        return true;
    }


    FragmentManager getSafeFragmentManager() {
        return getSupportFragmentManager();
    }

    @Override
    public void showHeroList(List<HeroBasicDto> list, int pos) {
        mAdapter.updateData(list);
        mViewPager.setCurrentItem(pos, true);
    }

    @Override
    public void showKenBurns(String bgLink) {
        mKenBurnsView.setResourceUrl(bgLink, 4);
    }

    ViewPager.OnPageChangeListener mChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //NOTHING
        }

        @Override
        public void onPageSelected(int position) {
            mPresenter.setSelectedPage(position);

            final int selected = mViewPager.getCurrentItem();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + selected);
            if (fragment != null) {

                ((HeroTabFragment) fragment).onPageSelected();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //NOTHING

        }
    };

    public void setSoundsList(List<? extends ISound> sounds) {
        mPlayService.setSoundsSource(MsConst.TYPE_HERO_SOUND, sounds);
    }

    @Subscribe
    public void getCircleClick(GoCircle mGoCircle) {
        tab = mGoCircle.mCircleFeatureDto.getName();
    }
}
