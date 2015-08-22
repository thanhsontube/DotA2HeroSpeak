package son.nt.dota2.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import son.nt.dota2.FacebookManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.customview.KenBurnsView;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.fragment.HomeFragment;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.RoleListFragment;
import son.nt.dota2.fragment.RolesFragment;
import son.nt.dota2.fragment.SavedFragment;
import son.nt.dota2.fragment.SearchableFragment;
import son.nt.dota2.gridmenu.GridMenuDialog;
import son.nt.dota2.ottobus_entry.GoAdapterRoles;
import son.nt.dota2.setting.SettingActivity;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsFeedback;
import son.nt.dota2.utils.TsGaTools;

public class HomeActivity extends AActivity implements HomeFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, SearchableFragment.OnFragmentInteractionListener, SavedFragment.OnFragmentInteractionListener {

    public static final String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    SearchView searchView;
    MenuItem menuSearch;
    KenBurnsView kenBurnsView;
    ImageView avatar;
    TextView txtFromName;
    TextView txtLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        OttoBus.register(this);
        initActionBar();
        initLayout();
        initListener();
        updateKensburn();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        handleSearch(getIntent());
        adMob();
        isAddMob();

    }

    public void isAddMob() {
        Logger.debug(TAG, ">>>" + "=====isAddMob");
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
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        getSafeActionBar().setHomeButtonEnabled(true);
        getSafeActionBar().setDisplayShowHomeEnabled(true);
        getSafeActionBar().setDisplayHomeAsUpEnabled(true);
        getSafeActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return HomeFragment.newInstance("", "");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.home_ll_main;
    }

    private void initLayout() {

        kenBurnsView = (KenBurnsView) findViewById(R.id.home_kenburns);


        navigationView = (NavigationView) findViewById(R.id.home_navigation);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_ll);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_clear, R.string.action_settings);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        avatar = (ImageView) findViewById(R.id.nav_avatar);
        txtFromName = (TextView) findViewById(R.id.nav_fromName);
        txtLogout = (TextView) findViewById(R.id.nav_logout);
        if (FacebookManager.getInstance().isLogin()) {
            try {
//                String link = String.format(MsConst.FB_AVATAR_LINK, FacebookManager.getInstance().getProfile().getId());
                String link = FacebookManager.getInstance().getLinkAvatar();
                Glide.with(this).load(link)
                        .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
                txtFromName.setText(FacebookManager.getInstance().getProfile().getName());
                txtLogout.setVisibility(View.VISIBLE);
            } catch (Exception e) {

            }

        } else {
            txtFromName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(LoginActivity.getIntent(HomeActivity.this));
                }
            });
            txtLogout.setVisibility(View.GONE);
        }

    }

    Handler handler = new Handler();

    private void initListener() {

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookManager.getInstance().logout();
                startActivity(LoginActivity.getIntent(HomeActivity.this));
                finish();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(final MenuItem menuItem) {

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawers();
                        menuItem.setChecked(true);
                    }
                }, 250L);
                updateKensburn();
                FragmentManager fm = getSafeFragmentManager();
                Fragment f = null;

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        TsGaTools.trackPages("/Home");
                        while (mFragmentTagStack.size() > 0) {
                            getSafeFragmentManager().popBackStackImmediate();
                        }
                        break;
                    case R.id.nav_roles:
                        TsGaTools.trackPages("/nav_roles");
                        if (mFragmentTagStack.size() > 0) {
                            //check if current position is same as new position, do not reload fragment
                            Fragment fTop = fm.findFragmentByTag(mFragmentTagStack.peek());
                            if (!(fTop instanceof RolesFragment)) {
                                while (mFragmentTagStack.size() > 0) {
                                    getSupportFragmentManager().popBackStackImmediate();
                                }
                                f = RolesFragment.newInstance("", "");
                                showFragment(f, false);
                            }
                        } else {
                            f = RolesFragment.newInstance("", "");
                            showFragment(f, false);
                        }


                        break;
                    case R.id.nav_playlist:
                        TsGaTools.trackPages("/nav_playlist");
                        if (mFragmentTagStack.size() > 0) {
                            //check if current position is same as new position, do not reload fragment
                            Fragment fTop = fm.findFragmentByTag(mFragmentTagStack.peek());
                            if (!(fTop instanceof SavedFragment)) {
                                while (mFragmentTagStack.size() > 0) {
                                    getSupportFragmentManager().popBackStackImmediate();
                                }
                                f = SavedFragment.newInstance("", "");
                                showFragment(f, true);
                            }
                        } else {
                            f = SavedFragment.newInstance("", "");
                            showFragment(f, true);
                        }
                        break;
                    case R.id.nav_rate:
                        TsGaTools.trackPages("/nav_rate");
                        TsFeedback.rating(HomeActivity.this);

                        break;
                    case R.id.nav_share:
                        TsGaTools.trackPages("/nav_share");
                        TsFeedback.shareApp(HomeActivity.this);
                        break;
                    case R.id.nav_share_fb:
                        TsFeedback.likePage(HomeActivity.this, MsConst.FB_PAGE_ID);
                        break;
                    case R.id.nav_settings:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));

                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentTagStack.size() > 0) {
                    Fragment fTop = getSafeFragmentManager().findFragmentByTag(mFragmentTagStack.peek());
                    //need to list all Fragment Called from menu to enable menu icon
                    if (fTop instanceof RolesFragment || fTop instanceof SavedFragment) {

                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {

                        getSafeFragmentManager().popBackStackImmediate();
                    }

                } else {
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackStackChanged() {
        super.onBackStackChanged();
        if (mFragmentTagStack.size() > 0) {

            Fragment fTop = getSafeFragmentManager().findFragmentByTag(mFragmentTagStack.peek());
            //need to list all Fragment Called from menu to enable menu icon
            if (fTop instanceof RolesFragment || fTop instanceof SavedFragment) {
                actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            } else {
                actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

            }

        } else {
            setTitle(getString(R.string.app_name));
//            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        menuSearch = menu.findItem(R.id.action_search_hero);
        searchView = (SearchView) menuSearch.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.action_search_hero));
//        }
        return true;
    }

    private void handleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Logger.debug(TAG, ">>>" + "handleSearch:" + query);

            if (mFragmentTagStack.size() == 0) {
                super.onPostResume();
                showFragment(SearchableFragment.newInstance(query, ""), true);
            } else {
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(mFragmentTagStack.peek());
                if (fragment != null && fragment instanceof SearchableFragment) {
                    //update data
                    Logger.debug(TAG, ">>>" + "update data");
                    ((SearchableFragment) fragment).doSearch(query);
                } else {

                    showFragment(SearchableFragment.newInstance(query, ""), true);
                }
            }
        }
        ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        OttoBus.register(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        OttoBus.unRegister(this);
//    }

    @Subscribe
    public void handleAbility(HeroEntry heroEntry) {
        Logger.debug(TAG, ">>>" + "=========handleAbility:" + heroEntry.name);
        Intent intent = new Intent(this, HeroActivity.class);
        intent.putExtra(MsConst.EXTRA_HERO, heroEntry);
        startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }

    @Override
    public void onSavedItemLongClick(SpeakDto dto) {

        FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
        Fragment f = getSafeFragmentManager().findFragmentByTag("long-click");
        if (f != null) {
            ft.remove(f);
        }
        GridMenuDialog dialog = GridMenuDialog.newInstance(dto);
        ft.add(dialog, "long-click");
        ft.commit();
    }

    private void updateKensburn() {
        if (ResourceManager.getInstance().listKenburns.size() > 0) {
            kenBurnsView.setResourceUrl(ResourceManager.getInstance().listKenburns);
            kenBurnsView.startLayoutAnimation();
        }

    }


    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Subscribe
    public void fromAdapterRoles(GoAdapterRoles dto) {
        showFragment(RoleListFragment.newInstance(dto.role), true);
    }
}
