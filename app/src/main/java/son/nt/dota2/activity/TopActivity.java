package son.nt.dota2.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import son.nt.dota2.R;
import son.nt.dota2.base.BaseFragmentActivity;
import son.nt.dota2.fragment.TopFragment;


public class TopActivity extends BaseFragmentActivity implements TopFragment.OnFragmentInteractionListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top);
    }

    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return TopFragment.newInstance("", "");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.ll_main;
    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_top, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//
//        if (id == R.id.action_settings) {
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    //TODO listenner

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
