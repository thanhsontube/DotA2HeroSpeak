package son.nt.dota2.htmlcleaner.abilities;

import android.text.TextUtils;

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
                        Logger.debug(TAG, ">>>" + "Node2 text:" + node2.getText() + ";size:" + node2.getChildTagList().size());
                        String imgPath = "//img[@src]";
                        Object[] oImgs = node2.evaluateXPath(imgPath);
                        Logger.debug(TAG, ">>>" + "oImgs:" + oImgs.length);
                        if (oImgs.length > 0) {
                            TagNode tnI = (TagNode) oImgs[0];
                            Logger.debug(TAG, ">>>" + "tnI:" + tnI.getAttributeByName("src"));
                            dto.linkImage = tnI.getAttributeByName("src");
                        }
                        //skill type

                        String affects = node2.getText().toString();
                        String[] arr = affects.split("\n");
                        List<String> listSkillType = new ArrayList<>();
                        Logger.debug(TAG, ">>>" + "arr:" + arr.length);
//                        int ii = 0;

                        for (String s : arr) {
//                            Logger.debug(TAG, ">>>" + "arr:" + s + ":" + ii++);
                            if (!TextUtils.isEmpty(s) && !s.equals(" ")) {
                                listSkillType.add(s);
                            }
                        }
                        Logger.debug(TAG, ">>>" + "ListSkill:" + listSkillType.size());

                        String ability = null;

                        String affect = null;
                        String damage = null;
                        if (listSkillType.size() == 6) {
                            ability = listSkillType.get(3);
                            affect = listSkillType.get(4);
                            damage = listSkillType.get(5);
                        }
                        if (listSkillType.size() == 4) {
                            ability = listSkillType.get(2);
                            affect = listSkillType.get(3);
                        }

                        dto.setTypes(ability, affect, damage);

                        //Ability Description
                        TagNode nodeDes = nodeA.getChildTagList().get(2);
                        String description = nodeDes.getText().toString();
                        dto.description = description;


                        //Coundown and mana

                        TagNode nodeTable = nodeA.getChildTagList().get(4);

                        String xPathTable = "//table";
                        Object[] oTable = nodeTable.evaluateXPath(xPathTable);
                        Logger.debug(TAG, ">>>" + "Table:" + oTable.length);
                        if (oTable.length > 0) {
                            //cooldown and mana
                            String xPathMana = "//a[@title='Mana']";
                            TagNode nodeCoolDownMana = (TagNode) oTable[0];
                            Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);
                            if (oMana.length > 0) {
                                String xPathCoMna = "./tbody/tr/td";
                                Object[] oComa = nodeCoolDownMana.evaluateXPath(xPathCoMna);
                                Logger.debug(TAG, ">>>" + "oComa:" + oComa.length);


                                //get cooldown
                                TagNode nodeCoolDown = (TagNode) oComa[0];


                                String coolDowns = nodeCoolDown.getText().toString().replace(".", "").trim();
                                String[] arrCoolDown = coolDowns.split("/");
                                Logger.debug(TAG, ">>>" + "arrCoolDown:" + arrCoolDown.length);
                                dto.coolDowns.clear();
                                if (arrCoolDown.length == 1) {

                                    dto.coolDowns.add(arrCoolDown[0]);
                                    dto.coolDowns.add(arrCoolDown[0]);
                                    dto.coolDowns.add(arrCoolDown[0]);
                                    dto.coolDowns.add(arrCoolDown[0]);
                                } else if (arrCoolDown.length == 4) {
                                    dto.coolDowns.add(arrCoolDown[0]);
                                    dto.coolDowns.add(arrCoolDown[1]);
                                    dto.coolDowns.add(arrCoolDown[2]);
                                    dto.coolDowns.add(arrCoolDown[3]);
                                } else if (arrCoolDown.length == 3) {
                                    dto.coolDowns.add(arrCoolDown[0]);
                                    dto.coolDowns.add(arrCoolDown[1]);
                                    dto.coolDowns.add(arrCoolDown[2]);
                                    dto.coolDowns.add("-");

                                    dto.isUltimate = true;
                                }

                                TagNode nodeMana = (TagNode) oComa[1];
                                String manaCosts = nodeMana.getText().toString().replace(".", "").trim();
                                String[] arrManaCost = manaCosts.split("/");
                                dto.manacCosts.clear();
                                if (arrManaCost.length == 1) {

                                    dto.manacCosts.add(arrManaCost[0]);
                                    dto.manacCosts.add(arrManaCost[0]);
                                    dto.manacCosts.add(arrManaCost[0]);
                                    dto.manacCosts.add(arrManaCost[0]);
                                } else if (arrManaCost.length == 4) {
                                    dto.manacCosts.add(arrManaCost[0]);
                                    dto.manacCosts.add(arrManaCost[1]);
                                    dto.manacCosts.add(arrManaCost[2]);
                                    dto.manacCosts.add(arrManaCost[3]);
                                } else if (arrManaCost.length == 3) {
                                    dto.manacCosts.add(arrManaCost[0]);
                                    dto.manacCosts.add(arrManaCost[1]);
                                    dto.manacCosts.add(arrManaCost[2]);
                                    dto.manacCosts.add("-");

                                    dto.isUltimate = true;
                                }
                            }

                        }


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
                                        if (k == 0 && j == 0) {
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
                                String old = d.name.toLowerCase().replace(" ", "").trim();
                                String new1 = dto.name.toLowerCase().replace(" ", "").trim();
                                if (new1.contains(old) || old.contains(new1)) {

                                } else {
                                    listAbilities.add(dto);

                                }
                            }
                        } else {
                            listAbilities.add(dto);
                        }

                        //Stop add another skill
                        if (dto.isUltimate) {
                            break;
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
