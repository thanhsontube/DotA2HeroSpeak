package son.nt.dota2.di.component.herolist;

import dagger.Subcomponent;
import son.nt.dota2.di.module.herolist.HeroListModule;
import son.nt.dota2.di.scoped.FragmentScoped;
import son.nt.dota2.fragment.HeroListFragment;

/**
 * Created by sonnt on 10/14/16.
 */
@FragmentScoped
@Subcomponent(modules = {HeroListModule.class})
public interface HeroListComponent {
    void inject (HeroListFragment fragment);
}
