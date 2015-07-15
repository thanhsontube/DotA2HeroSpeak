package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.htmlcleaner.role.RoleDto;

/**
 * Created by Sonnt on 7/14/15.
 */
public class HeroEntry implements Serializable {
    public String id;
    public String name; //Juggernaut
    public String fullName; // Yurnero, the Juggernaut
    public String nickName;
    public String friendlyName; //Ju
    //link voice
    public String voiceName; //http://hydra-media.cursecdn.com/dota2.gamepedia.com/1/1e/Jug_spawn_01.mp3

    //"By the Visage of Vengeance, which drowned in the Isle of Masks, I will carry on the rites of the Faceless Ones."
    public String slogan;

    //http://hydra-media.cursecdn.com/dota2.gamepedia.com/2/26/Jug_rare_09.mp3
    public String sloganVoice;

    //Pip carry.png Carry / Pip pusher.png Pusher
    public List<RoleDto> roles = new ArrayList<>();

    /**
     * No one has ever seen the face hidden beneath the mask of Yurnero the Juggernaut. It is only speculation that he even has one.
     */
    public String lore;




    public List<AbilityDto> listAbilities = new ArrayList<>();

    public List<GalleryDto> gallery = new ArrayList<>();

    // 0: Str, 1: Agi; 3: Intel
    public String group ;
    public String hrefDota2;
    public String avatarThubmail;
    public String avatarLarge;

    //http://hydra-media.cursecdn.com/dota2.gamepedia.com/thumb/0/08/Balance_of_the_Bladekeeper_Loading_Screen_16x9.png/1600px-Balance_of_the_Bladekeeper_Loading_Screen_16x9.png
    public String bgLink;
    public String bgName;

    public List<SpeakDto> listSpeaks = new ArrayList<>();
//    public List<EnumRoles> roles;

    public HeroEntry() {
    }
}
