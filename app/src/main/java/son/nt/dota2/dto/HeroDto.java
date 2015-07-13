package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeroDto implements Serializable{

    public String id;
    public String name;
//    public String voiceName;
    // 0: Str, 1: Agi; 3: Intel
    public String group ;
    public String hrefDota2;
    public String avatarThubmail;
    public String avatarLarge;

    public String bgLink;
    public String bgName;

    public List<SpeakDto> listSpeaks;
//    public List<EnumRoles> roles;

    public HeroDto() {
        listSpeaks = new ArrayList<SpeakDto>();
    }
    
    
}
