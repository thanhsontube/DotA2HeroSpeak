package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.fragment.HeroFragment;

public class HeroActivity extends AActivity implements HeroFragment.OnFragmentInteractionListener {
    HeroEntry heroEntry;

    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return HeroFragment.newInstance(heroEntry);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.hero_main_ll;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        heroEntry = (HeroEntry) getIntent().getExtras().getSerializable(MsConst.EXTRA_HERO);
        if (heroEntry != null) {

        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_hero, menu);
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
