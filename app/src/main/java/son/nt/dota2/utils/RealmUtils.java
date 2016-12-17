package son.nt.dota2.utils;

import io.realm.Realm;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/26/16.
 */

public class RealmUtils {

    public static HeroBasicDto getHeroBasicID(String heroId) {
        Realm realm = Realm.getDefaultInstance();
        final HeroBasicDto hero = realm.copyFromRealm(realm.where(HeroBasicDto.class).equalTo("heroId", heroId)
                .findFirst());
        realm.close();
        return hero;
    }

    public static void setFavorite(HeroResponsesDto dto) {
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        final HeroResponsesDto hero = realm.where(HeroResponsesDto.class).equalTo("link", dto.getLink()).findFirst();
        hero.setFavorite(true);
        realm.commitTransaction();
        realm.close();
    }
}
