package son.nt.dota2.manager;

import rx.Observable;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 2/15/17.
 */

public interface ISaveFetchManager {

    Observable<Boolean> saveFromBigSavedFile();

    Observable<Boolean> saveHeroBasicToJsonFile(HeroBasicDto d);
}
