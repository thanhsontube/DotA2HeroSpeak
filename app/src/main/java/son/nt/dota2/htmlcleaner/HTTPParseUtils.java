package son.nt.dota2.htmlcleaner;

import org.apache.http.client.methods.HttpGet;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.dto.save.SaveBasicHeroData;
import son.nt.dota2.dto.save.SaveHeroAbility;
import son.nt.dota2.dto.save.SaveMusicPack;
import son.nt.dota2.htmlcleaner.abilities.AbilitiesLoader;
import son.nt.dota2.htmlcleaner.abilities.ArcAbilitiesLoader;
import son.nt.dota2.htmlcleaner.hero.HeroListLoader;
import son.nt.dota2.htmlcleaner.hero.HeroNameLoader;
import son.nt.dota2.htmlcleaner.musicPack.MusicPackDetailsLoader;
import son.nt.dota2.htmlcleaner.musicPack.MusicPackLoader;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.htmlcleaner.role.RolesLoader;
import son.nt.dota2.htmlcleaner.voice.ArcVoiceLoader;
import son.nt.dota2.htmlcleaner.voice.VoiceLoader;
import son.nt.dota2.ottobus_entry.GoAdapterMusicPackDetails;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {
    public static final String TAG = "HTTPParseUtils";

    static HTTPParseUtils INSTANCE = null;
    public Context context;
    int max = 0;
    int start = 0;

    public static void createInstance(Context context) {
        INSTANCE = new HTTPParseUtils(context);

    }

    public HTTPParseUtils(Context context) {
        this.context = context;
    }

    public static HTTPParseUtils getInstance() {
        return INSTANCE;
    }

    public void withRoles() {
        HttpGet httpGet = new HttpGet(RolesLoader.PATH_ROLES);
        ResourceManager.getInstance().getContentManager().load(new RolesLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<RoleDto> entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed");
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }


    public void withAbility(final String heroID) {
        HttpGet httpGet = new HttpGet(AbilitiesLoader.PATH_ABILITY_ROOT + heroID);
        ResourceManager.getInstance().getContentManager().load(new AbilitiesLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<AbilityDto> entity) {
                for (AbilityDto d : entity) {
                    d.heroId = heroID;
                }
                Logger.debug(TAG, ">>>=====>>>>" + "withAbility hero:" + heroID + ";total abilities:" + entity.size());
                SaveHeroAbility saveHeroAbility = new SaveHeroAbility(heroID, entity);
                try {
                    FileUtil.saveAbilityObject(context, saveHeroAbility, heroID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onFinish();
                }

                //put this data on Parse

//                    uploadAbilityToParse(entity, name);

//                HeroEntry heroEntry = new HeroEntry();
//                heroEntry.name = name;
//                heroEntry.listAbilities.addAll(entity);
//                OttoBus.post(heroEntry);
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }

    public void withArcAbility(final String heroID) {
        HttpGet httpGet = new HttpGet(MsConst.ROOT_DOTA2 + heroID);
        ResourceManager.getInstance().getContentManager().load(new ArcAbilitiesLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<AbilityDto> entity) {
                for (AbilityDto d : entity) {
                    d.heroId = heroID;
                }
                Logger.debug(TAG, ">>>=====>>>>" + "withAbility hero:" + heroID + ";total abilities:" + entity.size());
                SaveHeroAbility saveHeroAbility = new SaveHeroAbility(heroID, entity);
                try {
                    FileUtil.saveAbilityObject(context, saveHeroAbility, heroID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (listener != null) {
                    listener.onFinish();
                }

                //put this data on Parse

//                    uploadAbilityToParse(entity, name);

//                HeroEntry heroEntry = new HeroEntry();
//                heroEntry.name = name;
//                heroEntry.listAbilities.addAll(entity);
//                OttoBus.post(heroEntry);
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }

    /**
     * get basic information of heroes
     * Notes: this function will be never call again, because I save this Object to Asset with Name : SaveBasicHeroData
     */
    public void withHeroListBasic() {
        Logger.debug(TAG, ">>>" + ">>>withHeroListBasic<<<");
        HttpGet httpGet = new HttpGet(HeroListLoader.URL_HERO_LIST);
        ResourceManager.getInstance().getContentManager().load(new HeroListLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<HeroEntry> entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed:" + entity.size());
                HeroManager.getInstance().listHeroes.clear();
                HeroManager.getInstance().listHeroes.addAll(entity);

                //save object
                try {
                    FileUtil.saveObject(context, new SaveBasicHeroData(entity), SaveBasicHeroData.class.getSimpleName());
                } catch (IOException e) {
                    e.printStackTrace();
                }

//                updateStep1();
//
//                for (HeroEntry dto : entity.getListHeroes()) {
//                    withHeroName(dto.heroId);
//                }


                if (listener != null) {
                    listener.onFinish();
                }

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }

    public void withHeroListFromParse() {

    }

    public void withHeroName(final String heroId) {
        String url = "http://dota2.gamepedia.com/" + heroId;
        Logger.debug(TAG, ">>>" + "withHeroName:" + url);
        HttpGet httpGet = new HttpGet(url);
        ResourceManager.getInstance().getContentManager().load(new HeroNameLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "withHeroName onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(HeroEntry entity) {
                entity.heroId = heroId;
                Logger.debug(TAG, ">>>" + "withHeroName onContentLoaderSucceed:" + entity.fullName);
                HeroEntry heroEntry = HeroManager.getInstance().getHero(entity.heroId);
                if (heroEntry != null) {
                    heroEntry.fullName = entity.fullName;
                    heroEntry.name = entity.name;
                    heroEntry.roles.clear();
                    heroEntry.lore = entity.lore;
                    heroEntry.roles.addAll(entity.roles);
                    Logger.debug(TAG, ">>>" + "----hero:" + entity.heroId);
                    for (String s : entity.roles) {
                        Logger.debug(TAG, ">>>" + "role:" + s);
                    }
                    if (listener != null) {
                        listener.onFinish();
                    }
//                    updateHeroRole(entity);
//                    updateStep2(entity);
                }

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "withHeroName onContentLoaderFailed:" + e.toString());
            }
        });
    }

    private void uploadAbilityToParse(final List<AbilityDto> list, final String heroName) {

    }

    //upload
    /*
    No
     String heroId = "";
            String href = "";
            String group = "";
            String avatar = "";
     */


    int count = 1;

    private void updateStep1() {

    }

    /*
     heroEntry.fullName = entity.fullName;
                    heroEntry.name = entity.name;
                    heroEntry.roles.clear();
                    heroEntry.lore = entity.lore;
                    heroEntry.roles.addAll(entity.roles);
     */
    private void updateStep2(final HeroEntry dto)
    {

    }

    private void updateHeroRole(HeroEntry heroEntry) {

    }

    public void withVoices(final String heroId) {
        Logger.debug(TAG, ">>>" + "====== withVoices:" + heroId);
        String pathSpeak = String.format(VoiceLoader.PATH, heroId);
        if (pathSpeak.contains("Natures_Prophet")) {
            pathSpeak = pathSpeak.replace("Natures", "Nature's");
        }
        HttpGet httpGet = new HttpGet(pathSpeak);
        ResourceManager.getInstance().getContentManager().load(new VoiceLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "withVoices start");
            }

            @Override
            public void onContentLoaderSucceed(List<SpeakDto> entity) {
                Logger.debug(TAG, ">>>" + "withVOICE:" + heroId + " onContentLoaderSucceed :" + entity.size());
                for (SpeakDto p : entity) {
                    p.heroId = heroId;
                }
//                HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
//                heroEntry.listSpeaks.clear();
//                heroEntry.listSpeaks.addAll(entity);
//
//                if (listener != null) {
//                    listener.onFinish();
//                }
                try {
                    FileUtil.saveObject(context, new HeroSpeakSaved(heroId, entity), "voice_" + heroId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "with Voices Error:" + e);
            }
        });

    }

    public void withArcVoices(final String heroId) {
        Logger.debug(TAG, ">>>" + "====== withArcVoices:" + heroId);
        String pathSpeak = String.format(VoiceLoader.PATH, heroId);
        HttpGet httpGet = new HttpGet(pathSpeak);
        ResourceManager.getInstance().getContentManager().load(new ArcVoiceLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "withArcVoices start");
            }

            @Override
            public void onContentLoaderSucceed(List<SpeakDto> entity) {
                Logger.debug(TAG, ">>>" + "withArcVoices:" + heroId + " onContentLoaderSucceed :" + entity.size());
                for (SpeakDto p : entity) {
                    p.heroId = heroId;
                }
//                HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
//                heroEntry.listSpeaks.clear();
//                heroEntry.listSpeaks.addAll(entity);
//
//                if (listener != null) {
//                    listener.onFinish();
//                }
                try {
                    FileUtil.saveObject(context, new HeroSpeakSaved(heroId, entity), "voice_" + heroId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + " withArcVoices Error:" + e);
            }
        });

    }


    public void setCallback(IParseCallBack cb) {
        this.listener = cb;

    }

    IParseCallBack listener;

    public interface IParseCallBack {
        void onFinish();
    }

    public void getAbilityFromServer(final String heroId) {

    }


    public void withMusicPacksList() {
        HttpGet httpGet = new HttpGet("http://dota2.gamepedia.com/Music");
        ResourceManager.getInstance().getContentManager().load(new MusicPackLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
            }

            @Override
            public void onContentLoaderSucceed(List<MusicPackDto> entity) {
                Logger.debug(TAG, ">>> :" + "withMusicPacksList onContentLoaderSucceed:");


                //add the default
                MusicPackDto dto = new MusicPackDto();
                dto.setName("Default Music Pack");
                dto.setLinkDetails("http://dota2.gamepedia.com/Music");
                dto.setCoverColor("#000000");
                dto.setHref("http://images.akamai.steamusercontent.com/ugc/433773677027904120/CC1E0F736AB7FAFFC297C87732B56422FEF9BF8D/");
                entity.add(0, dto);

                OttoBus.post(new SaveMusicPack(entity));

                ResourceManager.getInstance().saveMusicPack.list = entity;

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>> Error:" + "withMusicPacksList onContentLoaderFailed:" + e);
            }
        });
    }

    public void withMusicPacksDetails(String link) {
        if (link == null) {
            link = "http://dota2.gamepedia.com/Heroes_Within_Music_Pack";
        }
        HttpGet httpGet = new HttpGet(link);
        ResourceManager.getInstance().getContentManager().load(new MusicPackDetailsLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
            }

            @Override
            public void onContentLoaderSucceed(List<MusicPackSoundDto> entity) {
                Logger.debug(TAG, ">>> :" + "withMusicPacksDetails onContentLoaderSucceed:");
                OttoBus.post(new GoAdapterMusicPackDetails(entity));

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>> Error:" + "withMusicPacksDetails onContentLoaderFailed:" + e);
            }
        });
    }

    int i = 0;

    public void withMusicPacksDetails2() {
        String linkDetails = ResourceManager.getInstance().saveMusicPack.list.get(i).getLinkDetails();
//        HttpGet httpGet = new HttpGet("http://dota2.gamepedia.com/Heroes_Within_Music_Pack");
        HttpGet httpGet = new HttpGet(linkDetails);
        ResourceManager.getInstance().getContentManager().load(new MusicPackDetailsLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
            }

            @Override
            public void onContentLoaderSucceed(List<MusicPackSoundDto> entity) {
                Logger.debug(TAG, ">>> :" + "withMusicPacksDetails onContentLoaderSucceed:" + i);
                ResourceManager.getInstance().saveMusicPack.list.get(i).setList(entity);
                if (i < ResourceManager.getInstance().saveMusicPack.list.size() - 1) {
                    i++;
                    withMusicPacksDetails2();
                } else {
                    Logger.debug(TAG, ">>>" + "DONE  final ");

                    //update inside
                    for (MusicPackDto dto : ResourceManager.getInstance().saveMusicPack.list)
                    {
                        if (dto.getList() != null)
                        {
                            for (MusicPackSoundDto d : dto.getList())
                            {
                                d.setGroup(dto.getName());
                                d.setImage(dto.getHref());
                                d.setItemId("music_pack_" + FileUtil.createPathFromUrl(d.getLink()));
                            }
                        }

                    }
                    saveObject();

                    OttoBus.post(ResourceManager.getInstance().saveMusicPack);
                }

            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>> Error:" + "withMusicPacksDetails onContentLoaderFailed:" + e);
            }
        });
    }

    private void saveObject() {
        try {
            File woFile = new File(ResourceManager.getInstance().folderRingtone + File.separator + "musicPackData.json");
            if (woFile.exists()) {
                woFile.delete();
            }
            woFile.createNewFile();
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(woFile));
            oos.writeObject(ResourceManager.getInstance().saveMusicPack);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void readObject() {
        try {
            File woFile = new File(ResourceManager.getInstance().folderRingtone + File.separator + "musicPackData.json");
            if (!woFile.exists()) {
                return;
            }
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(woFile));
            SaveMusicPack wo = ((SaveMusicPack) ois.readObject());
            ois.close();
            Logger.debug(TAG, ">>>" + "wo:" + wo.list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
