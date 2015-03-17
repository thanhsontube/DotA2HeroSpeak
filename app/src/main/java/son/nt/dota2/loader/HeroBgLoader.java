package son.nt.dota2.loader;

import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.loader.base.ContentLoader;


public abstract class HeroBgLoader extends ContentLoader<HeroData> {

    public HeroBgLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected HeroData handleStream(InputStream in) throws IOException {
        HeroData herodata = new HeroData();
        List<HeroDto> listHeroes = herodata.listHeros;
        HeroDto heroDto = null;
        boolean isAdd = false;

        boolean isTitle = false;
        boolean isLink = false;
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
            Log.e("", "log>>>" + "data HeroBgLoader1:" + data.length);
            tagNode = (TagNode) data[0];
            xPath = "tr/td/a/[@href]";
            data = tagNode.evaluateXPath(xPath);
            Log.e("", "log>>>" + "data HeroBgLoader2:" + data.length);
            List<String> listString = new ArrayList<String>();
            for (int i = 0; i < data.length; i++) {

                tagNode = (TagNode) data[i];
                if (tagNode.hasAttribute("title")) {
                    // Log.e("", "log>>>" + "has Title at:" + i);
                    String name = tagNode.getAttributeByName("href").replace("/", "");
                    listString.add(name);
//                    Log.v("", "log>>>" + "name:" + name);
                } else if (tagNode.hasAttribute("href") && tagNode.hasAttribute("class")) {
                    // Log.e("", "log>>>" + "has Link at:" + i);
                    isAdd = true;
                    xPath = "./img";
                    Object[] myData = tagNode.evaluateXPath(xPath);
                    if (myData != null && myData.length > 0) {
                        isLink = true;
                        TagNode nodeMyData = (TagNode) myData[0];
                        String linkImage = nodeMyData.getAttributeByName("src").replace("250", "500");
                        linkImage = linkImage.substring(0, linkImage.indexOf("?version"));
//                        Log.v("", "log>>>" + "linkImage:" + linkImage);
                        listString.add(linkImage);
                    }
                }

            }


            for (int k = 0; k < listString.size(); k += 2) {
                heroDto = new HeroDto();
                heroDto.bgName = listString.get(k);
                heroDto.bgLink = listString.get(k + 1);
                listHeroes.add(heroDto);
            }

        } catch (Exception e) {
            Log.e("tag", "log>>> " + "error HeroBgLoader:" + e);
        }
        return herodata;
    }

}
