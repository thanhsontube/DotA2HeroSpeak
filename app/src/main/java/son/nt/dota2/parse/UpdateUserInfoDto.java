package son.nt.dota2.parse;


import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 11/28/15.
 */
public class UpdateUserInfoDto extends AObject {
    public String name;
    public String fbID;
    public String avatar;
    public String fbLink;

    public UpdateUserInfoDto(String name, String fbID, String avatar, String fbLink) {
        this.name = name;
        this.fbID = fbID;
        this.avatar = avatar;
        this.fbLink = fbLink;
    }
}
