package son.nt.dota2.manager;

import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import rx.Observable;
import rx.Subscriber;
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

/**
 * Created by sonnt on 2/15/17.
 */

public class SaveFetchManager implements ISaveFetchManager {

    IFolderStructureManager mFolderStructureManager;

    public SaveFetchManager(IFolderStructureManager folderStructureManager) {
        this.mFolderStructureManager = folderStructureManager;
    }


    @Override
    public Observable<Boolean> saveFromBigSavedFile() {
        return null;
    }

    @Override
    public Observable<Boolean> saveHeroBasicToJsonFile(final HeroBasicDto d) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {

                    final String path = mFolderStructureManager.getHeroBasicFile(d.heroId);
                    Timber.d(">>>" + "path:" + path);
                    File woFile = new File(path);
                    if (!woFile.isFile()) {
                        woFile.createNewFile();
                    }
                    final FileOutputStream fileOutputStream = new FileOutputStream(woFile, false);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
                    outputStreamWriter.write(d.toString());
                    outputStreamWriter.close();

                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    Log.e("Exception", ">>> File write failed: " + e.toString());
                    subscriber.onError(e);
                }
            }
        });
    }

}
