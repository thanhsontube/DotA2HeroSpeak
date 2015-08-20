package son.nt.dota2.gridmenu;

/**
 * Created by Sonnt on 8/7/15.
 */
public class GridMenuItem {
    public String title;
    public String tempTitle;
    public int iconID;
    //user for faverote and unfavorite
    public int tempIcon;

    public GridMenuItem(String title, int iconID) {
        this.title = title;
        this.iconID = iconID;
    }
}
