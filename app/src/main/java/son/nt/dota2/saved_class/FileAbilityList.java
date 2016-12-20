package son.nt.dota2.saved_class;

import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by sonnt on 12/11/16.
 */

public class FileAbilityList extends AObject {
    public int version = 1;
    public List<AbilitySoundDtoSaved> mList;

    public FileAbilityList() {
    }

    public FileAbilityList(List<AbilitySoundDtoSaved> list) {
        mList = list;
    }
}
