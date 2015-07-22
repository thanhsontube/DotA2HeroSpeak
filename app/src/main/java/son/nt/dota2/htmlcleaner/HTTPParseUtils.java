package son.nt.dota2.htmlcleaner;

import android.content.Context;
import android.text.TextUtils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.client.methods.HttpGet;

import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.htmlcleaner.abilities.AbilitiesLoader;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.htmlcleaner.role.RolesLoader;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {
    public static final String TAG = "HTTPParseUtils";

    static HTTPParseUtils INSTANCE = null;
    public Context context;

    public static void createInstance (Context context) {
        INSTANCE = new HTTPParseUtils(context);

    }
    public HTTPParseUtils (Context context) {
        this.context = context;
    }

    public static HTTPParseUtils getInstance () {
        return INSTANCE;
    }

    public void withRoles () {
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


    public void withAbility (final String name) {
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
                upLoadToParseService(entity, name);

                HeroEntry heroEntry = new HeroEntry();
                heroEntry.name = name;
                heroEntry.listAbilities.addAll(entity);
                OttoBus.post(heroEntry);
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }

    private void upLoadToParseService(final List<AbilityDto> list, final String heroName) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(AbilityDto.class.getSimpleName());
        query.whereEqualTo("heroName", heroName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> l, ParseException e) {
                if (e != null || l.size() > 0) {
                    return;

                }

                ParseObject p = null;
                int i = 1;
                for (AbilityDto dto: list) {
                    dto.heroName = heroName;
                    p = new ParseObject(AbilityDto.class.getSimpleName());
                    p.put("no", i);
                    p.put("heroName", dto.heroName);
                    p.put("name", dto.name);
                    p.put("ability", TextUtils.isEmpty(dto.ability) ? "" : dto.ability);
                    p.put("affects", TextUtils.isEmpty(dto.affects) ? "" : dto.affects);
                    p.put("damage", TextUtils.isEmpty(dto.damage) ? "" : dto.damage);
                    p.put("sound", TextUtils.isEmpty(dto.sound) ? "" : dto.sound);
                    p.put("description", TextUtils.isEmpty(dto.description) ? "" : dto.description);
                    p.put("linkImage", TextUtils.isEmpty(dto.linkImage) ? "" : dto.linkImage);
                    p.put("isUltimate", dto.isUltimate);
                    p.saveInBackground();
                    i++;
                    Logger.debug(TAG, ">>>" + "Put Parse success:" + i +":" + dto.name );
                }


            }
        });
    }


}
