package son.nt.dota2.story.search_sound;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.story.StoryPartDto;

/**
 * Created by sonnt on 12/6/16.
 */

public class SearchSoundPresenter extends BasePresenter implements SearchSoundContract.Presenter {
    SearchSoundContract.View mView;
    IHeroRepository mRepository;

    private String mFilter;
    private String mSide;


    public SearchSoundPresenter(SearchSoundContract.View view, IHeroRepository repository) {
        mView = view;
        this.mRepository = repository;
    }

    @Override
    public void setSide(String data) {
        mSide = data;

    }

    @Override
    public void wrapSimpleStory(String des, final HeroResponsesDto mHeroResponsesDto) {
        final long createdTime = System.currentTimeMillis();
        if (mSide.equals(MsConst.TYPE_SOUND_MIDDLE)) {
            StoryPartDto dto = new StoryPartDto();
            dto.setDescription(des);
            dto.setSide(mSide);
            dto.setCreatedTime(createdTime);
            dto.setViewType(mSide);

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                dto.setHeroId("story_" + user.getDisplayName() + "_" + des + createdTime);
            } else {
                dto.setHeroId(String.valueOf(createdTime));
            }

            mRepository.saveStoryPart(dto);
            mView.closeActivity();
            return;
        }
        mRepository.getHeroFromId(mHeroResponsesDto.getHeroId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<HeroBasicDto>() {
                    @Override
                    public void call(HeroBasicDto mHeroBasicDto) {

                        if (mHeroBasicDto == null || mHeroResponsesDto == null) {
                            return;
                        }
                        StoryPartDto dto = new StoryPartDto();

                        dto.setDescription(des);
                        dto.setHeroId(mHeroBasicDto.heroId);
                        dto.setHeroImage(mHeroBasicDto.avatar);

                        dto.setSoundLink(mHeroResponsesDto == null ? "" : mHeroResponsesDto.getLink());
                        dto.setSoundText(mHeroResponsesDto == null ? "" : mHeroResponsesDto.getText());

                        dto.setSide(mSide);
                        dto.setCreatedTime(createdTime);
                        dto.setViewType(mSide);

                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if (user != null) {
                            dto.setHeroId("story_" + user.getDisplayName() + "_" + des + createdTime);
                        } else {
                            dto.setHeroId(String.valueOf(createdTime));
                        }

                        mRepository.saveStoryPart(dto);


                        mView.closeActivity();
                    }
                });


    }

    @Override
    public void getSomeSounds() {
        if (mSide.equalsIgnoreCase(MsConst.TYPE_SOUND_MIDDLE)) {
            return;
        }

        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showListData(list);

            }
        };
        //todo hack
        mFilter = "Monkey_King";

        Subscription subscription = mRepository.getSounds(mFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void setFilter(String s1) {
        mFilter = s1;
    }

    @Override
    public void search() {

        Observer<List<HeroResponsesDto>> observer = new Observer<List<HeroResponsesDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<HeroResponsesDto> list) {
                mView.showListData(list);

            }
        };

        Subscription subscription = mRepository.searchSounds(mFilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
        mCompositeSubscription.add(subscription);

    }


}
