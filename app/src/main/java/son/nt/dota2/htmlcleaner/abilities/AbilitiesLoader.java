package son.nt.dota2.htmlcleaner.abilities;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public abstract class AbilitiesLoader extends ContentLoader<List<AbilityDto>> {
    public static final String PATH_ABILITY = "http://dota2.gamepedia.com/Outworld_Devourer";
    public static final String PATH_ABILITY_ROOT = "http://dota2.gamepedia.com/";
    public static final String TAG = "AbilitiesLoader";

    public AbilitiesLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<AbilityDto> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);
            Object[] data = null;

            TagNode tagNode = cleaner.clean(in);
            String xpath = "//table[@class='abilitytable']";
            data = tagNode.evaluateXPath(xpath);

            List<AbilityDto> listAbilities = new ArrayList<>();
            if (data != null && data.length > 0) {
                AbilityDto dto;

                int sizeA = data.length;

                for (int i = 0; i < sizeA; i++) {


                    try {
                        Logger.debug(TAG, ">>>" + "==========AAAA==========:" + sizeA + ":" + i);
                        dto = new AbilityDto();
                        //TODO need be care full with this
                        TagNode nodeA = ((TagNode) data[i]).getChildTagList().get(0);

                        try {
                            //get skill voice
                            String tnPath = "//span[@style='position:relative; top:-2px;']";
                            Object[] oTn = nodeA.evaluateXPath(tnPath);
                            Logger.debug(TAG, ">>>" + "oTN:" + oTn.length);
                            if (oTn.length > 0) {
                                TagNode tVoice = (TagNode) oTn[0];
                                String voice = tVoice.getChildTagList().get(0).getAttributeByName("href");
                                Logger.debug(TAG, ">>>" + "Voice:" + voice);
                                dto.sound = voice;
                            }

                        } catch (XPatherException e) {
                            Logger.error(TAG, ">>>" + "ERROR VOICE:" + e.toString());
                            e.printStackTrace();
                        }

                        int sizeB = nodeA.getChildTagList().size();

                        Logger.debug(TAG, ">>>" + "B:" + sizeB);

                        // skill name
                        TagNode node1 = nodeA.getChildTagList().get(0);
                        String skillName = node1.getText().toString().substring(1, node1.getText().toString().indexOf("   ")).trim();
                        Logger.debug(TAG, ">>>" + "skill name:" + skillName);
                        dto.name = skillName;


                        TagNode node2 = nodeA.getChildTagList().get(1);
                        Logger.debug(TAG, ">>>" + "Node2 text:" + node2.getText() + ";size:" + node2.getChildTagList().size() );
                        String imgPath = "//img[@src]";
                        Object []oImgs = node2.evaluateXPath(imgPath);
                        Logger.debug(TAG, ">>>" + "oImgs:" +oImgs.length);
                        if (oImgs.length > 0) {
                            TagNode tnI = (TagNode) oImgs[0];
                            Logger.debug(TAG, ">>>" + "tnI:" + tnI.getAttributeByName("src"));
                            dto.linkImage = tnI.getAttributeByName("src");
                        }
//                        Map<String, String> maps = node2.getAttributes();
//                        for (String s : maps.values()) {
//                            if (s.contains("http://")) {
//                                Logger.debug(TAG, ">>>" + "sLink:" +  s);
//                            }
//                        }

                        sizeB = 0;
                        for (int j = 0; j < sizeB; j++) {
                            try {
                                Logger.debug(TAG, ">>>" + "-------BBBB-------:" + sizeB + ":" + j);
                                TagNode nodeB = nodeA.getChildTagList().get(j);
                                int sizeC = nodeB.getChildTagList().size();
                                for (int k = 0; k < sizeC; k++) {
                                    try {
                                        TagNode nodeC = nodeB.getChildTagList().get(k);
                                        Logger.debug(TAG, ">>>" + "--CCC---:" + sizeC + ":" + k + ";nodeC:" + nodeC.getChildTagList().size());
                                        if (k == 0 && j ==0) {
                                            skillName = nodeC.getText().toString().substring(1, nodeC.getText().toString().indexOf("   "));
                                            Logger.debug(TAG, ">>>" + "skill name:" + skillName);
                                            dto.name = skillName;
                                        }
                                        Logger.debug(TAG, ">>>" + "tn2:" + nodeC.getText());
                                    } catch (Exception e) {
                                        Logger.error(TAG, ">>>" + "ERROR CCCC:" + e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            } catch (Exception e) {
                                Logger.error(TAG, ">>>" + "ERROR BBBB:" + e.toString());
                                e.printStackTrace();
                            }
                        }
                        if (listAbilities.size() > 0) {

                            for (AbilityDto d : listAbilities) {
                                if (!(dto.name.toLowerCase().contains(d.name.trim().toLowerCase()))) {
                                    listAbilities.add(dto);
                                }
                            }
                        } else {
                            listAbilities.add(dto);
                        }

                    } catch (Exception e) {
                        Logger.error(TAG, ">>>" + "ERROR AAAA:" + e.toString());
                        e.printStackTrace();
                    }
                }
            }
            return listAbilities;


        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "error ROOT:" + e.toString());

        }
        return null;
    }

