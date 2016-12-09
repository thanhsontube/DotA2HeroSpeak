package son.nt.dota2.story.story_list;

import java.util.List;

import son.nt.dota2.dto.story.StoryDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryListContract {

    public interface View {
        void showAddList(List<StoryDto> storyDtos);
    }

    public interface Presenter {
        void getStoryList();
    }
}
