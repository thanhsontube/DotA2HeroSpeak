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

    StoryFireBaseDto mStoryFireBaseDto;

    public StoryDetailPresenter(StoryDetailContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    public void setStoryFireBaseDto(StoryFireBaseDto data) {
        this.mStoryFireBaseDto = data;

    }


    @Override
    public void getStoryById() {
        mView.showList(mStoryFireBaseDto.getContents());

    }

    @Override
    public void playStory() {
        OttoBus.post(new GoStory(mStoryFireBaseDto.getContents(), mStoryFireBaseDto.getTitle(), mStoryFireBaseDto.getUserId()));

    }

    @Override
    public void stopStory() {
        OttoBus.post(new GoPlayerStop());
    }

    @Override
    public String getStoryId() {
        return mStoryFireBaseDto.getStoryId();
    }
}
