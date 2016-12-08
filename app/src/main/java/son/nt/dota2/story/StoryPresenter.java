package son.nt.dota2.story;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryPresenter extends BasePresenter implements StoryContract.Presenter {
    StoryContract.View mView;
    IHeroRepository mRepository;

    public StoryPresenter(StoryContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void createAddList() {

        Observer<List<StoryPartDto>> listObserver = new Observer<List<StoryPartDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<StoryPartDto> storyPartDtos) {
                if (storyPartDtos.isEmpty()) {
                    List<StoryPartDto> dtos = new ArrayList<>();
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    mView.showAddList(dtos);
                    return;
                }

                storyPartDtos.add(new StoryPartDto());
                storyPartDtos.add(new StoryPartDto());
                storyPartDtos.add(new StoryPartDto());

                mView.showAddList(storyPartDtos);
            }
        };

        Observable<List<StoryPartDto>> listObservable = mRepository.getCurrentCreateStory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = listObservable.subscribe(listObserver);
        mCompositeSubscription.add(subscription);


    }
}
