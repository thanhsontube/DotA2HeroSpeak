package son.nt.dota2.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/6/16.
 */

public interface IHeroRepository {

    Observable<Boolean> storeAllHeroes(List<HeroBasicDto> heroes);

    Observable<Boolean> storeAllLordResponses(List<HeroResponsesDto> list);

    Observable<Boolean> storeAlItemsResponses(List<ItemDto> list);

    Observable<List<HeroBasicDto>> getAllHeroes();

    Observable<List<HeroBasicDto>> getHeroesFromGroup(@NonNull String group);

    Observable<List<HeroBasicDto>> getAgiHeroes();

    Observable<List<HeroBasicDto>> getStrHeroes();

    Observable<List<HeroBasicDto>> getIntelHeroes();

    Observable<List<HeroBasicDto>> searchHero(String query);

    Observable<HeroBasicDto> getHeroFromId(String heroId);

    Observable<List<HeroResponsesDto>> getSounds(String heroID);
}
