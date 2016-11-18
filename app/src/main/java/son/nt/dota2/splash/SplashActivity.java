package son.nt.dota2.splash;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

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
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    IHeroRepository mRepository;

    CompositeSubscription mCompositeSubscription = new CompositeSubscription();
    boolean mIsLoadBasicHeroDone = false;
    boolean mIsLoadLordResponsesDone = false;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = new HeroRepository();
        startActivity(HomeActivity.getIntent(getApplicationContext()));
//        getBasicHeroList();
//        getLordResponseList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeSubscription != null && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
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
                        mIsLoadBasicHeroDone = true;
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
                        mIsLoadLordResponsesDone = true;
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
        if (!mIsLoadBasicHeroDone) {
            return;
        }
        if (!mIsLoadLordResponsesDone) {
            return;
        }
        startActivity(HomeActivity.getIntent(getApplicationContext()));
//        startActivity(LoginActivity.getIntent(getApplicationContext()));

    }


}
