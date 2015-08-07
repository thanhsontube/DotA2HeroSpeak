package son.nt.dota2;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import son.nt.dota2.dto.HeroData;
import son.nt.dota2.facebook.FbLoaderManager;
import son.nt.dota2.loader.MyPath;
import son.nt.dota2.loader.base.ContentManager;
import son.nt.dota2.utils.FileUtil;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class ResourceManager {
    static ResourceManager instance = null;
    public static final String ROOT = "/00-Dota2";
    private Context context;
    private MyPath myPath;
    private ContentManager contentManager;

    public FbLoaderManager fbLoaderManager;

    public String folderSave;
    public String folderAudio;
    public String folderHero;
    public String folderBlur;
    public String folderObject;
    public String folderRingtone;
    public String folderNotification;


    public String fileHeroList;

    private HeroData heroData;

    public void setHeroData(HeroData heroData) {
        this.heroData = heroData;
    }

    public HeroData getHeroData() {
        return heroData;
    }

    public ResourceManager(Context context) {
        this.context = context;
        initialize();
    }

    public static ResourceManager getInstance() {
        return instance;
    }

    public static void createInstance (Context context) {
        instance = new ResourceManager(context);
    }
    private void initialize() {
        try {
//            myPath = new MyPath(context);
            contentManager = new ContentManager(context, 100);
//            folderSave = getContext().getFilesDir().getPath();

            folderSave = Environment.getExternalStorageDirectory().toString() + ROOT;
            fileHeroList = folderSave +File.separator + "hero_list.dat";
            folderHero = folderSave+ File.separator + "hero" + File.separator;

//            File fileAudio =new File(Environment.getExternalStorageDirectory(), File.separator + "DotA2HeroSpeak" + File.separator + "audio" + File.separator);
            File fileAudio =new File(folderSave, "/audio/");
            if (!fileAudio.exists()) {
                fileAudio.mkdirs();
            }
            folderAudio = fileAudio.getPath();

//            File fileBlur =new File(Environment.getExternalStorageDirectory(), File.separator + "DotA2HeroSpeak" + File.separator + "blur" + File.separator);
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


            fbLoaderManager = new FbLoaderManager();


        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Context getContext() {
        return context;
    }

    public MyPath getMyPath() {
        return myPath;
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
}
