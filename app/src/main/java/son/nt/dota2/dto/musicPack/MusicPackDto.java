package son.nt.dota2.dto.musicPack;

import java.util.List;

/**
 * Created by sonnt on 5/23/16.
 * Music packs dto, contains things regarding this music pack, including the sounds when clicking that
 */
public class MusicPackDto {
    String group;
    String name;
    String href;
    String coverColor;
    String linkDetails;
    List<MusicPackSoundDto> list;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getCoverColor() {
        return coverColor;
    }

    public void setCoverColor(String coverColor) {
        this.coverColor = coverColor;
    }

    public String getLinkDetails() {
        return linkDetails;
    }

    public void setLinkDetails(String linkDetails) {
        this.linkDetails = linkDetails;
    }

    public List<MusicPackSoundDto> getList() {
        return list;
    }

    public void setList(List<MusicPackSoundDto> list) {
        this.list = list;
    }
}
