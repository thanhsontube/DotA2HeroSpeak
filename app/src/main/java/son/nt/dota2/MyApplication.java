package son.nt.dota2;

import android.app.Application;

import son.nt.dota2.data.TsSqlite;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());
        TsSqlite.createInstance(getApplicationContext());
    }
}
