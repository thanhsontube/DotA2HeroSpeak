package son.nt.dota2.saved_class;

import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by sonnt on 12/11/16.
 */

public class FileResponseList extends AObject {
    public int version = 1;
    public List<HeroResponsesDtoSaved> mList;

    public FileResponseList() {
    }

    public FileResponseList(List<HeroResponsesDtoSaved> list) {
        mList = list;
    }
}
