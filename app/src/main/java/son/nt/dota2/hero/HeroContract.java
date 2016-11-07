package son.nt.dota2.hero;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroContract {
    public interface View  {

        void showHeroList(List<HeroBasicDto> list);
    }

    public interface Presenter {

        void getData();
    }
}
