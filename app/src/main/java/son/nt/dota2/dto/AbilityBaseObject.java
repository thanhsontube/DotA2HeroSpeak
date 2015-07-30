package son.nt.dota2.dto;

/**
 * Created by Sonnt on 7/30/15.
 */
public class AbilityBaseObject extends Object {
    public String heroId;
    public String abilityName;
    public void setHeroIdAndAbilityName (String heroId, String abilityName) {
        this.heroId = heroId;
        this.abilityName = abilityName;
    }
}
