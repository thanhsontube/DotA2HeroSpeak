package son.nt.dota2.ottobus_entry;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 8/19/15.
 */
public class GoShare extends AObject{
    public String type;
    public SpeakDto speakDto;

    public GoShare(String type, SpeakDto speakDto) {
        this.type = type;
        this.speakDto = speakDto;
    }
}
