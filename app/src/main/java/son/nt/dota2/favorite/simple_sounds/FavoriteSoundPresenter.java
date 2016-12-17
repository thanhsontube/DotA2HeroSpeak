package son.nt.dota2.favorite.simple_sounds;

import java.util.List;

import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 11/7/16.
 * Input is heroID
 * Get HeroBasicDto from heroID -> update kenburns and get heroGroup,
 * Then get List <HeroBasicDto> based on heroGroup
 */

public class FavoriteSoundPresenter extends BasePresenter implements FavoriteSoundContract.Presenter {
    private FavoriteSoundContract.View mView;
    private IHeroRepository mRepository;
    private String mHeroID;

    private List<HeroResponsesDto> mHeroResponsesDtos;

    public FavoriteSoundPresenter(FavoriteSoundContract.View view, IHeroRepository repository) {
        mView = view;
        mRepository = repository;
    }


}
