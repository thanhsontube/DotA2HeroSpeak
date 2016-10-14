package son.nt.dota2.di.module.app;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sonnt on 10/14/16.
 * This is a module is used to pass Context dependence to the
 * {@link}
 */

@Module
public class AppModule {
    private final Context mContext;

    public AppModule(Context context) {
        mContext = context;
    }

    @Singleton
    @Provides
    Context provideContext() {
        return mContext;
    }

}
