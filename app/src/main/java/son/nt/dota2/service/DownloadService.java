package son.nt.dota2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.utils.Logger;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";

    LocalBinder binder = new LocalBinder();

    public boolean isQuit = false;

    private String heroId;

    public class LocalBinder extends Binder {

        public DownloadService getService() {
            return DownloadService.this;
        }
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, DownloadService.class);
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void addLinkMusicPack(List<MusicPackSoundDto> list) {
        DownloadLoaderMusicPack loader = new DownloadLoaderMusicPack();
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);
    }

    public void addLinkDto(List<SpeakDto> list, String heroId) {
        Logger.debug(TAG, ">>>" + "========addLinkDto======:" + list.size());
        this.heroId = heroId;
        DownloadLoader loader = new DownloadLoader();
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);
    }

    class DownloadLoader extends AsyncTask<List<SpeakDto>, Void, Integer> {
        @Override
        protected Integer doInBackground(List<SpeakDto>... params) {
            try {
                List<SpeakDto> list = params[0];

                int i = 0;
                for (SpeakDto dto : list) {
                    //                log.d("log>>>" + "=============download:" + i + "============");
                    if (isQuit) {
                        return -1;
                    }
                    File fileTo = new File(ResourceManager.getInstance().getPathAudio(dto.link, heroId));
                    downloadLink(dto.link, fileTo);
                    i++;
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            stopSelf();
        }
    }

    class DownloadLoaderMusicPack extends AsyncTask<List<MusicPackSoundDto>, Void, Integer> {
        @Override
        protected Integer doInBackground(List<MusicPackSoundDto>... params) {
            try {
                List<MusicPackSoundDto> list = params[0];
                Logger.debug(TAG, ">>>" + "addLinkDto:" + list.size());
                int i = 0;
                for (MusicPackSoundDto dto : list) {
                    //                log.d("log>>>" + "=============download:" + i + "============");
                    if (isQuit) {
                        return -1;
                    }
                    File fileTo = new File(ResourceManager.getInstance().getPathMusicPack(dto.getLink()));
                    downloadLinkMusicPacks(dto.getLink(), fileTo);
                    i++;
                }
                return 0;
            } catch (Exception e) {
                e.printStackTrace();
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Logger.debug(TAG, ">>>" + "===FINISHED prefetch===");
            stopSelf();
        }
    }

    public void downloadLink(String linkSpeak, File fileTo) {
        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }

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
                    Logger.debug(TAG, ">>>" + "downloadLink SUCCESS");
                }

            } else {
                Logger.error(TAG, ">>>" + "Download FAIL:" + status.getStatusCode());
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadLink err:" + e);
        }
    }

    public void downloadLinkMusicPacks(String linkSpeak, File fileTo) {
        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }

        if (fileTo.exists()) {
            return;
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(linkSpeak));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                String path = ResourceManager.getInstance().folderMusicPack + File.separator ;
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
