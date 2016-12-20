package son.nt.dota2.story.choose_hero;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class ChooseHeroContract {
    public interface View  {

        void showList(List<HeroBasicDto> list);
    }

    public interface Presenter {


        void getAllHeroList();

        void setFilter(String s1);

        void search();

    }
}
