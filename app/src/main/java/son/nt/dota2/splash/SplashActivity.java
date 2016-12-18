package son.nt.dota2.splash;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.activity.LoginActivity;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.test.TestActivity;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.PreferenceUtil;
import timber.log.Timber;

public class SplashActivity extends BaseActivity implements SplashContract.View {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 100;

    SplashContract.Presenter mPresenter;
    IHeroRepository mRepository;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    boolean isBasicLoaded = false;
    boolean isLordLoaded = false;
    boolean isNormalVoiceLoaded = false;
    boolean isKillingLoaded = false;
    boolean isItemsLoaded = false;
    boolean isBuyItemsLoaded = false;
    boolean isAbilityLoaded = true;

    boolean mIsNeedLoadData = false;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d(">>>" + "onCreate 3");
        super.onCreate(savedInstanceState);
        mPresenter = new SplashPresenter(this, new HeroRepository(this));
        mRepository = new HeroRepository();
        checkPermission();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void startLogin() {
        ActivityCompat.startActivity(this, LoginActivity.getIntent(this), null);
        finish();
    }

    private void loadData() {
//        startActivity(new Intent(this, TestActivity.class));
        ResourceManager.createInstance(getApplicationContext());
        try {
            FileUtil.copyAssets(this, "music", ResourceManager.getInstance().getFolderMusicPack());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPresenter.copyData();


//        if (!mIsNeedLoadData) {
////            getBasicHeroList2();
//
//            startActivity(new Intent(this, TestActivity.class));
////            ActivityCompat.startActivity(this, LoginActivity.getIntent(this), null);
////            startActivity(HomeActivity.getIntent(getApplicationContext()));
//        } else {
////            Realm realm = Realm.getDefaultInstance();
////            realm.beginTransaction();
////            realm.delete(HeroResponsesDto.class);
////            realm.commitTransaction();
////            realm.close();
//
//
//            getBasicHeroList();
//
////            isAbilityLoaded = true;
//            getAbis();
//
////            isLordLoaded = true;
//            getLordResponseList();
//
//
//            isKillingLoaded = true;
////            getKillingResponseList();
//
//            isNormalVoiceLoaded = true;
////            getNormalVoicesResponseList();
//
//            isBuyItemsLoaded = true;
////            getHeroResponseWithItemsList();
//
//            isItemsLoaded = true;
////            getItemsList();
//    }

    }

    private void removeTABLE_HERO_NORMAL_VOICE() {
        Timber.d(">>>" + "removeTABLE_HERO_NORMAL_VOICE");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child(MsConst.TABLE_HERO_ABI).setValue(null);
    }

    //get base hero List
    private void getBasicHeroList() {
        mRepository.getAllHeroes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HeroBasicDto>>() {
                    @Override
                    public void call(List<HeroBasicDto> heroBasicDtos) {
                        Timber.d(">>>" + "heroBasicDtos:" + heroBasicDtos.size());
                        isBasicLoaded = true;
                        checkAndComplete();

                    }
                });

    }

    private void getBasicHeroList2() {


        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(HeroBasicDto.class.getSimpleName());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    //get Abis List
    private void getAbis() {
        mRepository.getAllAbility()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<AbilitySoundDto>>() {
                    @Override
                    public void call(List<AbilitySoundDto> heroBasicDtos) {
                        Timber.d(">>>" + "getAbis:" + heroBasicDtos.size());
                        isAbilityLoaded = true;
                        checkAndComplete();

                    }
                });


//        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//        DatabaseReference reference = firebaseDatabase.getReference();
//        Query query = reference.child(MsConst.TABLE_HERO_ABI);
//        query.addListenerForSingleValueEvent(valueAbis);
    }

