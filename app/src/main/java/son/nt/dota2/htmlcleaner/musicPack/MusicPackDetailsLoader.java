package son.nt.dota2.htmlcleaner.musicPack;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 8/19/15.
 */
public abstract class MusicPackDetailsLoader extends ContentLoader<List<MusicPackSoundDto>> {
    public static final String TAG = MusicPackDetailsLoader.class.getSimpleName();

    public MusicPackDetailsLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<MusicPackSoundDto> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);

            TagNode tagNode = cleaner.clean(in);
            String xPath = "//div[@id='mw-content-text']";
            Object[] data = tagNode.evaluateXPath(xPath);

            TagNode nodeA = (TagNode) data[0];
            Logger.debug(TAG, ">>>" + "nodA:" + nodeA.getChildTagList().size());
            List<MusicPackSoundDto> list = new ArrayList<>();
            MusicPackSoundDto dto;


            for (TagNode tag : nodeA.getChildTagList()) {


                String tagName = tag.getName();
                if ("ul".equals(tagName)) {
                    for (TagNode t : tag.getChildTagList()) {
                        try {
                            dto = new MusicPackSoundDto();
//                        Logger.debug(TAG, ">>>" + "t:" + t.getText());
                            String text = t.getText().toString().replace("Play ", "");
                            dto.setTitle(text);
                            String link = t.getChildTagList().get(0).getAttributeByName("href");
//                        Logger.debug(TAG, ">>>" + "link:" + link);
                            dto.setLink(link);
                            if (link != null && link.contains("https") && !link.contains("youtube")) {
                                Logger.debug(TAG, ">>>" + "text:" + text + ";link:" + link);
                                list.add(dto);
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                }

            }


//            Logger.debug(TAG, ">>>" + "total:" + list.size());
//            for (MusicPackSoundDto d1 : list)
//            {
//                Logger.debug(TAG, ">>>" + "text1:" + d1.getName() + ";link1:" + d1.getLink());
//            }


            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
