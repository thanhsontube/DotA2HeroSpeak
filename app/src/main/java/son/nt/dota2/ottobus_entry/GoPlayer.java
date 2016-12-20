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
    public int pos;

    public GoPlayer(int command) {
        this.command = command;
    }

    public GoPlayer(int command, int pos) {
        this.command = command;
        this.pos = pos;
    }

    public GoPlayer(int command, MediaItem mediaItem, int pos) {
        this.command = command;
        this.mediaItem = mediaItem;
        this.pos = pos;
    }
}
