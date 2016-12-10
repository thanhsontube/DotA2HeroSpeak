package son.nt.dota2.story.add_simple_story;

import com.squareup.otto.Subscribe;

import android.text.TextUtils;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.ottobus_entry.GoAddASound;
import son.nt.dota2.ottobus_entry.GoAddSimpleStory;
import son.nt.dota2.utils.Dota2Util;

/**
 * Created by sonnt on 12/6/16.
 */

public class AddSimpleStoryPresenter extends BasePresenter implements AddSimpleStoryContract.Presenter {
    AddSimpleStoryContract.View mView;
    IHeroRepository mRepository;

    private String mSide;
    private String mFilter;
    private HeroBasicDto mHeroBasicDto;
    private HeroResponsesDto mHeroResponsesDto;

    public AddSimpleStoryPresenter(AddSimpleStoryContract.View view, IHeroRepository repository) {
        mView = view;
        this.mRepository = repository;
    }

    @Override
    public void setSide(String data) {
        mSide = data;

    }

    @Override
    public void wrapSimpleStory(String des) {

        if (mHeroBasicDto == null || (mHeroResponsesDto == null && TextUtils.isEmpty(des))) {
            return;
        }

        StoryPartDto dto = new StoryPartDto();

        dto.setDescription(des);
        dto.setHeroId(mHeroBasicDto.heroId);
        dto.setHeroImage(mHeroBasicDto.avatar);

        dto.setSoundLink(mHeroResponsesDto == null ? "" : mHeroResponsesDto.getLink());
        dto.setSoundText(mHeroResponsesDto == null ? "" : mHeroResponsesDto.getText());

        dto.setSide(mSide);
        dto.setNo(System.currentTimeMillis());
        dto.setViewType(mSide);

        mRepository.saveStoryPart(dto);

        mView.closeActivity();
    }

    @Subscribe
    public void onGetSelectedHero(GoAddSimpleStory data) {

        this.mHeroBasicDto = data.mHeroBasicDto;
        mView.updateAvatar(data.mHeroBasicDto);
    }

    @Subscribe
    public void onGetSelectedSound(GoAddASound data) {

        this.mHeroResponsesDto = data.mData;
        mView.updateSelectedSound(data.mData);
        final String fromHero = Dota2Util.getFromHero(mHeroResponsesDto);
        mRepository.getHeroFromId(fromHero)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HeroBasicDto>() {
                    @Override
                    public void call(HeroBasicDto heroBasicDto) {
                        mView.updateAvatar(heroBasicDto);
                    }
                });

    }


}
