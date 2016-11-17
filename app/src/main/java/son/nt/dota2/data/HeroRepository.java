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
                    removeAllLordResponses(realm);
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
    public Observable<HeroBasicDto> getHeroFromId(String heroId) {
        return null;
    }

    @Override
    public Observable<List<HeroBasicDto>> getAllHeroes() {
        return Observable.create(new Observable.OnSubscribe<List<HeroBasicDto>>() {
            @Override
            public void call(Subscriber<? super List<HeroBasicDto>> subscriber) {
                Realm realm = getRealm();
                final RealmResults<HeroBasicDto> group1 = realm.where(HeroBasicDto.class)
                        .findAll();
                subscriber.onNext(realm.copyFromRealm(group1));
                subscriber.onCompleted();
                realm.close();
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
}
