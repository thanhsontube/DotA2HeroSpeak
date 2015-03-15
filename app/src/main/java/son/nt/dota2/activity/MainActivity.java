package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import son.nt.dota2.R;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.fragment.MainFragment;

public class MainActivity extends BaseFragmentActivity implements MainFragment.OnFragmentInteractionListener{
    HeroDto heroDto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            heroDto = (HeroDto) getIntent().getSerializableExtra("data");
            Log.v("", "log>>>" + "heroDto:" + heroDto);
            if (heroDto != null) {
                Log.v("","log>>>" + "name:" + heroDto.name);
            }
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getActionBar().hide();

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
