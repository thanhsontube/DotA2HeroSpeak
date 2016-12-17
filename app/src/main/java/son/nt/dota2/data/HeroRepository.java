package son.nt.dota2.data;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import son.nt.dota2.MsConst;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.comments.CmtsDto;
import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.story.StoryDto;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.saved_class.FileAbilityList;
import son.nt.dota2.saved_class.FileHeroBasicList;
import son.nt.dota2.saved_class.FileResponseList;
import son.nt.dota2.utils.ConvertClassUtil;
import son.nt.dota2.utils.FileUtil;

/**
 * Created by sonnt on 11/7/16.
 */
public class HeroRepository implements IHeroRepository {
    public HeroRepository() {
    }

    @Override
    public Observable<Boolean> storeAllHeroes(final List<HeroBasicDto> heroes) {

        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = getRealm();
                try {
                    removeAllBasicHero(realm);
                    realm.beginTransaction();
                    realm.copyToRealm(heroes);
                    realm.commitTransaction();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                    realm.close();
                }


            }
        });
    }

    @Override
    public Observable<Boolean> storeAllLordResponses(List<HeroResponsesDto> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = getRealm();
                try {
//                    removeAllLordResponses(realm);
                    realm.beginTransaction();
                    realm.copyToRealm(list);
                    realm.commitTransaction();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                    realm.close();
                }


            }
        });
    }

    @Override
    public Observable<Boolean> storeAlItemsResponses(List<ItemDto> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = getRealm();
                try {
                    removeItems(realm);
                    realm.beginTransaction();
                    realm.copyToRealm(list);
                    realm.commitTransaction();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                    realm.close();
                }


            }
        });
    }

    @Override
    public Observable<Boolean> storeAbis(List<AbilitySoundDto> list) {
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                Realm realm = getRealm();
                try {
                    removeAbis(realm);
                    realm.beginTransaction();
                    realm.copyToRealm(list);
                    realm.commitTransaction();
                    subscriber.onNext(true);
                } catch (Exception e) {
                    subscriber.onError(e);
                } finally {
                    subscriber.onCompleted();
                    realm.close();
                }


            }
        });
    }

    @Override
    public Observable<List<HeroBasicDto>> getHeroesFromGroup(@NonNull String group) {
        return Observable.create(subscriber -> {
            Realm realm = getRealm();
            try {
                final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class).equalTo("group", group).findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
            } catch (Exception e) {
                e.printStackTrace();
                subscriber.onError(e);
            } finally {
                subscriber.onCompleted();
                realm.close();
            }

        });
    }

    @Override
    public Observable<List<HeroBasicDto>> searchHero(final String query) {
        return Observable.create(new Observable.OnSubscribe<List<HeroBasicDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroBasicDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class)
                        .contains("name", query, Case.INSENSITIVE)
                        .or()
                        .contains("fullName", query, Case.INSENSITIVE)
                        .findAll().distinct("heroId");
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<HeroBasicDto> getHeroFromId(String heroId) {
        return Observable.create(new Observable.OnSubscribe<HeroBasicDto>() {
            @Override
            public void call(Subscriber<? super HeroBasicDto> subscriber) {
                Realm realm = getRealm();
                final HeroBasicDto group1 = realm.where(HeroBasicDto.class).equalTo("heroId", heroId)
                        .findFirst();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroResponsesDto>> getSounds(String heroID) {
        return Observable.create(new Observable.OnSubscribe<List<HeroResponsesDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroResponsesDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroResponsesDto> group1 = realm.where(HeroResponsesDto.class)
                        .equalTo("heroId", heroID)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroResponsesDto>> getResponseSounds() {
        return Observable.create(new Observable.OnSubscribe<List<HeroResponsesDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroResponsesDto>> subscriber) {
//                Realm realm = getRealm();
//                final RealmResults<HeroResponsesDto> group1 = realm.where(HeroResponsesDto.class)
//                        .findAll();
//                subscriber.onNext(realm.copyFromRealm(group1));
//                subscriber.onCompleted();
//                realm.close();

                Realm realm = getRealm();
                //todo hack
                removeAllLordResponses(realm);
                final RealmResults<HeroResponsesDto> group1 = realm.where(HeroResponsesDto.class)
                        .findAll();
                List<HeroResponsesDto> list = realm.copyFromRealm(group1);

                if (list.isEmpty()) {
                    //read from file
                    try {
                        FileResponseList fileHeroBasicList = (FileResponseList) FileUtil.getObject(ResourceManager.getInstance().getContext(),
                                FileResponseList.class.getSimpleName());
                        list = ConvertClassUtil.createResponse(fileHeroBasicList);
                        realm.beginTransaction();
                        realm.copyToRealm(list);
                        realm.commitTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroResponsesDto>> searchSounds(String keyword) {
        return Observable.create(new Observable.OnSubscribe<List<HeroResponsesDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroResponsesDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroResponsesDto> group1 = realm.where(HeroResponsesDto.class)
                        .contains("text", keyword, Case.INSENSITIVE)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroResponsesDto>> getFavoriteSimpleSound() {
        return Observable.create(new Observable.OnSubscribe<List<HeroResponsesDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroResponsesDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroResponsesDto> group1 = realm.where(HeroResponsesDto.class)
                        .equalTo("isFavorite", true)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<AbilitySoundDto>> getAbis(String abiHeroID) {
        return Observable.create(new Observable.OnSubscribe<List<AbilitySoundDto>>() {
            @Override
            public void call(Subscriber<? super List<AbilitySoundDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<AbilitySoundDto> group1 = realm.where(AbilitySoundDto.class)
                        .equalTo("abiHeroID", abiHeroID)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public void saveStoryPart(StoryPartDto dto) {
        Realm realm = getRealm();
        realm.beginTransaction();
        realm.copyToRealm(dto);
        realm.commitTransaction();
        realm.close();
    }

    @Override
    public Observable<List<StoryPartDto>> getCurrentCreateStory() {
        return Observable.create(new Observable.OnSubscribe<List<StoryPartDto>>() {
            @Override
            public void call(Subscriber<? super List<StoryPartDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<StoryPartDto> group1 = realm.where(StoryPartDto.class)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<StoryFireBaseDto>> getStoryList() {
        return Observable.create(new Observable.OnSubscribe<List<StoryFireBaseDto>>() {
            @Override
            public void call(Subscriber<? super List<StoryFireBaseDto>> subscriber) {

                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference();
                reference.child(MsConst.TABLE_STORY).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<StoryFireBaseDto> dtos = new ArrayList<StoryFireBaseDto>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            dtos.add(snapshot.getValue(StoryFireBaseDto.class));
                        }
                        subscriber.onNext(dtos);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public Observable<StoryDto> getStoryById(String storyId) {
        return Observable.create(new Observable.OnSubscribe<StoryDto>() {
            @Override
            public void call(Subscriber<? super StoryDto> subscriber) {
                Realm realm = getRealm();
                final StoryDto group1 = realm.where(StoryDto.class)
                        .equalTo("storyId", storyId)
                        .findFirst();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroBasicDto>> getAllHeroes() {
        return Observable.create(new Observable.OnSubscribe<List<HeroBasicDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroBasicDto>> subscriber) {

                Realm realm = getRealm();
                //todo hack
                removeAllBasicHero(realm);
                final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class)
                        .findAll();
                List<HeroBasicDto> list = realm.copyFromRealm(group1);

                if (list.isEmpty()) {
                    //read from file
                    try {
                        FileHeroBasicList fileHeroBasicList = (FileHeroBasicList) FileUtil.getObject(ResourceManager.getInstance().getContext(),
                                FileHeroBasicList.class.getSimpleName());
                        list = ConvertClassUtil.createHeroBasicDto(fileHeroBasicList);
                        realm.beginTransaction();
                        realm.copyToRealm(list);
                        realm.commitTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<AbilitySoundDto>> getAllAbility() {
        return Observable.create(new Observable.OnSubscribe<List<AbilitySoundDto>>() {
            @Override
            public void call(Subscriber<? super List<AbilitySoundDto>> subscriber) {
                Realm realm = getRealm();
                removeAbis(realm);
                final RealmResults<AbilitySoundDto> group1 = realm.where(AbilitySoundDto.class)
                        .findAll();
                List<AbilitySoundDto> list = realm.copyFromRealm(group1);

                if (list.isEmpty()) {
                    //read from file
                    try {
                        FileAbilityList fileHeroBasicList = (FileAbilityList) FileUtil.getObject(ResourceManager.getInstance().getContext(),
                                FileAbilityList.class.getSimpleName());
                        list = ConvertClassUtil.createAbi(fileHeroBasicList);
                        realm.beginTransaction();
                        realm.copyToRealm(list);
                        realm.commitTransaction();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<CmtsDto>> getAllComments() {
        return Observable.create(new Observable.OnSubscribe<List<CmtsDto>>() {
            @Override
            public void call(Subscriber<? super List<CmtsDto>> subscriber) {
                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                DatabaseReference reference = firebaseDatabase.getReference();
                reference.child(MsConst.TABLE_COMMENTS).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        List<CmtsDto> dtos = new ArrayList<CmtsDto>();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            dtos.add(snapshot.getValue(CmtsDto.class));
                        }
                        subscriber.onNext(dtos);
                        subscriber.onCompleted();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public Observable<List<HeroBasicDto>> getAgiHeroes() {
        return null;
    }

    @Override
    public Observable<List<HeroBasicDto>> getStrHeroes() {
        return null;
    }

    @Override
    public Observable<List<HeroBasicDto>> getIntelHeroes() {
        return null;
    }

    private Realm getRealm() {
        return Realm.getDefaultInstance();
    }

    private void removeAllBasicHero(Realm realm) {
        realm.beginTransaction();
        realm.delete(HeroBasicDto.class);
        realm.commitTransaction();
    }

    private void removeAllLordResponses(Realm realm) {
        realm.beginTransaction();
        realm.delete(HeroResponsesDto.class);
        realm.commitTransaction();
    }

    private void removeItems(Realm realm) {
        realm.beginTransaction();
        realm.delete(ItemDto.class);
        realm.commitTransaction();
    }

    private void removeAbis(Realm realm) {
        realm.beginTransaction();
        realm.delete(AbilitySoundDto.class);
        realm.commitTransaction();
    }
}
