package son.nt.dota2.dto.story;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by sonnt on 12/7/16.
 */

@Parcel
public class StoryFireBaseDto {
    String storyId;
    String userId;
    String username;
    String userPicture;
    String title;
    long createdTime;
    List<StoryPartDto> contents;

    public StoryFireBaseDto(String storyId, String userId, String username, String userPicture, String title, long createdTime, List<StoryPartDto> contents) {
        this.storyId = storyId;
        this.userId = userId;
        this.username = username;
        this.userPicture = userPicture;
        this.title = title;
        this.createdTime = createdTime;
        this.contents = contents;
    }

    public StoryFireBaseDto() {
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

    public List<StoryPartDto> getContents() {
        return contents;
    }

    public void setContents(List<StoryPartDto> contents) {
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
