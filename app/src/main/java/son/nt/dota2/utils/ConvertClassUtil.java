package son.nt.dota2.utils;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.story.StoryDto;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 12/9/16.
 */

public class ConvertClassUtil {

    public static StoryFireBaseDto createStoryFireBaseDto (StoryDto s) {
        StoryFireBaseDto dto = new StoryFireBaseDto(s.getStoryId(), s.getUserId(), s.getTitle(), s.getCreatedTime());
        List<StoryPartDto> list = new ArrayList<>();
        for (StoryPartDto d : s.getContents()) {
            list.add(d);
        }

        dto.setContents(list);
        return dto;
    }
}
