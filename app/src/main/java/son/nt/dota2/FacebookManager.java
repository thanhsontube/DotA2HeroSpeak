package son.nt.dota2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 8/10/15.
 */
public class FacebookManager {
    public static final String TAG = "FacebookManager";
    public static  FacebookManager INSTANCE = null;
    public Context context;
    public String fromName;
    public String fromID;

    CallbackManager callbackManager;

    public static final void createInstance(Context context) {
        INSTANCE = new FacebookManager(context);
    }

    public FacebookManager (Context context) {
        this.context = context;
        initFacebook();
    }

    private void initFacebook () {
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Logger.debug(TAG, ">>>" + "initFacebook onSuccess");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {
                Logger.error(TAG, ">>>" + "initFacebook onError:" + e.toString());

            }
        });
    }

    public static FacebookManager getInstance () {
        return INSTANCE;
    }

    public boolean isLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null;
    }

    public Profile getProfile () {
        return Profile.getCurrentProfile();
    }

    public void login (Fragment fragment) {
        LoginManager.getInstance().logInWithReadPermissions(fragment, Arrays.asList(""));
    }

    public void requestUser () {
        GraphRequest graphRequest = GraphRequest.newGraphPathRequest(AccessToken.getCurrentAccessToken(),
                "", new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {

                    }
                });
        Bundle bundle = new Bundle();
        graphRequest.setParameters(bundle);
        graphRequest.executeAsync();
    }


}
