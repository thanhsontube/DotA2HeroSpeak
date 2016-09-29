package son.nt.dota2.jsoup;

import org.jsoup.Jsoup;
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
    public static final String HERO_LORD2 = "http://dota2.gamepedia.com/Underlord/Lore";
    public static final String HERO_RESPONSE = "http://dota2.gamepedia.com/Queen_of_Pain/Responses";

    public void withGetHeroBasic_Avatar_Description() {
        new GetHeroBasic_Avatar_Description().execute();
    }

    public void withGetHeroBasic_Lord() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Lord");
        new GetHeroBasic_Lord().execute();
    }

    public void withGetHeroBasic_Response() {
        Logger.debug(TAG, ">>>" + "withGetHeroBasic_Response");
        new GetHeroBasic_Responses().execute();
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
                Elements mains = document.select("div[id=mw-content-text]").get(0).getElementsByTag("ul");
                Logger.debug(TAG, ">>>" + "mains:" + mains.size());

                for (Element element : mains) {
                    Logger.debug(TAG, ">>>" + "-----");
                    Element before = element.previousElementSibling();
                    if (before.text().contains("Allies meeting")) {
                        Logger.debug(TAG, ">>>" + "***Allies meeting");
                        final Elements text = element.getElementsByTag("li");
                        for (Element d : text) {
                            Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                            String mp3 = d.select("a[class=sm2_button]").attr("href");
                            Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                            String id = d.select("img[alt]").get(0).parent().attr("href");
                            Logger.debug(TAG, ">>>" + "id:" + id);
                        }


                    }
                    if (before.text().contains("Enemies killing")) {
                        Logger.debug(TAG, ">>>" + "***Enemies killing");
                        final Elements href = element.getElementsByAttribute("href");
                        final Elements text = element.getElementsByTag("li");
                        for (Element d : text) {
                            Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                            String mp3 = d.select("a[class=sm2_button]").attr("href");
                            Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                            String id = d.select("img[alt]").get(0).parent().attr("href");
                            Logger.debug(TAG, ">>>" + "id:" + id);


                        }


                    }
                    if (before.text().contains("Others")) {
                        Logger.debug(TAG, ">>>" + "***Others");
                        final Elements href = element.getElementsByAttribute("href");
                        final Elements text = element.getElementsByTag("li");
                        for (Element d : text) {
                            Logger.debug(TAG, ">>>" + "d:" + d.ownText());
                            String mp3 = d.select("a[class=sm2_button]").attr("href");
                            Logger.debug(TAG, ">>>" + "mp3:" + mp3);

                            String id = d.select("img[alt]").get(0).parent().attr("href");
                            Logger.debug(TAG, ">>>" + "id:" + id);


                        }

                    }


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    //http://dota2.gamepedia.com/Queen_of_Pain/Responses
    class GetHeroBasic_Responses extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_RESPONSE).get();
                Elements mains = document.select("div[id=mw-content-text]").get(0).getElementsByTag("ul");
                Logger.debug(TAG, ">>>" + "mains:" + mains.size());

                int i = 0;
                for (Element element : mains) {
                    Logger.debug(TAG, ">>>" + "****:" + i + ";element:" + element.tagName());
                    Element before = element.previousElementSibling();
                    Logger.debug(TAG, ">>>" + "before:" + (before == null ? "NULL" : before.tagName()));
                    if (before != null && before.tagName().equals("p"))
                    {
                        Logger.debug(TAG, ">>>" + "P:" + before.text());
                    }
//                    if (before != null && before.tagName() != null) {
//                        if (before.tagName().equals("h2")) {
//                            Logger.debug(TAG, ">>>" + "Group:" + before.text());
//                        } else if (before.tagName().equals("p")) {
//                            before = before.previousElementSibling();
//                            if (before != null) {
//                                Logger.debug(TAG, ">>>" + "Group (P) :" + before.text());
//                            }
//                        }
//                        Logger.debug(TAG, ">>>" + "before:" + before.tag().getName());
//                    }



                    i ++;


                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    private void getLordVoice() {

    }
}
