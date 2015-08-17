package son.nt.dota2.data;

/**
 * Created by Sonnt on 3/21/2015.
 */
public class SaveDto {
    public String heroName;
    public String heroLink;
    public String speakContent;
    public String speakLink;
    public String speakGroup;
    public String rivalImage;
    public String saveTime;
    public String no = "";

    public SaveDto(String heroName, String heroLink, String speakContent, String speakLink, String time) {
        this.heroName = heroName;
        this.heroLink = heroLink;
        this.speakContent = speakContent;
        this.speakLink = speakLink;
        this.saveTime = time;
    }


}
