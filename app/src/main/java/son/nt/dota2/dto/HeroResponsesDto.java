package son.nt.dota2.dto;

import son.nt.dota2.base.MediaItem;

/**
 * class contain dota 2 hero responses.
 */
public class HeroResponsesDto extends MediaItem {
    public int no;
    public String heroId;
    public String voiceGroup;
    public String rivalId;
    public String rivalImage;
    public String rivalName;

    public int position;

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getVoiceGroup() {
        return voiceGroup;
    }

    public void setVoiceGroup(String voiceGroup) {
        this.voiceGroup = voiceGroup;
    }

    public String getRivalId() {
        return rivalId;
    }

    public void setRivalId(String rivalId) {
        this.rivalId = rivalId;
    }

    public String getRivalImage() {
        return rivalImage;
    }

    public void setRivalImage(String rivalImage) {
        this.rivalImage = rivalImage;
    }

    public String getRivalName() {
        return rivalName;
    }

    public void setRivalName(String rivalName) {
        this.rivalName = rivalName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
