package son.nt.dota2;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.Arrays;

import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;

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
    public String getLinkAvatar() {
        if (isLogin() && getProfile() != null) {
            return getProfile().getProfilePictureUri(100, 100).toString();
        }
        return null;
    }

    public String getFromName () {
        if (isLogin() && getProfile() != null) {
            return getProfile().getName();
        }
        return "";
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

    public void shareViaDialogFb (Activity activity, SpeakDto dto) {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String title = dto.heroId + ">>> " + dto.text;
            String text = MsConst.LINK_STORE;
            TsGaTools.trackPages(MsConst.TRACK_SHARE_VOICE);
            ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()
                    .setContentTitle(title)
                    .setContentDescription(text)
                    .setContentUrl(Uri.parse(dto.link))
                    .build();

            ShareDialog shareDialog = new ShareDialog(activity);
            shareDialog.show(shareLinkContent);
        }

    }

    public void shareViaTwitter (Context c, ISound dto) {
        String title = dto.getTitle() ;
        String text = dto.getTitle() + ">>>Download App:" + MsConst.LINK_STORE;
        Intent tweet = new Intent(Intent.ACTION_SEND);
        tweet.setType("text/plain");
        tweet.putExtra(Intent.EXTRA_SUBJECT, title);
        tweet.putExtra(Intent.EXTRA_TEXT, text);
        c.startActivity(Intent.createChooser(tweet, String.format("Share %s via", dto.getTitle())));
    }

    public void logout () {
        LoginManager.getInstance().logOut();
    }


}
