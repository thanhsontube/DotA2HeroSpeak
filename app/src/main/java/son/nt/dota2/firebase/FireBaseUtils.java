package son.nt.dota2.firebase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func7;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.comments.CmtsDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.kenburns.KenBurnsImageDto;
import son.nt.dota2.saved_class.AbilitySoundDtoSaved;
import son.nt.dota2.saved_class.FileAbilityList;
import son.nt.dota2.saved_class.FileHeroBasicList;
import son.nt.dota2.saved_class.FileResponseList;
import son.nt.dota2.saved_class.HeroBasicDtoSaved;
import son.nt.dota2.saved_class.HeroResponsesDtoSaved;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

/**
 * Created by sonnt on 10/9/16.
 */

public class FireBaseUtils {

    public static final String TAG = FireBaseUtils.class.getSimpleName();

    public static void find(String child, String key, String value, HeroBasicDto data) {
        Logger.debug(TAG, ">>>" + "find:" + child + ";key:" + key + ";value:" + value);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(child).orderByChild(key).equalTo(value);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Logger.debug(TAG, ">>>" + "find onDataChange:" + dataSnapshot);
//                update(dataSnapshot, data);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Logger.error(TAG, ">>> Error:" + "find onCancelled:" + databaseError);

            }
        });
    }

    // this one update data to firebase
    public static void update(DataSnapshot dataSnapshot, HeroBasicDto data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
        String key = nodeDataSnapshot.getKey();
        Logger.debug(TAG, ">>>" + "key:" + key);
        reference.child("/" + dataSnapshot.getKey() + "/" + key).updateChildren(data.toMap());
    }

    // this one update data to firebase
    public static void update(DataSnapshot dataSnapshot, HeroResponsesDto data) {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
        String key = nodeDataSnapshot.getKey();
        Logger.debug(TAG, ">>>" + "key:" + key);
        reference.child("/" + dataSnapshot.getKey() + "/" + key).updateChildren(data.toMap());
    }

    public static void saveHeroBaiscToFile() {
        Timber.d(">>>" + "saveHeroBaiscToFile");
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(HeroBasicDto.class.getSimpleName());
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroBasicDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroBasicDtoSaved post = postSnapshot.getValue(HeroBasicDtoSaved.class);
                        list.add(post);
                    }

                    Timber.d(">>>" + "list:" + list.size());

                    FileHeroBasicList file = new FileHeroBasicList(list);
                    try {
                        FileUtil.saveObject(file, FileHeroBasicList.class.getSimpleName());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public static void saveAbilityToFile() {
        Timber.d(">>>" + "saveAbilityToFile");
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_HERO_ABI);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<AbilitySoundDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        AbilitySoundDtoSaved post = postSnapshot.getValue(AbilitySoundDtoSaved.class);
                        list.add(post);
                    }


                    Timber.d(">>>" + "list:" + list.size());

                    FileAbilityList file = new FileAbilityList(list);
                    try {
                        FileUtil.saveObject(file, FileAbilityList.class.getSimpleName());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public static void saveLordToFile() {
        Timber.d(">>>" + "saveLordToFile");
        Observable<List<HeroResponsesDtoSaved>> lords = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_LORD_RESPONSES);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_LORD_RESPONSES:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });

        Observable<List<HeroResponsesDtoSaved>> killing = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_HERO_KILLING_MEETING);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_HERO_KILLING_MEETING:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });

        Observable<List<HeroResponsesDtoSaved>> items = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_ITEMS);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_ITEMS:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });

        Observable<List<HeroResponsesDtoSaved>> normals = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_HERO_NORMAL_VOICE);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_HERO_NORMAL_VOICE:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });
        Observable<List<HeroResponsesDtoSaved>> TABLE_MONKEY_KING = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_MONKEY_KING);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_HERO_NORMAL_VOICE:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });
        Observable<List<HeroResponsesDtoSaved>> TABLE_MONKEY_KING_KILLING = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_MONKEY_KING_KILLING);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_HERO_NORMAL_VOICE:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });
        Observable<List<HeroResponsesDtoSaved>> TABLE_MONKEY_KING_NORMAL = Observable.create(subscriber -> {
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference reference = firebaseDatabase.getReference();
            Query query = reference.child(MsConst.TABLE_MONKEY_KING_NORMAL);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    List<HeroResponsesDtoSaved> list = new ArrayList<>();
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        HeroResponsesDtoSaved post = postSnapshot.getValue(HeroResponsesDtoSaved.class);
                        list.add(post);
                    }

                    subscriber.onNext(list);
                    subscriber.onCompleted();


                    Timber.d(">>>" + "TABLE_HERO_NORMAL_VOICE:" + list.size());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        });

