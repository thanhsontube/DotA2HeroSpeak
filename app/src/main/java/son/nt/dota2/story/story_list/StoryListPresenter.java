package son.nt.dota2.story.story_list;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryListPresenter extends BasePresenter implements StoryListContract.Presenter {
    StoryListContract.View mView;
    IHeroRepository mRepository;
    String mUserId = "sonnt";

    public StoryListPresenter(StoryListContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void getStoryList() {

        Observer<List<StoryFireBaseDto>> listObserver = new Observer<List<StoryFireBaseDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<StoryFireBaseDto> list) {
                if (list.isEmpty()) {
                    return;
                }

                mView.showAddList(list);
            }
        };

        Observable<List<StoryFireBaseDto>> listObservable = mRepository.getStoryList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = listObservable.subscribe(listObserver);
        mCompositeSubscription.add(subscription);


    }

}
