package son.nt.dota2.service;

import android.app.IntentService;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PrefetchService extends IntentService {
    private static final String TAG = "PrefetchService";
    Logger log = new Logger(TAG);
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    public static final String ACTION_FOO = "son.nt.dota2.service.action.FOO";
    public static final String ACTION_BAZ = "son.nt.dota2.service.action.BAZ";

    // TODO: Rename parameters
    public static final String EXTRA_PARAM1 = "son.nt.dota2.service.extra.PARAM1";
    public static final String EXTRA_PARAM2 = "son.nt.dota2.service.extra.PARAM2";

    public PrefetchService() {
        super("PrefetchService");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        log.e("log>>>" + "-------------INTENT SERVICE onDestroy---------------------");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        log.d("log>>>" + "-------------INTENT SERVICE START---------------------");
        if (intent != null) {
            try {
                HeroData herodata = FileUtil.readHeroList(this);
                if (herodata == null) {
                    log.d("log>>>" + "herodata == null");
                    return;
                }
                int i = 0;
                for (HeroDto heroDto : herodata.listHeros) {
                    String name = heroDto.name;
                    if (FileUtil.readHeroSpeak(this, name) == null) {
                        //load here
                        String pathSpeak = String.format("http://dota2.gamepedia.com/%s_responses", heroDto.name);
                        if (saveHeroToCache(pathSpeak, name, heroDto)) {
                            log.e("log>>>" + i + "-prefetch success:" + name );
                        } else {
                            log.e("log>>>" + i + "-prefetch error:" + name + ";path:" + pathSpeak);
                        }
                    } else {
                        log.d("log>>>" + i + "-prefetch catch-ed:"  + name);
                    }
                    i++;
                }
            } catch (Exception e) {
                log.e("log>>>" + "Error Prefetch :" + e);
            }
        }
    }



    private boolean saveHeroToCache(String urlString, String fileName, HeroDto heroDto) {
        boolean isReturn = false;
        boolean isNetWorkOK = false;
        if (TextUtils.isEmpty(urlString) || urlString.contains("null") || urlString.contains("NULL")) {
            log.d("log>>>" + "urlString empty");
            return false;
        }

        if (urlString.contains("Natures_Prophet")) {
            urlString = urlString.replace("Natures", "Nature's");
        }
        List<SpeakDto> listSpeaks = heroDto.listSpeaks;
        SpeakDto speakDto;
        String lastTitle = "";
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(urlString));

            StatusLine status = httpResponse.getStatusLine();
            if (status.getStatusCode() != HttpStatus.SC_OK) {
                log.e("log>>>" + "HttpStatus ERROR");
                isNetWorkOK = false;
                return false;
            }
            isNetWorkOK = true;
            log.d("log>>>" + "HttpStatus.SC_OK");
            InputStream in = httpResponse.getEntity().getContent();

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
//                    Log.v("", "log>>>" + i + " ====data HeroSpeakLoader header:" + builder.toString());
                    speakDto.title = builder.toString();
                    speakDto.isTitle = true;
                    listSpeaks.add(speakDto);
                    lastTitle = builder.toString();
                } else {
                    // speak text and link

                    // normal
                    if (lastTitle.contains("Purchasing a Specific Item")  ||lastTitle.contains("Killing a Rival")
                            || lastTitle.contains("Meeting an Ally") || lastTitle.contains("Removed from game") ) {
                        log.d("log>>>" + "Purchasing a Specific Item");
                    } else {

                        xPath = "./li/a[@href]";
                        data = tag.evaluateXPath(xPath);

                        // content speak
                        String xPathContent = "./li";
                        Object[] dataContent = tag.evaluateXPath(xPathContent);
                        TagNode tagContent;

                        xPath = "./li";
                        if (data != null && data.length > 0) {
                            for (int j = 0; j < data.length; j++) {
                                headerTag = (TagNode) data[j];
                                tagContent = (TagNode) dataContent[j];
                                link = headerTag.getAttributeByName("href");
//                                Log.i(TAG, "log>>>" + "LINK:" + link);
                                builder = (StringBuilder) tagContent.getText();
//                                Log.i(TAG, "log>>>" + "what:" + builder.toString());

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
            isReturn = true;
        } catch (final Exception e) {
            isReturn = false;
        } finally {
            try {
                if(isNetWorkOK) {
                    FileUtil.saveHeroSpeak(this, heroDto, fileName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return isReturn;
        }

    }
}
