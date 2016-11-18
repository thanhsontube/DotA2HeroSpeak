package son.nt.dota2.hero.hero_fragment;

import java.util.List;

import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroResponseContract {
    public interface View  {

        void showResponse(List<HeroResponsesDto> list);

    }

    public interface Presenter {

        void getData();

        void fetchBasicHeroFromHeroId(String heroId);
    }
}
