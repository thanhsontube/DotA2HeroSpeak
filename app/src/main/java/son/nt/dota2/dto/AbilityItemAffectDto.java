package son.nt.dota2.dto;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/17/15.
 * This object is used to save all items affecting to skill
 * 07-17 11:08:53.388  16742-17064/dev.son.nt.dota2 D/AbilitiesLoader﹕ >>>n0:  Causes each initial hero hit to echo twice.
 ;item:http://hydra-media.cursecdn.com/dota2.gamepedia.com/thumb/e/eb/Aghanim%27s_Scepter_small.png/16px-Aghanim%27s_Scepter_small.png?version=0b2f025311b734c868a454afc6579640;alt:Can be Improved by Aghanim&#39;s Scepter (* shows the improved values).

 */
public class AbilityItemAffectDto extends AObject {

    public String heroId;
    public String abilityName;

    public String src;
    public String alt;
    public String text;

    public AbilityItemAffectDto(String src, String alt, String text) {
        this.src = src;
        this.alt = alt;
        this.text = text;
    }
}
