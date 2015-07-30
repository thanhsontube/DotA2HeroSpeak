package son.nt.dota2.htmlcleaner.abilities;

import android.text.TextUtils;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.AbilityItemAffectDto;
import son.nt.dota2.dto.AbilityLevelDto;
import son.nt.dota2.dto.AbilityNotesDto;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public abstract class AbilitiesLoader extends ContentLoader<List<AbilityDto>> {
    public static final String PATH_ABILITY = "http://dota2.gamepedia.com/Outworld_Devourer";
    public static final String PATH_ABILITY_ROOT = "http://dota2.gamepedia.com/";
    public static final String TAG = "AbilitiesLoader";

    public static final int POS_ABILITY_NAME = 0;
    public static final int POS_ABILITY_IMAGE = 1;
    public static final int POS_ABILITY_DESCRIPTION = 2;
    public static final int POS_ABILITY_INFO = 3;
    public static final int POS_ABILITY_COOL_DOWN_MANA = 4;

    public static final String DOT = " ";

    public AbilitiesLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    private String getName(TagNode nodeA) throws Exception {
        TagNode node1 = nodeA.getChildTagList().get(POS_ABILITY_NAME);
        return node1.getText().toString().substring(1, node1.getText().toString().indexOf("   ")).trim();
    }

    private String getImage(TagNode nodeA) throws Exception {
        TagNode node2 = nodeA.getChildTagList().get(POS_ABILITY_IMAGE);
        String imgPath = "//img[@src]";
        Object[] oImgs = node2.evaluateXPath(imgPath);
        TagNode tnI = (TagNode) oImgs[0];
        return tnI.getAttributeByName("src");
    }

    private String getSound(TagNode nodeA) throws Exception {
        String tnPath = "//span[@style='position:relative; top:-2px;']";
        Object[] oTn = nodeA.evaluateXPath(tnPath);
        if (oTn.length > 0) {
            TagNode tVoice = (TagNode) oTn[0];
            String voice = tVoice.getChildTagList().get(0).getAttributeByName("href");
            return voice;
        }
        return "";
    }

    private String getDescription(TagNode nodeA) {
        TagNode nodeDes = nodeA.getChildTagList().get(POS_ABILITY_DESCRIPTION);
        return nodeDes.getText().toString();
    }

    private String[] getThreeTypes(TagNode nodeA) {
        TagNode node2 = nodeA.getChildTagList().get(POS_ABILITY_IMAGE);
        String affects = node2.getText().toString();
        String[] arr = affects.split("\n");
        List<String> skillTypes = new ArrayList<>();

        for (String s : arr) {
            if (!TextUtils.isEmpty(s) && !s.equals(" ")) {
                skillTypes.add(s);
            }
        }
        String ability = "";

        String affect = "";
        String damage = "";
        if (skillTypes.size() == 6) {
            ability = skillTypes.get(3);
            affect = skillTypes.get(4);
            damage = skillTypes.get(5);
        }
        if (skillTypes.size() == 4) {
            ability = skillTypes.get(2);
            affect = skillTypes.get(3);
        }
        return new String[]{ability, affect, damage};
    }

    private AbilityDto getManaLevel(TagNode nodeA) throws Exception {
        AbilityDto abilityDto = new AbilityDto();
        AbilityLevelDto abilityMana = new AbilityLevelDto();
        abilityDto.listAbilityPerLevel.add(abilityMana);

        abilityMana.name = "Mana Cost";
        for (int i = 0; i <= 3; i++) {
            abilityMana.list.add("-");
        }
        TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);


        String xPathTable = "//table";
        Object[] oTable = nodeTable.evaluateXPath(xPathTable);

        String xPathMana = "//a[@title='Mana']";
        TagNode nodeCoolDownMana = (TagNode) oTable[0];
        Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);

        if (oMana.length > 0) {
            String xPathCoMna = "./tbody/tr/td";
            TagNode node = (TagNode) oTable[0];
            Object[] oComa = node.evaluateXPath(xPathCoMna);


            TagNode nodeMana = (TagNode) oComa[1];
            String string = nodeMana.getText().toString().replace(".", "").trim();
            String[] arr = string.split("/");
            abilityMana.list.clear();
            if (arr.length == 1) {

                abilityMana.list.add(arr[0]);
                abilityMana.list.add(arr[0]);
                abilityMana.list.add(arr[0]);
                abilityMana.list.add(arr[0]);
            } else if (arr.length == 4) {
                abilityMana.list.add(arr[0]);
                abilityMana.list.add(arr[1]);
                abilityMana.list.add(arr[2]);
                abilityMana.list.add(arr[3]);
            } else if (arr.length == 3) {

                abilityMana.list.add(arr[0]);
                abilityMana.list.add(arr[1]);
                abilityMana.list.add(arr[2]);
                abilityMana.list.add("-");
                abilityDto.isUltimate = true;
            }

        }
        return abilityDto;
    }

    private AbilityDto getCoolDownLevel(TagNode nodeA) throws Exception {
        AbilityDto abilityDto = new AbilityDto();
        AbilityLevelDto abilityCoolDown = new AbilityLevelDto();
        abilityDto.listAbilityPerLevel.add(abilityCoolDown);

        abilityCoolDown.name = "CoolDown";
        for (int i = 0; i <= 3; i++) {
            abilityCoolDown.list.add("-");
        }
        TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);


        String xPathTable = "//table";
        Object[] oTable = nodeTable.evaluateXPath(xPathTable);

        String xPathMana = "//a[@title='Cooldown']";
        TagNode nodeCoolDownMana = (TagNode) oTable[0];
        Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);

        if (oMana.length > 0) {
            String xPathCoMna = "./tbody/tr/td";
            TagNode nodeCoMana = (TagNode) oTable[0];
            Object[] oComa = nodeCoMana.evaluateXPath(xPathCoMna);


            TagNode node = (TagNode) oComa[0];
            String string = node.getText().toString().replace(".", "").trim();
            String[] arr = string.split("/");
            abilityCoolDown.list.clear();
            if (arr.length == 1) {

                abilityCoolDown.list.add(arr[0]);
                abilityCoolDown.list.add(arr[0]);
                abilityCoolDown.list.add(arr[0]);
                abilityCoolDown.list.add(arr[0]);
            } else if (arr.length == 4) {
                abilityCoolDown.list.add(arr[0]);
                abilityCoolDown.list.add(arr[1]);
                abilityCoolDown.list.add(arr[2]);
                abilityCoolDown.list.add(arr[3]);
            } else if (arr.length == 3) {

                abilityCoolDown.list.add(arr[0]);
                abilityCoolDown.list.add(arr[1]);
                abilityCoolDown.list.add(arr[2]);
                abilityCoolDown.list.add("-");
                abilityDto.isUltimate = true;
            }

        }
        return abilityDto;
    }

    private int getStart(TagNode nodeA) throws Exception {
        TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);


        String xPathTable = "//table";
        Object[] oTable = nodeTable.evaluateXPath(xPathTable);

        String xPathMana = "//a[@title='Cooldown']";
        TagNode nodeCoolDownMana = (TagNode) oTable[0];
        Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);

        if (oMana.length > 0) {
            return 1;
        }
        return 0;
    }

    private List<AbilityItemAffectDto> getItemAffects(TagNode nodeA) throws Exception {
        List<AbilityItemAffectDto> list = new ArrayList<>();

        int start = 0;
        TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);
        String xPathTable = "//table";
        Object[] oTable = nodeTable.evaluateXPath(xPathTable);

        String xPathMana = "//a[@title='Cooldown']";
        TagNode nodeCoolDownMana = (TagNode) oTable[0];
        Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);

        if (oMana.length > 0) {
            start = 1;
        }
        for (int ii = start; ii < oTable.length; ii++) {
            TagNode nodeOther = (TagNode) oTable[ii];
            String xPath = "./tbody/tr/td";
            Object[] oComa = nodeOther.evaluateXPath(xPath);

            if (oComa.length > 0) {
                TagNode n0 = (TagNode) oComa[0];
                String apath = "//img[@src]";
                Object[] o = n0.evaluateXPath(apath);
                String src = null;
                String alt = null;
                if (o.length > 0) {
                    src = ((TagNode) o[0]).getAttributeByName("src");
                    alt = ((TagNode) o[0]).getAttributeByName("alt");
                }
                list.add(new AbilityItemAffectDto(src, alt, n0.getText().toString()));
            }

        }
        return list;
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

            if (data == null && data.length == 0) {
                return null;
            }
            AbilityDto dto;

            int sizeA = data.length;

            for (int i = 0; i < sizeA; i++) {
                dto = new AbilityDto();
                try {

                    Logger.debug(TAG, ">>>" + "==========AAAA==========:" + sizeA + ":" + i);
                    TagNode nodeA = ((TagNode) data[i]).getChildTagList().get(0);
//                    //Skill Image AND TYPE
//
//                    String imgPath = "//img[@src]";
//                    Object[] oImgs = node2.evaluateXPath(imgPath);
//                    if (oImgs.length > 0) {
//                        TagNode tnI = (TagNode) oImgs[0];
//                        dto.linkImage = tnI.getAttributeByName("src");
//                        Logger.debug(TAG, ">>>" + "Image:" + dto.linkImage);
//                    }
                    //skill type

                    dto.name = getName(nodeA);
                    dto.linkImage = getImage(nodeA);
                    dto.sound = getSound(nodeA);
                    Logger.debug(TAG, ">>>" + "Skill:" + dto.name + ";Image:" + dto.linkImage);

//                    TagNode node2 = nodeA.getChildTagList().get(POS_ABILITY_IMAGE);
//                    String affects = node2.getText().toString();
//                    String[] arr = affects.split("\n");
//                    List<String> skillTypes = new ArrayList<>();
//
//                    for (String s : arr) {
//                        if (!TextUtils.isEmpty(s) && !s.equals(" ")) {
//                            skillTypes.add(s);
//                        }
//                    }
//                    String ability = null;
//
//                    String affect = null;
//                    String damage = null;
//                    if (skillTypes.size() == 6) {
//                        ability = skillTypes.get(3);
//                        affect = skillTypes.get(4);
//                        damage = skillTypes.get(5);
//                    }
//                    if (skillTypes.size() == 4) {
//                        ability = skillTypes.get(2);
//                        affect = skillTypes.get(3);
//                    }
//
//                    dto.setTypes(ability, affect, damage);

                    dto.setTypes(getThreeTypes(nodeA));
                    dto.description = getDescription(nodeA);

//                    //Ability Description
//                    TagNode nodeDes = nodeA.getChildTagList().get(POS_ABILITY_DESCRIPTION);
//                    String description = nodeDes.getText().toString();
//                    dto.description = description;


                    //Coundown and mana

                    AbilityLevelDto d1 = new AbilityLevelDto();
                    d1.name = "";
                    d1.list.add("Lv1");
                    d1.list.add("Lv2");
                    d1.list.add("Lv3");
                    d1.list.add("Lv4");
                    dto.listAbilityPerLevel.add(d1);

                    dto.listAbilityPerLevel.add(getManaLevel(nodeA).listAbilityPerLevel.get(0));
                    if (getManaLevel(nodeA).isUltimate) {
                        dto.isUltimate = true;
                    }

                    dto.listAbilityPerLevel.add(getCoolDownLevel(nodeA).listAbilityPerLevel.get(0));
                    if (getCoolDownLevel(nodeA).isUltimate) {
                        dto.isUltimate = true;
                    }


//                    TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);
//
//                    String xPathTable = "//table";
//                    Object[] oTable = nodeTable.evaluateXPath(xPathTable);
//                    int start = 0;
//                    if (oTable.length > 0) {
//                        start = 1;
//                        //cooldown and mana
//                        String xPathMana = "//a[@title='Mana']";
//                        TagNode nodeCoolDownMana = (TagNode) oTable[0];
//                        Object[] oMana = nodeCoolDownMana.evaluateXPath(xPathMana);
//                        if (oMana.length > 0) {
//                            String xPathCoMna = "./tbody/tr/td";
//                            Object[] oComa = nodeCoolDownMana.evaluateXPath(xPathCoMna);
//
//
//                            //get cooldown
//                            TagNode nodeCoolDown = (TagNode) oComa[0];
//
//
//                            String coolDowns = nodeCoolDown.getText().toString().replace(".", "").trim();
//                            String[] arrCoolDown = coolDowns.split("/");
//                            AbilityLevelDto abiDto;
//                            abiDto = new AbilityLevelDto();
//                            abiDto.name = "CoolDown";
////                            dto.listAbilityPerLevel.add(abiDto);
//                            if (arrCoolDown.length == 1) {
//
//                                abiDto.list.add(arrCoolDown[0]);
//                                abiDto.list.add(arrCoolDown[0]);
//                                abiDto.list.add(arrCoolDown[0]);
//                                abiDto.list.add(arrCoolDown[0]);
//                            } else if (arrCoolDown.length == 4) {
//
//
//                                abiDto.list.add(arrCoolDown[0]);
//                                abiDto.list.add(arrCoolDown[1]);
//                                abiDto.list.add(arrCoolDown[2]);
//                                abiDto.list.add(arrCoolDown[3]);
//                            } else if (arrCoolDown.length == 3) {
//                                abiDto.list.add(arrCoolDown[0]);
//                                abiDto.list.add(arrCoolDown[1]);
//                                abiDto.list.add(arrCoolDown[2]);
//                                abiDto.list.add("-");
//
//
//                                dto.isUltimate = true;
//                            } else {
//                                abiDto.list.add("-");
//                                abiDto.list.add("-");
//                                abiDto.list.add("-");
//                                abiDto.list.add("-");
//                            }
//
//                            TagNode nodeMana = (TagNode) oComa[1];
//                            String manaCosts = nodeMana.getText().toString().replace(".", "").trim();
//                            String[] arrManaCost = manaCosts.split("/");
//                            AbilityLevelDto abilityMana = new AbilityLevelDto();
//                            abilityMana.name = "Mana Cost";
////                            dto.listAbilityPerLevel.add(abilityMana);
//                            if (arrManaCost.length == 1) {
//
//
//                                abilityMana.list.add(arrManaCost[0]);
//                                abilityMana.list.add(arrManaCost[0]);
//                                abilityMana.list.add(arrManaCost[0]);
//                                abilityMana.list.add(arrManaCost[0]);
//                            } else if (arrManaCost.length == 4) {
//
//
//                                abilityMana.list.add(arrManaCost[0]);
//                                abilityMana.list.add(arrManaCost[1]);
//                                abilityMana.list.add(arrManaCost[2]);
//                                abilityMana.list.add(arrManaCost[3]);
//                            } else if (arrManaCost.length == 3) {
//
//                                abilityMana.list.add(arrManaCost[0]);
//                                abilityMana.list.add(arrManaCost[1]);
//                                abilityMana.list.add(arrManaCost[2]);
//                                abilityMana.list.add("-");
//
//                                dto.isUltimate = true;
//                            } else {
//                                abilityMana.list.add("-");
//                                abilityMana.list.add("-");
//                                abilityMana.list.add("-");
//                                abilityMana.list.add("-");
//                            }
//                        }
//
//
//                    }

                    //another fields
//                    int start = getStart(nodeA);
//                    TagNode nodeTable = nodeA.getChildTagList().get(POS_ABILITY_COOL_DOWN_MANA);
//                    String xPathTable = "//table";
//                    Object[] oTable = nodeTable.evaluateXPath(xPathTable);
//                    Logger.debug(TAG, ">>>" + "<<>>> yyyyy2");
//
//                    for (int ii = start; ii < oTable.length; ii++) {
//                        TagNode nodeOther = (TagNode) oTable[ii];
//                        String xPath = "./tbody/tr/td";
//                        Object[] oComa = nodeOther.evaluateXPath(xPath);
//
//                        if (oComa.length > 0) {
//                            TagNode n0 = (TagNode) oComa[0];
//                            String apath = "//img[@src]";
//                            Object[] o = n0.evaluateXPath(apath);
//                            String src = null;
//                            String alt = null;
//                            if (o.length > 0) {
//                                src = ((TagNode) o[0]).getAttributeByName("src");
//                                alt = ((TagNode) o[0]).getAttributeByName("alt");
//                            }
//                            dto.listItemAffects.add(new AbilityItemAffectDto(src, alt, n0.getText().toString()));
//                        }
//
//                    }

                    dto.listItemAffects.clear();
                    dto.listItemAffects.addAll(getItemAffects(nodeA));


//                    //ABILITY INFO
//                    TagNode nodeInfo = nodeA.getChildTagList().get(POS_ABILITY_INFO);
//                    String full = nodeInfo.getChildTagList().get(0).getText().toString().replace(" ", "");
//                    char[] arrC = full.toCharArray();
//                    List<Integer> list = new ArrayList<>();
//                    list.add(0);
//                    String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
//                    for (int j = 0; j < arrC.length - 1; j++) {
//                        int k = j + 1;
//                        char a1 = arrC[j];
//                        char a2 = arrC[k];
//                        if (((Character.isDigit(a1) || specialChars.contains(String.valueOf(a1)))) && Character.isLetter(a2)) {
//
//                            list.add(k);
//                        }
//
//                    }
//                    int end;
//                    AbilityLevelDto abilityLevelDto;
//
//
//                    for (int j = 0; j < list.size(); j++) {
//                        if (j < list.size() - 1) {
//                            end = list.get(j + 1);
//                        } else {
//                            end = full.length();
//                        }
//                        String a1 = full.substring(list.get(j), end).replace("\n", "").replace(" ", "").trim();
//                        Logger.debug(TAG, ">>>" + "arrPerLevel String:" + a1);
//
//                        String[] arr2 = a1.split(":");
//                        abilityLevelDto = new AbilityLevelDto();
//                        abilityLevelDto.name = arr2[0];
//
//                        String[] arrPerLevel = arr2[1].split("/");
//                        Logger.debug(TAG, ">>>" + "arrPerLevel:" + arrPerLevel.length);
//                        if (arrPerLevel.length == 1) {
//
//                            abilityLevelDto.list.add(arr2[1]);
//                            abilityLevelDto.list.add(arr2[1]);
//                            abilityLevelDto.list.add(arr2[1]);
//                            if (!dto.isUltimate) {
//                                abilityLevelDto.list.add(arr2[1]);
//                            }
//                        }
//
//                        if (arrPerLevel.length == 3) {
//                            dto.isUltimate = true;
//                            for (int k = 0; k < arrPerLevel.length; k++) {
//                                abilityLevelDto.list.add(arrPerLevel[k]);
//                            }
//                            abilityLevelDto.list.add("-");
//                        }
//
//                        if (arrPerLevel.length == 4) {
//                            for (int k = 0; k < arrPerLevel.length; k++) {
//                                abilityLevelDto.list.add(arrPerLevel[k]);
//                            }
//                        }
//                        dto.listAbilityPerLevel.add(abilityLevelDto);
//                    }

                    try {
                        dto.listAbilityPerLevel.addAll(getInfoAbility(nodeA).listAbilityPerLevel);
                        if (getInfoAbility(nodeA).isUltimate) {
                            dto.isUltimate = true;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
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


            // Notes
            String nodePath = "//td[@style = 'vertical-align:top;border-left:1px solid black;padding:3px 5px;']";
            Object[] dataNotes = tagNode.evaluateXPath(nodePath);
            for (int i = 0; i < listAbilities.size(); i++) {
                TagNode tn = (TagNode) dataNotes[i];
                String node = tn.getText().toString();
                String[] arr = node.split("\n");
                for (String s : arr) {
                    try {

                        if (!TextUtils.isEmpty(s.trim()) && !s.trim().equals("Notes:")) {
                            listAbilities.get(i).listNotes.add(new AbilityNotesDto(s.replace(DOT, " ").trim()));
                        }

                    } catch (Exception e) {
                        Logger.error(TAG, ">>>" + "Note error:" + e.toString());

                    }
                }
            }


            return listAbilities;


        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "error ROOT:" + e.toString());

        }
        return null;
    }

    private AbilityDto getInfoAbility(TagNode nodeA) throws Exception {
        AbilityDto abilityDto = new AbilityDto();
        List<AbilityLevelDto> listAbi = new ArrayList<>();
        abilityDto.listAbilityPerLevel.addAll(listAbi);

        //ABILITY INFO
        TagNode nodeInfo = nodeA.getChildTagList().get(POS_ABILITY_INFO);
        String fullText = nodeInfo.getChildTagList().get(0).getText().toString().trim();
        Logger.debug(TAG, ">>>" + "Full Text:" + fullText);
        String full = fullText.replace("&", " ").
                replace("&", " ").replace("(", " ");
        Logger.debug(TAG, ">>>" + "Info all:" + full);
        char[] arrC = full.toCharArray();
        List<Integer> list = new ArrayList<>();
        list.add(0);
        String specialChars = "/*!@#$%^&*()\"{}_[]|\\?/<>,.";
        for (int j = 0; j < arrC.length - 1; j++) {
            int k = j + 1;
            char a1 = arrC[j];
            char a2 = arrC[k];
            if (((Character.isDigit(a1) || specialChars.contains(String.valueOf(a1)))) && Character.isLetter(a2)) {

                list.add(k);
            }

        }
        int end;
        AbilityLevelDto abilityLevelDto;


        for (int j = 0; j < list.size(); j++) {
            if (j < list.size() - 1) {
                end = list.get(j + 1);
            } else {
                end = full.length();
            }
            String a1 = full.substring(list.get(j), end).replace("\n", "");
            Logger.debug(TAG, ">>>" + "info:" + a1);

            String[] arr2 = a1.split(":");
            abilityLevelDto = new AbilityLevelDto();
            abilityLevelDto.name = arr2[0];

            String[] arrPerLevel = arr2[1].split("/");
            Logger.debug(TAG, ">>>" + "full arr:" + arrPerLevel.length);
            if (arrPerLevel.length == 1) {

                abilityLevelDto.list.add(arr2[1]);
                abilityLevelDto.list.add(arr2[1]);
                abilityLevelDto.list.add(arr2[1]);
                abilityLevelDto.list.add(arr2[1]);
            }

            if (arrPerLevel.length == 3) {
                abilityDto.isUltimate = true;
                for (int k = 0; k < arrPerLevel.length; k++) {
                    abilityLevelDto.list.add(arrPerLevel[k]);
                }
                abilityLevelDto.list.add("-");
            }

            if (arrPerLevel.length >= 4) {
                for (int k = 0; k < 4; k++) {
                    abilityLevelDto.list.add(arrPerLevel[k]);
                }
            }
            abilityDto.listAbilityPerLevel.add(abilityLevelDto);
        }
        return abilityDto;
    }


}
