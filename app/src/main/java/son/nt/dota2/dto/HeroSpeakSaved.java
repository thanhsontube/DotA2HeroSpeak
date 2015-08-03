package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 8/3/15.
 */
public class HeroSpeakSaved extends AObject {
    public String heroId;
    public List<SpeakDto> listSpeaks = new ArrayList<>();

    public HeroSpeakSaved(String heroId, List<SpeakDto> listSpeaks) {
        this.heroId = heroId;
        this.listSpeaks = listSpeaks;
    }
}
