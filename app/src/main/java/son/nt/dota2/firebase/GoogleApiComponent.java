package son.nt.dota2.firebase;

import dagger.Component;
import son.nt.dota2.di.scoped.ActivityScoped;

/**
 * Created by sonnt on 8/20/16.
 */
@ActivityScoped
@Component(modules = {GoogleApiClientModule.class, FireBaseModule.class})
public interface GoogleApiComponent {
    void inject(FireBaseActivity activity);

}
