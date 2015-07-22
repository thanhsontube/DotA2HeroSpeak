package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AbilityDto  extends AObject{
    public int no;
    public String heroName; //Mirana
    public String name; //Starstorm
    public String ability; // No targer
    public String affects; //enemies
    public String damage; //magical

    public String sound;

    public String description;//Calls down a wave of meteors to damage nearby enemy units. The nearest enemy unit in 325 range will take a second hit for 75% of the damage.
    public String linkImage ; //

    public boolean isUltimate = false;
    public List<AbilityLevelDto> listAbilityPerLevel = new ArrayList<>();
    public List<AbilityItemAffectDto> listItemAffects = new ArrayList<>();
    public List<String> listNotes = new ArrayList<>();

    public AbilityDto () {
        no = -1;
        heroName = name = ability = affects = damage = sound = description = linkImage = "";
    }

    public void setTypes (String ability, String affect, String damage) {
        this.ability = ability;
        this.affects = affect;
        this.damage = damage;
    }

    public void setTypes (String [] arr) {
        this.ability = arr[0];
        this.affects = arr[1];
        this.damage = arr[2];
    }
}
