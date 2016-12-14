package son.nt.dota2.dto.kenburns;

import com.google.gson.Gson;

import io.realm.RealmObject;

/**
 * Created by sonnt on 12/14/16.
 * Save nice background and display as a background
 */

public class BgImageDto extends RealmObject {

    public int no;
    public String link;
    public String note;

    public BgImageDto() {
    }

    public BgImageDto(int no, String link, String note) {
        this.no = no;
        this.link = link;
        this.note = note;
    }

    public BgImageDto(String link, String note) {
        this.link = link;
        this.note = note;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
