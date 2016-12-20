package son.nt.dota2.utils;

import android.support.annotation.NonNull;

import son.nt.dota2.MsConst;
import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 12/10/16.
 */

public class Dota2Util {
    public static String getFromHero (@NonNull HeroResponsesDto dto) {
        final boolean isSwap = dto.getVoiceGroup().endsWith(MsConst.LORD_KILLING) || dto.getVoiceGroup().endsWith(MsConst.LORD_MEETING);
        if (isSwap) {
            return dto.getToHeroId();
        }
        return dto.getHeroId();
    }
}
