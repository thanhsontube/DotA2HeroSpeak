package son.nt.dota2.home.herolist;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

/**
 * Created by sonnt on 10/14/16.
 */

public class HeroListPresenter extends BasePresenter implements HeroListContract.Presenter {
    private static final String TAG = HeroListPresenter.class.getSimpleName();
    HeroListContract.View mView;
    IHeroRepository mRepository;

    private String mGroup;

    public HeroListPresenter(HeroListContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void setGroup(String group) {
        this.mGroup = group;

    }

    @Override
    public void getHeroList() {

        Observer<List<HeroBasicDto>> observer = new Observer<List<HeroBasicDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroBasicDto> list) {
                for (HeroBasicDto d : list) {
                    Timber.d(">>>" + "d:" + d.toString());
                }
                mView.showHeroList(list);

            }
        };

        Subscription subscription = mRepository.getHeroesFromGroup(mGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }

//    @Subscribe
//    public void onGetHeroList (BasicHeroListResponse response) {
//        Logger.debug(TAG, ">>>" + "onGetHeroList:" + response);
//        if (response != null) {
//            mView.showHeroList (response.getHeroBasicDtoList());
//        }
//
//    }


}
