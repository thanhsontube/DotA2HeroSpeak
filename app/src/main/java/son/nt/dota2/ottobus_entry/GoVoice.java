package son.nt.dota2.ottobus_entry;

import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/1/16.
 */

public class GoVoice {
    public ISound mHeroResponsesDto;
    public boolean arcana;

    public GoVoice(ISound heroResponsesDto, boolean arcana) {
        mHeroResponsesDto = heroResponsesDto;
        this.arcana = arcana;
    }

    public GoVoice() {
    }
}
