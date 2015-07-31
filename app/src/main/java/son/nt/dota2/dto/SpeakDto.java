package son.nt.dota2.dto;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 3/15/2015.
 */
public class SpeakDto extends AObject {
    public int no;
    public String heroId;
    public String voiceGroup;
    public String link;
    public String text;
    public String rivalImage;
    public String rivalName;
    public boolean isTitle = false;
    public boolean isPlaying = false;

    public void setRival(String rivalName, String rivalImage, String voiceText, String voiceMp3) {
        this.rivalName = rivalName;
        this.rivalImage = rivalImage;
        this.text = voiceText;
        this.link = voiceMp3;
    }
}
