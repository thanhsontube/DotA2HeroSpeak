package son.nt.dota2.htmlcleaner.hero;

import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/23/15.
 */
public abstract class HeroListLoader extends ContentLoader<List<HeroEntry>> {


    public static final String URL_HERO_LIST = "http://www.dota2.com/heroes/";
    public static final String TAG = "HeroListLoader";

    public HeroListLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<HeroEntry> handleStream(InputStream in) throws IOException {
        List<HeroEntry> listHeroes = new ArrayList<>();
        try {

            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);

            TagNode tagNode = cleaner.clean(in);
            String xPath = "//div[@class='heroIcons']";
            Object[] data = tagNode.evaluateXPath(xPath);

            HeroEntry heroDto;
            String heroId = "";
            String href = "";
            String group = "";
            String avatar = "";
            int countNo = 1;
            if (data.length > 0) {
                for (int i = 0; i < data.length; i++) {
                    Log.e("HeroListLoader", "log>>>" + "===========" + i);
                    TagNode myNode = (TagNode) data[i];
                    List<TagNode> mList = (List<TagNode>) myNode.getAllElementsList(false);

                    Log.e("", "log>>>" + "list:" + mList.size());
                    for (TagNode tag : mList) {
                        heroDto = new HeroEntry();
                        if (i == 0 || i == 3) {
                            group = "Str";
                        } else if (i == 1 || i == 4) {
                            group = "Agi";
                        } else {
                            group = "Intel";
                        }

                        href = tag.getAttributeByName("href");
                        List<TagNode> listChild = (List<TagNode>) tag.getAllElementsList(false);
                        TagNode t2 = listChild.get(0);
                        avatar = t2.getAttributeByName("src");

                        //hero Name
                        //http://www.dota2.com/hero/Huskar/
                        String[] array = href.split("/");
                        heroId = array[array.length - 1];
                        Logger.debug(TAG, ">>>" + "No:" + countNo + ";HeroId:" + heroId + ";link:" + avatar);
                        heroDto.setBaseInfo(heroId,href, avatar, group);
                        heroDto.no = countNo;
                        listHeroes.add(heroDto);
                        countNo ++;
                        //avatar large
                        //http://cdn.dota2.com/apps/dota2/images/heroes/tinker_vert.jpg
                        //http://cdn.dota2.com/apps/dota2/images/heroes/queenofpain_hphover.png
                    }
                }
            }
        } catch (Exception e) {
            Log.e("HeroListLoader", "log>>>" + "data err :" + e);
        }
        return listHeroes;
    }
}
