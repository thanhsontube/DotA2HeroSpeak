package son.nt.dota2.activity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.BuildConfig;
import son.nt.dota2.HeroManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.firebase.FireBaseUtils;
import son.nt.dota2.fragment.HomeFragment;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.RoleListFragment;
import son.nt.dota2.fragment.RolesFragment;
import son.nt.dota2.fragment.SavedFragment;
import son.nt.dota2.fragment.SearchableFragment;
import son.nt.dota2.gridmenu.GridMenuDialog;
import son.nt.dota2.musicPack.MusicPackListActivity;
import son.nt.dota2.ottobus_entry.GoAdapterRoles;
import son.nt.dota2.setting.SettingActivity;
import son.nt.dota2.story.story_list.StoryListActivity;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsFeedback;
import son.nt.dota2.utils.TsGaTools;

/**
 * This class is called after login.
 */

public class HomeActivity extends AActivity implements
        HomeFragment.OnFragmentInteractionListener,
        MainFragment.OnFragmentInteractionListener,
        SearchableFragment.OnFragmentInteractionListener,
        SavedFragment.OnFragmentInteractionListener {

    public static final String TAG = HomeActivity.class.getSimpleName();
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    SearchView searchView;
    MenuItem menuSearch;
    ImageView avatar;
    TextView txtFromName;
    TextView txtLogout;
    Handler handler = new Handler();

    KenBurnsView2 kenBurnsView;

    //    @Inject
    FirebaseAuth mFirebaseAuth;
    //    @Inject
    FirebaseUser mFirebaseUser;

    //    @Inject
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        OttoBus.register(this);
        initGoogleAccount();
        initActionBar();
        initLayout();
        initListener();
        updateKensburn();
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        handleSearch(getIntent());
        adMob();
        if (!BuildConfig.DEBUG) {
            isAddMob();
        }


        //// TODO: 10/11/16
//        new GetFromParse().execute();

    }

    private void initGoogleAccount() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,
                        null /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
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
        return HomeFragment.newInstance();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.home_ll_main;
    }

    private void initLayout() {

        kenBurnsView = (KenBurnsView2) findViewById(R.id.home_kenburns);
        navigationView = (NavigationView) findViewById(R.id.home_navigation);

        drawerLayout = (DrawerLayout) findViewById(R.id.home_drawer_ll);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.action_clear, R.string.action_settings);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        avatar = (ImageView) headerView.findViewById(R.id.nav_avatar);
        txtFromName = (TextView) headerView.findViewById(R.id.nav_fromName);
        txtLogout = (TextView) headerView.findViewById(R.id.nav_logout);

        if (mFirebaseUser == null) {
            txtFromName.setText("Login");
            txtLogout.setVisibility(View.GONE);
            txtFromName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(LoginActivity.getIntent(HomeActivity.this));
                    finish();
                }
            });
        } else {
            txtLogout.setVisibility(View.VISIBLE);
            txtFromName.setText(mFirebaseUser.getDisplayName());
            Glide.with(this).load(mFirebaseUser.getPhotoUrl())
                    .fitCenter().diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
            txtLogout.setOnClickListener(view -> {
                mFirebaseAuth.signOut();
                startActivity(LoginActivity.getIntent(HomeActivity.this));
                finish();
            });
        }


    }


    private void initListener() {

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
                        TsGaTools.trackNav("/Home");
                        while (mFragmentTagStack.size() > 0) {
                            getSafeFragmentManager().popBackStackImmediate();
                        }
                        break;
                    case R.id.nav_story: {
                        TsGaTools.trackNav("/nav_story");
                        startActivity(new Intent(getApplicationContext(), StoryListActivity.class));
                        break;
                    }

                    case R.id.nav_music_packs: {
                        TsGaTools.trackNav("/nav_music_packs");
                        startActivity(new Intent(getApplicationContext(), MusicPackListActivity.class));
                        break;
                    }
                    case R.id.nav_roles:
                        TsGaTools.trackNav("/nav_roles");
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
                        TsGaTools.trackNav("/nav_playlist");
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
                        TsGaTools.trackNav("/nav_rate");
                        TsFeedback.rating(HomeActivity.this);

                        break;
                    case R.id.nav_share:
                        TsGaTools.trackNav("/nav_share");
                        TsFeedback.shareApp(HomeActivity.this);
                        break;
                    case R.id.nav_share_fb:
                        TsGaTools.trackNav("/nav_fb");
                        TsFeedback.likePage(HomeActivity.this, MsConst.FB_PAGE_ID);
                        break;
                    case R.id.nav_settings:
                        TsGaTools.trackNav("/nav_setting");
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
        return true;
    }

    private void handleSearch(Intent intent) {
        if (Intent.ACTION_SEARCH.equalsIgnoreCase(intent.getAction())) {
            TsGaTools.trackNav(MsConst.TRACK_SEARCH);
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
        } else {
            kenBurnsView.setResourceUrl("http://dota2walls.com/wp-content/uploads/2014/11/invoker-arsenal-magus-wallpaper.png", false);
        }

    }


    public static Intent getIntent(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Subscribe
    public void fromAdapterRoles(GoAdapterRoles dto) {
        showFragment(RoleListFragment.newInstance(dto.role), true);
    }

    class GetFromParse extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();

            reference.child(HeroBasicDto.class.getSimpleName()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroBasicDto> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                        list.add(post);
                    }

                    HeroEntry entry = null;
                    for (HeroBasicDto hero : list) {
                        entry = findHero(hero);
                        if (entry != null) {
                            Logger.debug(TAG, ">>>" + "Found:" + hero.heroId);
                            hero.fullName = entry.fullName;
                            hero.group = entry.group;
                            hero.bgLink = entry.bgLink;
                        } else {
                            Logger.error(TAG, ">>> Error:" + "Cannot find:" + hero.heroId);
                            if (hero.heroId.equals("Underlord")) {
                                hero.fullName = "Vrogros, the Underlord";
                                hero.group = "Str";
                                hero.bgLink = "https://hydra-media.cursecdn.com/dota2.gamepedia.com/7/73/Underlord_Guide_Header.png?version=8437b238c54fee321e175962f4bbf012";
                            }

                            if (hero.heroId.equals("Monkey_King")) {
                                hero.group = "Agi";
                                hero.fullName = "Sun Wukong, the Monkey King";
                                hero.bgLink = "https://hydra-media.cursecdn.com/dota2.gamepedia.com/f/f4/Monkey_King.png";

                                Logger.debug(TAG, ">>>" + "***------***** Monkey_King****");

                            }

                        }
                        //find it in firebase, then update
                        FireBaseUtils.find(HeroBasicDto.class.getSimpleName(), "heroId", hero.heroId, hero);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return null;
        }
    }

    private HeroEntry findHero(HeroBasicDto dto) {
        try {
            String heroId = dto.heroId;
            if (dto.heroId.equals("Nature%27s_Prophet")) {
                heroId = "Natures_Prophet";
            }
            return HeroManager.getInstance().getHero(heroId);

        } catch (Exception e) {
            Logger.error(TAG, ">>> Error:" + "findHero:" + e);
            return null;
        }
    }
}
