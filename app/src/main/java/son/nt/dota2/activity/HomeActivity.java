package son.nt.dota2.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseObject;
import com.squareup.otto.Subscribe;

import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.fragment.HomeFragment;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.SearchableFragment;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

public class HomeActivity extends AActivity implements HomeFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener, SearchableFragment.OnFragmentInteractionListener {

    public static final String TAG = "HomeActivity";
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initActionBar();
        initLayout();
        initListener();
        handleSearch(getIntent());

        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();

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

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_ll);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_clear, R.string.action_settings);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void initListener() {
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
            actionBarDrawerToggle.setDrawerIndicatorEnabled(false);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        } else {
            setTitle(getString(R.string.app_name));
            actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.action_search_hero);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint(getString(R.string.action_search_hero));
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
        showFragment(MainFragment.newInstance("", heroDto), true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleSearch(intent);
    }


}
