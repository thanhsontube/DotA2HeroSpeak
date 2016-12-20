package son.nt.dota2.dto.musicPack;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sonnt on 5/23/16.
 */
public class MusicPackSoundRealm extends RealmObject {

    @PrimaryKey
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

    public static MusicPackSoundDto copyData(MusicPackSoundRealm dto) {

        MusicPackSoundDto result = new MusicPackSoundDto();
        result.setItemId(dto.getItemId());
        result.setTitle(dto.getTitle());
        result.setLink(dto.getLink());
        result.setImage(dto.getImage());
        result.setGroup(dto.getGroup());
        result.setDuration(dto.getDuration());
        result.setSub(dto.getSub());
        result.setTotalLike(dto.getTotalLike());
        result.setTotalComments(dto.getTotalComments());
        result.setPlaying(dto.isPlaying());
        result.setFavorite(dto.isFavorite());
        result.setLiked(dto.isLiked());
        return result;
    }
    
    public void createDb (MusicPackSoundDto dto)
    {
         itemId = dto.getItemId();
         title = dto.getTitle();
         link = dto.getLink();
         image = dto.getImage();
         group = dto.getGroup();
         duration = dto.getDuration();
         sub = dto.getSub();
         totalLike = dto.getTotalLike();
         totalComments = dto.getTotalComments();
         isPlaying = dto.isPlaying();
         isFavorite = dto.isFavorite();
         isLiked = dto.isLiked();
    }

    public MusicPackSoundRealm() {
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public int getTotalComments() {
        return totalComments;
    }

    public void setTotalComments(int totalComments) {
        this.totalComments = totalComments;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
}
