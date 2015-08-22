package son.nt.dota2.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import son.nt.dota2.HeroManager;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.ottobus_entry.GoDownload;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsLog;

/**
 * Created by Sonnt on 8/22/15.
 */
public class DownloadIntentService extends IntentService {
    public static final String TAG = "DownloadIntentService";
    TsLog log = new TsLog("DownloadIntentService");
    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    public DownloadIntentService(String name) {
        super(name);
    }

    int group = 0;
    int count = 0;

    public static Intent getIntent(Context context) {
        return new Intent(context, DownloadIntentService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (HeroEntry p : HeroManager.getInstance().getTest10()) {
            group++;
            count = 0;
            //get list speak
            try {
                String heroID = p.heroId;
                AObject heroSpeak = FileUtil.getObject(this, "voice_" + heroID);
                if (heroSpeak != null) {
                    Logger.debug(TAG, ">>>" + "heroSpeak != null");
                    HeroSpeakSaved heroSpeakSaved = (HeroSpeakSaved) heroSpeak;

                    for (SpeakDto speakDto : heroSpeakSaved.listSpeaks) {
                        count++;
                        OttoBus.post(new GoDownload(group, count, heroID, speakDto.link, speakDto.text));
                        downloadLink(speakDto.link, p.heroId);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        OttoBus.post(new GoDownload(0, 0, "Now you can hear offline", "", "Download successful"));
    }

    public void downloadLink(String linkSpeak, String heroId) {
        Logger.debug(TAG, ">>>" + "Download:" + group + " heroID:" + heroId + ">" + count + ">count:" + linkSpeak);

        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }
        File fileTo = new File(ResourceManager.getInstance().getPathAudio(linkSpeak, heroId));
        if (fileTo.exists()) {
            return;
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(linkSpeak));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                String path = ResourceManager.getInstance().folderAudio + File.separator + heroId + File.separator;
                File f = new File((path));
                if (!f.exists()) {
                    f.mkdirs();
                }
                File fileOut = new File(path, "down_temp");

                OutputStream out = new FileOutputStream(fileOut, false);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                in.close();

                if (fileOut.renameTo(fileTo)) {
                    log.d("log>>>" + "downloadLink SUCCESS");
                }

            } else {
                log.e("log>>>" + "Download FAIL:" + status.getStatusCode());
            }

        } catch (Exception e) {
            log.e("log>>>" + "downloadLink err:" + e);
        }
    }

}
