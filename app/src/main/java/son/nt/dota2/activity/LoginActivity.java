package son.nt.dota2.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import son.nt.dota2.R;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.fragment.LoginFragment;

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
    }
}
