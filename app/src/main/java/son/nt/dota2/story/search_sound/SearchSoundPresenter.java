package son.nt.dota2.story.search_sound;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class SearchSoundPresenter extends BasePresenter implements SearchSoundContract.Presenter {
    SearchSoundContract.View mView;
    IHeroRepository mRepository;

    private String mFilter;

    public SearchSoundPresenter(SearchSoundContract.View view, IHeroRepository repository) {
        mView = view;
        this.mRepository = repository;
    }


    @Override
    public void getSomeSounds() {

        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showListData (list);

            }
        };
        //todo hack
        mFilter = "Crystal_Maiden";

        Subscription subscription = mRepository.getSounds(mFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void setFilter(String s1) {
        mFilter = s1;
    }

    @Override
    public void search() {
        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showListData (list);

            }
        };

        Subscription subscription = mRepository.searchSounds(mFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);

    }


}
