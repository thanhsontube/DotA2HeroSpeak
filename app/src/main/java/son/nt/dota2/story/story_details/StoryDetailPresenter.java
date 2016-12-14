package son.nt.dota2.story.story_details;

import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.ottobus_entry.GoPlayerStop;
import son.nt.dota2.ottobus_entry.GoStory;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryDetailPresenter extends BasePresenter implements StoryDetailContract.Presenter {
    StoryDetailContract.View mView;
    IHeroRepository mRepository;

    StoryFireBaseDto mStoryId;

    public StoryDetailPresenter(StoryDetailContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void setStoryId(StoryFireBaseDto data) {
        this.mStoryId = data;

    }


    @Override
    public void getStoryById() {
        mView.showList(mStoryId.getContents());



    }

    @Override
    public void playStory() {
        OttoBus.post(new GoStory(mStoryId.getContents(), mStoryId.getTitle(), mStoryId.getUserId()));

    }

    @Override
    public void stopStory() {
        OttoBus.post(new GoPlayerStop());
    }
}
