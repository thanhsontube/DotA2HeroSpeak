package son.nt.dota2.favorite.simple_sounds;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;

/**
 * Created by sonnt on 11/7/16.
 * Get favorite Hero Sound
 */

public class FavoriteSoundPresenter extends BasePresenter implements FavoriteSoundContract.Presenter {
    private FavoriteSoundContract.View mView;
    private IHeroRepository mRepository;

    public FavoriteSoundPresenter(FavoriteSoundContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void fetchData() {
        mRepository.getFavoriteSimpleSound()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(heroResponsesDtos -> {
                    mView.showHeroSoundsList(heroResponsesDtos);
                });
    }
}
