package son.nt.dota2.htmlcleaner.role;

import org.apache.http.client.methods.HttpUriRequest;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.loader.base.ContentLoader;
import son.nt.dota2.utils.ImageDownload;
import son.nt.dota2.utils.Logger;

/**
 * Created by Sonnt on 7/13/15.
 */
public abstract class RolesLoader extends ContentLoader<List<RoleDto>> {
    public static final String PATH_ROLES = "http://dota2.gamepedia.com/Role";
    public static final String TAG = "RolesLoader";

    public RolesLoader(HttpUriRequest request, boolean useCache) {
        super(request, useCache);
    }

    @Override
    protected List<RoleDto> handleStream(InputStream in) throws IOException {
        try {
            HtmlCleaner cleaner = new HtmlCleaner();
            CleanerProperties props = cleaner.getProperties();
            props.setAllowHtmlInsideAttributes(true);
            props.setAllowMultiWordAttributes(true);
            props.setRecognizeUnicodeChars(true);
            props.setOmitComments(true);
            Object[] data;

            TagNode tagNode = cleaner.clean(in);



            /*

<span class="mw-headline" id="Carry"><img alt="Pip carry.png" src="http://hydra-media.cursecdn.com/dota2.gamepedia.com/thumb/5/52/Pip_carry.png/32px-Pip_carry.png?version=d0e4ea56acc484e048b1dc237b1b131d" width="32" height="32" srcset="http://hydra-media.cursecdn.com/dota2.gamepedia.com/thumb/5/52/Pip_carry.png/48px-Pip_carry.png 1.5x, http://hydra-media.cursecdn.com/dota2.gamepedia.com/5/52/Pip_carry.png 2x"> Carry</span>



             */
            String xpath = "//span[@class='mw-headline']";
            data = tagNode.evaluateXPath(xpath);
            if (data != null && data.length > 0) {
                Logger.debug(TAG, ">>>" + "length:" + data
                        .length);

                List<RoleDto> listRoles = new ArrayList<>();
                RoleDto dto;

                for (int i = 1; i < data.length; i++) {
                    dto = new RoleDto();

                    TagNode tNode = (TagNode) data[i];

                    if (tNode != null && tNode.getChildTagList() != null && tNode.getChildTagList().size() >= 0) {

                        int size = tNode.getChildTagList().size();
                        if (size == 0) {
                            Logger.debug(TAG, ">>>" + "Node 0:" + tNode.getText());
                            dto.name =  tNode.getText().toString();
                        } else {

                            TagNode imgNode = tNode.getChildTagList().get(0);
                            String tagName = imgNode.getName();
                            Logger.debug(TAG, ">>>" + "------" + tagName + "----------");
                            if ("img".equals(tagName)) {
                                String linkImage = imgNode.getAttributeByName("src");
                                String name = imgNode.getAttributeByName("alt").replace(" ", "_").replace("-", "_").toLowerCase();
                                linkImage = linkImage.substring(0, linkImage.indexOf("?version"));
                                Logger.debug(TAG, ">>>" + "Link:" + linkImage);

                                ImageDownload.downloadFile(ResourceManager.getInstance().getContext(), linkImage, name);
                                dto.linkIcon = linkImage;


                            }
                            dto.name = tNode.getText().toString();
                            Logger.debug(TAG, ">>>" + "Tex1:" + tNode.getText());
                        }
                    }
                    dto.no = i;
                    if(!dto.name.contains("Unofficial")) {
                        listRoles.add(dto);
                    }


                }
                upLoadToParseService(listRoles);
            }


        } catch (Exception e) {

        }
        return null;
    }

    private void upLoadToParseService (final List<RoleDto> listRoles) {

    }


}
