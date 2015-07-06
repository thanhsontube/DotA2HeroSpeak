package son.nt.dota2.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import java.security.MessageDigest;

import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.utils.Logger;

public class LoginFragment extends AFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    public static final String TAG = "LoginFragment";

    TextView txtGuest;

    //facebook
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfilePictureView profilePictureView;
    ProfileTracker profileTracker;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(false);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_login, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fb_logout:
                LoginManager.getInstance().logOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }



    private void initLayout(View view) {
        loginButton = (LoginButton) view.findViewById(R.id.login_button_login);
        loginButton.setFragment(this);
        profilePictureView = (ProfilePictureView) view.findViewById(R.id.profilePicture);
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile profile, Profile profile1) {
                updateUI();
            }
        };
        txtGuest = (TextView) view.findViewById(R.id.skip_login_button);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        Profile profile = Profile.getCurrentProfile();
        if (profile != null) {
            profilePictureView.setProfileId(profile.getId());
            txtGuest.setText("Hi!" + profile.getName() + "-Click to continue");
        } else {
            profilePictureView.setProfileId(null);
            txtGuest.setText("Continue as a guest");
        }
    }

    private void initListener () {
        txtGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });

    }

    private void initData() {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.debug(TAG, ">>>" + "onSuccess");
                updateUI();

            }

            @Override
            public void onCancel() {
                Logger.debug(TAG, ">>>" + "onSuccess");
                updateUI();

            }

            @Override
            public void onError(FacebookException e) {
                Logger.debug(TAG, ">>>" + "onError");
                updateUI();

            }
        });

        try {
            PackageInfo info = getActivity().getPackageManager().getPackageInfo(
                    getActivity().getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", ">>>Hey hash dota:" + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (Exception e) {

        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
