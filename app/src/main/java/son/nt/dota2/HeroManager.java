package son.nt.dota2;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroSavedDto;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public class HeroManager extends AObject{
    public static final String TAG = "HeroManager";

    public HeroData heroData;

    public List<HeroEntry> listHeroes = new ArrayList<>();

    public String[] groups = new String[]{"STRENGTH", "AGILITY", "INTELLIGENCE"};

    static HeroManager INSTANCE = null;

    private Context context;

    public static void createInstance(Context context) {
        INSTANCE = new HeroManager(context);
    }

    public HeroManager(Context context) {
        this.context = context;
//        initData();
    }

    public static HeroManager getInstance () {
        return INSTANCE;
    }

    private void initData() {
        try {
            ResourceManager resource = ResourceManager.getInstance();
            File fOut = new File(resource.folderSave, File.separator + "data.zip");
            if (fOut.exists()) {
                heroData = FileUtil.readHeroList(context);
                return;
            }
            InputStream in = context.getAssets().open("data.zip");
            OutputStream out = new FileOutputStream(fOut, false);
            int read;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
            out.close();
            in.close();

            //unzip
            boolean isUnzip = FileUtil.unpackZip(resource.folderSave + File.separator, "data.zip");
            if (isUnzip) {
                heroData = FileUtil.readHeroList(context);

            } else {
                Toast.makeText(context, "Sorry, can not initialize data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public HeroEntry getHero(String heroId) {
        for (HeroEntry dto : listHeroes) {
            if (dto.heroId.equals(heroId)) {
                return dto;
            }
        }
        return null;
    }

    public List<HeroEntry> getStrHeroes () {
        List<HeroEntry> list = new ArrayList<>();
        for (HeroEntry dto : listHeroes) {
            if (dto.group.equals("Str")) {
                list.add(dto);
            }
        }
        Collections.shuffle(list);
        return  list;
    }
    public List<HeroEntry> getAgiHeroes () {
        List<HeroEntry> list = new ArrayList<>();
        for (HeroEntry dto : listHeroes) {
            if (dto.group.equals("Agi")) {
                list.add(dto);
            }
        }
        Collections.shuffle(list);
        return  list;
    }
    public List<HeroEntry> getIntelHeroes () {
        List<HeroEntry> list = new ArrayList<>();
        for (HeroEntry dto : listHeroes) {
            if (dto.group.equals("Intel")) {
                list.add(dto);
            }
        }
        Collections.shuffle(list);
        return  list;
    }

    public void initDataSelf () {
        try {
            File fileBasicHeroList = new File (ResourceManager.getInstance().folderObject, HeroSavedDto.class.getSimpleName());
            if (!fileBasicHeroList.exists()) {
                Logger.debug(TAG, ">>>" + "COPY>>>>>>>");
                FileUtil.copyAssets(context, fileBasicHeroList.getParent());
            }

            AObject saveObject = FileUtil.getObject(context, HeroSavedDto.class.getSimpleName());
            if (saveObject != null) {
                HeroSavedDto heroData = (HeroSavedDto) saveObject;
                Logger.debug(TAG, ">>>" + "saveObject != null:" + heroData.listHeroes.size());
                HeroManager.getInstance().listHeroes.clear();
                HeroManager.getInstance().listHeroes.addAll(heroData.listHeroes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<HeroEntry> getHeroesByRole (String role) {
        List<HeroEntry> list = new ArrayList<>();
        for (HeroEntry p : listHeroes) {
            for (String s : p.roles) {
                if (role.equalsIgnoreCase(s)) {
                    list.add(p);
                }
            }
        }
        return list;
    }
}
