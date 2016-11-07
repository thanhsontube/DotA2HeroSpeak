package son.nt.dota2.dto;

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
    public String rivalId;
    public String rivalImage;
    public String rivalName;

    public int position;

    String text;

    String itemId;

    String title;
    String link;
    String image;
    String group;

    long duration;
    String sub;

    int totalLike;
    int totalComments;

    boolean isPlaying;
    boolean isFavorite;
    boolean isLiked;

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

    public String getRivalId() {
        return rivalId;
    }

    public void setRivalId(String rivalId) {
        this.rivalId = rivalId;
    }

    public String getRivalImage() {
        return rivalImage;
    }

    public void setRivalImage(String rivalImage) {
        this.rivalImage = rivalImage;
    }

    public String getRivalName() {
        return rivalName;
    }

    public void setRivalName(String rivalName) {
        this.rivalName = rivalName;
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
}
