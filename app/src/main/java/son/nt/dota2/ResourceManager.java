package son.nt.dota2;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    public String folderHero;
    public String folderBlur;
    public String folderObject;
    public String folderRingtone;
    public String folderNotification;
    public String folderAlarm;


    public String fileHeroList;
    public List<String> listKenburns = new ArrayList<>();


    public ResourceManager(Context context) {
        this.context = context;
        initialize();
    }

    public static ResourceManager getInstance() {
        return INSTANCE;
    }

    public static void createInstance (Context context) {
        INSTANCE = new ResourceManager(context);
    }
    private void initialize() {
        try {
            myPath = new MyPath(context);
            contentManager = new ContentManager(context, 100);
            folderSave = getContext().getFilesDir().getPath();

//            folderSave = Environment.getExternalStorageDirectory().toString() + ROOT;
            fileHeroList = folderSave +File.separator + "hero_list.dat";
            folderHero = folderSave+ File.separator + "hero" + File.separator;

            File fileAudio =new File(folderSave, "/audio/");
            if (!fileAudio.exists()) {
                fileAudio.mkdirs();
            }
            folderAudio = fileAudio.getPath();

            File fileBlur =new File(folderSave, "/blur/");
            if (!fileBlur.exists()) {
                fileBlur.mkdirs();
            }
            folderBlur = fileBlur.getPath();

            File fObject = new File(folderSave, "/object/");
            if (!fObject.exists()) {
                fObject.mkdirs();
            }

            folderObject = fObject.getPath();

            File fRingtone = new File (folderSave, "/ringtone/");
            folderRingtone = fRingtone.getPath();

            File fNoti = new File (folderSave, "/notification/");
            folderNotification = fNoti.getPath();

            File fAlarm = new File (folderSave, "/alarm/");
            folderAlarm = fAlarm.getPath();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }


    public ContentManager getContentManager() {
        return contentManager;
    }

    public String getPathAudio (String link, String heroID) {
        String path = ResourceManager.getInstance().folderAudio + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return ResourceManager.getInstance().folderAudio + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat");
    }

    public String getPathRingtone (String link, String heroID) {
        String path = ResourceManager.getInstance().folderRingtone + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return ResourceManager.getInstance().folderRingtone + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public String getPathNotification (String link, String heroID) {
        String path = ResourceManager.getInstance().folderNotification + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return ResourceManager.getInstance().folderNotification + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public String getPathAlarm (String link, String heroID) {
        String path = ResourceManager.getInstance().folderAlarm + File.separator + heroID + File.separator;
        File f = new File((path));
        if (!f.exists()) {
            f.mkdirs();
        }
        return ResourceManager.getInstance().folderAlarm + File.separator + heroID + File.separator + FileUtil.createPathFromUrl(link).replace(".dat", ".mp3");
    }

    public MyPath getMyPath() {
        return myPath;
    }
}
