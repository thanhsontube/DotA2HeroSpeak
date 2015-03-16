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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.FilterLog;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class PrefetchService extends IntentService {
    private static final String TAG = "PrefetchService";
    FilterLog log = new FilterLog(TAG);
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
    protected void onHandleIntent(Intent intent) {
        log.d("log>>>" + "-------------INTENT SERVICE START---------------------");
        if (intent != null) {
            try {
                HeroData herodata = FileUtil.readHeroList(this);
                if (herodata == null) {
                    return;
                }

                HeroDto heroDto = herodata.listHeros.get(0);
                String name = heroDto.name;
                HeroDto dto = FileUtil.readHeroSpeak(this, name);
                if (dto == null) {
                    //load here
                    int i = 0;
                    String pathSpeak = String.format("http://dota2.gamepedia.com/%s_responses", heroDto.name);
                    if (saveHeroToCache(pathSpeak, name)) {
                        log.d("log>>>" + i + "-Down success:" + name);
                        i++;
                    } else {
                        i++;
                        log.e("log>>>" + i + "-Error download:" + name);
                    }
                }
//                for (HeroDto heroDto : herodata.listHeros) {
//                    String name = heroDto.name;
//                    HeroDto dto = FileUtil.readHeroSpeak(this, name);
//                    if (dto == null) {
//                        //load here
//                        int i = 0;
//                        String pathSpeak = String.format("http://dota2.gamepedia.com/%s_responses", heroDto.name);
//                        if (saveHeroToCache(pathSpeak, name)) {
//                            log.d("log>>>" + i + "-Down success:" + name);
//                            i++;
//                        } else {
//                            i++;
//                            log.e("log>>>" + i + "-Error download:" + name);
//                        }
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }



    private boolean saveHeroToCache(String urlString, String fileName) {
        if (TextUtils.isEmpty(urlString) || urlString.contains("null") || urlString.contains("NULL")) {
            return false;
        }
        HeroData herodata = new HeroData();
        HeroDto heroDto = new HeroDto();
        List<SpeakDto> listSpeaks = heroDto.listSpeaks;
        SpeakDto speakDto;
        herodata.listHeros.add(heroDto);
        try {
            String path = ResourceManager.getInstance().folderHero + File.separator + fileName;
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(new HttpGet(urlString));
            StatusLine status = httpResponse.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {
                log.d("log>>>" + "HttpStatus.SC_OK");
            } else {
                log.e("log>>>" + "HttpStatus ERROR");
            }
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
            log.d("log>>>" + "data:" + data.length);
            tagNode = (TagNode) data[0];
            // Log.e("", "log>>>" + "data HeroSpeakLoader:" + data.length);

            List<TagNode> listNodes = (List<TagNode>) tagNode.getAllElementsList(false);
            log.d("log>>>" + "listNodes:" + listNodes.size());
            int i = 0;
            TagNode headerTag;
            StringBuilder builder;
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
                } else {
                    //speak text and link
                    xPath = "./li/a[@href]";
                    data = tag.evaluateXPath(xPath);

                    //content speak
                    String xPathContent = "./li";
                    Object[] dataContent = tag.evaluateXPath(xPathContent);
                    TagNode tagContent;


                    xPath = "./li";
                    if (data != null && data.length > 0) {
//                        Log.v(TAG, "log>>>" + "data:" + data.length);
                        for (int j = 0; j < data.length; j++) {
                            speakDto = new SpeakDto();
                            headerTag = (TagNode) data[j];
                            tagContent = (TagNode) dataContent[j];
                            String link = headerTag.getAttributeByName("href");
                            Log.v(TAG, "log>>>" + "LINK:" + link);
                            builder = (StringBuilder) tagContent.getText();
                            Log.v(TAG, "log>>>" + "what:" + builder.toString());
                            speakDto.link = link;
                            speakDto.text = builder.toString().replace("Play", "").trim();
                            listSpeaks.add(speakDto);
                        }
                    }
                }


            }

            Log.e("", "log>>>" + "data HeroSpeakLoader listNodes:" + listNodes.size());
            FileUtil.saveHeroSpeak(this, heroDto, fileName);
//            File fileFrom = new File(pathTemp);
//            File fileTo = new File(path);
//            fileFrom.renameTo(fileTo);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
        }
        try {
            FileUtil.saveHeroSpeak(this, heroDto, fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
