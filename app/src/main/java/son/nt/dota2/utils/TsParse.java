package son.nt.dota2.utils;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.dto.GalleryDto;
import son.nt.dota2.dto.HeroEntry;

/**
 * Created by Sonnt on 8/18/15.
 */
public class TsParse {
    public static final String TAG ="TsParse";

    public static HeroEntry parse(ParseObject p) {
        HeroEntry dto = new HeroEntry();
        int no = p.getInt("no");
        String heroId = p.getString("heroId");
        String name = p.getString("name");
        String fullName = p.getString("fullName");
        String group = p.getString("group");
        String href = p.getString("href");
        String avatar = p.getString("avatar");
        String lore = p.getString("lore");

        dto.setBaseInfo(heroId, href, avatar, group);
        dto.no = no;
        dto.name = name;
        dto.fullName = fullName;
        dto.lore = lore;
        return dto;
    }

    public static void getGif () {
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Dota2BgDto");
            query.whereEqualTo("group","gif");
            query.setLimit(200);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e != null || list.size() == 0) {
                        Logger.error(TAG, ">>>" + "Error getData nodata" + e.toString());
                        return;
                    }
                    Logger.debug(TAG, ">>>" + "getData size:" + list.size());
                    GalleryDto galleryDto;
                    for (ParseObject p : list) {

                        String heroID = p.getString("heroID");
                        String linkGif = p.getString("link");
                        galleryDto = new GalleryDto();
                        galleryDto.group = "gif";
                        galleryDto.link = linkGif;
                        galleryDto.heroID = heroID;
                        HeroManager.getInstance().getHero(heroID).gallery.add(galleryDto);
                    }
                    OttoBus.post(new GalleryDto());
                }
            });

        } catch (Exception e) {

        }
    }
}
