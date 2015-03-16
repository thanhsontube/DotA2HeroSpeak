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
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.FilterLog;


public abstract class HeroSpeakLoader extends ContentLoader<HeroData> {
    private static final String TAG = "HeroSpeakLoader";
    private String lastTitle = "";
    FilterLog log = new FilterLog(TAG);

    public HeroSpeakLoader(HttpUriRequest httpRequest, boolean isCache) {
        super(httpRequest, isCache);
    }

    @Override
    protected HeroData handleStream(InputStream in) throws IOException {
        HeroData herodata = new HeroData();
        HeroDto heroDto = new HeroDto();
        List<SpeakDto> listSpeaks = heroDto.listSpeaks;
        SpeakDto speakDto;
        herodata.listHeros.add(heroDto);
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
            tagNode = (TagNode) data[0];
            // Log.e("", "log>>>" + "data HeroSpeakLoader:" + data.length);

            List<TagNode> listNodes = (List<TagNode>) tagNode.getAllElementsList(false);
            int i = 0;
            TagNode headerTag;
            StringBuilder builder = new StringBuilder();
            String linkImage = "";
            String link =  "";
            for (TagNode tag : listNodes) {


                //title
                xPath = "./span[@class='mw-headline']";
                data = tag.evaluateXPath(xPath);
                if (data != null && data.length > 0) {
                    speakDto = new SpeakDto();
                    i++;
                    headerTag = (TagNode) data[0];
                    builder = (StringBuilder) headerTag.getText();
                    Log.e("", "log>>>" + i + " data HeroSpeakLoader header:" + builder.toString());
                    speakDto.title = builder.toString();
                    speakDto.isTitle = true;
                    listSpeaks.add(speakDto);
                    lastTitle = builder.toString();
                } else {
                    // speak text and link

                    // normal
                    if (lastTitle.contains("Purchasing a Specific Item")  ||lastTitle.contains("Killing a Rival")
                            || lastTitle.contains("Meeting an Ally") ) {
                        log.d("log>>>" + "Purchasing a Specific Item");
                        // xPath = "./li";
                        /*xPath = "./li/a";
                        data = tag.evaluateXPath(xPath);

                        String xPathContent = "./li";
                        Object[] dataContent = tag.evaluateXPath(xPathContent);
                        TagNode tagContent;
                        if (data != null && data.length > 0) {
                            int countName = 0;
                            for (int j = 0; j < data.length; j++) {
                                headerTag = (TagNode) data[j];

                                if (j % 2 == 0) {
                                     link = headerTag.getAttributeByName("href");
                                    Log.v(TAG, "log>>>" + "LINK:" + link);

                                    tagContent = (TagNode) dataContent[countName];
                                    countName ++;
                                    builder = (StringBuilder) tagContent.getText();
                                    Log.v(TAG, "log>>>" + "what:" + builder.toString());

                                } else {
                                    xPath = "./img";
                                    Object[] myData = headerTag.evaluateXPath(xPath);
                                    if (myData != null && myData.length > 0) {
                                        TagNode nodeMyData = (TagNode) myData[0];
                                        linkImage = nodeMyData.getAttributeByName("src").replace("22", "44");
                                        linkImage = linkImage.substring(0, linkImage.indexOf("?version"));
                                        Log.v(TAG, "log>>>" + "linkImage:" + linkImage);
                                    }
                                }
//                                speakDto = new SpeakDto();
//                                speakDto.link = link;
//                                speakDto.text = builder.toString().replace("Play", "").trim();
//                                speakDto.imageItem = linkImage;
//
//                                listSpeaks.add(speakDto);
                            }
                        }*/
                    } else {

                        xPath = "./li/a[@href]";
                        data = tag.evaluateXPath(xPath);

                        // content speak
                        String xPathContent = "./li";
                        Object[] dataContent = tag.evaluateXPath(xPathContent);
                        TagNode tagContent;

                        xPath = "./li";
                        if (data != null && data.length > 0) {
                            Log.v(TAG, "log>>>" + "data:" + data.length);

                            for (int j = 0; j < data.length; j++) {
                                headerTag = (TagNode) data[j];
                                tagContent = (TagNode) dataContent[j];
                                link = headerTag.getAttributeByName("href");
                                Log.v(TAG, "log>>>" + "LINK:" + link);
                                builder = (StringBuilder) tagContent.getText();
                                Log.v(TAG, "log>>>" + "what:" + builder.toString());

                                speakDto = new SpeakDto();
                                speakDto.link = link;
                                speakDto.text = builder.toString().replace("Play", "").trim();
                                listSpeaks.add(speakDto);
                            }

                        }
                    }

                }


            }

            Log.e("", "log>>>" + "data HeroSpeakLoader listNodes:" + listNodes.size());
        } catch (Exception e) {
        }
        return herodata;
    }

//    speakDto.link = link;
//    speakDto.text = builder.toString().replace("Play", "").trim();
//    listSpeaks.add(speakDto);

}
