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

    public void withGetHeroBasic_Avatar_Description (){
        new GetHeroBasic_Avatar_Description().execute();
    }

    class GetHeroBasic_Avatar_Description extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_AVATAR).get();
                String href = "/Doom";

//                Elements elements = document.select("a[href=/Doom]");
//                Elements elements = document.select("a[href=/Doom]").select("a[img]");
                String img = document.select("a[href=/Doom]").select("img").attr("src");
                img = img.substring(0, img.lastIndexOf("?version"));


//                Logger.debug(TAG, ">>>" + "elements:" + elements.size());
                Logger.debug(TAG, ">>>" + "img:" + img);

                Elements elements = document.select("a[href=/Doom]").parents();
                String des = elements.select("td[style=padding:8px]").text();
                Logger.debug(TAG, ">>>" + "des:" + des);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


    //http://dota2.gamepedia.com/Heroes_by_release
    public void withGetHeroBasic_Icon() {
        new GetHeroBasic_Icon().execute();

    }

    class GetHeroBasic_Icon extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document document = Jsoup.connect(HERO_ICON).get();
                Elements icons = document.select("span[style=white-space:nowrap]");
                Logger.debug(TAG, ">>>" + "icons:" + icons.size());
                List<HeroBasicDto> heroes = new ArrayList<>();
                HeroBasicDto dto;
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
                    dto.heroId = href.replace("/","");
                    dto.heroIcon = heroIcon;

                    heroes.add(dto);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
