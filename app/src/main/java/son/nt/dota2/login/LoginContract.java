package son.nt.dota2.login;

/**
 * Created by sonnt on 10/10/16.
 */

public class LoginContract {
    public interface View {
        void loginFacebook();
        void loginGoogle();
        void loginEmail(String email, String password);

        void showLogin(String userName);

        void showNotLogin();
    }

    public interface Presenter {
        void checkLogin();
        void skipLogin();

        void loginFacebook();
        void loginGoogle();
        void loginEmail(String email, String password);

    }
}
