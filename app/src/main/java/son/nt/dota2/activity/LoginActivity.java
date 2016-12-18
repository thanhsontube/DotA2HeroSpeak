package son.nt.dota2.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.fragment.LoginFragment;
import son.nt.dota2.fragment.SignUpFragment;
import son.nt.dota2.utils.TsGaTools;

public class LoginActivity extends AActivity implements LoginFragment.OnFragmentInteractionListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @Override
    protected Fragment onCreateMainFragment(Bundle savedInstanceState) {
        return LoginFragment.newInstance();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.login_ll_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TsGaTools.trackPages(MsConst.TRACK_START);
        setContentView(R.layout.activity_login);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, LoginActivity.class);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onSignUp(String email, String password) {
        showFragment(SignUpFragment.newInstance(email, password), true);

    }
}
