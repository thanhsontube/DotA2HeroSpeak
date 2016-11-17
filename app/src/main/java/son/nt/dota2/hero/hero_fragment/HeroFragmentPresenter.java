package son.nt.dota2.hero.hero_fragment;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroFragmentPresenter extends BasePresenter implements HeroResponseContract.Presenter {
    private HeroResponseContract.View mView;
    private IHeroRepository mRepository;
    private String mHeroID;

    public HeroFragmentPresenter(HeroResponseContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void fetchBasicHeroFromHeroId(String heroId) {
        this.mHeroID = heroId;
        final Subscription subscribe = mRepository.getHeroFromId(heroId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(heroBasicDto -> {

                            getData();

                        }
                );
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void getData() {
        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showResponse(list);

            }
        };

        Subscription subscription = mRepository.getSounds(mHeroID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);


    }
}
