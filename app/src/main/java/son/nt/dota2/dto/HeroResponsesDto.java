package son.nt.dota2.dto;

import com.google.firebase.database.Exclude;
import com.google.gson.Gson;

import org.parceler.Parcel;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import io.realm.annotations.RealmClass;
import son.nt.dota2.MsConst;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * class contain dota 2 hero responses.
 */
@RealmClass      // required if using JDK 1.6 (unrelated to Parceler issue)
@Parcel(value = Parcel.Serialization.BEAN, analyze = {HeroResponsesDto.class})
public class HeroResponsesDto extends RealmObject implements ISound {
    public int no;

    public String heroId;
    public String heroName;
    public String heroIcon;

    public String voiceGroup;

    public String toHeroId;
    public String toHeroIcon;
    public String toHeroName;

    //sound info
    public String text;
    public String link;
    public String linkArcana;
    public String sub;

    public int position;

    public String itemId;
    public String title;
    public String image;
    public String group;

    public long duration;


    public int totalLike;
    public int totalComments;

    public boolean isPlaying;
    public boolean isFavorite;
    public boolean isLiked;

    public HeroResponsesDto(int no) {
        this.no = no;
    }

    public HeroResponsesDto() {

    }

    public HeroResponsesDto(int no, String heroId, String heroName, String heroIcon, String voiceGroup, String toHeroId, String toHeroIcon, String toHeroName, String text, String link, String linkArcana, String sub, int position, String itemId, String title, String image, String group, long duration, int totalLike, int totalComments, boolean isPlaying, boolean isFavorite, boolean isLiked) {
        this.no = no;
        this.heroId = heroId;
        this.heroName = heroName;
        this.heroIcon = heroIcon;
        this.voiceGroup = voiceGroup;
        this.toHeroId = toHeroId;
        this.toHeroIcon = toHeroIcon;
        this.toHeroName = toHeroName;
        this.text = text;
        this.link = link;
        this.linkArcana = linkArcana;
        this.sub = sub;
        this.position = position;
        this.itemId = itemId;
        this.title = title;
        this.image = image;
        this.group = group;
        this.duration = duration;
        this.totalLike = totalLike;
        this.totalComments = totalComments;
        this.isPlaying = isPlaying;
        this.isFavorite = isFavorite;
        this.isLiked = isLiked;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getHeroId() {
        return heroId;
    }

    public void setHeroId(String heroId) {
        this.heroId = heroId;
    }

    public void setHeroName(String heroName) {
        this.heroName = heroName;
    }

    public void setHeroIcon(String heroIcon) {
        this.heroIcon = heroIcon;
    }

    public String getVoiceGroup() {
        return voiceGroup;
    }

    public void setVoiceGroup(String voiceGroup) {
        this.voiceGroup = voiceGroup;
    }

    public String getToHeroId() {
        return toHeroId;
    }

    public void setToHeroId(String toHeroId) {
        this.toHeroId = toHeroId;
    }


    public String getToHeroIcon() {
        return toHeroIcon;
    }

    public void setToHeroIcon(String toHeroIcon) {
        this.toHeroIcon = toHeroIcon;
    }

    public String getToHeroName() {
        return toHeroName;
    }

    public void setToHeroName(String toHeroName) {
        this.toHeroName = toHeroName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLinkArcana() {
        return linkArcana;
    }

    public void setLinkArcana(String linkArcana) {
        this.linkArcana = linkArcana;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSub() {
        return sub;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getTitle() {
        return text;
    }

    @Override
    public String getLink() {
        return link;
    }

    @Override
    public String getImage() {
        return heroIcon;
    }

    @Override
    public String getGroup() {
        return group;
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Exclude
    public boolean isAlliMeetingGroup() {
        return "Allies Meeting".equalsIgnoreCase(voiceGroup);
    }

    @Exclude
    public boolean isEnemiesKillingGroup() {
        return "Enemies killing".equalsIgnoreCase(voiceGroup);
    }

    public String getHeroIcon() {
        return heroIcon;
    }

    @Override
    public int getSoundType() {
        return MsConst.TYPE_HERO_SOUND;
    }

    @Override
    public String getArcanaLink() {
        return linkArcana;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("no", no);

        result.put("heroId", heroId);
        result.put("heroName", heroName);
        result.put("heroIcon", heroIcon);

        result.put("voiceGroup", voiceGroup);

        result.put("toHeroId", toHeroId);
        result.put("toHeroIcon", toHeroIcon);
        result.put("toHeroName", toHeroName);

        result.put("text", text);
        result.put("link", link);
        result.put("linkArcana", linkArcana);
        result.put("sub", sub);
        return result;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    @Override
    public String getSavedRootFolder() {
        return MsConst.SAVE_FOLDER_HERO_SOUND;
    }

    @Override
    public String getSavedBranchFolder() {
        return heroId;
    }

    @Override
    public String getId() {
        return link;
    }
}
