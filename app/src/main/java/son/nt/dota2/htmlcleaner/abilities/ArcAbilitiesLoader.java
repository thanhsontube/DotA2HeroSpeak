package son.nt.dota2.htmlcleaner.abilities;

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
public abstract class ArcAbilitiesLoader extends ContentLoader<List<AbilityDto>> {
    public static final String PATH_ABILITY = "http://dota2.gamepedia.com/Outworld_Devourer";
    public static final String PATH_ABILITY_ROOT = "http://dota2.gamepedia.com/";
    public static final String TAG = "AbilitiesLoader";

    public static final int POS_ABILITY_NAME = 0;
    public static final int POS_ABILITY_IMAGE = 1;
    public static final int POS_ABILITY_DESCRIPTION = 2;
    public static final int POS_ABILITY_INFO = 3;
    public static final int POS_ABILITY_COOL_DOWN_MANA = 4;

    public static final String DOT = " ";

    public ArcAbilitiesLoader(HttpUriRequest request, boolean useCache) {
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

            List<AbilityDto> listAbilities = new ArrayList<>();

            listAbilities.addAll(getListFromDota2(tagNode));


            return listAbilities;


        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "error ROOT:" + e.toString());

        }
        return null;
    }

    //get list from this one : http://www.dota2.com/hero/Arc_Warden/
    private List<AbilityDto> getListFromDota2 (TagNode nodeA) {
        Logger.debug(TAG, ">>>" + "getListFromDota2");
        List<AbilityDto> listAbilities = new ArrayList<>();


        try {
            String xPath = "//div[@id ='overviewHeroAbilities']";
            Object[] obs = nodeA.evaluateXPath(xPath);
            Logger.debug(TAG, ">>>" + "obs:" + obs.length);
            TagNode tagAbis = (TagNode) obs[0];
            for (TagNode tag  : tagAbis.getChildTagList()) {
                TagNode tagIcon = tag.getChildTagList().get(0);
                TagNode tagDes = tag.getChildTagList().get(1);
                String image = tagIcon.getChildTagList().get(0).getAttributeByName("src");
                List<TagNode> listDes = tagDes.getChildTagList();
                AbilityDto dto = new AbilityDto();
                dto.name = listDes.get(0).getText().toString().trim();
                dto.description = listDes.get(1).getText().toString().trim();
                Logger.debug(TAG, ">>>" +  tagDes.getText());
                dto.linkImage = image;
                listAbilities.add(dto);

            }
        } catch (XPatherException e) {
            e.printStackTrace();
        }


        return listAbilities;
    }




}
