package son.nt.dota2.home;

import io.realm.RealmObject;

/**
 * Created by sonnt on 9/20/16.
 * heroId
 name
 fullName
 avatar
 avatar2
 heroIcon
 welcomeVoice
 group
 priority (999 default)
 attackType (melle, ranged)
 roles

 how to get HeroBasicDto from http://dota2.gamepedia.com/Heroes ????
 */
public class HeroBasicDto extends RealmObject {
    /**
     * get info from http://www.dota2.com/heroes/
     */
    public String heroId; //Dragon_Knight
    public int no = 999;
    public String name; //Juggernaut
    public String fullName; // Yurnero, the Juggernaut
    public String avatar;
    public String heroIcon;
    public String welcomeVoice;
    public String attribute;

    public String roles;
    public String description;
    public String avatar2;
    public String href;
}
