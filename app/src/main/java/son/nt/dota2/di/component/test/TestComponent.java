package son.nt.dota2.di.component.test;

import dagger.Subcomponent;
import son.nt.dota2.di.module.test.TestModule;
import son.nt.dota2.di.scoped.ActivityScoped;
import son.nt.dota2.test.TestActivity;

/**
 * Created by sonnt on 2/16/17.
 */
@ActivityScoped
@Subcomponent (modules = {TestModule.class})
public interface TestComponent {
    void inject (TestActivity testActivity);
}
