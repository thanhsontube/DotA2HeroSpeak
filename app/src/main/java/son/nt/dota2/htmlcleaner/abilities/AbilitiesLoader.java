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
import son.nt.dota2.dto.AbilityItemAffectDto;
import son.nt.dota2.dto.AbilityLevelDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public abstract class AbilitiesLoader extends ContentLoader<List<AbilityDto>> {
    public static final String PATH_ABILITY = "http://dota2.gamepedia.com/Outworld_Devourer";
    public static final String PATH_ABILITY_ROOT = "http://dota2.gamepedia.com/";
    public static final String TAG = "AbilitiesLoader";

    public static final int POS_ABILITY_NAME= 0;
    public static final int POS_ABILITY_IMAGE= 1;
    public static final int POS_ABILITY_DESCRIPTION= 2;
    public static final int POS_ABILITY_INFO = 3;
    public static final int POS_ABILITY_COOL_DOWN_MANA= 4;

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
            listAbilities.clear();
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
                            if (oTn.length > 0) {
                                TagNode tVoice = (TagNode) oTn[0];
                                String voice = tVoice.getChildTagList().get(0).getAttributeByName("href");
                                dto.sound = voice;
                            }

                        } catch (XPatherException e) {
                            Logger.error(TAG, ">>>" + "ERROR VOICE:" + e.toString());
                            e.printStackTrace();
                        }
                        // skill name
                        TagNode node1 = nodeA.getChildTagList().get(POS_ABILITY_NAME);
                        String skillName = node1.getText().toString().substring(1, node1.getText().toString().indexOf("   ")).trim();
                        dto.name = skillName;


                        //Skill Image AND TYPE

                        TagNode node2 = nodeA.getChildTagList().get(POS_ABILITY_IMAGE);
                        String imgPath = "//img[@src]";
                        Object[] oImgs = node2.evaluateXPath(imgPath);
                        if (oImgs.length > 0) {
                            TagNode tnI = (TagNode) oImgs[0];
                            dto.linkImage = tnI.getAttributeByName("src");
                            Logger.debug(TAG, ">>>" + "Image:" + dto.linkImage);
                        }
                        //skill type

                        String affects = node2.getText().toString();
                        String[] arr = affects.split("\n");
                        List<String> skillTypes = new ArrayList<>();

                        for (String s : arr) {
                            if (!TextUtils.isEmpty(s) && !s.equals(" ")) {
                                skillTypes.add(s);
                            }
                        }
                        String ability = null;

                        String affect = null;
                        String damage = null;
                        if (skillTypes.size() == 6) {
                            ability = skillTypes.get(3);
                            affect = skillTypes.get(4);
                            damage = skillTypes.get(5);
                        }
                        if (skillTypes.size() == 4) {
                            ability = skillTypes.get(2);
                            affect = skillTypes.get(3);
                        }

                        dto.setTypes(ability, affect, damage);

                        //Ability Description
                        TagNode nodeDes = nodeA.getChildTagList().get(POS_ABILITY_DESCRIPTION);
                        String description = nodeDes.getText().toString();
                        dto.description = description;


                        //Coundown and mana

                        TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);

                        String xPathTable = "//table";
                        Object[] oTable = nodeTable.evaluateXPath(xPathTable);
                        int start = 0;
                        if (oTable.length > 0) {
                            start = 1;
                            //cooldown and mana
                            String xPathMana = "//a[@title='Mana']";
                            TagNode nodeCoolDownMana = (TagNode) oTable[0];
                            Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);
                            if (oMana.length > 0) {
                                String xPathCoMna = "./tbody/tr/td";
                                Object[] oComa = nodeCoolDownMana.evaluateXPath(xPathCoMna);


                                //get cooldown
                                TagNode nodeCoolDown = (TagNode) oComa[0];


                                String coolDowns = nodeCoolDown.getText().toString().replace(".", "").trim();
                                String[] arrCoolDown = coolDowns.split("/");
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

                        //another fields
                        Logger.debug(TAG, ">>>" + "<<>>> yyyyy2");

                        for (int ii = start ; ii < oTable.length; ii ++ ) {
                            TagNode nodeOther = (TagNode) oTable[ii];
                            String xPath = "./tbody/tr/td";
                            Object[] oComa = nodeOther.evaluateXPath(xPath);

                            if (oComa.length > 0) {
                                TagNode n0 = (TagNode) oComa[0];
                                String apath = "//img[@src]";
                                Object [] o = n0.evaluateXPath(apath);
                                String src = null;
                                String alt = null;
                                if (o.length > 0) {
                                    src = ((TagNode)o[0]).getAttributeByName("src");
                                    alt = ((TagNode)o[0]).getAttributeByName("alt");
                                }
                                dto.itemAffects.add(new AbilityItemAffectDto(src, alt, n0.getText().toString()));
                            }

                        }

                        //ABILITY INFO
                        TagNode nodeInfo = nodeA.getChildTagList().get(POS_ABILITY_INFO);
                        String full = nodeInfo.getChildTagList().get(0).getText().toString().replace(" ", "");
                        char []arrC =  full.toCharArray();
                        List<Integer> list = new ArrayList<>();
                        list.add(0);
                        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
                        for (int j  = 0; j < arrC.length -1; j ++) {
                            int k = j + 1;
                            char a1 = arrC[j];
                            char a2 = arrC[k];
                            if (((Character.isDigit(a1) || specialChars.contains(String.valueOf(a1)))) && Character.isLetter(a2)) {

                                list.add(k);
                            }

                        }
                        int end;
                        AbilityLevelDto abilityLevelDto;


                        AbilityLevelDto d1 = new AbilityLevelDto();
                        d1.name = "";
                        d1.list.add("Lv1");
                        d1.list.add("Lv2");
                        d1.list.add("Lv3");
                        d1.list.add("Lv4");
                        dto.abilityLevel.add(d1);
                        for (int j = 0; j < list.size()  ; j ++) {
                            if (j < list.size() -1) {
                                end = list.get(j + 1);
                            } else {
                                end = full.length();
                            }
                            String a1 = full.substring(list.get(j), end);
                            Logger.debug(TAG, ">>>" + "a1:" + a1);

                            String []arr2 = a1.split(":");
                            abilityLevelDto = new AbilityLevelDto();
                            abilityLevelDto.name = arr2[0];

                            String []arr3 = arr2[1].split("/");
                            Logger.debug(TAG, ">>>" + "arr3:" + arr3.length);
                            if (arr3.length == 1) {
                                abilityLevelDto.list.add(arr2[1]);
                                abilityLevelDto.list.add(arr2[1]);
                                abilityLevelDto.list.add(arr2[1]);
                                abilityLevelDto.list.add(arr2[1]);
                            } else {
                                for (int k =0; k < arr3.length; k ++) {
                                    abilityLevelDto.list.add(arr3[k]);
                                }
                            }

                            dto.abilityLevel.add(abilityLevelDto);
                        }

                        boolean isAdd = true;
                        for (AbilityDto d : listAbilities) {
                            String old = d.name.toLowerCase().replace(" ", "").trim();
                            String new1 = dto.name.toLowerCase().replace(" ", "").trim();
                            if (new1.contains(old) || old.contains(new1) || new1.contains("(pre")
                                    || (dto.linkImage != null && dto.linkImage.contains("Unknown_icon.png"))) {
                                isAdd = false;
                                break;
                            }
                        }

                        if (isAdd) {
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
