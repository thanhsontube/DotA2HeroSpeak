package son.nt.dota2.loader;

import android.util.Log;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.loader.base.ContentLoader;


public abstract class DataLoader extends ContentLoader<HeroData> {
    
    private static final String TAG = "DataLoader";

    public DataLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }
    
    @Override
    protected HeroData handleStream(InputStream in) throws IOException {
        HeroData herodata = new HeroData();
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
            Log.e("", "log>>>" + "data:" + data.length);

            List<HeroDto> list = herodata.listHeros;
            list.clear();
            HeroDto heroDto;
            if (data.length > 0) { 
                for (int i = 0; i < data.length; i ++) {
                    Log.e("", "log>>>" + "===========" + i);
                    TagNode myNode = (TagNode) data[i];
                    List<TagNode> mList  = (List<TagNode>) myNode.getAllElementsList(false);
                    
                    Log.e("", "log>>>" + "list:" + mList.size());
                    for (TagNode tag : mList) {
                        heroDto = new HeroDto();
                        if(i == 0 || i == 3) {
                            heroDto.group = "Str";
                        } else if (i == 1 || i == 4) {
                            heroDto.group = "Agi";
                        } else {
                            heroDto.group = "Intel";
                        }
                        
                        String hrefDota2 = tag.getAttributeByName("href");
                        List<TagNode> listChild = (List<TagNode>) tag.getAllElementsList(false);
                        TagNode t2 = listChild.get(0);
                        String avatarThubmail = t2.getAttributeByName("src");
                        heroDto.hrefDota2 = hrefDota2;
                        heroDto.avatarThubmail = avatarThubmail;
                        
                        //hero Name
                        //http://www.dota2.com/hero/Huskar/
                        String []array = hrefDota2.split("/");
                        String name = array[array.length -1];
                        Log.v(TAG, "log>>>" + "name:" + name);
                        heroDto.name = name;
                        list.add(heroDto);
                        
                        //avatar large
                        //http://cdn.dota2.com/apps/dota2/images/heroes/tinker_vert.jpg
                        
                        //http://cdn.dota2.com/apps/dota2/images/heroes/queenofpain_hphover.png
                        
                    }

                }
            }
            return herodata;
        } catch (Exception e) {
            Log.e("", "log>>>" + "data err :" + e);
        }
        return herodata;
    }
    
}
