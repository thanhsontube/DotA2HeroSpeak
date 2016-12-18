package son.nt.dota2.hero;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroActivityPresenter extends BasePresenter implements HeroContract.Presenter {
    private HeroContract.View mView;
    private IHeroRepository mRepository;

    private String mGroup = "Str";
    private HeroBasicDto mSelectedHero;

    private List<HeroBasicDto> mHeroBasicDtos;

    public HeroActivityPresenter(HeroContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }

    @Override
    public void setSelectedPage(int position) {
        if (mHeroBasicDtos == null || mHeroBasicDtos.isEmpty()) {
            return;
        }
        mView.showKenBurns(mHeroBasicDtos.get(position).bgLink);

    }

    public void setSelectedHero(HeroBasicDto selectedHero) {
        this.mSelectedHero = selectedHero;
    }

    @Override
    public void getDataToUpdateView() {
        mView.showKenBurns(mSelectedHero.bgLink);

        //get All hero InGroup
        mRepository.getHeroesFromGroup(mSelectedHero.group)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HeroBasicDto>>() {
                    @Override
                    public void call(List<HeroBasicDto> list) {
                        mHeroBasicDtos = list;
                        for (int i = 0; i < list.size(); i++) {
                            if (list.get(i).heroId.equalsIgnoreCase(mSelectedHero.heroId)) {

                                mView.showHeroList(list, i);
                                break;
                            }
                        }
                    }
                });
    }

}
