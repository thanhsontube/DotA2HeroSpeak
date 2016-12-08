package son.nt.dota2.dto.story;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by sonnt on 12/7/16.
 */

public class StoryDto  {
    String storyId;
    String userId;
    String title;
    String createdTime;

    List<StoryPartDto> contents;

    public StoryDto() {
    }
}
