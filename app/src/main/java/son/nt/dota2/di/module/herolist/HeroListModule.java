package son.nt.dota2.di.module.herolist;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.di.scoped.FragmentScoped;
import son.nt.dota2.firebase.IFireBaseRepository;
import son.nt.dota2.home.herolist.HeroListContract;
import son.nt.dota2.home.herolist.HeroListPresenter;

/**
 * Created by sonnt on 10/14/16.
 */
@Module
public class HeroListModule {

    HeroListContract.View mView;

    public HeroListModule(HeroListContract.View view) {
        mView = view;
    }

    @FragmentScoped
    @Provides
    HeroListContract.View provideView () {
        return mView;
    }

    @FragmentScoped
    @Provides
    HeroListContract.Presenter providePresenter (HeroListContract.View view, IFireBaseRepository iFireBaseRepository) {
        return new HeroListPresenter(view, iFireBaseRepository);
    }


}