    private void getLordResponseList() {

        mRepository.getResponseSounds()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<HeroResponsesDto>>() {
                    @Override
                    public void call(List<HeroResponsesDto> heroBasicDtos) {
                        Timber.d(">>>" + "getLordResponseList:" + heroBasicDtos.size());
                        isLordLoaded = true;
                        checkAndComplete();

                    }
                });

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(MsConst.TABLE_LORD_RESPONSES);
        query.addListenerForSingleValueEvent(valueLordEventListener);
    }

    private void getKillingResponseList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(MsConst.TABLE_HERO_KILLING_MEETING);
        query.addListenerForSingleValueEvent(valueKillingEventListener);
    }

    private void getHeroResponseWithItemsList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(MsConst.TABLE_HERO_ITEMS);
        query.addListenerForSingleValueEvent(valueHeroWithItemsEventListener);
    }

    private void getNormalVoicesResponseList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(MsConst.TABLE_HERO_NORMAL_VOICE);
        query.addListenerForSingleValueEvent(valueNormalVoicesEventListener);
    }

    private void getItemsList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(MsConst.TABLE_ITEMS);
        query.addListenerForSingleValueEvent(valueItemsEventListener);
    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroBasicDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                list.add(post);
            }
            Timber.d(">>>basic size:" + list.size());
            Subscription subscription = mRepository.storeAllHeroes(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save DB:" + aBoolean);
                        startActivity(new Intent(getApplicationContext(), TestActivity.class));
                        isBasicLoaded = true;

                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };

    ValueEventListener valueAbis = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<AbilitySoundDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                AbilitySoundDto post = postSnapshot.getValue(AbilitySoundDto.class);
                list.add(post);
            }
            Timber.d(">>>basic size:" + list.size());
            Subscription subscription = mRepository.storeAbis(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>valueAbis save DB:" + aBoolean);
                        isAbilityLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };

    ValueEventListener valueLordEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroResponsesDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroResponsesDto post = postSnapshot.getValue(HeroResponsesDto.class);
                list.add(post);
            }
            Timber.d(">>>Lord size:" + list.size());
            Subscription subscription = mRepository.storeAllLordResponses(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save Lord responses DB:" + aBoolean);
                        isLordLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };
    ValueEventListener valueNormalVoicesEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroResponsesDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroResponsesDto post = postSnapshot.getValue(HeroResponsesDto.class);
                list.add(post);
            }
            Timber.d(">>>valueNormalVoicesEventListener size:" + list.size());
            Subscription subscription = mRepository.storeAllLordResponses(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save valueNormalVoicesEventListener responses DB:" + aBoolean);
                        isNormalVoiceLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };
    ValueEventListener valueKillingEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroResponsesDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroResponsesDto post = postSnapshot.getValue(HeroResponsesDto.class);
                list.add(post);
            }
            Timber.d(">>>valueKillingEventListener size:" + list.size());
            Subscription subscription = mRepository.storeAllLordResponses(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save valueKillingEventListener responses DB:" + aBoolean);
                        isKillingLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };

    ValueEventListener valueHeroWithItemsEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroResponsesDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroResponsesDto post = postSnapshot.getValue(HeroResponsesDto.class);
                list.add(post);
            }
            Timber.d(">>>valueHeroWithItemsEventListener size:" + list.size());
            Subscription subscription = mRepository.storeAllLordResponses(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save valueHeroWithItemsEventListener responses DB:" + aBoolean);
                        isBuyItemsLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };

    ValueEventListener valueItemsEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<ItemDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                ItemDto post = postSnapshot.getValue(ItemDto.class);
                list.add(post);
            }
            Timber.d(">>>Lord size:" + list.size());
            Subscription subscription = mRepository.storeAlItemsResponses(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save Items responses DB:" + aBoolean);
                        isItemsLoaded = true;
                        checkAndComplete();
                    });
            mCompositeSubscription.add(subscription);

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };

    private void checkAndComplete() {

        Timber.d(">>>" + "checkAndComplete isBasicLoaded:" + isBasicLoaded
                + "isLordLoaded:" + isLordLoaded + ";isKillingLoaded:" + isKillingLoaded
                + ";isNormalVoiceLoaded:" + isNormalVoiceLoaded
                + ";isBuyItemsLoaded:" + isBuyItemsLoaded + ";isItemsLoaded:" + isItemsLoaded);
        if (!isBasicLoaded) {
            return;
        }
        if (!isLordLoaded) {
            return;
        }
        if (!isKillingLoaded) {
            return;
        }
        if (!isNormalVoiceLoaded) {
            return;
        }
        if (!isBuyItemsLoaded) {
            return;
        }

        if (!isItemsLoaded) {
            return;
        }

        if (!isAbilityLoaded) {
            return;
        }

        PreferenceUtil.setPreference(this, MsConst.PREFETCH, true);

        ActivityCompat.startActivity(this, LoginActivity.getIntent(this), null);

    }

    private void checkPermission() {
        final int checkSelfPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (checkSelfPermission != PermissionChecker.PERMISSION_GRANTED) {
            requestPermission();

        } else {
            loadData();
        }
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Timber.d(">>>" + "onRequestPermissionsResult");
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
                    loadData();
                } else {
                    Toast.makeText(this, "Need write external store for offline sounds and set ringtone/notification, please Allow it , thanks ", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            }
        }
    }
}
