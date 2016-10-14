package son.nt.dota2.dto.home;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

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
    @PrimaryKey
    public String heroId; //Dragon_Knight
    public int no;
    public int priority = 999;
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
    public String group;
    public String bgLink;


    public HeroBasicDto() {
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("heroId",heroId);
        result.put("no",no);
        result.put("priority",priority);
        result.put("name",name);
        result.put("fullName",fullName);
        result.put("avatar",avatar);
        result.put("heroIcon",heroIcon);
        result.put("welcomeVoice",welcomeVoice);
        result.put("attribute",attribute);
        result.put("roles",roles);
        result.put("description",description);
        result.put("avatar2",avatar2);
        result.put("href",href);
        result.put("group",group);
        result.put("bgLink",bgLink);
        return result;
    }
}
