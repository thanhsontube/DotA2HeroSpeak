package son.nt.dota2.data;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Completable;
import rx.Observable;
import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 11/6/16.
 */

public interface IHeroRepository {

    Observable<Boolean> storeAllHeroes(List<HeroBasicDto> heroes);

    Observable<Boolean> storeAllLordResponses(List<HeroResponsesDto> list);

    Observable<Boolean> storeAlItemsResponses(List<ItemDto> list);

    Observable<Boolean> storeAbis(List<AbilitySoundDto> list);

    Observable<List<HeroBasicDto>> getAllHeroes();

    Observable<List<AbilitySoundDto>> getAllAbility();

    Observable<List<HeroBasicDto>> getHeroesFromGroup(@NonNull String group);

    Observable<List<HeroBasicDto>> getAgiHeroes();

    Observable<List<HeroBasicDto>> getStrHeroes();

    Observable<List<HeroBasicDto>> getIntelHeroes();

    Observable<List<HeroBasicDto>> searchHero(String query);

    Observable<HeroBasicDto> getHeroFromId(String heroId);

    Observable<List<HeroResponsesDto>> getSounds(String heroID);

    Observable<List<HeroResponsesDto>> searchSounds(String keyword);

    Observable<List<AbilitySoundDto>> getAbis(String heroID);

    void saveStoryPart(StoryPartDto dto);

    Observable<List<StoryPartDto>> getCurrentCreateStory();
}
