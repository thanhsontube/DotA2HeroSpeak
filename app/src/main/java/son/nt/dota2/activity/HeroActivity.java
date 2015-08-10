package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.squareup.otto.Subscribe;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.gridmenu.CommentDialog;
import son.nt.dota2.gridmenu.GridMenuDialog;
import son.nt.dota2.fragment.HeroFragment;
import son.nt.dota2.gridmenu.SpeakLongClick;
import son.nt.dota2.ottobus_entry.GoLoginDto;
import son.nt.dota2.utils.OttoBus;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hero);
        OttoBus.register(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
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

        if (id == android.R.id.home) {
            if (mFragmentTagStack.size() == 0) {
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

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
    public void goLogin (GoLoginDto dto) {
        if (!dto.isLogin)
        {

            startActivity(LoginActivity.getIntent(this));
        } else {
            FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
            Fragment f = getSafeFragmentManager().findFragmentByTag("cmt");
            if (f != null) {
                ft.remove(f);
            }
            CommentDialog dialog = CommentDialog.createInstance();
            ft.add(dialog, "cmt");
            ft.commit();
        }
    }


}
