package son.nt.dota2.htmlcleaner.voice;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsLog;


public abstract class VoiceLoader extends ContentLoader<List<SpeakDto>> {
    public static final String PATH = "http://dota2.gamepedia.com/%s_responses";
    private static final String TAG = "HeroSpeakLoader";
    private String lastTitle = "";
    TsLog log = new TsLog(TAG);

    public VoiceLoader(HttpUriRequest httpRequest, boolean isCache) {
        super(httpRequest, isCache);
    }

    @Override
    protected List<SpeakDto> handleStream(InputStream in) throws IOException {
        List<SpeakDto> listSpeaks = new ArrayList<>();
        SpeakDto speakDto;
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
            List<SpeakDto> listKillingRival = getKillingARiver(nodeA);
            if (listKillingRival.size() > 0) {
                return listKillingRival;
            }

        } catch (Exception e) {
        }
        return listSpeaks;
    }


    private List<SpeakDto> getKillingARiver(TagNode nodeA) throws XPatherException {
        List<SpeakDto> list = new ArrayList<>();
        Logger.debug(TAG, ">>>" + "getKillingARiver:" + nodeA);
        List<TagNode> tagNodes = nodeA.getChildTagList();
        Logger.debug(TAG, ">>>" + "getKillingARiver size:" + tagNodes.size());
        String xpathA = "";
        Object[] objA;
        TagNode tag = null;
        TagNode tagRival = null;
        SpeakDto d = new SpeakDto();
        d.text = "Killing a rival";
        d.isTitle = true;
        d.voiceGroup = "Killing a rival";
        list.add(d);
        SpeakDto speakDto;
        for (int i = 0; i < tagNodes.size(); i++) {

            tag = tagNodes.get(i);
            try {
                //search title Killing A Rival

                xpathA = "//span[@id='Killing_a_Rival']";
                objA = tag.evaluateXPath(xpathA);
                if (objA.length > 0) {
                    tagRival = tagNodes.get(i + 1);
                    break;
                }
            } catch (XPatherException e) {
                e.printStackTrace();
            }

        }

        if (tagRival != null) {
            Logger.debug(TAG, ">>>" + "TAG rival != NULL");
            xpathA = "./li";
            Object[] objB = tagRival.evaluateXPath(xpathA);
            for (int i = 0; i < objB.length; i++) {
                speakDto = new SpeakDto();
                TagNode nodetext = (TagNode) objB[i];
                TagNode t1 = nodetext.getChildTagList().get(0);
                TagNode t2 = nodetext.getChildTagList().get(1);
                String mp3 = t1.getAttributeByName("href");
                String rivalImage = "http://dota2.gamepedia.com" + t2.getAttributeByName("href");

                String rivalName = t2.getAttributeByName("title");
                String text = nodetext.getText().toString().replace("Play", "").trim();

                rivalImage = t2.getChildTagList().get(0).getAttributeByName("srcset");
                String []arr = rivalImage.split(",");
                rivalImage = arr[1].replace("2x", "").trim();

                speakDto.setRival(rivalName, rivalImage, text, mp3);
                speakDto.voiceGroup = "Killing a rival";
                speakDto.no = i;
                list.add(speakDto);
            }
        }
        return list;
    }
}
