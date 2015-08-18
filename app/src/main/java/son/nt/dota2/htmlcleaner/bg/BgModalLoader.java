package son.nt.dota2.htmlcleaner.bg;

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
 * Created by Sonnt on 8/19/15.
 */
public abstract class BgModalLoader extends ContentLoader<List<HeroEntry>> {
    public static final String TAG = "BgModalLoader";

    public BgModalLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<HeroEntry> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);

            TagNode tagNode = cleaner.clean(in);
            String xPath = "//table[@class='wikitable']/tbody";
            Object[] data = tagNode.evaluateXPath(xPath);

            TagNode nodeA = (TagNode) data[0];
            Logger.debug(TAG, ">>>" + "nodA:" + nodeA.getChildTagList().size());
            int i = 1;
            List<HeroEntry> list = new ArrayList<>();
            HeroEntry heroEntry;
            for (TagNode tag : nodeA.getChildTagList()) {
                Logger.debug(TAG, ">>>" + "----Tag:" + i++);
                String pathB = "//a[@class='image']";
                Object[] objectB = tag.evaluateXPath(pathB);
                if (objectB.length > 0) {
                    TagNode nodeB = ((TagNode) objectB[0]).getChildTagList().get(0);
                    String alt = nodeB.getAttributeByName("alt");
                    //Drow Ranger model.png
                    if (alt.contains("Nature") && alt.contains("Prophet")) {
                        alt = "Natures_Prophet";
                    }
                    String heroID = alt.replace(" model.png", "").replace(" model v2.png", "").trim();
                    heroID = heroID.replace(" ", "_");
                    if (heroID.equals("Wraith_king")) {
                        heroID = "Wraith_King";
                    }

                    if (heroID.equals("Doom_Bringer")) {
                        heroID = "Doom";
                    }
                    if (heroID.equals("Necrolyte")) {
                        heroID = "Necrophos";
                    }
                    if (heroID.equals("Lycanthrope")) {
                        heroID = "Lycan";
                    }
                    if (heroID.equals("Windrunner")) {
                        heroID = "Windranger";
                    }
                    if (heroID.equals("Bane_Elemental")) {
                        heroID = "Bane";
                    }

//                    String src = nodeB.getAttributeByName("src");
                    String srcset = nodeB.getAttributeByName("srcset");
                    String link = "";
                    if (srcset.contains("1.5x,")) {

                        link = srcset.substring(0, srcset.indexOf("1.5x,")).trim();
                    }
                    Logger.debug(TAG, ">>>" + "heroID:" + heroID + ";link:" + link);
                    heroEntry = new HeroEntry();
                    heroEntry.heroId = heroID;
                    heroEntry.bgLink = link;
                    list.add(heroEntry);

                }
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
