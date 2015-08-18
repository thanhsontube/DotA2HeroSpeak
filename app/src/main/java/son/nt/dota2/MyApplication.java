package son.nt.dota2;

import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.facebook.stetho.Stetho;
import com.parse.Parse;
import com.parse.ParseInstallation;

import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.TsGaTools;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());

        //FACEBOOK SDK
        FacebookSdk.sdkInitialize(getApplicationContext());


        TsSqlite.createInstance(getApplicationContext());
        TsGaTools.createInstance(getApplicationContext());
        HeroManager.createInstance(getApplicationContext());
        HTTPParseUtils.createInstance(getApplicationContext());
//
//        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
//
        Parse.initialize(this, "MXj3eiU5G9qQxEHD9aVFdbHZbtcInx6v1VNIjmyf", "LsnCDCPtmWxnLVeKQg7Vc5oSEYQGJR6Tcz3Ettwi");
        ParseInstallation.getCurrentInstallation().saveInBackground();
//
//
        FacebookManager.createInstance(getApplicationContext());
//
//        //history chat
        ChatHistoryManager.createInstance(getApplicationContext());

        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());
    }
}
