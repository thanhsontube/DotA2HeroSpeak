package son.nt.dota2.htmlcleaner.role;

import java.io.Serializable;

/**
 * Created by Sonnt on 7/13/15.
 */
public class RoleDto implements Serializable{
    public int no;
    public String name;
    public String linkIcon;
    public int icon;
    public String slogan;
    public String description;

    public RoleDto() {
        icon = 0;
        slogan = "";
        description = "";
        name = "";
        linkIcon = "";
    }
}
