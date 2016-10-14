package son.nt.dota2.home.herolist;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 10/14/16.
 */

public interface HeroListContract {
    interface View {

        void showHeroList(List<HeroBasicDto> heroBasicDtoList);
    }

    interface Presenter {

        void setGroup(String group);

        void getHeroList();

    }
}
