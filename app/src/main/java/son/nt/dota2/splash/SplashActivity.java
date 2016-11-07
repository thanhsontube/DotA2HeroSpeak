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
import son.nt.dota2.R;
import son.nt.dota2.activity.HomeActivity;
import son.nt.dota2.activity.LoginActivity;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import timber.log.Timber;

public class SplashActivity extends BaseActivity {

    IHeroRepository mRepository;
    Subscription subscription;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRepository = new HeroRepository();
        getList();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private void getList() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(HeroBasicDto.class.getSimpleName());
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroBasicDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                list.add(post);
            }
            Timber.d(">>>size:" + list.size());
            subscription = mRepository.storeAllHeroes(list)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aBoolean -> {
                        Timber.d(">>>Done save DB:" + aBoolean);

                        //todo check login
//                        startActivity(HomeActivity.getIntent(getApplicationContext()));

                        startActivity(LoginActivity.getIntent(getApplicationContext()));
                    });

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Timber.e(">>>onCancelled:" + databaseError);

        }
    };


}
