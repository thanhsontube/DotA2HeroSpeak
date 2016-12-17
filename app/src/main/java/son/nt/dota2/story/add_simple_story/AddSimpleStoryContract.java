package son.nt.dota2.story.add_simple_story;

import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class AddSimpleStoryContract {
    public interface View {

        void updateAvatar(HeroBasicDto heroBasicDto);

        void updateSelectedSound(HeroResponsesDto data);

        void closeActivity();
    }

    public interface Presenter {
        void wrapSimpleStory(String des);

        void setSide(String data);
    }
}
