package son.nt.dota2.login;

import rx.Observable;
import son.nt.dota2.dto.user.UserDto;

/**
 * Created by sonnt on 10/10/16.
 */

public interface ILoginRepo {
    void login();
    void logout();

    Observable<UserDto> checkLogin();
}
