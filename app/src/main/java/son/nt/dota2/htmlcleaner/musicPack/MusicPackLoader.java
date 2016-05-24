package son.nt.dota2.htmlcleaner.musicPack;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 8/19/15.
 */
public abstract class MusicPackLoader extends ContentLoader<List<MusicPackDto>> {
    public static final String TAG = "BgModalLoader";

    public MusicPackLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<MusicPackDto> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);

            TagNode tagNode = cleaner.clean(in);
            String xPath = "//div[@style='padding:0em 0.25em']";
            Object[] data = tagNode.evaluateXPath(xPath);

            TagNode nodeA = (TagNode) data[0];
            Logger.debug(TAG, ">>>" + "nodA:" + nodeA.getChildTagList().size());
            int i = 1;
            List<MusicPackDto> list = new ArrayList<>();
            MusicPackDto dto;
            for (TagNode tag : nodeA.getChildTagList()) {
                Logger.debug(TAG, ">>>" + "----Tag:" + i++);
                Logger.debug(TAG, ">>>" + "Text:" + tag.getName() + ";text:" + tag.getText());
                dto = new MusicPackDto();
                dto.setName(tag.getText().toString());
                dto.setCoverColor("#8847ff");
                TagNode note = tag.getChildTagList().get(0).getChildTagList().get(0);
                String detail = "http://dota2.gamepedia.com" + note.getAttributeByName("href");
                dto.setLinkDetails(detail);
                TagNode imageNote = note.getChildTagList().get(0);
                String src = imageNote.getAttributeByName("src").replace("100px", "200px");
                Logger.debug(TAG, ">>>" + "src:" + src);
                dto.setHref(src);


                list.add(dto);

            }
            nodeA = (TagNode) data[1];
            for (TagNode tag : nodeA.getChildTagList()) {
                dto = new MusicPackDto();
                dto.setName(tag.getText().toString());
                dto.setCoverColor("#d32ce6");
                TagNode note = tag.getChildTagList().get(0).getChildTagList().get(0);
                String detail = "http://dota2.gamepedia.com" + note.getAttributeByName("href");
                dto.setLinkDetails(detail);
                TagNode imageNote = note.getChildTagList().get(0);
                String src = imageNote.getAttributeByName("src").replace("100px", "200px");
                Logger.debug(TAG, ">>>" + "src:" + src);
                dto.setHref(src);


                list.add(dto);
            }
//            parseDetails(list, props, cleaner);


            Logger.debug(TAG, ">>>" + "total:" + list.size());


            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void parseDetails(List<MusicPackDto> list, CleanerProperties props, HtmlCleaner cleaner) {
        //add details
        for (MusicPackDto d : list) {
            String linkDetail = d.getLinkDetails();
            try {
                Logger.debug(TAG, ">>>" + "linkDetail:" + linkDetail);
                URL url = new URL(linkDetail);
                URLConnection urlConnection = url.openConnection();
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
// do parsing
//                TagNode tagNode = new HtmlCleaner(props).clean(
//                        new URL(linkDetail)
//                );
                TagNode tagNode = cleaner.clean(in);
                Logger.debug(TAG, ">>>" + "TagNode:" + tagNode);
                String xPath = "//div[@id='mw-content-text']";
                Object[] data = tagNode.evaluateXPath(xPath);
                if (data ==null || data.length == 0)
                {
                    Logger.error(TAG, ">>> Error:" + "data is NULL");
                    return;
                }

                TagNode nodeA = (TagNode) data[0];
                Logger.debug(TAG, ">>>" + "nodA:" + nodeA.getChildTagList().size());
                List<MusicPackSoundDto> listDetails = new ArrayList<>();
                MusicPackSoundDto dto;


                for (TagNode tag : nodeA.getChildTagList()) {

                    dto = new MusicPackSoundDto();
                    String tagName = tag.getName();
                    if ("ul".equals(tagName)) {
                        Logger.debug(TAG, ">>>" + "tag name:" + tagName + ";size:" + tag.getChildTagList().size());
                        for (TagNode t : tag.getChildTagList()) {
                            dto.setName(t.getText().toString());
                            Logger.debug(TAG, ">>>" + "t:" + t.getText());
                            String link = t.getChildTagList().get(0).getAttributeByName("href");
                            Logger.debug(TAG, ">>>" + "link:" + link);
                            dto.setLink(link);
                            listDetails.add(dto);
                        }
                    }

                }
                d.setList(listDetails);
            } catch (Exception e) {
                Logger.error(TAG, ">>> Error:" + e.toString() + ";link:"+ linkDetail);
            }


        }


    }
}
