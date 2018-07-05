package son.nt.dota2.di.module.test;

import dagger.Module;
import dagger.Provides;
import son.nt.dota2.di.scoped.ActivityScoped;
import son.nt.dota2.manager.IFolderStructureManager;
import son.nt.dota2.manager.ISaveFetchManager;
import son.nt.dota2.manager.SaveFetchManager;

/**
 * Created by sonnt on 2/16/17.
 */

@Module
public class TestModule {

    @ActivityScoped
    @Provides
    public ISaveFetchManager provideSaveFetchManager (IFolderStructureManager folderStructureManager) {
        return new SaveFetchManager(folderStructureManager);
    }
}
