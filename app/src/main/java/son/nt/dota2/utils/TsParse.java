package son.nt.dota2.utils;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.comments.CommentDto;
import son.nt.dota2.dto.GalleryDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroRoleDto;
import son.nt.dota2.dto.HeroSavedDto;

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
        String bgLink = p.getString("bgLink");

        dto.setBaseInfo(heroId, href, avatar, group);
        dto.no = no;
        dto.name = name;
        dto.fullName = fullName;
        dto.lore = lore;
        dto.bgLink = bgLink;
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

    public static void updateBgToHeroEntry(final HeroEntry dto) {
        Logger.debug(TAG, ">>>" + "updateBgToHeroEntry:" + dto.heroId);

        ParseQuery<ParseObject> query = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
        query.whereEqualTo("heroId", dto.heroId);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null || list.size() > 0) {

                    ParseObject p = list.get(0);
                    String objectId = p.getObjectId();
                    Logger.debug(TAG, ">>>" + "objectId:" + objectId);

                    ParseQuery<ParseObject> mQuery = ParseQuery.getQuery(HeroEntry.class.getSimpleName());
                    mQuery.getInBackground(objectId, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject p, ParseException e) {
                            p.put("bgLink", dto.bgLink);
                            p.saveInBackground();
                            Logger.debug(TAG, ">>>" + "updateBgToHeroEntry OK with:" + dto.fullName);
                        }
                    });

                }
            }
        });
    }
    public static void getkensBurns () {
        final List<String> listKens = new ArrayList<>();
        try
        {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Dota2BgDto");
            query.whereNotEqualTo("group", "gif");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    if (e != null || list.size() == 0) {
                        Logger.error(TAG, ">>>" + "Error getData nodata" + e.toString());
                        return;
                    }
                    Logger.debug(TAG, ">>>" + "getData size:" + list.size());
                    for (ParseObject p : list) {

                        String linkGif = p.getString("link");
                        listKens.add(linkGif);
                    }
                    ResourceManager.getInstance().listKenburns.clear();
                    ResourceManager.getInstance().listKenburns.addAll(listKens);

                }
            });

        } catch (Exception e) {

        }
    }

    public static void push(CommentDto d) {
        TsGaTools.trackPages(MsConst.TRACK_PUSH_COMMENT);
        ParsePush p = new ParsePush();
        p.setChannel(MsConst.CHANNEL_COMMON);
        StringBuilder s = new StringBuilder();
        s.append(d.getFromName());
        s.append(">>>");
        s.append(d.getSpeakDto().heroId);
        s.append("(" + d.getSpeakDto().text + "):");
        s.append(d.getMessage());

        p.setMessage(s.toString());
        p.sendInBackground();
    }

    public static  void getHeroesRoles () {
        Logger.debug(TAG, ">>>" + "=====getHeroesRoles");
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(HeroRoleDto.class.getSimpleName());
        query.setLimit(400);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    return;
                }
                for (ParseObject p : list) {
                    String heroID = p.getString("heroID");
                    String role = p.getString("roleName");
                    HeroManager.getInstance().getHero(heroID).roles.add(role);

                }
                Logger.error(TAG, ">>>" + "---finsh getHeroesRoles");
                try {
                    FileUtil.saveObject(ResourceManager.getInstance().getContext(), new HeroSavedDto(HeroManager.getInstance().listHeroes),
                            HeroSavedDto.class.getSimpleName());
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        });
    }
}
