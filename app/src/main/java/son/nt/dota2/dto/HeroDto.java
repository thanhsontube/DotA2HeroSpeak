package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeroDto implements Serializable{
    public String id;
    public String name;
    // 0: Str, 1: Agi; 3: Intel
    public String group ;
    public String hrefDota2;
    public String avatarThubmail;
    public String avatarLarge;

    public List<SpeakDto> listSpeaks = new ArrayList<SpeakDto>();
    
    
}
