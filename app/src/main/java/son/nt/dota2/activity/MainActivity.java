package son.nt.dota2.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LikeView;
import com.facebook.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import son.nt.dota2.BuildConfig;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adMob.AdMobUtils;
import son.nt.dota2.adapter.AdapterDrawerLeft;
import son.nt.dota2.adapter.AdapterDrawerRight;
import son.nt.dota2.base.AActivity;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.LeftDrawerDto;
import son.nt.dota2.facebook.UserDto;
import son.nt.dota2.fragment.MainFragment;
import son.nt.dota2.fragment.PlayListFragment;
import son.nt.dota2.fragment.SavedFragment;
import son.nt.dota2.utils.CommonUtil;
import son.nt.dota2.utils.DatetimeUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsFeedback;
import son.nt.dota2.utils.TsGaTools;

public class MainActivity extends AActivity implements MainFragment.OnFragmentInteractionListener,
        SavedFragment.OnFragmentInteractionListener, PlayListFragment.OnFragmentInteractionListener {
    private static final String TAG = "MainActivity";
    private static final String PERMISSION = "publish_actions";
    HeroDto heroDto;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Toolbar toolBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private RecyclerView leftDrawer;
    private RecyclerView rightDrawer;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManagerR;
    private AdapterDrawerLeft adapterLeft;
    private AdapterDrawerRight adapterRight;
    private View viewLeft;

    private List<LeftDrawerDto> list = new ArrayList<>();
    private List<UserDto> listCmts = new ArrayList<UserDto>();
    Logger log = new Logger(TAG);

    private LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getIntent() != null) {
            heroDto = (HeroDto) getIntent().getSerializableExtra("data");
        }
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        //        getSupportActionBar().show();
        initData();
        initLayout();
        setupDrawerRight();
        initListener();
        updateLayout();
        adMob();
        uiLifecycleHelper = new UiLifecycleHelper(this, statusCallback);
        uiLifecycleHelper.onCreate(savedInstanceState);
        CommonUtil.getKeyHashForFacebook(this);
    }

    private void adMob() {
        AdMobUtils.init(findViewById(R.id.ll_ads), R.id.adView);
        if (BuildConfig.DEBUG) {
            AdMobUtils.hide();
        } else {
            AdMobUtils.show();
        }
    }

    private void initData() {
        list.clear();
        LeftDrawerDto dto = new LeftDrawerDto(heroDto.name, true);
        dto.isHeader = true;
        dto.heroName = heroDto.name;
        dto.heroUrl = heroDto.avatarThubmail;
        list.add(dto);
        list.add(new LeftDrawerDto(heroDto.name, true));
        list.add(new LeftDrawerDto("Favorite"));
        list.add(new LeftDrawerDto("Rate this app"));
        list.add(new LeftDrawerDto("Share this app"));
        list.add(new LeftDrawerDto("(HOT)Dota 2 Videos"));
    }

    private void initLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, 0);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        leftDrawer = (RecyclerView) findViewById(R.id.left_drawer);
        leftDrawer.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        leftDrawer.setLayoutManager(layoutManager);
        adapterLeft = new AdapterDrawerLeft(this, list);
        leftDrawer.setAdapter(adapterLeft);

        loginButton = (LoginButton) findViewById(R.id.right_fb_login);
        viewLeft = findViewById(R.id.left_view);
    }

    private Handler handler = new Handler();

    private void initListener() {
        adapterLeft.setOnCallback(new AdapterDrawerLeft.IAdapterCallback() {
            @Override
            public void onClick(int position, LeftDrawerDto leftDrawerDto) {
                switch (position) {
                    case 0:
                    case 1:
                        FragmentManager fm = getSupportFragmentManager();
                        while (mFragmentTagStack.size() > 0) {
                            fm.popBackStackImmediate();
                        }
                        break;
                    case 2:
                        TsGaTools.trackPages("/Saved");
                        SavedFragment f = SavedFragment.newInstance("", "");
                        showFragment(f, true);
                        break;

                    case 3:
                        TsGaTools.trackPages("/Feedback");
                        //                        TsFeedback.sendEmailbyGmail(getApplicationContext(), "thanhsontube@gmail.com", "FeedBack for DotA 2 Heros Speak", "Hi, ...");
                        TsFeedback.rating(getApplicationContext());
                        break;
                    case 4:
                        TsGaTools.trackPages("/ShareApp");
                        facebookShareWithDialog();
                        break;
                    case 5:
                        TsGaTools.trackPages("/Videos");
                        showFragment(new PlayListFragment(), true);
                        break;
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawerLayout.closeDrawer(Gravity.START);
                    }
                }, 250L);

            }
        });

        rightDrawer.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
                        : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void updateLayout() {
        viewPostBy.setVisibility(View.GONE);
        loginButtonOut.setVisibility(View.GONE);
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
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_chat, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.action_chat) {
            drawerLayout.openDrawer(Gravity.RIGHT);
        } else if (item.getItemId() == R.id.action_remove_fb) {
            removePermission();
        }
        return super.onOptionsItemSelected(item);
    }

    //facebook login
    private enum PendingAction {
        NONE, LOGIN, REQUESTING_POST_PER, SHARE, WAIT_LOGIN_RESULT, GET_INFO
    }

    private UiLifecycleHelper uiLifecycleHelper;
    SessionState state = SessionState.CLOSED;
    private PendingAction pendingAction = PendingAction.NONE;
    private static final String PERMISSION_PUSHLISH_ACTIONS = "publish_actions";
    private static final String PERMISSION_BIRTHDAY = "user_birthday";
    private static final String PERMISSION_LOCATION = "user_location";
    private static final String PERMISSION_HOMETOWN = "user_hometown";
    //facebook login
    String fbId;
    String fbName;
    String gender;
    String email;
    String dob;
    String photo;
    String zip;

    boolean isPost = false;

    private Session.StatusCallback statusCallback = new Session.StatusCallback() {

        @Override
        public void call(Session session,
                         SessionState state,
                         Exception exception) {
            log.d("log>>>" + "statusCallback state:" + state
                    + ";session:"
                    + session.getState());
            onCallbackStatus(session, state, exception);
        }
    };

    private void handlePendingAction() {
        PendingAction prevPendingAction = pendingAction;
        log.d("log>>>" + "handlePendingAction state:" + state + ";prevPendingAction:" + prevPendingAction);
        pendingAction = PendingAction.NONE;
        if (state == SessionState.OPENED) {
            controllerLoadCmt.load();
        } else {
            llFbLogin.setVisibility(View.VISIBLE);
            viewPostBy.setVisibility(View.GONE);
            loginButtonOut.setVisibility(View.GONE);
            listCmts.clear();
            ;
            adapterRight.notifyDataSetChanged();
        }
        switch (prevPendingAction) {
            case LOGIN:
                if (prevPendingAction == PendingAction.LOGIN && !isLogin()) {
                    pendingAction = PendingAction.WAIT_LOGIN_RESULT;
                    log.d("log>>>" + "WAIT_LOGIN_RESULT");
                }
                break;

            case WAIT_LOGIN_RESULT:
                if (isLogin()) {
                    log.d("log>>>" + "login success isPost:" + isPost);
                    if (isPost) {
                        isPost = false;
                    }
                } else {
                    log.d("log>>>" + "login false");
                }
                isPost = false;
                break;

            case REQUESTING_POST_PER:
                log.d("log>>>" + "REQUESTING_POST_PER");
                // requestPostPermission();

                //check is cancel
                if (!hasPostFacebookPermission()) {
                    log.d("log>>>" + "Cancel per");
                } else {
                    postComment();
                }
                if (prevPendingAction == PendingAction.REQUESTING_POST_PER) {
                    pendingAction = PendingAction.WAIT_LOGIN_RESULT;
                }
                break;

            default:
                break;
        }

    }

    private void onCallbackStatus(Session session, SessionState state, Exception exception) {
        this.state = state;
        handlePendingAction();
    }

    private FacebookDialog.Callback dialogCallback = new FacebookDialog.Callback() {
        @Override
        public void onError(FacebookDialog.PendingCall pendingCall,
                            Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format(
                    ">>> dialogCallback Error: %s",
                    error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall,
                               Bundle data) {
            Log.d("HelloFacebook", ">>> dialogCallback Success!");
        }
    };

    private boolean isLogin() {
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            return true;
        }
        return false;
    }

    public void facebookLogin() {
        Session session = Session.getActiveSession();
        log.d("log>>>" + "facebookLogin:" + session);
        if (session != null) {
            pendingAction = PendingAction.LOGIN;
            Session.openActiveSession(this, true, null, statusCallback);
            //            Session.openActiveSession(this, true, Arrays.asList("email", PERMISSION_BIRTHDAY, PERMISSION_BIRTHDAY, PERMISSION_LOCATION, PERMISSION_HOMETOWN), statusCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
        Session session = Session.getActiveSession();
        if (session != null && (session.isOpened() || session.isClosed())) {
            onCallbackStatus(session, session.getState(), null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
        likeView.handleOnActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifecycleHelper.onSaveInstanceState(outState);

    }

    Controller controllerLoadCmt = new Controller() {
        @Override
        public void load() {
            Bundle params = new Bundle();
            params.putBoolean("summary", true);
            params.putString("filter", "toplevel");
            params.putString("limit", "500");
            Session session = Session.getActiveSession();
            swipeRefreshLayout.setRefreshing(true);

            new Request(session, MsConst.FB_COMMENT_TO, params, HttpMethod.GET,
                    new Request.Callback() {
                        public void onCompleted(Response response) {
                            try {
                                swipeRefreshLayout.setRefreshing(false);
                                FacebookRequestError err = response
                                        .getError();
                                log.d("log>>>" + "FacebookRequestError:"
                                        + err);
                                if (err != null) {
                                    llFbLogin.setVisibility(View.VISIBLE);
                                    return;
                                }
                                llFbLogin.setVisibility(View.GONE);
                                String tl = response.getGraphObject()
                                        .toString();
                                log.d("log>>>" + "Facebook object:"
                                        + tl.toString());
                                JSONObject jsonObject = response
                                        .getGraphObject()
                                        .getInnerJSONObject();
                                JSONArray jsonArray = jsonObject
                                        .getJSONArray("data");
                                List<UserDto> listCmt = new ArrayList<UserDto>();
                                UserDto dto = null;
                                String id = "-1";
                                String name = "";
                                String cmt = "";
                                String timePost = "";
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jChild = jsonArray
                                            .getJSONObject(i);
                                    try {
                                        timePost = jChild
                                                .getString("created_time");
                                        timePost = DatetimeUtils
                                                .convertFbTime(timePost);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        cmt = jChild.getString("message");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    try {
                                        JSONObject jChild2 = jChild
                                                .getJSONObject("from");
                                        name = jChild2.getString("name");
                                        id = jChild2.getString("id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                    dto = new UserDto(id, name, cmt,
                                            timePost);
                                    String avatar = String.format(
                                            MsConst.FB_AVATAR_LINK,
                                            id);
                                    dto.avatar = avatar;
                                    if (!id.equals("-1")) {
                                        listCmt.add(dto);
                                    }

                                }

                                log.d("log>>>" + "List cmt:"
                                        + listCmt.size());
                                listCmts.clear();
                                listCmts.addAll(listCmt);
                                adapterRight.notifyDataSetChanged();
                                controllerUserFb.load();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }).executeAsync();

        }
    };
    private ImageView imgSend;
    private ImageView imgHeroRight;
    private EditText edtCmt;
    private TextView txtReload;
    private View llFbLogin;
    private LikeView likeView;

    private void setupDrawerRight() {

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.right_swipe_refresh);
        imgSend = (ImageView) findViewById(R.id.right_img_send);
        imgHeroRight = (ImageView) findViewById(R.id.right_img_hero);
        edtCmt = (EditText) findViewById(R.id.right_txt_comments);

        rightDrawer = (RecyclerView) findViewById(R.id.right_recycle_view);
        layoutManagerR = new LinearLayoutManager(this);
        rightDrawer.setLayoutManager(layoutManagerR);
        rightDrawer.setHasFixedSize(true);

        adapterRight = new AdapterDrawerRight(this, listCmts);
        rightDrawer.setAdapter(adapterRight);

        //swipe
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                controllerLoadCmt.load();
            }
        });

        //send btn
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TsGaTools.trackPages("/PostComment");
                isPost = true;
                sendCmt(edtCmt.getText().toString());
            }
        });

        txtReload = (TextView) findViewById(R.id.right_txt_reload);
        txtReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebookLogin();
            }
        });
        likeView = (LikeView) findViewById(R.id.right_fb_like_view);
        likeView.setObjectId(MsConst.FB_ID_POST_TO);

        llFbLogin = findViewById(R.id.right_ll_cmt);

        setupPostByView();

    }

    String message;

    private void sendCmt(String message) {
        CommonUtil.hideKeyboard(this);
        this.message = message;
        if (TextUtils.isEmpty(message)) {
            return;
        }

        if (llFbLogin.getVisibility() == View.VISIBLE) {
            Toast.makeText(this, "Please Login first!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!hasPostFacebookPermission()) {
            pendingAction = PendingAction.REQUESTING_POST_PER;
            Session.getActiveSession().requestNewPublishPermissions(
                    new Session.NewPermissionsRequest(this, Arrays.asList(PERMISSION)));
        } else {
            postComment();
        }

    }

    private boolean hasPostFacebookPermission() {
        Session session = Session.getActiveSession();
        if (session != null && session.isPermissionGranted(PERMISSION)) {
            return true;
        }
        return false;

    }

    private void postComment() {

        Session session = Session.getActiveSession();
        log.d("log>>>" + "postComment");
        Bundle params = new Bundle();
        params.putString("message", message);
        new Request(session, MsConst.FB_COMMENT_TO, params, HttpMethod.POST, new Request.Callback() {
            public void onCompleted(Response response) {
                edtCmt.setText("");
                edtCmt.clearFocus();

                controllerLoadCmt.load();
            }
        }).executeAsync();
    }

    private void removePermission() {
        log.d("log>>>" + "removePermission");
        Bundle bundle = new Bundle();
        Request request = new Request(Session.getActiveSession(), "me/permissions", bundle, HttpMethod.DELETE,
                new Request.Callback() {

                    @Override
                    public void onCompleted(Response response) {
                        log.d("log>>>" + "response:" + response.getError());
                        Session.getActiveSession().refreshPermissions();
                        if (response.getError() == null) {
                            Toast.makeText(getApplicationContext(), "Logout success", Toast.LENGTH_SHORT)
                                    .show();

                        } else {
                            Toast.makeText(getApplicationContext(), "logout fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        request.executeAsync();
    }

    private void facebookShareWithDialog() {
        String message = "Full 110 Dota2 hero with speak and text";
        String name = "DOTA 2 Hero Speak";
        String link = "https://play.google.com/store/apps/details?id=" + getPackageName();

        if (FacebookDialog.canPresentShareDialog(this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(this).setDescription(message)
                    .setName(name).setLink(link).build();
            uiLifecycleHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            //open facebook appp
            TsFeedback.installFB(this);
        }
    }

    Controller controllerUserFb = new Controller() {
        @Override
        public void load() {
            final Request request = Request.newMeRequest(Session.getActiveSession(),
                    new Request.GraphUserCallback() {
                        @Override
                        public void onCompleted(GraphUser graphUser,
                                                Response response) {
                            FacebookRequestError err = response.getError();
                            if (err != null) {
                                log.d("log>>>" + "Error controllerUserFb:"
                                        + err.toString());
                                viewPostBy.setVisibility(View.GONE);
                                loginButtonOut.setVisibility(View.GONE);
                                return;
                            }

                            viewPostBy.setVisibility(View.VISIBLE);
                            loginButtonOut.setVisibility(View.VISIBLE);
                            String firstName = graphUser.getFirstName();
                            String lastName = graphUser.getLastName();
                            if (TextUtils.isEmpty(firstName)) {
                                firstName = "";
                            }
                            if (TextUtils.isEmpty(lastName)) {
                                lastName = "";
                            }
                            String id = graphUser.getId();
                            String avatar = String.format(MsConst.FB_AVATAR_LINK,
                                    id);
                            //update
                            txtNamePostBy.setText(firstName + " " + lastName + ")");

                            AQuery aq = new AQuery(getApplicationContext());
                            aq.id(imgAvatarPostBy).image(avatar, true, true);

                        }
                    });
            request.executeAsync();
        }

    };

    View viewPostBy;
    TextView txtNamePostBy;
    ImageView imgAvatarPostBy;
    LoginButton loginButtonOut;

    private void setupPostByView() {
        viewPostBy = findViewById(R.id.right_view_post_by);
        txtNamePostBy = (TextView) findViewById(R.id.right_post_by_name);
        imgAvatarPostBy = (ImageView) findViewById(R.id.right_post_by_avatar);
        loginButtonOut = (LoginButton) findViewById(R.id.right_fb_logout);
    }
}
