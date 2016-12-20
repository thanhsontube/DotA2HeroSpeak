package son.nt.dota2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

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

public class SettingDownloadService extends Service {
    private static final String TAG = SettingDownloadService.class.getSimpleName();
    private boolean isStop = false;

    public SettingDownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, SettingDownloadService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        isStop = false;
        new Download().execute();
    }

    @Override
    public void onDestroy() {
        isStop = true;
        super.onDestroy();

    }

    private class Download extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            downloadEveryThing();
            return null;
        }
    }

    private void downloadEveryThing() {
        Logger.debug(TAG, ">>>" + "downloadEveryThing");
        try {
            HeroManager heroManager = HeroManager.getInstance();
            for (HeroEntry heroEntry : heroManager.listHeroes) {
                String heroId = heroEntry.heroId;
                if (heroId != null) {
                    List<SpeakDto> listVoices = getVoicePackages(heroId);
                    if (listVoices != null && !listVoices.isEmpty()) {
                        for (SpeakDto speakDto : listVoices) {
//                            if (isStop) {
//                                return;
//                            }
                            String link = speakDto.link;
                            if (!TextUtils.isEmpty(link) && !TextUtils.isEmpty(heroId)) {
                                File fileTo = new File(ResourceManager.getInstance().getPathAudio(link, heroId));
                                downloadLink(link, fileTo, heroId);
                                OttoBus.post(new GoDownload(0, 0, heroEntry.name, link, speakDto.text));

                            }
                        }
                    }

                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<SpeakDto> getVoicePackages(String heroID) {
        try {
            AObject heroSpeak = FileUtil.getObject(this, "voice_" + heroID);
            if (heroSpeak != null) {
                HeroSpeakSaved heroSpeakSaved = (HeroSpeakSaved) heroSpeak;
                return heroSpeakSaved.listSpeaks;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void downloadLink(String linkSpeak, File fileTo, String heroId) {
        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }

        if (fileTo.exists()) {
            Logger.debug(TAG, ">>>" + "Exists linkSpeak:" + linkSpeak);
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
                    Logger.debug(TAG, ">>>" + "downloadLink SUCCESS");
                }

            } else {
                Logger.error(TAG, ">>>" + "Download FAIL:" + status.getStatusCode());
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadLink err:" + e);
        }
    }
}
