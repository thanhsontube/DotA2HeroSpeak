package son.nt.dota2.data;

import android.support.annotation.NonNull;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;
import rx.Subscriber;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;

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
                    for (HeroBasicDto heroBasicDto : heroes) {
                        realm.beginTransaction();
                        realm.copyToRealm(heroBasicDto);
                        realm.commitTransaction();
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onNext(false);
                    subscriber.onError(e);
                } finally {
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
                    removeAllLordResponses(realm);
                    for (HeroResponsesDto dto : list) {
                        realm.beginTransaction();
                        realm.copyToRealm(dto);
                        realm.commitTransaction();
                    }
                    subscriber.onNext(true);
                    subscriber.onCompleted();
                } catch (Exception e) {
                    subscriber.onNext(false);
                    subscriber.onError(e);
                } finally {
                    realm.close();
                }


            }
        });
    }

    @Override
    public Observable<List<HeroBasicDto>> getHeroesFromGroup(@NonNull String group) {
        return Observable.create(subscriber -> {
            Realm realm = getRealm();
            final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class).equalTo("group", group).findAll();
            subscriber.onNext(realm.copyFromRealm(group1));
            subscriber.onCompleted();
            realm.close();

        });
    }

    @Override
    public Observable<List<HeroBasicDto>> searchHero(final String query) {
        return Observable.create(new Observable.OnSubscribe<List<HeroBasicDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroBasicDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class)
                        .beginGroup()
//                        .contains("fullName", query.toLowerCase())
//                        .or()
                        .contains("name", query.toLowerCase())
                        .endGroup()
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
            }
        });
    }

    @Override
    public Observable<List<HeroBasicDto>> getAllHeroes() {
        return null;
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
}
