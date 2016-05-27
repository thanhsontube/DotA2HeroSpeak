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

/**
 * Created by Sonnt on 3/14/2015.
 */
public class ResourceManager {
    static ResourceManager INSTANCE = null;
    public static final String ROOT = "/00-Dota2";
    private Context context;
    private MyPath myPath;
    private ContentManager contentManager;


    public String folderSave;
    public String folderAudio;
    public String folderMusicPack;
    public String folderHero;
    public String folderBlur;
    public String folderObject;

    private String folderRoot;
    public String folderRingtone;
    public String folderNotification;
    public String folderAlarm;


    public String fileHeroList;
    public List<String> listKenburns = new ArrayList<>();
    public SaveMusicPack saveMusicPack;


    public ResourceManager(Context context) {
        this.context = context;
        initialize();
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
            folderSave = getContext().getFilesDir().getPath();

            saveMusicPack = new SaveMusicPack();

            folderRoot = Environment.getExternalStorageDirectory().toString() + ROOT;

            File fRoot = new File(folderRoot);
            if (!fRoot.exists()) {
                fRoot.mkdirs();
            }
            fileHeroList = folderSave + File.separator + "hero_list.dat";
            folderHero = folderSave + File.separator + "hero" + File.separator;

            File fileAudio = new File(folderSave, "/audio/");
            if (!fileAudio.exists()) {
                fileAudio.mkdirs();
            }
            folderAudio = fileAudio.getPath();

//            File fileMusicPack = new File(folderSave, "/musicPack/");
            //todo hack
            File fileMusicPack = new File(folderRoot, "/musicPack/");
            if (!fileMusicPack.exists()) {
                fileMusicPack.mkdirs();
            }
            folderMusicPack = fileMusicPack.getPath();


            File fileBlur = new File(folderSave, "/blur/");
            if (!fileBlur.exists()) {
                fileBlur.mkdirs();
            }
            folderBlur = fileBlur.getPath();

            File fObject = new File(folderSave, "/object/");
            if (!fObject.exists()) {
                fObject.mkdirs();
            }

            folderObject = fObject.getPath();

            File fRingtone = new File(folderRoot, "/ringtone/");
            if (!fRingtone.exists()) {
                fRingtone.mkdirs();
            }
            folderRingtone = fRingtone.getPath();

            File fNoti = new File(folderRoot, "/notification/");
            if (!fNoti.exists()) {
                fNoti.mkdirs();
            }
            folderNotification = fNoti.getPath();

            File fAlarm = new File(folderRoot, "/alarm/");
            if (!fAlarm.exists()) {
                fAlarm.mkdirs();
            }
            folderAlarm = fAlarm.getPath();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }


    public ContentManager getContentManager() {
        return contentManager;
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
//        return ResourceManager.getInstance().folderMusicPack + File.separator + FileUtil.createPathFromUrl(link);
    }

    public String getPathRingtone(String link, String heroID) {
        String path = ResourceManager.getInstance().folderRingtone + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }

        File fRingtone = new File(folderRoot, "/ringtone/");
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


        File fNoti = new File(folderRoot, "/notification/");
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

        File fAlarm = new File(folderRoot, "/alarm/");
        if (!fAlarm.exists()) {
            fAlarm.mkdirs();
        }
        folderAlarm = fAlarm.getPath();
        return folderAlarm + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public MyPath getMyPath() {
        return myPath;
    }


    public String getFolderAudio() {
        File fileAudio = new File(getFolderSave(), "/audio/");
        if (!fileAudio.exists()) {
            fileAudio.mkdirs();
        }
        folderAudio = fileAudio.getPath();
        return folderAudio;
    }

    public String getFolderHero() {
        folderHero = getFolderSave() + File.separator + "hero" + File.separator;
        return folderHero;
    }

    public String getFolderBlur() {
        return folderBlur;
    }

    public String getFolderObject() {

        File fObject = new File(getFolderSave(), "/object/");
        if (!fObject.exists()) {
            fObject.mkdirs();
        }

        folderObject = fObject.getPath();


        return folderObject;
    }

    public String getFolderSave() {
        folderSave = getContext().getFilesDir().getPath();

        //test
//        folderSave = Environment.getExternalStorageDirectory().toString() + ROOT;
        return folderSave;
    }
}
