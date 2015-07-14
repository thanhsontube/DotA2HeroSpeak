package son.nt.dota2.dto;

import java.util.Map;

/**
 * Created by Sonnt on 7/14/15.
 */
public class AbilityDto {
    public String name; //Starstorm
    public String heroName; //Mirana
    public String ability; // No targer
    public String affects; //enemies
    public String damage; //magical

    public String sound;

    public String description;//Calls down a wave of meteors to damage nearby enemy units. The nearest enemy unit in 325 range will take a second hit for 75% of the damage.
    public String linkImage ; //

    Map<String, String> killInfo;



}
