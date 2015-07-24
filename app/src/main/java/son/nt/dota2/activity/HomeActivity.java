package son.nt.dota2.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.squareup.otto.Subscribe;

import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.fragment.AbilityFragment;
import son.nt.dota2.fragment.HomeFragment;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.RolesFragment;
import son.nt.dota2.fragment.SearchableFragment;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;

public class HomeActivity extends AActivity implements HomeFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, SearchableFragment.OnFragmentInteractionListener {

    public static final String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    SearchView searchView;
    MenuItem menuSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initActionBar();
        initLayout();
        initListener();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        handleSearch(getIntent());
        testAbilities();
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

        navigationView = (NavigationView) findViewById(R.id.home_navigation);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_ll);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_clear, R.string.action_settings);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initListener() {


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        TsGaTools.trackPages("/Home");
                        while (mFragmentTagStack.size() > 0) {
                            getSafeFragmentManager().popBackStackImmediate();
                        }
                        break;
                    case R.id.nav_roles:
                        TsGaTools.trackPages("/Roles");
                        showFragment(RolesFragment.newInstance("", ""), true);
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
                menuSearch.setVisible(false);
            }
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            if (menuSearch != null) {
                menuSearch.setVisible(true);
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
        if (mFragmentTagStack.size() > 0) {
            menuSearch.setVisible(false);
        } else {
            menuSearch.setVisible(true);
        }
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
//        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            getSafeFragmentManager().popBackStackImmediate();
//        }

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
    public void handleDataFromAdapterRcvHome(HeroDto heroDto) {
        Logger.debug(TAG, ">>>" + "handleDataFromAdapterRcvHome:" + heroDto.name);
//        showFragment(MainFragment.newInstance("", heroDto), true);
    }
    @Subscribe
    public void handleAbility(HeroEntry heroEntry) {
        Logger.debug(TAG, ">>>" + "handleAbility:" + heroEntry.name);
        showFragment(AbilityFragment.newInstance(heroEntry, ""), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }

    private void testAbilities () {
        HTTPParseUtils.getInstance().withHeroList();
    }


}
