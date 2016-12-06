package son.nt.dota2.story;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.story.StoryDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryPresenter implements StoryContract.Presenter {
    StoryContract.View mView;

    public StoryPresenter(StoryContract.View view) {
        mView = view;
    }

    @Override
    public void createAddList() {
        List<StoryDto>  dtos = new ArrayList<>();
        dtos.add(new StoryDto());
        dtos.add(new StoryDto());
        dtos.add(new StoryDto());
        dtos.add(new StoryDto());
        dtos.add(new StoryDto());
        mView.showAddList (dtos);
    }
}
