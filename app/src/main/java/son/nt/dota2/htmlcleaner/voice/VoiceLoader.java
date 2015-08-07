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
            List<TagNode> tagNodes = nodeA.getChildTagList();
            TagNode normalNode = null;

            //Spawning
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Spawning']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Spawning"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }

            }

            //Killing_a_Rival
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Killing_a_Rival']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getVoiceWithImage(normalNode, "Killing_a_Rival"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Meeting_an_Ally
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Meeting_an_Ally']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getVoiceWithImage(normalNode, "Meeting_an_Ally"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Beginning_the_Battle

            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Beginning_the_Battle']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Beginning_the_Battle"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }



            //Purchasing_a_Specific_Item
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Purchasing_a_Specific_Item']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getVoiceWithImage(normalNode, "Purchasing_a_Specific_Item"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Removed_from_game_2
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Removed_from_game_2']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getVoiceWithImage(normalNode, "Removed_from_game_2"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Drawing_First_Blood
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Drawing_First_Blood']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Drawing_First_Blood"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Thanking
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Thanking']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Thanking"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Respawning

            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Respawning']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Respawning"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Last_Hitting
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Last_Hitting']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Last_Hitting"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            //Dying
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@id='Dying']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        normalNode = tagNodes.get(i + 1);
                        listSpeaks.addAll(getNormal(normalNode, "Dying"));
                        break;
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }

            List<String> listBand = new ArrayList<>();
            listBand.add("Dying");
            listBand.add("Last_Hitting");
            listBand.add("Respawning");
            listBand.add("Thanking");
            listBand.add("Drawing_First_Blood");
            listBand.add("Removed_from_game_2");
            listBand.add("Purchasing_a_Specific_Item");
            listBand.add("Meeting_an_Ally");
            listBand.add("Beginning_the_Battle");
            listBand.add("Killing_a_Rival");
            listBand.add("Spawning");
            //another
            for (int i = 0; i < tagNodes.size(); i++) {

                TagNode tag = tagNodes.get(i);
                try {
                    String xpathA = "//span[@class='mw-headline']";
                    Object[] objA = tag.evaluateXPath(xpathA);
                    if (objA.length > 0) {
                        TagNode nodeTitle = (TagNode) objA[0];
                        String id = nodeTitle.getAttributeByName("id");

                        boolean isAdd = true;
                        for (String s : listBand) {
                            if (id.equals(s)) {
                                isAdd = false;
                            }
                        }
                        Logger.debug(TAG, ">>>" + "Id:" + id + ";IsAdd:" + isAdd);
                        if (isAdd) {
                            normalNode = tagNodes.get(i + 1);
                            listSpeaks.addAll(getNormal(normalNode, id));
                        }
                    }
                } catch (XPatherException e) {
                    e.printStackTrace();
                }
            }



        } catch (Exception e) {
        }
        return listSpeaks;
    }

    private List<SpeakDto> getNormal(TagNode nodeA, String group) {
        List<SpeakDto> list = new ArrayList<>();
        SpeakDto d = new SpeakDto();
        d.text = group;
        d.isTitle = true;
        d.voiceGroup = group;
        list.add(d);
        SpeakDto speakDto;
        try {
            String xpathA = "./li";
            Object[] objB = nodeA.evaluateXPath(xpathA);
            for (int i = 0; i < objB.length; i++) {
                speakDto = new SpeakDto();
                TagNode nodetext = (TagNode) objB[i];
                TagNode t1 = nodetext.getChildTagList().get(0);
                String mp3 = t1.getAttributeByName("href");
                String text = nodetext.getText().toString().replace("Play", "").trim();
                speakDto.voiceGroup = group;
                speakDto.link = mp3;
                speakDto.text = text;
                speakDto.no = i;
                list.add(speakDto);
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "Error getNormal:" + e.toString());
        }
        return list;
    }

    private List<SpeakDto> getVoiceWithImage(TagNode nodeA, String group) {
        List<SpeakDto> list = new ArrayList<>();
        SpeakDto d = new SpeakDto();
        d.text = group;
        d.isTitle = true;
        d.voiceGroup = group;
        list.add(d);
        SpeakDto speakDto;
        try {
            String xpathA = "./li";
            Object[] objB = nodeA.evaluateXPath(xpathA);
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
                speakDto.voiceGroup = group;
                speakDto.no = i;
                list.add(speakDto);
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "Error getNormal:" + e.toString());
        }
        return list;
    }

}
