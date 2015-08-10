package son.nt.dota2.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;

import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.fragment.LoginFragment;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;

public class LoginActivity extends AActivity {


    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return LoginFragment.newInstance("", "");
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_ll_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        testAbilities();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (BuildConfig.DEBUG) {
//            startActivity(new Intent(this, HomeActivity.class));
//            finish();
//        }
    }

    private void testAbilities() {
        HTTPParseUtils.getInstance().withHeroList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_push_step1:
                break;
            case R.id.action_push_step2:
                break;
            case R.id.action_push_step3:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public static Intent getIntent (Context context) {
        return new Intent(context, LoginActivity.class);
    }
}
