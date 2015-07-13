package son.nt.dota2.dto;

import son.nt.dota2.R;

/**
 * Created by Sonnt on 7/13/15.
 */
enum EnumRoles {
        CARRRY(R.drawable.ic_agi_24),
        SUPPORT (R.drawable.ic_str_24);



    private int icon;
    EnumRoles (int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

}
