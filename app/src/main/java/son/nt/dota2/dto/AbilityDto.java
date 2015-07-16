package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AbilityDto  extends AObject{
    public String name; //Starstorm
    public String heroName; //Mirana
    public String ability; // No targer
    public String affects; //enemies
    public String damage; //magical

    public String sound;

    public String description;//Calls down a wave of meteors to damage nearby enemy units. The nearest enemy unit in 325 range will take a second hit for 75% of the damage.
    public String linkImage ; //

    Map<String, String> killInfo;
    public boolean isUltimate = false;
    public List<String> coolDowns = new ArrayList<>();
    public List<String> manacCosts = new ArrayList<>();

    public void setTypes (String ability, String affect, String damage) {
        this.ability = ability;
        this.affects = affect;
        this.damage = damage;
    }

    public AbilityDto() {
        coolDowns.add("-");
        coolDowns.add("-");
        coolDowns.add("-");
        coolDowns.add("-");

        manacCosts.add("-");
        manacCosts.add("-");
        manacCosts.add("-");
        manacCosts.add("-");
    }
}
