package son.nt.dota2.base;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.FacebookDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 1/31/2015.
 * this class is used for all fragments that want to user Facebook SDk for login
 */
public abstract class FacebookBaseFragment extends BaseFragment {

    private static final String TAG = "FacebookBaseFragment";
    Logger log = new Logger(TAG);

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiLifecycleHelper = new UiLifecycleHelper(getActivity(), statusCallback);
        uiLifecycleHelper.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    // --------------------------------------------------------
    //facebook login
    String fbId;
    String fbName;
    String gender;
    String email;
    String dob;
    String photo;
    String zip;


    private Session.StatusCallback statusCallback = new Session.StatusCallback() {

        @Override
        public void call(Session session, SessionState state, Exception exception) {
            log.d("log>>>" + "statusCallback state:" + state + ";session:" + session.getState());
            onCallbackStatus(session, state, exception);
        }
    };

    private void handlePendingAction() {
        PendingAction prevPendingAction = pendingAction;
        log.d("log>>>" + "handlePendingAction state:" + state + ";PendingAction:" + prevPendingAction);

        switch (prevPendingAction) {
            case LOGIN:
                if (pendingAction == PendingAction.LOGIN && !isLogin()) {
                    pendingAction = PendingAction.WAIT_LOGIN_RESULT;
                } else {
                    pendingAction = PendingAction.WAIT_LOGIN_RESULT;
                }
                break;

            case WAIT_LOGIN_RESULT:
                log.d("log>>>" + "WAIT_LOGIN_RESULT");
                if (isLogin()) {
                    log.d("log>>>" + "login success :");
                    pendingAction = PendingAction.NONE;
                    getInfoFB();

                } else {
                    log.d("log>>>" + "login false");
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
        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
            Log.d("HelloFacebook", String.format(">>> dialogCallback Error: %s", error.toString()));
        }

        @Override
        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
            Log.d("HelloFacebook", ">>> dialogCallback Success!");
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        uiLifecycleHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data, dialogCallback);
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
//
//    @SuppressWarnings("null")
//    private void loginByApp() {
//        Session session = Session.getActiveSession();
//        log.d("log>>>" + "loginByApp session:" + session);
//        if (session != null) {
//            pendingAction = PendingAction.GET_INFO;
//            Session.openActiveSession(getActivity(), this, true, Arrays.asList("email", PERMISSION_BIRTHDAY), statusCallback);
//        }
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        menu.add(0, 10, 0, "remove facebook session");
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case 10:
                logoutApp();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void logoutApp() {
        log.d("log>>>" + "logoutApp state:" + state);
        Session session = Session.getActiveSession();
        if (session == null) {
            log.d("log>>>" + "session == null");
            return;
        }
        if (state != SessionState.OPENED) {
            Session.openActiveSession(getActivity(), this, true, Arrays.asList("email", PERMISSION_BIRTHDAY, PERMISSION_LOCATION), statusCallback);
            return;
        }
        removePermission();
    }

    public void logoutFb() {
        log.d("log>>>" + "logoutByApp");
        Session session = Session.getActiveSession();
        if (session != null && session.isOpened()) {
            log.d("log>>>" + "session.isOpened");
            session.closeAndClearTokenInformation();
        }else {
            log.e("log>>>" + "CAN NOT LOG OUT");
        }
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
                            Toast.makeText(getActivity(), "Logout success", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getActivity(), "logout fail", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
        request.executeAsync();
    }

    public void getInfoFB() {
        Session session = Session.getActiveSession();
        if (isLogin()) {
            Request request = Request.newMeRequest(session, new Request.GraphUserCallback() {
                @Override
                public void onCompleted(GraphUser user, Response response) {
                    pendingAction = PendingAction.NONE;
                    if (response.getError() != null) {
                        Toast.makeText(context, "Error login:" + response.getError(), Toast.LENGTH_SHORT).show();
                        onFacebookLoginFail(response.getError().toString());
                        return;
                    }
                    fbId = user.getId();
                    fbName = user.getName();
                    dob = user.getBirthday();
                    photo = String.format(Locale.US, "https://graph.facebook.com/%s/picture?width=200", fbId);

                    email = " ";
                    zip = " ";
                    gender = " ";
                    try {
                        if (user.asMap().containsKey("email")) {
                            email = user.asMap().get("email").toString();
                        }

                        if (user.asMap().containsKey("gender")) {
                            gender = user.asMap().get("gender").toString();
                        }

                        if (user.asMap().containsKey("location")) {
                            zip = (new JSONObject(user.asMap().get("location").toString()).getString("name"));
                        } else if (user.asMap().containsKey("hometown")) {
                            zip = (new JSONObject(user.asMap().get("hometown").toString()).getString("name"));
                        } else {
                            zip = "";
                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });
            request.executeAsync();

        } else {
            facebookLogin();
        }
    }

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
            Session.openActiveSession(getActivity(), this, true, Arrays.asList("email", PERMISSION_BIRTHDAY, PERMISSION_BIRTHDAY, PERMISSION_LOCATION, PERMISSION_HOMETOWN), statusCallback);
        }
    }

    public void onFacebookLoginSuccess(String []strings){

    }

    public void onFacebookLoginFail(String error){

    }


}
