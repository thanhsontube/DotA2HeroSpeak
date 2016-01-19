package son.nt.dota2.facebook;

import son.nt.dota2.base.AObject;

/**
 * Created by 4210047 on 3/23/2015.
 */
public class UserDto extends AObject {
    public String id;
    public String name;
    public String avatar;
    public String cmt;
    public String email;
    public String password;
    public String datePost;

    public UserDto() {
    }

    public UserDto(String id, String name, String cmt, String datePost) {
        this.id = id;
        this.name = name;
        this.cmt = cmt;
        this.datePost = datePost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCmt() {
        return cmt;
    }

    public void setCmt(String cmt) {
        this.cmt = cmt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }
}
