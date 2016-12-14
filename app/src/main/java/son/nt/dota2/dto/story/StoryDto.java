package son.nt.dota2.dto.story;

import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sonnt on 12/7/16.
 */

public class StoryDto extends RealmObject {

    String storyId;
    String userId;
    String username;
    String userPicture;
    String title;
    long createdTime;

    RealmList<StoryPartDto> contents;

    public StoryDto() {
    }

    public String getStoryId() {
        return storyId;
    }

    public void setStoryId(String storyId) {
        this.storyId = storyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public RealmList<StoryPartDto> getContents() {
        return contents;
    }

    public void setContents(RealmList<StoryPartDto> contents) {
        this.contents = contents;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture;
    }
}
