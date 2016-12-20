package son.nt.dota2.dto.user;

/**
 * Created by sonnt on 10/10/16.
 */

public class UserDto {
    String userName;
    boolean isLogin;

    public String getUserName() {
        return userName;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
