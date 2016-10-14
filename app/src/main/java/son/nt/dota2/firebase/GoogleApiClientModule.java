package son.nt.dota2.firebase;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.di.scoped.ActivityScoped;

/**
 * Created by sonnt on 8/20/16.
 */
@Module
public class GoogleApiClientModule {

    AppCompatActivity mActivity;
    String mIdToken;
    GoogleApiClient.OnConnectionFailedListener mConnectionFailedListener;

    public GoogleApiClientModule(AppCompatActivity fragmentActivity, String idToken, GoogleApiClient.OnConnectionFailedListener connectionFailedListener) {
        this.mActivity = fragmentActivity;
        this.mIdToken = idToken;
        this.mConnectionFailedListener = connectionFailedListener;
    }

    @Provides
    @ActivityScoped
    FragmentActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @ActivityScoped
    String provideIdToken() {
        return mIdToken;
    }

    @Provides
    @ActivityScoped
    GoogleApiClient.OnConnectionFailedListener provideConnectionFailedListener() {
        return mConnectionFailedListener;
    }

    @ActivityScoped
    @Provides
    GoogleSignInOptions provideGoogleSignInOptions(String idToken) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken).requestEmail().build();
        return gso;
    }

    @ActivityScoped
    @Provides
    GoogleApiClient provideGoogleApiClient(GoogleSignInOptions googleSignInOptions, FragmentActivity fragmentActivity, GoogleApiClient.OnConnectionFailedListener onConnectionFailedListener) {
        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(fragmentActivity)
                .enableAutoManage(fragmentActivity /* FragmentActivity */,
                        onConnectionFailedListener /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions).build();
        return googleApiClient;
    }
}
