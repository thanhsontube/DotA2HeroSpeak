package son.nt.dota2;

import android.app.Application;

import com.facebook.FacebookSdk;

import son.nt.dota2.data.TsSqlite;
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
    }
}
