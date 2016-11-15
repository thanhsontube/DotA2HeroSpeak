package son.nt.dota2.hero.hero_fragment;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.hero.HeroContract;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroResponsePresenter extends BasePresenter implements HeroResponseContract.Presenter {
    private HeroResponseContract.View mView;
    private IHeroRepository mRepository;

    public HeroResponsePresenter(HeroResponseContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void fetchBasicHeroFromHeroId(String heroId) {
        Observer<HeroBasicDto> observer = new Observer<HeroBasicDto>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(HeroBasicDto heroBasicDto) {

            }
        };

        Subscription subscription = mRepository.getHeroFromId(heroId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void getData() {
        Observer<List<HeroBasicDto>> observer = new Observer<List<HeroBasicDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroBasicDto> list) {

            }
        };

        Subscription subscription = mRepository.getHeroesFromGroup("Str")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);


    }
}
