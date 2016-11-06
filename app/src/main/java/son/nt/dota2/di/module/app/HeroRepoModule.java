package son.nt.dota2.di.module.app;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;

/**
 * Created by sonnt on 10/14/16.
 */

@Module
public class HeroRepoModule {

    @Singleton
    @Provides
    IHeroRepository provideFireBaseRepo() {
        return new HeroRepository();
    }
}
