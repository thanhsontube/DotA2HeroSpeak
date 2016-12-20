package son.nt.dota2.story.choose_hero;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class ChooseHeroPresenter extends BasePresenter implements ChooseHeroContract.Presenter {
    ChooseHeroContract.View mView;
    IHeroRepository mRepository;

    private String mFilter;

    public ChooseHeroPresenter(ChooseHeroContract.View view, IHeroRepository repository) {
        mView = view;
        this.mRepository = repository;
    }

    @Override
    public void getAllHeroList() {
        Observer<List<HeroBasicDto>> observer = new Observer<List<HeroBasicDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroBasicDto> list) {
                mView.showList(list);

            }
        };

        Subscription subscription = mRepository.getAllHeroes()
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
        Observer<List<HeroBasicDto>> observer = new Observer<List<HeroBasicDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroBasicDto> list) {
                mView.showList(list);

            }
        };

        Subscription subscription = mRepository.searchHero(mFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);

    }
}