//    @Override
//    protected List<AbilityDto> handleStream(InputStream in) throws IOException {
//        try {
//            HtmlCleaner cleaner = new HtmlCleaner();
//            CleanerProperties props = cleaner.getProperties();
//            props.setAllowHtmlInsideAttributes(true);
//            props.setAllowMultiWordAttributes(true);
//            props.setRecognizeUnicodeChars(true);
//            props.setOmitComments(true);
//            Object[] data = null;
//
//            TagNode tagNode = cleaner.clean(in);
//            String xpath = "//table[@class='abilitytable']";
//            data = tagNode.evaluateXPath(xpath);
//
//            List<AbilityDto> listAbilities = new ArrayList<>();
//            if (data != null && data.length > 0) {
//                AbilityDto dto;
//
//                int sizeA = data.length;
//
//                for (int i = 0; i < sizeA; i++) {
//
//
//                    try {
//                        Logger.debug(TAG, ">>>" + "==========AAAA==========:" + sizeA + ":" + i);
//                        dto = new AbilityDto();
//
//                        //TODO need be care full with this
//                        TagNode nodeA = ((TagNode) data[i]).getChildTagList().get(0);
//
//                        try {
//                            //get skill voice
//                            String tnPath = "//span[@style='position:relative; top:-2px;']";
//                            Object[] oTn = nodeA.evaluateXPath(tnPath);
//                            Logger.debug(TAG, ">>>" + "oTN:" + oTn.length);
//
//                            TagNode tVoice = (TagNode) oTn[0];
//                            String voice = tVoice.getChildTagList().get(0).getAttributeByName("href");
//                            Logger.debug(TAG, ">>>" + "Voice:" + voice);
//
//                            dto.sound = voice;
//                        } catch (XPatherException e) {
//                            Logger.error(TAG, ">>>" + "ERROR VOICE:" + e.toString());
//                            e.printStackTrace();
//                        }
//
//                        int sizeB = nodeA.getChildTagList().size();
//                        for (int j = 0; j < sizeB; j++) {
//                            try {
//                                Logger.debug(TAG, ">>>" + "-------BBBB-------:" + sizeB + ":" + j);
//                                TagNode nodeB = nodeA.getChildTagList().get(j);
//                                int sizeC = nodeB.getChildTagList().size();
//                                for (int k = 0; k < sizeC; k++) {
//                                    try {
//                                        TagNode nodeC = nodeB.getChildTagList().get(k);
//                                        Logger.debug(TAG, ">>>" + "--CCC---:" + sizeC + ":" + k + ";nodeC:" + nodeC.getChildTagList().size());
//                                        if (k == 0 && j ==0) {
//                                            String skillName = nodeC.getText().toString().substring(1, nodeC.getText().toString().indexOf("   "));
//                                            Logger.debug(TAG, ">>>" + "skill name:" + skillName);
//                                            dto.name = skillName;
//                                        }
//                                        Logger.debug(TAG, ">>>" + "tn2:" + nodeC.getText());
//                                    } catch (Exception e) {
//                                        Logger.error(TAG, ">>>" + "ERROR CCCC:" + e.toString());
//                                        e.printStackTrace();
//                                    }
//                                }
//                            } catch (Exception e) {
//                                Logger.error(TAG, ">>>" + "ERROR BBBB:" + e.toString());
//                                e.printStackTrace();
//                            }
//                        }
//                        listAbilities.add(dto);
//                    } catch (Exception e) {
//                        Logger.error(TAG, ">>>" + "ERROR AAAA:" + e.toString());
//                        e.printStackTrace();
//                    }
//                }
//            }
//            return listAbilities;
//
//
//        } catch (Exception e) {
//            Logger.error(TAG, ">>>" + "error ROOT:" + e.toString());
//
//        }
//        return null;
//    }

    private void upLoadToParseService(final List<AbilityDto> listRoles) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery(AbilityDto.class.getSimpleName());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> l, ParseException e) {
                if (e != null || l.size() > 0) {
                    return;

                }


            }
        });
    }


}
