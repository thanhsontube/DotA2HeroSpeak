package son.nt.dota2.activity;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.fragment.HomeFragment;

public class HomeActivity extends AActivity implements HomeFragment.OnFragmentInteractionListener {

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initLayout();
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
        toolbar = (Toolbar) findViewById(R.id.home_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar,  R.string.app_name, R.string.action_settings);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
}
