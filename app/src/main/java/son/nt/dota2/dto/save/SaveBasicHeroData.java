package son.nt.dota2.dto.save;

import java.util.List;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.HeroEntry;

/**
 * Created by Sonnt on 8/18/15.
 */
public class SaveBasicHeroData extends AObject {
    public List<HeroEntry> list;

    public SaveBasicHeroData(List<HeroEntry> list) {
        this.list = list;
    }
}
