package son.nt.dota2;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.di.component.app.AppComponent;
import son.nt.dota2.di.component.app.DaggerAppComponent;
import son.nt.dota2.di.module.app.AppModule;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends MultiDexApplication {


    private AppComponent mAppComponent;

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
        setupDi();

        Logger.setDEBUG(BuildConfig.DEBUG);

        //
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void setupRealm() {
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private void setupCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/DroidSans.ttf")
                .setFontAttrId(R.attr.fontPath).build());
    }

    private void setupDi() {
        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public static MyApplication get(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

}
