package son.nt.dota2.di.component.app;

import javax.inject.Singleton;

import dagger.Component;
import son.nt.dota2.di.component.herolist.HeroListComponent;
import son.nt.dota2.di.module.app.AppModule;
import son.nt.dota2.di.module.app.HeroRepoModule;
import son.nt.dota2.di.module.firebase.FireBaseModule;
import son.nt.dota2.di.module.herolist.HeroListModule;

/**
 * Created by sonnt on 10/14/16.
 */

@Singleton
@Component(modules = {AppModule.class, FireBaseModule.class, HeroRepoModule.class})

public interface AppComponent {
    HeroListComponent plus(HeroListModule module);
}
