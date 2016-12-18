package son.nt.dota2.fragment;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;

import butterknife.BindView;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.firebase.DaggerGoogleApiComponent;
import son.nt.dota2.firebase.GoogleApiClientModule;
import son.nt.dota2.login.LoginContract;
import son.nt.dota2.login.LoginPresenter;
import son.nt.dota2.login.LoginRepo;
import son.nt.dota2.rx.SchedulerProvider;
import son.nt.dota2.test.TestActivity;
import son.nt.dota2.utils.KeyBoardUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;

public class LoginFragment extends AFragment implements View.OnClickListener,
        LoginContract.View, GoogleApiClient.OnConnectionFailedListener {
    public static final String TAG = "LoginFragment";

    private static final int RC_SIGN_IN = 9001;

    TextView txtGuest;

    //facebook
    CallbackManager callbackManager;
    LoginButton loginButton;
    ProfilePictureView profilePictureView;
    ProfileTracker profileTracker;


    @BindView(R.id.login_forgot_password)
    TextView txtForgotPassword;

    @BindView(R.id.login_txt_sign_up)
    TextView txtSignUp;
    @BindView(R.id.login_by_facebook)
    TextView loginWithFacebook;

    @BindView(R.id.login_username)
    AppCompatEditText txtEmail;

    @BindView(R.id.login_password)
    AppCompatEditText txtPassword;

    @BindView(R.id.sign_in_button)
    SignInButton mSignInButton;


    LoginContract.Presenter mPresenter;

    //    @Inject
    FirebaseAuth mFirebaseAuth;
    //    @Inject
    FirebaseUser mFirebaseUser;

    //    @Inject
    GoogleApiClient mGoogleApiClient;


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
        mPresenter = new LoginPresenter(this, new LoginRepo(), SchedulerProvider.getInstance());

//        setupDI();

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        if (mFirebaseUser != null) {
            startActivity(HomeActivity.getIntent(getActivity()));
            getActivity().finish();
            return;
        }
//
//        if (mFirebaseUser != null) {
//            mFirebaseAuth.signOut();
//        }

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                .enableAutoManage(getActivity() /* FragmentActivity */,
                        this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();


    }

    private void setupDI() {
        GoogleApiClientModule googleApiClientModule = new GoogleApiClientModule((AppCompatActivity) getActivity(), getString(R.string.default_web_client_id), this);
        DaggerGoogleApiComponent.builder().googleApiClientModule(googleApiClientModule).build().inject(this);

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
//        checkingLogin();


        //todo hack
        view.findViewById(R.id.login_welcome).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), TestActivity.class));
//                FireBaseUtils.find(HeroBasicDto.class.getSimpleName(), "heroId", "Sven");
            }
        });

        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);
        loginWithFacebook.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);




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

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google Sign In failed
                Log.e(TAG, "Google Sign In failed.");
            }
        }
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


        getContext().startActivity(new Intent(getContext(), HomeActivity.class));
        getActivity().finish();

    }



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


                break;
            case R.id.sign_in_button: {
                loginGoogle();
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


    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);

        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(authResult -> {
                    Logger.debug(TAG, ">>>" + "onSuccess");
                    showLogin(authResult.getUser().getDisplayName());

                })

                .addOnFailureListener(e -> Logger.debug(TAG, ">>>" + "onFailure:" + e));


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    //Contract - View

    @Override
    public void loginFacebook() {

    }

    @Override
    public void loginGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void loginEmail(String email, String password) {

    }

    @Override
    public void showLogin(String userName) {
        Toast.makeText(getContext(), "Hello:" + userName, Toast.LENGTH_SHORT).show();
        startActivity(HomeActivity.getIntent(getActivity()));
    }

    @Override
    public void showNotLogin() {
        Toast.makeText(getContext(), "Hello:" + "Login please", Toast.LENGTH_SHORT).show();

    }
}
