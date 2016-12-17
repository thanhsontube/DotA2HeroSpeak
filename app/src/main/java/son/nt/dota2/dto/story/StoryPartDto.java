package son.nt.dota2.dto.story;

import org.parceler.Parcel;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import son.nt.dota2.MsConst;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/5/16.
 */
@RealmClass
@Parcel(value = Parcel.Serialization.BEAN, analyze = {StoryPartDto.class})
public class StoryPartDto extends RealmObject implements ISound {

    //story_username_title_[time]
    String id;
    long createdTime;
    String heroId;
    String heroImage;

    String side;
    String description;

    String soundLink;
    String soundText;

    String viewType = MsConst.TYPE_ADD; //add , more, sound


    public StoryPartDto() {
    }

    @Override
    public String getId() {
        return id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public String getHeroImage() {
        return heroImage;
    }

    public void setHeroImage(String heroImage) {
        this.heroImage = heroImage;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSoundLink() {
        return soundLink;
    }

    public void setSoundLink(String soundLink) {
        this.soundLink = soundLink;
    }

    public String getSoundText() {
        return soundText;
    }

    public void setSoundText(String soundText) {
        this.soundText = soundText;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    @Override
    public String getTitle() {
        return soundText;
    }

    @Override
    public String getLink() {
        return soundLink;
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
        return MsConst.TYPE_CREATIVE_SOUND;
    }

    @Override
    public String getSavedRootFolder() {
        return MsConst.SAVE_FOLDER_HERO_SOUND;
    }

    @Override
    public String getSavedBranchFolder() {
        return heroId;
    }
}
