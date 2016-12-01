package son.nt.dota2.activity;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import son.nt.dota2.FacebookManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.adapter.AdapterPagerHero;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.base.HeroTabFragment;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.CircleFeatureDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.gridmenu.CommentDialog;
import son.nt.dota2.gridmenu.GridMenuDialog;
import son.nt.dota2.gridmenu.SpeakLongClick;
import son.nt.dota2.hero.AdapterCircleFeature;
import son.nt.dota2.hero.HeroActivityPresenter;
import son.nt.dota2.hero.HeroContract;
import son.nt.dota2.ottobus_entry.GoLoginDto;
import son.nt.dota2.ottobus_entry.GoShare;
import son.nt.dota2.service.PlayService2;
import son.nt.dota2.utils.OttoBus;

/**
 * * Get HeroBasicDto from heroID -> update kenburns and get heroGroup,
 * Then get List <HeroBasicDto> based on heroGroup
 */
public class HeroActivity extends BaseActivity implements HeroContract.View {

    HeroContract.Presenter mPresenter;
    HeroEntry heroEntry;
    FloatingActionButton fabChat;

    private String mHeroId;

    @BindView(R.id.viewpager)
    ViewPager mViewPager;

    @BindView(R.id.tabs)
    TabLayout mTabLayout;

    @BindView(R.id.home_kenburns)
    KenBurnsView2 mKenBurnsView;

    /**
     * the recyclerView contains : response / hero skill / comments / bio ....
     */
    @BindView(R.id.rcv_feature)
    RecyclerView mRecyclerViewFeature;
    private AdapterCircleFeature mAdapterCircleFeature;

    private AdapterPagerHero mAdapter;

    IHeroRepository mRepository;


    private EditText mSearchText;
    private View mClearButton;

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


    public static void startActivity(Context context, String heroID) {
        Intent intent = new Intent(context, HeroActivity.class);
        intent.putExtra("data", heroID);
        context.startActivity(intent);
    }

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_hero;
    }

    private List<CircleFeatureDto> createListCircle() {
        List<CircleFeatureDto> featureDtos = new ArrayList<>();
        featureDtos.add(new CircleFeatureDto("Sound", R.drawable.ability_icon, true));
        featureDtos.add(new CircleFeatureDto("Skill", R.drawable.ability_icon, false));
        featureDtos.add(new CircleFeatureDto("Bio", R.drawable.ability_icon, false));
        featureDtos.add(new CircleFeatureDto("Comments", R.drawable.ability_icon, false));
        return featureDtos;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        final String heroID = getIntent().getStringExtra("data");
        mPresenter.fetchHero(heroID);

//        heroEntry = (HeroEntry) getIntent().getExtras().getSerializable(MsConst.EXTRA_HERO);
//        mHeroId = getIntent().getExtras().getString("data");
//        fabChat = (FloatingActionButton) findViewById(R.id.btn_chat_hero);
//        fabChat.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TsGaTools.trackPages(MsConst.TRACK_CHAT);
//                FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
//                Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
//                if (f != null) {
//                    ft.remove(f);
//                }
//                ChatDialog dialog = ChatDialog.newInstance();
//                ft.add(dialog, "chat");
//                ft.commit();
//            }
//        });
        OttoBus.register(this);
        adMob();
//        isAddMob();

    }

    public void isAddMob() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Admob");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e != null) {
                    return;
                }
                String enable = parseObject.getString("isEnable");
                if (enable.equals("off")) {
                    AdMobUtils.hide();
                } else {
                    AdMobUtils.show();
                }
            }
        });

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


    @Subscribe
    public void voiceLongItemClick(SpeakLongClick dto) {
        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
        Fragment f = getSafeFragmentManager().findFragmentByTag("long-click");
        if (f != null) {
            ft.remove(f);
        }
        GridMenuDialog dialog = GridMenuDialog.newInstance(dto.speakDto);
        ft.add(dialog, "long-click");
        ft.commit();
    }

    @Subscribe
    public void goLogin(GoLoginDto dto) {
        if (!dto.isLogin) {

            startActivity(LoginActivity.getIntent(HeroActivity.this));
        } else {
            FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
            Fragment f = getSafeFragmentManager().findFragmentByTag("cmt");
            if (f != null) {
                ft.remove(f);
            }
            CommentDialog dialog = CommentDialog.createInstance(dto.speakDto);
            ft.add(dialog, "cmt");
            ft.commit();
        }
    }

    @Subscribe
    public void goShare(GoShare dto) {
        if (dto.type.equals("facebook")) {
            FacebookManager.getInstance().shareViaDialogFb(HeroActivity.this, dto.speakDto);
        }
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

        }

        @Override
        public void onPageSelected(int position) {
            mPresenter.setSelectedPage(position);

            final int selected = mViewPager.getCurrentItem();
            Fragment fragment = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":"
                    + selected);
            if (fragment != null) {

                ((HeroTabFragment) fragment).onPageSelected();
            }


        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void setSoundsList(List<? extends ISound> sounds) {
        mPlayService.setSoundsSource(MsConst.TYPE_HERO_SOUND, sounds);
    }

    //    @Subscribe
//    public void goChatDialog (CommentDto dto) {
//        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
//        Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
//        if (f != null) {
//            ft.remove(f);
//        }
//        ChatDialog dialog = ChatDialog.newInstance();
//        ft.add(dialog, "chat");
//        ft.commit();
//    }

    @Subscribe
    public void getCircleClick(CircleFeatureDto circleFeatureDto) {
        tab = circleFeatureDto.getName();

    }

    public String tab= "Sound";


}
