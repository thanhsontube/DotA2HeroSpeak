package son.nt.dota2;

import android.app.Application;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ResourceManager.createInstance(getApplicationContext());
    }
}
