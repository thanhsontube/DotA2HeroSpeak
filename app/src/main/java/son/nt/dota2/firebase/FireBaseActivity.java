package son.nt.dota2.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;

/**
 * Created by sonnt on 10/9/16.
 */

public abstract class FireBaseActivity extends ASafeActivity implements GoogleApiClient.OnConnectionFailedListener {

    DatabaseReference mFirebaseDatabaseReference;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        injectDI();
    }

    private void injectDI() {
        GoogleApiClientModule googleApiClientModule = new GoogleApiClientModule(this, getString(R.string.default_web_client_id), this);
        DaggerGoogleApiComponent.builder().googleApiClientModule(googleApiClientModule).build().inject(this);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
