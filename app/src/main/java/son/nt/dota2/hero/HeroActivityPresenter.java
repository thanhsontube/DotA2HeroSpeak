package son.nt.dota2.hero;

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
 * Created by sonnt on 11/7/16.
 */

public class HeroActivityPresenter extends BasePresenter implements HeroContract.Presenter {
    private HeroContract.View mView;
    private IHeroRepository mRepository;

    private String mGroup = "Str";
    private String mHeroID;

    private List<HeroBasicDto> mHeroBasicDtos;

    public HeroActivityPresenter(HeroContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }


    @Override
    public void fetchHero(String heroID) {
        this.mHeroID = heroID;
        Observer<HeroBasicDto> heroBasicDtoObserver = new Observer<HeroBasicDto>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(">>>" + "error:" + e);

            }

            @Override
            public void onNext(HeroBasicDto heroBasicDto) {
                Timber.d(">>>" + "fetchHero next:" + heroBasicDto.bgLink);
                mGroup = heroBasicDto.group;
                mView.showKenBurns(heroBasicDto.bgLink);
                getData();
            }
        };
        final Subscription subscribe = mRepository.getHeroFromId(heroID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(heroBasicDtoObserver);
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void setSelectedPage(int position) {
        if (mHeroBasicDtos == null || mHeroBasicDtos.isEmpty()) {
            return;
        }
        mView.showKenBurns(mHeroBasicDtos.get(position).bgLink);

    }

    @Override
    public void getData() {
        Observer<List<HeroBasicDto>> observer = new Observer<List<HeroBasicDto>>() {
            @Override
            public void onCompleted() {
                Timber.d(">>>" + "get data onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Timber.d(">>>" + "onError:" + e);

            }

            @Override
            public void onNext(List<HeroBasicDto> list) {
                mHeroBasicDtos = list;
                Timber.d(">>>" + "get Data:" + list.size());
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).heroId.equalsIgnoreCase(mHeroID)) {

                        mView.showHeroList(list, i);
                    }
                }

            }
        };

        Subscription subscription = mRepository.getHeroesFromGroup(mGroup)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);


    }
}
