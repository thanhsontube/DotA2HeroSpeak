package son.nt.dota2.gridmenu;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 8/7/15.
 */
public class SpeakLongClick extends AObject {
    public SpeakDto speakDto;

    public SpeakLongClick(SpeakDto speakDto) {
        this.speakDto = speakDto;
    }
}
