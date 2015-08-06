package son.nt.dota2.dto;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AbilityNotesDto extends AObject {

    public String heroId;
    public String abilityName;
    public String notes;

    public AbilityNotesDto(String notes) {
        this.notes = notes;
    }
}
