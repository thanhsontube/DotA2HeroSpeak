package son.nt.dota2.story.story_details;

import java.util.List;

import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryDetailContract {

    public interface View {
        void showList(List<StoryPartDto> contents);
    }

    public interface Presenter {
        void setStoryId(StoryFireBaseDto data);

        void getStoryById();

        void playStory();

    }
}
