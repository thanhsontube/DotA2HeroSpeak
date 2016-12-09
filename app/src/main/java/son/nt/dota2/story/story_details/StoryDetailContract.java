package son.nt.dota2.story.story_details;

import io.realm.RealmList;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryDetailContract {

    public interface View {
        void showList(RealmList<StoryPartDto> contents);
    }

    public interface Presenter {
        void setStoryId(String data);

        void getStoryById();

        void playStory();

    }
}
