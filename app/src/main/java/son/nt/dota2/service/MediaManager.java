package son.nt.dota2.service;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Sonnt on 7/15/15.
 */
public class MediaManager {
    public static MediaManager INSTANCE = null;
    MediaPlayer player;
    public static void createInstance (Context context) {
        INSTANCE = new MediaManager(context);
    }

    public static MediaManager getInstance () {
        return INSTANCE;
    }

    public MediaManager (Context context) {


    }

    public void play (String link) {

    }
}
