package son.nt.dota2.login;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import rx.Observable;
import son.nt.dota2.dto.user.UserDto;

/**
 * Created by sonnt on 10/10/16.
 */

public class LoginRepo implements ILoginRepo {

    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;

    public LoginRepo() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
    }

    @Override
    public Observable<UserDto> checkLogin() {
        UserDto userDto = new UserDto();
        if (mFirebaseUser == null) {
            userDto.setLogin(false);
        } else {
            userDto.setLogin(true);
            userDto.setUserName(mFirebaseUser.getDisplayName());
        }
        return Observable.just(userDto);
    }

    @Override
    public void login() {

    }

    @Override
    public void logout() {

    }
}
