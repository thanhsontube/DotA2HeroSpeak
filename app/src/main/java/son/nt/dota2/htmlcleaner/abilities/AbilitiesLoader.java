package son.nt.dota2.htmlcleaner.abilities;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

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
                    Logger.debug(TAG, ">>>" + "==========AAAA==========:" + sizeA + ":" + i);
                    dto = new AbilityDto();

                    //TODO need be care full with this
                    TagNode nodeA = ((TagNode) data[i]).getChildTagList().get(0);

                    //get skill voice
                    String tnPath = "//span[@style='position:relative; top:-2px;']";
                    Object[] oTn = nodeA.evaluateXPath(tnPath);
                    Logger.debug(TAG, ">>>" + "oTN:" + oTn.length);

                    TagNode tVoice = (TagNode) oTn[0];
                    String voice = tVoice.getChildTagList().get(0).getAttributeByName("href");
                    Logger.debug(TAG, ">>>" + "Voice:" + voice);

                    dto.sound = voice;

                    int sizeB = nodeA.getChildTagList().size();
                    for (int j = 0; j < sizeB; j++) {
                        Logger.debug(TAG, ">>>" + "-------BBBB-------:" + sizeB + ":" + j);
                        TagNode nodeB = nodeA.getChildTagList().get(j);
                        int sizeC = nodeB.getChildTagList().size();
                        for (int k = 0; k < sizeC; k++) {
                            TagNode nodeC = nodeB.getChildTagList().get(k);
                            Logger.debug(TAG, ">>>" + "--CCC---:" + sizeC + ":" + k + ";nodeC:" + nodeC.getChildTagList().size());
                            if (k == 0 && j ==0) {
                                String skillName = nodeC.getText().toString().substring(1, nodeC.getText().toString().indexOf("   "));
                                Logger.debug(TAG, ">>>" + "skill name:" + skillName);
                                dto.name = skillName;
                            }
                            Logger.debug(TAG, ">>>" + "tn2:" + nodeC.getText());
                        }
                    }
                    listAbilities.add(dto);
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
