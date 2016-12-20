package son.nt.dota2.ottobus_entry;

import son.nt.dota2.base.MediaItem;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 5/26/16.
 */
public class GoPlayer2 {
    public static final int DO_PLAY = 1;
    public static final int DO_PAUSE = 2;
    public int command;
    public ISound mediaItem;
    public int pos;

    public GoPlayer2(int command) {
        this.command = command;
    }

    public GoPlayer2(int command, int pos) {
        this.command = command;
        this.pos = pos;
    }

    public GoPlayer2(int command, MediaItem mediaItem, int pos) {
        this.command = command;
        this.pos = pos;
    }

    public GoPlayer2(int command, HeroResponsesDto selectedDto, int pos) {

    }
}
