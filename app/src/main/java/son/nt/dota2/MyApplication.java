package son.nt.dota2;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());

        HeroManager.createInstance(getApplicationContext());

        //FACEBOOK SDK
        FacebookSdk.sdkInitialize(getApplicationContext());


        TsSqlite.createInstance(getApplicationContext());
        TsGaTools.createInstance(getApplicationContext());

        HTTPParseUtils.createInstance(getApplicationContext());
//
//        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
//
        Parse.initialize(this, "MXj3eiU5G9qQxEHD9aVFdbHZbtcInx6v1VNIjmyf", "LsnCDCPtmWxnLVeKQg7Vc5oSEYQGJR6Tcz3Ettwi");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(getApplicationContext());
//
        FacebookManager.createInstance(getApplicationContext());
//
//        //history chat
        CommentManager.createInstance(getApplicationContext());
        setupCalligraphy();
        setupRealm();

        Logger.setDEBUG(BuildConfig.DEBUG);

    }

    private void setupRealm() {
        RealmConfiguration config = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(config);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }
}
