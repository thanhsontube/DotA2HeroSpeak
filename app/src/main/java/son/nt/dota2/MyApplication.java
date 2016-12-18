package son.nt.dota2;

import com.facebook.FacebookSdk;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import son.nt.dota2.di.component.app.AppComponent;
import son.nt.dota2.di.component.app.DaggerAppComponent;
import son.nt.dota2.di.module.app.AppModule;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.service.PlayService2;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsGaTools;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends Application {


    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
//        ResourceManager.createInstance(getApplicationContext());

//        HeroManager.createInstance(getApplicationContext());

        //FACEBOOK SDK
        FacebookSdk.sdkInitialize(getApplicationContext());


        TsGaTools.createInstance(getApplicationContext());

        HTTPParseUtils.createInstance(getApplicationContext());

        FacebookManager.createInstance(getApplicationContext());
//
//        //history chat
        setupCalligraphy();
        setupRealm();
        setupDi();

        Logger.setDEBUG(BuildConfig.DEBUG);

        //
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        startWholeAppMusicService();

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

    private void startWholeAppMusicService() {
        startService(PlayService2.getIntentService(this));
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        stopService(PlayService2.getIntentService(this));
    }
}
