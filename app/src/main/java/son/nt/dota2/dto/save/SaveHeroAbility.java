package son.nt.dota2.dto.save;

import java.util.List;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.AbilityDto;

/**
 * Created by Sonnt on 8/6/15.
 */
public class SaveHeroAbility extends AObject {
    public String heroID;
    public List<AbilityDto> listAbility;

    public SaveHeroAbility(String heroID, List<AbilityDto> listAbility) {
        this.heroID = heroID;
        this.listAbility = listAbility;
    }
}
