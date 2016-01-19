package son.nt.dota2.parse;

import com.parse.ParseException;

import son.nt.dota2.facebook.UserDto;


/**
 * Created by Sonnt on 11/14/15.
 */
public interface IUserParse {

    void createAccount(UserDto userDto);
    void isUserExist(String email);

    void setIUserParseCallback(Callback callback);

    void updateUserInfo(UpdateUserInfoDto updateUserInfoDto);

    void resetPassWord(String email);

    interface Callback {

        void onCheckingUserExist(String email, boolean isExist);

        void onUserCreated(UserDto userDto, ParseException error);

        void onFinishResetPw(ParseException error);
    }

}
