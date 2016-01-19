package son.nt.dota2.parse;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import son.nt.dota2.facebook.UserDto;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 1/19/16.
 */
public class AppAPI implements IUserParse {
    public static final String TAG = "AppAPI";
    private Context context;
    IUserParse.Callback parseUserCallback = null;

    public AppAPI(Context context) {
        this.context = context;

    }

    @Override
    public void createAccount(final UserDto userDto) {
        Logger.debug(TAG, ">>>" + "createAccount:" + userDto.email);
        ParseUser parseUser = new ParseUser();
        parseUser.setUsername(userDto.getEmail());
        parseUser.setEmail(userDto.getEmail());
        parseUser.setPassword(userDto.getPassword());

        parseUser.put("name", userDto.getName());
        parseUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                Logger.debug(TAG, ">>>" + "Done e:" + e);
                if (parseUserCallback != null) {
                    parseUserCallback.onUserCreated(userDto, e);
                }
            }
        });
    }

    @Override
    public void isUserExist(String email) {

    }

    @Override
    public void setIUserParseCallback(Callback callback) {

    }

    @Override
    public void updateUserInfo(UpdateUserInfoDto d) {
        ParseUser parseUser = ParseUser.getCurrentUser();
        if (parseUser == null) {
            Toast.makeText(context, "ERROR : updateUserInfo parseUser is NULL", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!TextUtils.isEmpty(d.fbID)) {
            parseUser.put("fbId", d.fbID);
        }

        if (!TextUtils.isEmpty(d.name)) {
            parseUser.put("name", d.name);
        }
        if (!TextUtils.isEmpty(d.avatar)) {
            parseUser.put("avatar", d.avatar);
        }

        if (!TextUtils.isEmpty(d.fbLink)) {
            parseUser.put("fbLink", d.fbLink);
        }

        parseUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Logger.debug(TAG, ">>>" + "updateUserInfo DONE with e:" + e);
            }
        });
    }

    @Override
    public void resetPassWord(String email) {

    }
}
