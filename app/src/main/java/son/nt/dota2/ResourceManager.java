package son.nt.dota2;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.save.SaveMusicPack;
import son.nt.dota2.loader.MyPath;
import son.nt.dota2.loader.base.ContentManager;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class ResourceManager {

    private static final String TAG = ResourceManager.class.getSimpleName();
    static ResourceManager INSTANCE = null;
    public static final String ROOT = "/00-Dota2";
    private Context context;
    private MyPath myPath;
    private ContentManager contentManager;


    public String mAppInternalFolder;
    public String folderAudio;
    public String folderMusicPack;
    public String folderHero;
    public String folderBlur;
    public String folderObject;

    private String mAppExternalFolder;

    public String folderRingtone;
    public String folderNotification;
    public String folderAlarm;


    public String fileHeroList;
    public List<String> listKenburns = new ArrayList<>();
    public SaveMusicPack saveMusicPack;


    public ResourceManager(Context context) {
        this.context = context;
        initialize();
        copyAssets();
    }

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public static void createInstance(Context context) {
        INSTANCE = new ResourceManager(context);
    }

    private void initialize() {
        try {
            myPath = new MyPath(context);
            contentManager = new ContentManager(context, 100);
            saveMusicPack = new SaveMusicPack();

            //saving file :
            if (BuildConfig.DEBUG) {
                mAppInternalFolder = Environment.getExternalStorageDirectory() + File.separator + ROOT + File.separator + "00_dota_internal";

            } else {
                mAppInternalFolder = getContext().getFilesDir().getPath() + File.separator + ROOT + File.separator + "00_dota_internal";
            }
            mAppExternalFolder = Environment.getExternalStorageDirectory() + File.separator + ROOT + File.separator + "01_dota_external";

            Logger.debug(TAG, ">>>" + "mAppInternalFolder:" + mAppInternalFolder);

            Logger.debug(TAG, ">>>" + "mAppExternalFolder:" + mAppExternalFolder);


            File fExternal = new File(mAppExternalFolder);
            if (!fExternal.exists()) {
                fExternal.mkdirs();
            }
            File fInternal = new File(mAppInternalFolder);
            if (!fInternal.exists()) {
                fInternal.mkdirs();
            }


            fileHeroList = mAppInternalFolder + File.separator + "hero_list.dat";
            folderHero = mAppInternalFolder + File.separator + "hero" + File.separator;

            File fileAudio = new File(mAppInternalFolder, "/audio/");
            if (!fileAudio.exists()) {
                fileAudio.mkdirs();
            }
            folderAudio = fileAudio.getPath();

            File fileMusicPack = new File(mAppInternalFolder, "/musicPack/");
            if (!fileMusicPack.exists()) {
                fileMusicPack.mkdirs();
            }
            folderMusicPack = fileMusicPack.getPath();


            File fileBlur = new File(mAppInternalFolder, "/blur/");
            if (!fileBlur.exists()) {
                fileBlur.mkdirs();
            }
            folderBlur = fileBlur.getPath();

            File fObject = new File(mAppInternalFolder, "/object/");
            if (!fObject.exists()) {
                fObject.mkdirs();
            }

            folderObject = fObject.getPath();

            File fRingtone = new File(mAppExternalFolder, "/ringtone/");
            if (!fRingtone.exists()) {
                fRingtone.mkdirs();
            }
            folderRingtone = fRingtone.getPath();

            File fNoti = new File(mAppExternalFolder, "/notification/");
            if (!fNoti.exists()) {
                fNoti.mkdirs();
            }
            folderNotification = fNoti.getPath();

            File fAlarm = new File(mAppExternalFolder, "/alarm/");
            if (!fAlarm.exists()) {
                fAlarm.mkdirs();
            }
            folderAlarm = fAlarm.getPath();

        } catch (Exception e) {
            Logger.error(TAG, ">>> Error:" + "e:" + e.toString());
        }
    }

    public Context getContext() {
        return context;
    }


    public ContentManager getContentManager() {
        return contentManager;
    }

    public String getPathSound(String link, String root, String branch) {
        String path = ResourceManager.getInstance().getAppInternalFolder() + File.separator + root + File.separator + branch;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return path + File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat");
    }

    public String getPathAudio(String link, String heroID) {
        String path = ResourceManager.getInstance().folderAudio + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return ResourceManager.getInstance().folderAudio + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat");
    }

    public String getPathMusicPack(String link) {

        return ResourceManager.getInstance().folderMusicPack + File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat");
    }

    public String getPathRingtone(String link, String heroID) {
        String path = ResourceManager.getInstance().folderRingtone + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }

        File fRingtone = new File(mAppExternalFolder, "/ringtone/");
        if (!fRingtone.exists()) {
            fRingtone.mkdirs();
        }
        folderRingtone = fRingtone.getPath();
        return folderRingtone + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public String getPathNotification(String link, String heroID) {
        String path = ResourceManager.getInstance().folderNotification + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }


        File fNoti = new File(mAppExternalFolder, "/notification/");
        if (!fNoti.exists()) {
            fNoti.mkdirs();
        }
        folderNotification = fNoti.getPath();
        return folderNotification + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public String getPathAlarm(String link, String heroID) {
        String path = ResourceManager.getInstance().folderAlarm + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }

        File fAlarm = new File(mAppExternalFolder, "/alarm/");
        if (!fAlarm.exists()) {
            fAlarm.mkdirs();
        }
        folderAlarm = fAlarm.getPath();
        return folderAlarm + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public String getPathDownloadMusicPack(String link) {
        File save = new File(Environment.getExternalStorageDirectory(), "DownloadDota2Sound");
        if (!save.exists()) {
            save.mkdirs();
        }

        File fDetails = new File(save, link + ".mp3");
        return fDetails.getPath();
    }

    public MyPath getMyPath() {
        return myPath;
    }


    public String getFolderAudio() {
        File fileAudio = new File(mAppInternalFolder, "/audio/");
        if (!fileAudio.exists()) {
            fileAudio.mkdirs();
        }
        folderAudio = fileAudio.getPath();
        return folderAudio;
    }

    public String getFolderHero() {
        folderHero = mAppInternalFolder + File.separator + "hero" + File.separator;
        return folderHero;
    }

    public String getFolderObject() {

        File fObject = new File(mAppInternalFolder, "/object/");
        if (!fObject.exists()) {
            fObject.mkdirs();
        }

        folderObject = fObject.getPath();


        return folderObject;
    }

    public String getFolderMusicPack() {
        return folderMusicPack;
    }

    public String getAppInternalFolder() {
//        mAppInternalFolder = getContext().getFilesDir().getPath();
        return mAppInternalFolder;
    }

    private void copyAssets () {
        FileUtil.copyAssets(getContext(), "music", getFolderMusicPack());
    }
}
