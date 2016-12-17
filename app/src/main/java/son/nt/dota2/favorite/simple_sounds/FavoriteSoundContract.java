package son.nt.dota2.favorite.simple_sounds;

import java.util.List;

import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class FavoriteSoundContract {
    public interface View  {

        void showHeroSoundsList(List<HeroResponsesDto> list);


    }

    public interface Presenter {


    }
}
