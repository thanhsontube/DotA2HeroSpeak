package son.nt.dota2.hero.hero_fragment;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

/**
 * Created by sonnt on 11/7/16.
 * Input is heroID
 * Get HeroBasicDto from heroID -> update kenburns and get heroGroup,
 * Then get List <HeroBasicDto> based on heroGroup
 */

public class HeroFragmentPresenter extends BasePresenter implements HeroResponseContract.Presenter {
    private HeroResponseContract.View mView;
    private IHeroRepository mRepository;
    private String mHeroID;
    private HeroBasicDto mSelectedHero;

    private boolean mBindFetchServiceDone = false;
    private boolean mHeroSoundsLoaded = false;
    private List<HeroResponsesDto> mHeroResponsesDtos;

    public HeroFragmentPresenter(HeroResponseContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void fetchBasicHeroFromHeroId(String heroId) {
        this.mHeroID = heroId;
        final Subscription subscribe = mRepository.getHeroFromId(heroId).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(heroBasicDto -> {
                            this.mSelectedHero = heroBasicDto;
                            mView.updateArcana(mSelectedHero.isArcana());
                            getHeroSounds();

                        }
                );
        mCompositeSubscription.add(subscribe);
    }

    @Override
    public void setFetchServiceBind(boolean bind) {
        this.mBindFetchServiceDone = bind;
        startPrefetch();
    }

    @Override
    public List<HeroResponsesDto> getSoundsList() {
        return mHeroResponsesDtos;
    }

    @Override
    public void getAbi() {
        final Subscription subscribe = mRepository.getAbis(mHeroID).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(data -> {
                    mView.updateAbi (data);

                        }
                );
        mCompositeSubscription.add(subscribe);
    }

    private void startPrefetch() {
        Timber.d(">>>" + ">>>" + "startPrefetch mBindFetchServiceDone" + mBindFetchServiceDone + ";mHeroSoundsLoaded:" + mHeroSoundsLoaded);
        if (mBindFetchServiceDone && mHeroSoundsLoaded) {
            mView.addDataToDownload(mHeroResponsesDtos, mHeroID);
        }
    }

    @Override
    public void getHeroSounds() {
        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {
                // Do nothing

            }

            @Override
            public void onError(Throwable e) {
                Timber.e(">>>" + "error getAllHeroBasicOnGroup:" + e);

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showHeroSoundsList(list);
                mHeroResponsesDtos = list;
//                startPrefetch();

            }
        };

        Subscription subscription = mRepository.getSounds(mHeroID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }
}
