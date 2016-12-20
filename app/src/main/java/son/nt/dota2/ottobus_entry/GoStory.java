package son.nt.dota2.ottobus_entry;

import java.util.List;

import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/1/16.
 */

public class GoStory {
    public List<? extends ISound> mlist;
    public String mTitle;
    public String mUser;

    public GoStory(List<? extends ISound> mlist, String title, String user) {
        this.mlist = mlist;
        mTitle = title;
        mUser = user;
    }

    public GoStory() {
    }
}
