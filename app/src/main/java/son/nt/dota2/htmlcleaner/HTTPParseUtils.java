package son.nt.dota2.htmlcleaner;

import android.content.Context;
import android.text.TextUtils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.AbilityItemAffectDto;
import son.nt.dota2.dto.AbilityLevelDto;
import son.nt.dota2.dto.AbilityNotesDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroRoleDto;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.htmlcleaner.abilities.AbilitiesLoader;
import son.nt.dota2.htmlcleaner.hero.HeroListLoader;
import son.nt.dota2.htmlcleaner.hero.HeroNameLoader;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.htmlcleaner.role.RolesLoader;
import son.nt.dota2.htmlcleaner.voice.VoiceLoader;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;

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


    public void withAbility(final String name) {
        HttpGet httpGet = new HttpGet(AbilitiesLoader.PATH_ABILITY_ROOT + name);
        ResourceManager.getInstance().getContentManager().load(new AbilitiesLoader(httpGet, true) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<AbilityDto> entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed:" + entity.size());
                //put this data on Parse

                    uploadAbilityToParse(entity, name);

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
     */
    public void withHeroList() {
        Logger.debug(TAG, ">>>" + ">>>withHeroList<<<");
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
        Logger.debug(TAG, ">>>" + "-----------uploadAbilityToParse:" + heroName);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(AbilityDto.class.getSimpleName());
        query.whereEqualTo("heroId", heroName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> l, ParseException e) {
                if (e != null || l.size() > 0) {
                    return;

                }

                ParseObject p = null;
                int i = 1;
                for (AbilityDto dto : list) {
                    dto.heroId = heroName;
                    p = new ParseObject(AbilityDto.class.getSimpleName());
                    p.put("no", i);
                    p.put("heroId", dto.heroId);
                    p.put("abilityName", dto.name);
                    p.put("ability", TextUtils.isEmpty(dto.ability) ? "" : dto.ability);
                    p.put("affects", TextUtils.isEmpty(dto.affects) ? "" : dto.affects);
                    p.put("damage", TextUtils.isEmpty(dto.damage) ? "" : dto.damage);
                    p.put("sound", TextUtils.isEmpty(dto.sound) ? "" : dto.sound);
                    p.put("description", TextUtils.isEmpty(dto.description) ? "" : dto.description);
                    p.put("linkImage", TextUtils.isEmpty(dto.linkImage) ? "" : dto.linkImage);
                    p.put("isUltimate", dto.isUltimate);
                    p.saveInBackground();

                    //upload AbilityLevelDto
                    for (AbilityLevelDto d: dto.listAbilityPerLevel) {
                        ParseObject p1 = new ParseObject(AbilityLevelDto.class.getSimpleName());
                        p1.put("heroId", dto.heroId);
                        p1.put("abilityName", dto.name);
                        p1.put("abilityLevelName", d.name);
                        StringBuilder value = new StringBuilder();
                        for (int j = 0; j < d.list.size(); j ++) {
                            value.append(d.list.get(j));
                            if (j < d.list.size() -1) {
                                value.append("/");
                            }
                        }
                        p1.put("abilityLevelValue", value.toString());
                        p1.saveInBackground();
                        Logger.debug(TAG, ">>>" + "OK AbilityLevelDto");
                    }

                    //upload AbilityItemAffectDto
                    for (AbilityItemAffectDto d : dto.listItemAffects) {
                        ParseObject p1 = new ParseObject(AbilityItemAffectDto.class.getSimpleName());
                        p1.put("heroId", dto.heroId);
                        p1.put("abilityName", dto.name);
                        p1.put("src", TextUtils.isEmpty(d.src)? "": d.src);
                        p1.put("alt", TextUtils.isEmpty(d.alt)? "": d.alt);
                        p1.put("text", TextUtils.isEmpty(d.text)? "": d.text);
                        p1.saveInBackground();
                        Logger.debug(TAG, ">>>" + "OK AbilityItemAffectDto");
                    }

                    //upload AbilityNotesDto
                    for (AbilityNotesDto d : dto.listNotes) {
                        ParseObject p1 = new ParseObject(AbilityNotesDto.class.getSimpleName());
                        p1.put("heroId", dto.heroId);
                        p1.put("abilityName", dto.name);
                        p1.put("notes", TextUtils.isEmpty(d.notes)? "": d. notes);
                        p1.saveInBackground();
                        Logger.debug(TAG, ">>>" + "OK AbilityNotesDto");
                    }
                    i++;
                    Logger.debug(TAG, ">>>" + "Put Parse success:" + i + ":" + dto.name);
                }


            }
        });
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

        for (final HeroEntry dto : HeroManager.getInstance().listHeroes) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
            query.whereEqualTo("heroId", dto.heroId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e != null || list.size() > 0) {
                        return;

                    }
                    ParseObject p = new ParseObject(HeroEntry.class.getSimpleName());
                    p.put("no", dto.no);
                    p.put("heroId", dto.heroId);
                    p.put("href", dto.href);
                    p.put("group", dto.group);
                    p.put("avatar", dto.avatarThumbnail);
                    p.saveInBackground();
                    Logger.debug(TAG, ">>>" + "Put updateStep1 success:" + count + ":" + dto.heroId);
                    count++;
                }
            });


        }
    }

    /*
     heroEntry.fullName = entity.fullName;
                    heroEntry.name = entity.name;
                    heroEntry.roles.clear();
                    heroEntry.lore = entity.lore;
                    heroEntry.roles.addAll(entity.roles);
     */
    private void updateStep2(final HeroEntry dto) {
        Logger.debug(TAG, ">>>" + "updateStep2:" + dto.heroId);

            ParseQuery<ParseObject> query = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
            query.whereEqualTo("heroId", dto.heroId);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e != null || list.size() > 0) {

                        ParseObject p = list.get(0);
                        String objectId = p.getObjectId();
                        Logger.debug(TAG, ">>>" + "objectId:" + objectId);

                        ParseQuery<ParseObject> mQuery = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
                        mQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
                            @Override
                            public void done(ParseObject p, ParseException e) {
                                p.put("fullName", dto.fullName);
                                p.put("name", dto.name);
                                p.put("lore", dto.lore);
                                p.saveInBackground();
                                Logger.debug(TAG, ">>>" + "updateStep2 OK with:" + dto.fullName);
                            }
                        });

                    }
                }
            });



    }

    private void updateHeroRole(HeroEntry heroEntry) {
        List <HeroRoleDto> list = new ArrayList<>();
        for (String s : heroEntry.roles) {
            HeroRoleDto heroRoleDto = new HeroRoleDto(heroEntry.heroId, s);
            list.add(heroRoleDto);
        }

        for (HeroRoleDto d : list) {
            ParseObject p = new ParseObject(HeroRoleDto.class.getSimpleName());
            p.put("heroId",d.heroId);
            p.put("roleName",d.roleName);
            p.saveInBackground();


        }
    }

    public void withVoices (final String heroId) {
        Logger.debug(TAG, ">>>" + "withVoices:" + heroId);
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
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed :" + entity.size());
                HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
                heroEntry.listSpeaks.clear();
                heroEntry.listSpeaks.addAll(entity);

                if (listener != null) {
                    listener.onFinish();
                }
                try {
                    FileUtil.saveObject(context, new HeroSpeakSaved(heroId, entity), heroId);
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


    public void setCallback(IParseCallBack cb) {
        this.listener = cb;

    }

    IParseCallBack listener;

    public interface IParseCallBack {
        void onFinish();
    }
    
    public void getAbilityFromServer (final String heroId) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(AbilityDto.class.getSimpleName());
        query.whereEqualTo("heroId", heroId);
        query.orderByAscending("no");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                List<AbilityDto> listAbi = new ArrayList<AbilityDto>();
                AbilityDto d;
                for (ParseObject p : list) {
                    d = new AbilityDto();
                    d.no = p.getInt("no");
                    d.name = p.getString("abilityName");
                    d.ability = p.getString("ability");
                    d.affects = p.getString("affects");
                    d.damage = p.getString("damage");
                    d.sound = p.getString("sound");
                    d.description = p.getString("description");
                    d.linkImage = p.getString("linkImage");
                    d.isUltimate = p.getBoolean("isUltimate");

                    //list AbilityLevelDto
//                    ParseQuery<ParseObject> q = new ParseQuery<ParseObject>(AbilityLevelDto.class.getSimpleName());
//                    q.whereEqualTo("heroId", heroId);
//                    q.whereEqualTo("abilityName", d.name);
//                    q.findInBackground(new FindCallback<ParseObject>() {
//                        @Override
//                        public void done(List<ParseObject> list, ParseException e) {
//
//                        }
//                    });
                    listAbi.add(d);
                }
                HeroManager.getInstance().getHero(heroId).listAbilities.clear();
                HeroManager.getInstance().getHero(heroId).listAbilities.addAll(listAbi);
                if (listener != null) {
                    listener.onFinish();
                }
            }
        });
    }


}
