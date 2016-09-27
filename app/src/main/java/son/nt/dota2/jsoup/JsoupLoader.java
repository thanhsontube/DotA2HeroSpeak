package son.nt.dota2.jsoup;

import org.jsoup.Jsoup;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.home.HeroBasicDto;
import son.nt.dota2.utils.Logger;

/**
 * Created by sonnt on 9/20/16.
 */
public class JsoupLoader {
    public static final String TAG = JsoupLoader.class.getSimpleName();
    public static final String HERO_ICON = "http://dota2.gamepedia.com/Heroes_by_release";
    public static final String HERO_AVATAR = "http://dota2.gamepedia.com/Heroes";
    public static final String HERO_LORD = "http://dota2.gamepedia.com/Anti-Mage/Lore";

    public void withGetHeroBasic_Avatar_Description() {
        new GetHeroBasic_Avatar_Description().execute();
    }

    public void withGetHeroBasic_Lord() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Lord");
        new GetHeroBasic_Lord().execute();
    }

    /**
     * this one will get Avatar
     */
    class GetHeroBasic_Avatar_Description extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_AVATAR).get();
                String img = document.select("a[href=/Doom]").select("img").attr("src");
                img = img.substring(0, img.lastIndexOf("?version"));
                Logger.debug(TAG, ">>>" + "img:" + img);

//                Elements elements = document.select("a[href=/Doom]").get(0).parents();
//                String des = elements.select("td[style=padding:8px]").text();
//                Logger.debug(TAG, ">>>" + "des:" + des);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private String getAvatar(String heroID) {
        try {
            Document document = Jsoup.connect(HERO_AVATAR).get();
            final String cssQuery = "a[href=/" + heroID + "]";
            String img = document.select(cssQuery).select("img").attr("src");
            img = img.substring(0, img.lastIndexOf("?version"));
            Logger.debug(TAG, ">>>" + "img:" + img);
            return img;

//                Elements elements = document.select("a[href=/Doom]").get(0).parents();
//                String des = elements.select("td[style=padding:8px]").text();
//                Logger.debug(TAG, ">>>" + "des:" + des);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //http://dota2.gamepedia.com/Heroes_by_release
    public void withGetHeroBasic_Icon() {
        new GetHeroBasic_Icon().execute();

    }

    /**
     * this one will get : name; heroID; heroIcon; no
     */
    class GetHeroBasic_Icon extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_ICON).get();
                Elements icons = document.select("span[style=white-space:nowrap]");
                Logger.debug(TAG, ">>>" + "icons:" + icons.size());
                List<HeroBasicDto> heroes = new ArrayList<>();
                HeroBasicDto dto;
                int no = 1;
                for (Element icon : icons) {
                    Elements iconChild = icon.select("a[href]");
                    Element child = iconChild.get(0);
                    String title = child.attr("title");
                    String href = child.attr("href");
                    Logger.debug(TAG, ">>>" + "title:" + title + ";href:" + href);

                    String heroIcon = child.select("img").attr("src");
                    heroIcon = heroIcon.substring(0, heroIcon.lastIndexOf("?version"));
                    Logger.debug(TAG, ">>>" + "src:" + heroIcon);

                    dto = new HeroBasicDto();
                    dto.name = title;
                    dto.heroId = href.replace("/", "");
                    dto.heroIcon = heroIcon;
                    dto.no = no;

                    heroes.add(dto);
                    no++;
                }

                for (HeroBasicDto hero : heroes) {

                    final String avatar = getAvatar(hero.heroId);
                    if (avatar == null) {
                        Logger.error(TAG, ">>> Error:" + "Null avatar:" + avatar);
                    } else {
                        hero.avatar = avatar;
                    }

                }


//                Realm realm = Realm.getDefaultInstance();
//                realm.beginTransaction();
//
//
//
//                realm.commitTransaction();
//                realm.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class GetHeroBasic_Lord extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_LORD).get();
                Elements main = document.select("div[id=mw-content-text]");
                List<DataNode> dataNodes = main.get(0).dataNodes();
                Logger.debug(TAG, ">>>" + "dataNodes:" + dataNodes.size());

                for (DataNode e : dataNodes) {
                    Logger.debug(TAG, ">>>" + "e:" + e.nodeName());
                }
//
//                int nodeMeeting;
//                int nodeKilling;
//                int nodeOthers;
//
//                Element elementMeeting = null;
//                Element elementKilling = null;
//                int i = 0;
//                for (Element element : elements) {
//                    if (element.nodeName().equalsIgnoreCase("p") && element.text() != null && element.text().contains("Allies meeting")) {
//                        Logger.error(TAG, ">>> Error nodeMeeting:" + i + ":" + element.text());
//                        nodeMeeting = i;
//                        elementMeeting = elements.get(nodeMeeting + 1);
//                    }
//
//                    if (element.nodeName().equalsIgnoreCase("p") && element.text() != null && element.text().contains("Enemies killing ")) {
//                        Logger.error(TAG, ">>> Error nodeKilling:" + i + ":" + element.text());
//                        nodeKilling = i;
//                        elementKilling = elements.get(nodeKilling + 1);
//                    }
//
//                    if (element.nodeName().equalsIgnoreCase("p") && element.text() != null && element.text().contains("Others ")) {
//                        Logger.error(TAG, ">>> Error nodeOthers:" + i + ":" + element.text());
//                        nodeOthers = i;
//                    }
//
//                    i++;
//                }
//
//                if (elementMeeting != null) {
//
//                    Logger.debug(TAG, ">>>" + "elementMeeting:" + elementMeeting.getAllElements().size());
//                }
//
//                if (elementKilling != null) {
//
//                    Logger.debug(TAG, ">>>" + "elementKilling:" + elementKilling.getAllElements().size());
//                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void getLordVoice() {

    }
}
