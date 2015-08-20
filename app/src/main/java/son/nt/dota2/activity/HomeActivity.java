package son.nt.dota2.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
import com.facebook.share.widget.LikeView;
import com.squareup.otto.Subscribe;

import son.nt.dota2.FacebookManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.customview.KenBurnsView;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.fragment.HomeFragment;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.RolesFragment;
import son.nt.dota2.fragment.SavedFragment;
import son.nt.dota2.fragment.SearchableFragment;
import son.nt.dota2.gridmenu.GridMenuDialog;
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
    LikeView likeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initActionBar();
        initLayout();
        initListener();
        updateKensburn();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        handleSearch(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        if (FacebookManager.getInstance().isLogin()) {
            Glide.with(this).load(FacebookManager.getInstance().getProfile().getProfilePictureUri(100,100).toString())
                    .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
            txtFromName.setText(FacebookManager.getInstance().getProfile().getName());
        } else {
            txtFromName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(LoginActivity.getIntent(HomeActivity.this));
                }
            });
        }

        likeView = (LikeView) findViewById(R.id.nav_likeview);
        likeView.setObjectIdAndType(MsConst.FB_ID_POST_TO, LikeView.ObjectType.PAGE);
        likeView.setLikeViewStyle(LikeView.Style.BOX_COUNT);


    }

    private void initListener() {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                updateKensburn();

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        TsGaTools.trackPages("/Home");
                        while (mFragmentTagStack.size() > 0) {
                            getSafeFragmentManager().popBackStackImmediate();
                        }
                        break;
                    case R.id.nav_roles:
                        TsGaTools.trackPages("/nav_roles");
                        showFragment(RolesFragment.newInstance("", ""), true);
                        break;
                    case R.id.nav_playlist:
                        TsGaTools.trackPages("/nav_playlist");
                        showFragment(SavedFragment.newInstance("", ""), true);
                        break;
                    case R.id.nav_rate:
                        TsGaTools.trackPages("/nav_rate");
                        TsFeedback.rating(HomeActivity.this);

                        break;
                    case R.id.nav_share:
                        TsGaTools.trackPages("/nav_share");
                        TsFeedback.shareApp(HomeActivity.this);

                        break;

                }
                return true;
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFragmentTagStack.size() > 0) {
                    getSafeFragmentManager().popBackStackImmediate();
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
            if (menuSearch != null) {
//                menuSearch.setVisible(false);
            }
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            if (menuSearch != null) {
//                menuSearch.setVisible(true);
            }
            setTitle(getString(R.string.app_name));
            navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
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
//        if (mFragmentTagStack.size() > 0) {
//            menuSearch.setVisible(false);
//        } else {
//            menuSearch.setVisible(true);
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

    @Override
    protected void onResume() {
        super.onResume();
        OttoBus.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        OttoBus.unRegister(this);
    }

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

    private void updateKensburn () {
        if (ResourceManager.getInstance().listKenburns.size() > 0) {
            kenBurnsView.setResourceUrl(ResourceManager.getInstance().listKenburns);
            kenBurnsView.startLayoutAnimation();
        }

    }
}
