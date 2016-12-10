package son.nt.dota2.story.story_details;

import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.ottobus_entry.GoStory;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryDetailPresenter extends BasePresenter implements StoryDetailContract.Presenter {
    StoryDetailContract.View mView;
    IHeroRepository mRepository;
    String mUserId = "sonnt";

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

//        Observer<StoryDto> listObserver = new Observer<StoryDto>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(StoryDto dto) {
//                mStoryDto = dto;
//                mView.showList(dto.getContents());
//            }
//        };
//
//        Observable<StoryDto> listObservable = mRepository.getStoryById(mStoryId)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread());
//
//        Subscription subscription = listObservable.subscribe(listObserver);
//        mCompositeSubscription.add(subscription);


    }

    @Override
    public void playStory() {
        OttoBus.post(new GoStory(mStoryId.getContents(), mStoryId.getTitle(), mStoryId.getUserId()));

    }
}
