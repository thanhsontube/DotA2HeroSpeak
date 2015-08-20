package son.nt.dota2.htmlcleaner.role;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/13/15.
 */
public class RoleDto extends AObject{
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