//        Observable<List<HeroResponsesDto>> total = Observable.zip(lords, killing, items, normals, TABLE_MONKEY_KING_NORMAL, TABLE_MONKEY_KING_KILLING, TABLE_MONKEY_KING,
//                new Func7<List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDto>>() {
//                    @Override
//                    public List<HeroResponsesDto> call(List<HeroResponsesDtoSaved> heroResponsesDtoSaveds, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds2, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds3, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds4, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds5, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds6, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds7) {
//                        List<HeroResponsesDto> list = new ArrayList<HeroResponsesDto>();
//                        HeroResponsesDto dto;
//                        List<HeroResponsesDtoSaved> group = new ArrayList<HeroResponsesDtoSaved>();
//                        group.addAll(heroResponsesDtoSaveds);
//                        group.addAll(heroResponsesDtoSaveds2);
//                        group.addAll(heroResponsesDtoSaveds3);
//                        group.addAll(heroResponsesDtoSaveds4);
//                        group.addAll(heroResponsesDtoSaveds5);
//                        group.addAll(heroResponsesDtoSaveds6);
//                        group.addAll(heroResponsesDtoSaveds7);
//                        for (HeroResponsesDtoSaved d : group) {
//                            dto = new HeroResponsesDto(d.no, d.heroId, d.heroName, d.heroIcon, d.voiceGroup,
//                                    d.toHeroId, d.toHeroIcon, d.toHeroName, d.text, d.link,
//                                    d.linkArcana, d.sub, d.position, d.itemId, d.title, d.image, d.group,
//                                    d.duration, d.totalLike, d.totalComments, d.isPlaying, d.isFavorite,
//                                    d.isLiked);
//                            list.add(dto);
//                        }
//
//                        FileResponseList file = new FileResponseList(group);
//                        try {
//                            FileUtil.saveObject(file, FileResponseList.class.getSimpleName());
//                        } catch (IOException e) {
//
//                            e.printStackTrace();
//                        }
//
//                        return list;
//                    }
//                }

        Observable<List<HeroResponsesDto>> total = Observable.zip(lords, killing, items, normals, TABLE_MONKEY_KING_NORMAL, TABLE_MONKEY_KING_KILLING, TABLE_MONKEY_KING,
                new Func7<List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDtoSaved>, List<HeroResponsesDto>>() {
                    @Override
                    public List<HeroResponsesDto> call(List<HeroResponsesDtoSaved> heroResponsesDtoSaveds, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds2, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds3, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds4, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds5, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds6, List<HeroResponsesDtoSaved> heroResponsesDtoSaveds7) {
                        List<HeroResponsesDto> list = new ArrayList<HeroResponsesDto>();
                        HeroResponsesDto dto;
                        List<HeroResponsesDtoSaved> group = new ArrayList<HeroResponsesDtoSaved>();
                        group.addAll(heroResponsesDtoSaveds);
                        group.addAll(heroResponsesDtoSaveds2);
                        group.addAll(heroResponsesDtoSaveds3);
                        group.addAll(heroResponsesDtoSaveds4);
                        group.addAll(heroResponsesDtoSaveds5);
                        group.addAll(heroResponsesDtoSaveds6);
                        group.addAll(heroResponsesDtoSaveds7);
                        for (HeroResponsesDtoSaved d : group) {
                            dto = new HeroResponsesDto(d.no, d.heroId, d.heroName, d.heroIcon, d.voiceGroup,
                                    d.toHeroId, d.toHeroIcon, d.toHeroName, d.text, d.link,
                                    d.linkArcana, d.sub, d.position, d.itemId, d.title, d.image, d.group,
                                    d.duration, d.totalLike, d.totalComments, d.isPlaying, d.isFavorite,
                                    d.isLiked);
                            list.add(dto);
                        }

                        FileResponseList file = new FileResponseList(group);
                        try {
                            FileUtil.saveObject(file, FileResponseList.class.getSimpleName());
                        } catch (IOException e) {

                            e.printStackTrace();
                        }

                        return list;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        total.subscribe(heroResponsesDtos -> {
            Timber.d(">>>" + "total heroResponsesDtos:" + heroResponsesDtos.size());

        });

    }

    public static void pushBg() {

    }

    public static void sendComments(String text, FirebaseUser user, String type, String toId) {
        Observable.create(subscriber -> {

                    CmtsDto cmtsDto = new CmtsDto();
                    final long createTime = System.currentTimeMillis();
                    cmtsDto.setCreateTime(createTime);
                    cmtsDto.setFromID(user.getUid());
                    cmtsDto.setFromImage(user.getPhotoUrl().toString());
                    final String displayName = user.getDisplayName();
                    cmtsDto.setFromName(displayName);

                    cmtsDto.setId("cmt_" + displayName + "_" + createTime);

                    cmtsDto.setMessage(text);
                    cmtsDto.setType(type);
                    cmtsDto.setToId(toId);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();
                    reference.child(MsConst.TABLE_COMMENTS).push().setValue(cmtsDto);
                }

        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public static void sendStoryComments(String mess, String storyId) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Observable.create(subscriber -> {

                    CmtsDto cmtsDto = new CmtsDto();
                    final long createTime = System.currentTimeMillis();
                    cmtsDto.setCreateTime(createTime);
                    cmtsDto.setFromID(user.getUid());
                    cmtsDto.setFromImage(user.getPhotoUrl().toString());
                    final String displayName = user.getDisplayName();
                    cmtsDto.setFromName(displayName);

                    cmtsDto.setId("cmt_" + displayName + "_" + createTime);

                    cmtsDto.setMessage(mess);
                    cmtsDto.setType(MsConst.COMMENT_TYPE_SIMPLE_STORY);
                    cmtsDto.setToId(storyId);

                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();
                    reference.child(MsConst.TABLE_COMMENTS).push().setValue(cmtsDto);
                }

        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    public static void sendkenBurns(List<KenBurnsImageDto> bgImageDtos) {
        Observable.create(subscriber -> {
                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference reference = firebaseDatabase.getReference();
                    for (KenBurnsImageDto d : bgImageDtos) {

                        reference.child(MsConst.TABLE_KENBURNS).push().setValue(d);
                    }


                }

        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}
