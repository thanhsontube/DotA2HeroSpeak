package son.nt.dota2.facebook;

/**
 * Created by 4210047 on 3/23/2015.
 */
public class UserDto {
    public String id;
    public String name;
    public String avatar;
    public String cmt;
    public String datePost;

    public UserDto() {
    }

    public UserDto(String id, String name, String cmt, String datePost) {
        this.id = id;
        this.name = name;
        this.cmt = cmt;
        this.datePost = datePost;
    }
}
