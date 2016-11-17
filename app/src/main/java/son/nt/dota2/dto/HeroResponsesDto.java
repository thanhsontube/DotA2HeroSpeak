package son.nt.dota2.dto;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import io.realm.RealmObject;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * class contain dota 2 hero responses.
 */
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
    public String sub;

    public int position;

    String itemId;
    String title;
    String image;
    String group;

    long duration;


    int totalLike;
    int totalComments;

    boolean isPlaying;
    boolean isFavorite;
    boolean isLiked;

    public HeroResponsesDto(int no) {
        this.no = no;
    }

    public HeroResponsesDto() {

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
        return title;
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
        result.put("sub", sub);
        return result;
    }
}