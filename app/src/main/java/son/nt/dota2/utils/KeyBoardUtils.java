package son.nt.dota2.utils;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Sonnt on 11/13/15.
 */
public class KeyBoardUtils {
    public static void close(Activity c) {
        if (c == null || c.getCurrentFocus() == null) {
            return;
        }
        InputMethodManager inputManager = (InputMethodManager)
                c.getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(c.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
