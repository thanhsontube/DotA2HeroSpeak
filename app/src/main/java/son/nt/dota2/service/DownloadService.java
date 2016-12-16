package son.nt.dota2.service;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.utils.Logger;
import timber.log.Timber;

public class DownloadService extends Service {

    private static final String TAG = DownloadService.class.getSimpleName();

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

    public void addList(List<? extends ISound> list) {
        for (ISound d : list) {
            download(d);
        }
    }

    private void download(ISound iSound) {
        Observable.create((Observable.OnSubscribe<String>) subscriber -> {
            File fileTo = new File(ResourceManager.getInstance().getPathSound(iSound.getLink(), iSound.getSavedRootFolder(), iSound.getSavedBranchFolder()));
            downloadSound(iSound.getLink(), fileTo);

            if (!TextUtils.isEmpty(iSound.getArcanaLink())) {
                File fileToArcana = new File(ResourceManager.getInstance().getPathSound(iSound.getArcanaLink(), iSound.getSavedRootFolder(), iSound.getSavedBranchFolder()));
                downloadSound(iSound.getArcanaLink(), fileToArcana);
            }

            subscriber.onNext("");
            subscriber.onCompleted();

        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    Timber.d(">>>" + "download done:" + iSound.getSavedRootFolder() + "/" + iSound.getSavedBranchFolder());

                });
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

    public void downloadSound(String linkSpeak, File fileTo) {
        Timber.d(">>>" + "downloadSound:" + linkSpeak + " ;to:" + fileTo.toString());
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

                OutputStream out = new FileOutputStream(fileTo, false);
                byte[] buffer = new byte[1024];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                in.close();

                Logger.debug(TAG, ">>>" + "downloadLink SUCCESS");

            } else {
                Logger.error(TAG, ">>>" + "Download FAIL:" + status.getStatusCode());
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadSound err:" + e);
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
                String path = ResourceManager.getInstance().folderMusicPack + File.separator;
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

    public void addLinkDto2(List<HeroResponsesDto> list, String heroId) {
        Logger.debug(TAG, ">>>" + "========addLinkDto 2======:" + list.size());
        this.heroId = heroId;
        DownloadLoaderResponse loader = new DownloadLoaderResponse();
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);
    }

    class DownloadLoaderResponse extends AsyncTask<List<HeroResponsesDto>, Void, Integer> {
        @Override
        protected Integer doInBackground(List<HeroResponsesDto>... params) {
            try {
                List<HeroResponsesDto> list = params[0];

                int i = 0;
                for (HeroResponsesDto dto : list) {
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
    }
}
