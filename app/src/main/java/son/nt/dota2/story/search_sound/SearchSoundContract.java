package son.nt.dota2.story.search_sound;

import java.util.List;

import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class SearchSoundContract {
    public interface View  {

        void showListData(List<HeroResponsesDto> list);

        void closeActivity();
    }

    public interface Presenter {

        void setFilter(String s1);

        void search();

        void getSomeSounds();

        void setSide(String data);

        void wrapSimpleStory(String s, HeroResponsesDto heroResponsesDto);
    }
}
