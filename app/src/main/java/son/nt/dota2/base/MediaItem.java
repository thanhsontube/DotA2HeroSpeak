package son.nt.dota2.base;

/**
 * Created by sonnt on 5/25/16.
 */
public class MediaItem {
    String title;
    String mUrl;
    String image;
    String group;

    public MediaItem(String name, String link, String o, String g)
    {
        title = name;
        mUrl = link;
        image = o;
        group = g;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
