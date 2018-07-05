package son.nt.dota2.manager;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

import son.nt.dota2.BuildConfig;

/**
 * Created by sonnt on 2/15/17.
 */

public class FolderStructureManager implements IFolderStructureManager {

    public static final String TAG = FolderStructureManager.class.getSimpleName();

    private static final String ROOT = "/00-Dota2";
    private static final String BASIC_FOLDER = "hero_basic";
    private static final String ABI_FOLDER = "hero_abi";
    private static final String VOICE_FOLDER = "hero_voice";

    public String mAppInternalFolder;
    private String mAppExternalFolder;
    Context mContext;

    public FolderStructureManager(Context context) {
        mContext = context;
    }

    @Override
    public String getInternalFolder() {
        if (BuildConfig.DEBUG) {
            final String path = Environment.getExternalStorageDirectory() + ROOT + File.separator + "00_dota_internal";
            createFolderIfNeeded(path);
            return path;
//           return getContext().getFilesDir().getPath() + File.separator + ROOT + File.separator + "00_dota_internal";

//            if (isExternalAvailable()) {
//                return Environment.getExternalStorageDirectory() + File.separator + ROOT + File.separator + "00_dota_internal";
//            } else {
//                return getContext().getFilesDir().getPath() + File.separator + ROOT + File.separator + "00_dota_internal";
//            }

        } else {

            final String path = getContext().getFilesDir().getPath() + ROOT + File.separator + "00_dota_internal";
            createFolderIfNeeded(path);
            return path;
        }
    }

    private void createFolderIfNeeded(String path) {
        File f = new File(path);
        if (!f.exists()) {
            f.mkdirs();
        }
    }

    @Override
    public String getExternalFolder() {
        final String path = Environment.getExternalStorageDirectory() + ROOT + File.separator + "01_dota_external";
        createFolderIfNeeded(path);
        return path;
    }

    @Override
    public String getHeroBasicFolderPath() {
        final String path = getInternalFolder() + File.separator + BASIC_FOLDER;
        createFolderIfNeeded(path);
        return path;
    }

    @Override
    public String getHeroAbiFolderPath() {
        final String path = getInternalFolder() + File.separator + ABI_FOLDER;
        createFolderIfNeeded(path);
        return path;
    }

    @Override
    public String getHeroVoiceFolderPath() {
        final String path = getInternalFolder() + File.separator + VOICE_FOLDER;
        createFolderIfNeeded(path);
        return path;
    }

    @Override
    public String getHeroBasicFile(@NonNull String heroId) {
        return getHeroBasicFolderPath() + File.separator + heroId + ".json";
    }

    private Context getContext() {
        return mContext;
    }

    private boolean isExternalAvailable() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
