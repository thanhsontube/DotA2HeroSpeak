package son.nt.dota2.login;

import android.support.annotation.NonNull;

import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import son.nt.dota2.dto.user.UserDto;
import son.nt.dota2.rx.BaseSchedulerProvider;
import son.nt.dota2.utils.Logger;

/**
 * Created by sonnt on 10/10/16.
 */

public class LoginPresenter implements LoginContract.Presenter {
    public static final String TAG = LoginPresenter.class.getSimpleName();

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

        mCompositeSubscription = new CompositeSubscription();

    }

    @Override
    public void checkLogin() {

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
                        Logger.error(TAG, ">>> Error:" + e);

                    }

                    @Override
                    public void onNext(UserDto userDto) {
                        Logger.debug(TAG, ">>>" + "onNext:" + userDto.isLogin());
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
