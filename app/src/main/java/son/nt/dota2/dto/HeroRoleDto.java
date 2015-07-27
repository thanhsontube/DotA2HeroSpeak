package son.nt.dota2.dto;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/27/15.
 */
public class HeroRoleDto extends AObject{
    public String heroId;
    public String roleName;

    public HeroRoleDto(String heroId, String roleName) {
        this.heroId = heroId;
        this.roleName = roleName;
    }
}
