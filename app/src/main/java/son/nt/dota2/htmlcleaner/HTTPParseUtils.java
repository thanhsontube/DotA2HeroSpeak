package son.nt.dota2.htmlcleaner;

import android.content.Context;

import org.apache.http.client.methods.HttpGet;

import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.htmlcleaner.abilities.AbilitiesLoader;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.htmlcleaner.role.RolesLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {

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


    public void withAbility () {
        HttpGet httpGet = new HttpGet(AbilitiesLoader.PATH_ABILITY);
        ResourceManager.getInstance().getContentManager().load(new AbilitiesLoader(httpGet, false) {
            @Override
            public void onContentLoaderStart() {
                Logger.debug(TAG, ">>>" + "onContentLoaderStart");
            }

            @Override
            public void onContentLoaderSucceed(List<AbilityDto> entity) {
                Logger.debug(TAG, ">>>" + "onContentLoaderSucceed");
            }

            @Override
            public void onContentLoaderFailed(Throwable e) {
                Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
            }
        });
    }


}
