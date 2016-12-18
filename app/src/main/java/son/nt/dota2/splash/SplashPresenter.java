package son.nt.dota2.splash;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

/**
 * Created by sonnt on 12/6/16.
 */

public class SplashPresenter extends BasePresenter implements SplashContract.Presenter {
    SplashContract.View mView;
    IHeroRepository mRepository;

    public SplashPresenter(SplashContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void copyData() {

        mRepository.getHeroFromId("Sniper")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HeroBasicDto>() {
                    @Override
                    public void call(HeroBasicDto heroBasicDto) {
                        if (heroBasicDto != null) {
                            Timber.d(">>>" + "Copied already");
                            mView.startLogin();
                        } else {
                            mRepository.copyData()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Action1<Boolean>() {
                                        @Override
                                        public void call(Boolean aBoolean) {
                                            Timber.d(">>>" + "copyData:" + aBoolean);
                                            mView.startLogin();
                                        }
                                    });
                        }
                    }
                });


    }
}
