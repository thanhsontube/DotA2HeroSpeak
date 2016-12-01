package son.nt.dota2.splash;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.ItemDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.test.TestActivity;
import son.nt.dota2.utils.PreferenceUtil;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    IHeroRepository mRepository;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    boolean isBasicLoaded = false;
    boolean isLordLoaded = false;
    boolean isNormalVoiceLoaded = false;
    boolean isKillingLoaded = false;
    boolean isItemsLoaded = false;
    boolean isBuyItemsLoaded = false;

    boolean mIsNeedLoadData = false;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Timber.d(">>>" + "onCreate 3");
        super.onCreate(savedInstanceState);
        mRepository = new HeroRepository();
//        mIsNeedLoadData = !PreferenceUtil.getPreference(this, MsConst.PREFETCH, false);
        if (!mIsNeedLoadData) {

//            removeTABLE_HERO_NORMAL_VOICE();
            startActivity(new Intent(this, TestActivity.class));
//            startActivity(HomeActivity.getIntent(getApplicationContext()));
        } else {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            realm.delete(HeroResponsesDto.class);
            realm.commitTransaction();
            realm.close();

            isLordLoaded = true;
//            isKillingLoaded = true;
            isNormalVoiceLoaded = true;
            isBuyItemsLoaded = true;
            isItemsLoaded = true;

            getBasicHeroList();
//            getLordResponseList();
            getKillingResponseList();
//            getNormalVoicesResponseList();
//            getHeroResponseWithItemsList();
//            getItemsList();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
    }

    private void removeTABLE_HERO_NORMAL_VOICE() {
        Timber.d(">>>" + "removeTABLE_HERO_NORMAL_VOICE");
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        reference.child(MsConst.TABLE_HERO_ITEMS).setValue(null);
    }

    //get base hero List
    private void getBasicHeroList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(HeroBasicDto.class.getSimpleName());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    private void getLordResponseList() {
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

        PreferenceUtil.setPreference(this, MsConst.PREFETCH, true);
        startActivity(HomeActivity.getIntent(getApplicationContext()));
//        startActivity(LoginActivity.getIntent(getApplicationContext()));

    }


}
