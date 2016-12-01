package son.nt.dota2.ottobus_entry;

import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 12/1/16.
 */

public class GoVoice {
    public HeroResponsesDto mHeroResponsesDto;
    public boolean arcana;

    public GoVoice(HeroResponsesDto heroResponsesDto, boolean arcana) {
        mHeroResponsesDto = heroResponsesDto;
        this.arcana = arcana;
    }

    public GoVoice() {
    }
}
