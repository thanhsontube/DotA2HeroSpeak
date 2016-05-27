package son.nt.dota2.ottobus_entry;

import son.nt.dota2.base.MediaItem;

/**
 * Created by sonnt on 5/26/16.
 */
public class GoPlayer {
    public static final int DO_PLAY = 1;
    public static final int DO_PAUSE = 2;
    public int command;
    public MediaItem mediaItem;

    public GoPlayer(int command) {
        this.command = command;
    }

    public GoPlayer(int command, MediaItem mediaItem) {
        this.command = command;
        this.mediaItem = mediaItem;
    }
}
