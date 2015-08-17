package son.nt.dota2.comments;

import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 8/12/15.
 */
public class CommentDto extends AObject{
    int id;
    SpeakDto speakDto;
    String message;
    String image;
    long createTime;
    String fromID;
    String fromName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SpeakDto getSpeakDto() {
        return speakDto;
    }

    public void setSpeakDto(SpeakDto speakDto) {
        this.speakDto = speakDto;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getFromID() {
        return fromID;
    }

    public void setFromID(String fromID) {
        this.fromID = fromID;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public CommentDto() {
    }
}
