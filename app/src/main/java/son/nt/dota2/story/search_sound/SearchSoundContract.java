package son.nt.dota2.story.search_sound;

import java.util.List;

import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class SearchSoundContract {
    public interface View  {

        void showListData(List<HeroResponsesDto> list);
    }

    public interface Presenter {

        void setFilter(String s1);

        void search();

        void getSomeSounds();

    }
}
