package son.nt.dota2.story;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.base.BasePresenter;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.story.StoryDto;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.utils.ConvertClassUtil;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

/**
 * Created by sonnt on 12/6/16.
 */

public class StoryPresenter extends BasePresenter implements StoryContract.Presenter {
    StoryContract.View mView;
    IHeroRepository mRepository;
    String mUserId = "sonnt";

    public StoryPresenter(StoryContract.View view, IHeroRepository repo) {
        mView = view;
        this.mRepository = repo;
    }

    @Override
    public void createAddList() {

        Observer<List<StoryPartDto>> listObserver = new Observer<List<StoryPartDto>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<StoryPartDto> storyPartDtos) {
                if (storyPartDtos.isEmpty()) {
                    List<StoryPartDto> dtos = new ArrayList<>();
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    dtos.add(new StoryPartDto());
                    mView.showAddList(dtos);
                    return;
                }

                storyPartDtos.add(new StoryPartDto());
                storyPartDtos.add(new StoryPartDto());
                storyPartDtos.add(new StoryPartDto());

                mView.showAddList(storyPartDtos);
            }
        };

        Observable<List<StoryPartDto>> listObservable = mRepository.getCurrentCreateStory()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        Subscription subscription = listObservable.subscribe(listObserver);
        mCompositeSubscription.add(subscription);


    }

    @Override
    public void saveStory(String s) {
        Observable.create(new Observable.OnSubscribe<RealmList<StoryPartDto>>() {
            @Override
            public void call(Subscriber<? super RealmList<StoryPartDto>> subscriber) {
                Realm realm = Realm.getDefaultInstance();
                final RealmResults<StoryPartDto> group1 = realm.where(StoryPartDto.class)
                        .findAll();

                RealmList<StoryPartDto> finalList = new RealmList<StoryPartDto>();

                finalList.addAll(realm.copyFromRealm(group1.subList(0, group1.size())));
                subscriber.onNext(finalList);
                subscriber.onCompleted();
                realm.close();

            }
        }).map(new Func1<RealmList<StoryPartDto>, StoryDto>() {
            @Override
            public StoryDto call(RealmList<StoryPartDto> storyPartDtos) {

                StoryDto storyDto = new StoryDto();
                storyDto.setStoryId(mUserId + "_" + (TextUtils.isEmpty(s) ? "notitle" : s) + "_" + System.currentTimeMillis());
                storyDto.setUserId(mUserId);
                storyDto.setTitle(s);
                storyDto.setCreatedTime(System.currentTimeMillis());
                storyDto.setContents(storyPartDtos);

                Realm realm = Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealm(storyDto);
                realm.commitTransaction();
                realm.close();

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference();

                reference.child(MsConst.TABLE_STORY).push().setValue(ConvertClassUtil.createStoryFireBaseDto(storyDto))
                        .addOnSuccessListener(aVoid -> Logger.debug("", ">>>push " + ":" + "onSuccess pushItem:"))
                        .addOnFailureListener(e -> Logger.error("", ">>> Error:" + "onFailure:" + e))

                ;

                return storyDto;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StoryDto>() {
                    @Override
                    public void call(StoryDto storyDto) {
                        Timber.d(">>>" + "saveStory:" + storyDto.getTitle() + ";id:" + storyDto.getUserId());
                        for (StoryPartDto d : storyDto.getContents()) {
                            Timber.d(">>>" + "d:" + d.getSoundText());
                        }

                    }
                });

    }
}
