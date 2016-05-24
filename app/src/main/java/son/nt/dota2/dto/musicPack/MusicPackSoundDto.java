package son.nt.dota2.dto.musicPack;

import son.nt.dota2.base.AObject;

/**
 * Created by sonnt on 5/23/16.
 */
public class MusicPackSoundDto extends AObject{
    String name;
    String link;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
