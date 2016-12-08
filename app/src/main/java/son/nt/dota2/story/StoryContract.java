package son.nt.dota2.story;

import java.util.List;

import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class StoryContract {

    public interface View {
        void showAddList(List<StoryPartDto> dtos);
    }

    public interface Presenter {
        void createAddList();
    }
}
