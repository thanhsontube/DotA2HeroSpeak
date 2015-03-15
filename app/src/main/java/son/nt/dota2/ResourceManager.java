package son.nt.dota2;

import android.content.Context;
import android.os.Environment;

import java.io.File;

import son.nt.dota2.loader.MyPath;
import son.nt.dota2.loader.base.ContentManager;

/**
 * Created by Sonnt on 3/14/2015.
 */
public class ResourceManager {
    static ResourceManager instance = null;
    private Context context;
    private MyPath myPath;
    private ContentManager contentManager;

    public String folderSave;
    public String folderAudio;
    public String folderHero;

    public String fileHeroList;

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
            myPath = new MyPath(context);
            contentManager = new ContentManager(context, 100);
            File file = new File(Environment.getExternalStorageDirectory(), File.separator + "00-save" + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderSave = file.getPath();
            fileHeroList = folderSave +File.separator + "hero_list.dat";

            file =new File(Environment.getExternalStorageDirectory(), File.separator + "00-save" + File.separator + "audio" + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderAudio = file.getPath();

            file =new File(Environment.getExternalStorageDirectory(), File.separator + "00-save" + File.separator + "hero" + File.separator);
            if (!file.exists()) {
                file.mkdirs();
            }
            folderHero = file.getPath();


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
}
