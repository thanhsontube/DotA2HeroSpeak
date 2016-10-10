package son.nt.dota2.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.widget.LikeView;
import com.google.android.gms.common.SignInButton;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;

import java.security.MessageDigest;

import butterknife.Bind;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.facebook.UserDto;
import son.nt.dota2.login.LoginContract;
import son.nt.dota2.login.LoginPresenter;
import son.nt.dota2.login.LoginRepo;
import son.nt.dota2.parse.AppAPI;
import son.nt.dota2.parse.IUserParse;
import son.nt.dota2.parse.UpdateUserInfoDto;
import son.nt.dota2.utils.KeyBoardUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;

public class LoginFragment extends AFragment implements View.OnClickListener,
        LoginContract.View {
    public static final String TAG = "LoginFragment";

    TextView txtGuest;

    //facebook
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfilePictureView profilePictureView;
    ProfileTracker profileTracker;


    @Bind(R.id.login_forgot_password)
    TextView txtForgotPassword;

    @Bind(R.id.login_txt_sign_up)
    TextView txtSignUp;
    @Bind(R.id.login_by_facebook)
    TextView loginWithFacebook;

    @Bind(R.id.login_username)
    AppCompatEditText txtEmail;

    @Bind(R.id.login_password)
    AppCompatEditText txtPassword;

    @Bind(R.id.sign_in_button)
    SignInButton mSignInButton;

    AppAPI appAPI;

    LoginContract.Presenter mPresenter;


    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        mPresenter = new LoginPresenter(this, new LoginRepo());
        appAPI = new AppAPI(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_login, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initData();
//        initLayout(view);
//        initListener();
        mPresenter.checkLogin();
        checkingLogin();

        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        loginWithFacebook.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);


        view.findViewById(R.id.login_enter).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(txtEmail.getText().toString(), txtPassword.getText().toString(), new LogInCallback() {
                    @Override
                    public void done(ParseUser parseUser, ParseException e) {
                        if (e != null) {
                            Toast.makeText(getActivity(), "Login Error!", Toast.LENGTH_SHORT).show();
                        } else {
                            View view = getActivity().getCurrentFocus();
                            if (view != null) {
                                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                            }
                            Toast.makeText(getActivity(), "Hello:" + parseUser.getUsername(), Toast.LENGTH_SHORT).show();
                            getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                            getActivity().finish();
                        }

                    }
                });
            }
        });

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

    private void checkingLogin() {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser != null) {
            String name = parseUser.getString("name");
            Toast.makeText(getActivity(), "Welcome:" + name, Toast.LENGTH_SHORT).show();
            startActivity(HomeActivity.getIntent(getActivity()));
            getActivity().finish();
        }
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

        LikeView likeView = (LikeView) view.findViewById(R.id.login_like_view);
        likeView.setLikeViewStyle(LikeView.Style.STANDARD);
        likeView.setObjectIdAndType(MsConst.FB_ID_POST_TO, LikeView.ObjectType.PAGE);
        likeView.setFragment(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        updateUI();
//        if (FacebookManager.getInstance().isLogin()) {
//            startActivity(HomeActivity.getIntent(getActivity()));
//            getActivity().finish();
//        }
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

    private void initListener() {
        txtGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TsGaTools.trackPages(MsConst.TRACK_LOGIN);
                startActivity(HomeActivity.getIntent(getActivity()));
                getActivity().finish();
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
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
//        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loginByFacebook() {
        Logger.debug(TAG, ">>>" + "loginByFacebook");
        ParseFacebookUtils.logInWithPublishPermissionsInBackground(this, null, new LogInCallback() {
            @Override
            public void done(final ParseUser user, ParseException e) {
                Logger.debug(TAG, ">>>" + "loginByFacebook:" + e);
                if (e != null) {
                    Toast.makeText(getActivity(), "Login Error:" + e.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (user == null) {
                    Logger.debug("MyApp", ">>>Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Logger.debug("MyApp", ">>>User signed up and logged in through Facebook!");
                } else {
                    Logger.debug("MyApp", ">>>User logged in through Facebook!");
                    Logger.debug(TAG, ">>>" + "Email:" + user.getEmail() + ";name:" + user.getUsername());
                }

                if (user == null) {
                    return;
                }

                if (ParseUser.getCurrentUser() != null && ParseFacebookUtils.isLinked(ParseUser.getCurrentUser())) {
                    Logger.debug(TAG, ">>>" + "user link fb already");
                    getUserInfo();
                    return;
                }


            }
        });
    }

    private void getUserInfo() {
        Logger.debug(TAG, ">>>" + "getUserInfo");
        Profile profile = Profile.getCurrentProfile();
        if (profile == null) {
            return;
        }
        String name = profile.getName();
        String fbId = profile.getId();
        Uri image = profile.getProfilePictureUri(480, 480);
        Logger.debug(TAG, ">>>" + "image:" + image.toString() + " ;name:" + name + ";fbId:" + profile.getId() + ";info:" + profile.describeContents());

        appAPI.updateUserInfo(new UpdateUserInfoDto(name, fbId, image.toString(), profile.getLinkUri().toString()));


        getContext().startActivity(new Intent(getContext(), HomeActivity.class));
        getActivity().finish();

    }

    IUserParse.Callback userCallback = new IUserParse.Callback() {
        @Override
        public void onCheckingUserExist(String email, boolean isExist) {

        }

        @Override
        public void onUserCreated(UserDto userDto, ParseException error) {

        }

        @Override
        public void onFinishResetPw(ParseException error) {

        }
    };

    @Override
    public void onClick(View view) {
        KeyBoardUtils.close((Activity) getContext());
        switch (view.getId()) {
            case R.id.login_forgot_password:
                getContext().startActivity(new Intent(getContext(), HomeActivity.class));
                getActivity().finish();
                break;
            case R.id.login_txt_sign_up:
                if (mListener != null) {
                    mListener.onSignUp(txtEmail.getText().toString(), txtPassword.getText().toString());
                }
                break;
            case R.id.login_by_facebook:
                loginByFacebook();


                break;
            case R.id.sign_in_button: {
                break;
            }

        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);

        void onSignUp(String email, String password);
    }

    OnFragmentInteractionListener mListener;

    //Contract - View

    @Override
    public void loginFacebook() {

    }

    @Override
    public void loginGoogle() {

    }

    @Override
    public void loginEmail(String email, String password) {

    }

    @Override
    public void showLogin(String userName) {
        Toast.makeText(getContext(), "Hello:" + userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNotLogin() {


    }
}
