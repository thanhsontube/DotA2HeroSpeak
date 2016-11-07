package son.nt.dota2.hero;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroPresenter extends BasePresenter implements HeroContract.Presenter {
    private HeroContract.View mView;
    private IHeroRepository mRepository;

    public HeroPresenter(HeroContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
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
                mView.showHeroList(list);

            }
        };

        Subscription subscription = mRepository.getHeroesFromGroup("Str")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);


    }
}
