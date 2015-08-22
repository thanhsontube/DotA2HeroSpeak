package son.nt.dota2.ottobus_entry;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 8/22/15.
 */
public class GoDownload extends AObject {
    int group;
    int count;
    String heroID;
    String link;
    String voiceText;

    public GoDownload(int group, int count, String heroID, String link, String text) {
        this.group = group;
        this.count = count;
        this.heroID = heroID;
        this.link = link;
        this.voiceText = text;
    }

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getHeroID() {
        return heroID;
    }

    public void setHeroID(String heroID) {
        this.heroID = heroID;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
