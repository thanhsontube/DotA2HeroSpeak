package son.nt.dota2.htmlcleaner.hero;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;

import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/23/15.
 */
public abstract class HeroNameLoader extends ContentLoader<HeroEntry> {
    private static final String TAG = "HeroNameLoader";

    public HeroNameLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected HeroEntry handleStream(InputStream in) throws IOException {
        HeroEntry heroEntry = new HeroEntry();
        String heroName = "";

        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);

            TagNode tagNode = cleaner.clean(in);
            String xPath = "//h1[@id='firstHeading'] [@class='firstHeading']";
            Object[] data = tagNode.evaluateXPath(xPath);
            if (data.length > 0) {
                TagNode nodeA = ((TagNode) data[0]).getChildTagList().get(0);
                 heroName = nodeA.getText().toString();
                heroEntry.name = heroName;
                Logger.debug(TAG, ">>>" + "heroId:" + heroName);
            }

            String xpath2 = "//td[@style='font-weight:bold;font-size:13px;text-align:center;']";
            Object []data2 = tagNode.evaluateXPath(xpath2);
            if (data2.length > 0) {
                TagNode nodeA = (TagNode) data2[0];
                Logger.debug(TAG, ">>>" + "Fullname:" + nodeA.getText().toString().replace("\n","").trim());
                heroEntry.fullName = nodeA.getText().toString().replace("\n","").trim();
            }


            // roles
            String xpath3 = "//table[@style='background: none; border-top: 1px solid #AAAAAA; border-bottom: 1px solid #AAAAAA;']";
            Object []data3 = tagNode.evaluateXPath(xpath3);
            TagNode nodeA = (TagNode) data3[0];
            Logger.debug(TAG, ">>>" + "NodeA:" + nodeA.getChildTagList().size());
            Logger.debug(TAG, ">>>" + "nideA text:" + nodeA.getText());
            String textA = nodeA.getText().toString();
            String []arr = textA.split("\n");
            String roles = "";
            String lore = "";
            for (int i = 0; i < arr.length ; i ++) {
                if (arr[i].contains("Role:")) {
                    roles = arr[i + 1];
                }

                if (arr[i].contains("Lore:")) {
                    lore = arr[i + 1];
                }
            }
            Logger.debug(TAG, ">>>" + "roles:" + roles);
            heroEntry.lore = lore;

            String []arr2 = roles.split("/");
            for (String s : arr2) {
                heroEntry.roles.add(s.trim());
            }
        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "Error:" + e.toString());
        }
        return heroEntry;

    }
}
