package son.nt.dota2.dto.save;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;
import son.nt.dota2.htmlcleaner.role.RoleDto;

/**
 * Created by Sonnt on 8/20/15.
 */
public class SaveRoles extends AObject {
    public List<RoleDto> list = new ArrayList<>();

    public SaveRoles(List<RoleDto> list) {
        this.list = list;
    }
}
