package son.nt.dota2.firebase;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 10/14/16.
 */

public interface IFireBaseRepository {

    public interface CallBack {
        void onGetData(List<HeroBasicDto> list);
    }

    void setCallBack(CallBack callBack);

    void loginEmail(String email, String password);

    void loginFacebook();

    void loginGoogle();

    boolean isLoggedIn();

    void logout();

    void getHeroBasic(String group);
}
