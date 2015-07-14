package son.nt.dota2;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;

import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.dto.HeroManager;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());
        TsSqlite.createInstance(getApplicationContext());
        TsGaTools.createInstance(getApplicationContext());
        FacebookSdk.sdkInitialize(getApplicationContext());
        HeroManager.createInstance(getApplicationContext());

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "MXj3eiU5G9qQxEHD9aVFdbHZbtcInx6v1VNIjmyf", "LsnCDCPtmWxnLVeKQg7Vc5oSEYQGJR6Tcz3Ettwi");
    }
}
