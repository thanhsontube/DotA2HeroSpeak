package son.nt.dota2.htmlcleaner;

import android.content.Context;

/**
 * Created by Sonnt on 7/13/15.
 */
public class HTTPParseUtils {

    static HTTPParseUtils INSTANCE = null;
    public Context context;

    public static void createInstance (Context context) {
        INSTANCE = new HTTPParseUtils(context);

    }
    public HTTPParseUtils (Context context) {
        this.context = context;
    }

    public static HTTPParseUtils getInstance () {
        return INSTANCE;
    }


}
