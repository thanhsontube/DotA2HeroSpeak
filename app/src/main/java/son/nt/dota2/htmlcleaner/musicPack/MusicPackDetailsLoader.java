package son.nt.dota2.htmlcleaner.musicPack;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 8/19/15.
 */
public abstract class MusicPackDetailsLoader extends ContentLoader<List<MusicPackDto>> {
    public static final String TAG = "BgModalLoader";

    public MusicPackDetailsLoader(HttpUriRequest request, boolean useCache) {
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
                String herf = "http://dota2.gamepedia.com" + note.getAttributeByName("href");
                dto.setLinkDetails(herf);
                TagNode imageNote = note.getChildTagList().get(0);
                String src = imageNote.getAttributeByName("src").replace("100px", "200px");
                Logger.debug(TAG, ">>>" + "src:" + src);


                list.add(dto);
            }
            nodeA = (TagNode) data[1];
            for (TagNode tag : nodeA.getChildTagList()) {
                Logger.debug(TAG, ">>>" + "----Tag:" + i++);
                Logger.debug(TAG, ">>>" + "Text:" + tag.getName() + ";text:" + tag.getText());
                dto = new MusicPackDto();
                dto.setName(tag.getText().toString());
                dto.setCoverColor("#8847ff");
                TagNode note = tag.getChildTagList().get(0).getChildTagList().get(0);
                String herf = "http://dota2.gamepedia.com" + note.getAttributeByName("href");
                dto.setLinkDetails(herf);
                TagNode imageNote = note.getChildTagList().get(0);
                String src = imageNote.getAttributeByName("src").replace("100px", "200px");
                Logger.debug(TAG, ">>>" + "src:" + src);


                list.add(dto);
            }

            Logger.debug(TAG, ">>>" + "total:" + list.size());


            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
