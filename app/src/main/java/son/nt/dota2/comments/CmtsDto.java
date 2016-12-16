package son.nt.dota2.comments;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/5/16.
 */

public class CmtsDto extends RealmObject implements ISound {

    @PrimaryKey
    int id;
    String message;
    String image;
    long createTime;
    String fromID;
    String fromName;
    String createAt;

    public CmtsDto() {
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getLink() {
        return null;
    }

    @Override
    public String getArcanaLink() {
        return null;
    }

    @Override
    public String getImage() {
        return null;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getSoundType() {
        return 0;
    }

    @Override
    public String getSavedRootFolder() {
        return null;
    }

    @Override
    public String getSavedBranchFolder() {
        return null;
    }
}
