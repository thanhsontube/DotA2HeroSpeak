package son.nt.dota2.login;

import android.support.annotation.NonNull;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import son.nt.dota2.dto.user.UserDto;
import son.nt.dota2.rx.BaseSchedulerProvider;

/**
 * Created by sonnt on 10/10/16.
 */

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private LoginContract.View mView;

    @NonNull
    ILoginRepo mLoginRepo;

    @NonNull
    BaseSchedulerProvider mProvider;

    CompositeSubscription mCompositeSubscription;


    public LoginPresenter(@NonNull LoginContract.View mView,
                          @NonNull ILoginRepo mILoginRepo,
                          @NonNull BaseSchedulerProvider provider) {
        this.mView = mView;
        this.mLoginRepo = mILoginRepo;
        this.mProvider = provider;

    }

    @Override
    public void checkLogin() {
        Observable<UserDto> observable = mLoginRepo.checkLogin();

        Subscription subscription = null;

        subscription = mLoginRepo.checkLogin()
                .subscribeOn(mProvider.computation())
                .observeOn(mProvider.ui())
                .subscribe(new Subscriber<UserDto>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(UserDto userDto) {
                        if (userDto.isLogin()) {
                            mView.showLogin (userDto.getUserName());

                        } else {
                            mView.showNotLogin();

                        }

                    }
                });


        mCompositeSubscription.add(subscription);
    }

    @Override
    public void skipLogin() {

    }

    @Override
    public void loginFacebook() {

    }

    @Override
    public void loginGoogle() {

    }

    @Override
    public void loginEmail(String email, String password) {

    }
}
