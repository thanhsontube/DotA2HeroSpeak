package son.nt.dota2.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterDrawerLeft;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.LeftDrawerDto;
import son.nt.dota2.fragment.MainFragment;

public class MainActivity extends BaseFragmentActivity implements MainFragment.OnFragmentInteractionListener {
    HeroDto heroDto;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolBar;

    private RecyclerView leftDrawer;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapterLeft;

    private List<LeftDrawerDto> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            heroDto = (HeroDto) getIntent().getSerializableExtra("data");
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().show();
        initData();
        initLayout();
        initListener();
        updateLayout();
    }

    private void initData() {
        list.clear();
        LeftDrawerDto dto = new LeftDrawerDto(heroDto.name, true);
        dto.isHeader = true;
        dto.heroName = heroDto.name;
        dto.heroUrl = heroDto.avatarThubmail;
        list.add(dto);
        list.add(new LeftDrawerDto(heroDto.name, true));
        list.add(new LeftDrawerDto("Favorite"));
        list.add(new LeftDrawerDto("Feedback"));
    }

    private void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        leftDrawer = (RecyclerView) findViewById(R.id.left_drawer);
        leftDrawer.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        leftDrawer.setLayoutManager(layoutManager);
        adapterLeft = new AdapterDrawerLeft(this, list);
        leftDrawer.setAdapter(adapterLeft);
    }

    private void initListener() {
//        leftDrawer.setoni
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void updateLayout() {

    }


    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return MainFragment.newInstance("", heroDto);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.ll_main_hero;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
