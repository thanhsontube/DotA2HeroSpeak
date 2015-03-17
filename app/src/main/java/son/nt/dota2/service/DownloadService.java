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
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.FilterLog;

public class DownloadService extends Service {
    private static final String TAG = "DownloadService";
    FilterLog log = new FilterLog(TAG);

    LocalBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {

        public DownloadService getService() {
            return DownloadService.this;
        }
    }
    public Intent getIntent(Context context) {
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

    public void addLink(List<String> list) {
        log.d("log>>>" + "addLink:" + list.size());
        int i = 0;
        for (String link : list) {
            log.d("log>>>" + "=============download:" + i + "============");
            downloadLink(link);
            i++;
            stopSelf();
        }
    }

    public void addLinkDto(List<SpeakDto> list) {
        DownloadLoader loader = new DownloadLoader();
        loader.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);
    }

    class DownloadLoader extends AsyncTask<List<SpeakDto>, Void, Integer> {
        @Override
        protected Integer doInBackground(List<SpeakDto>... params) {
            List<SpeakDto> list = params[0];
            log.d("log>>>" + "addLinkDto:" + list.size());
            int i = 0;
            for (SpeakDto dto : list) {
                log.d("log>>>" + "=============download:" + i + "============");
                downloadLink(dto.link);
                i++;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            log.d("log>>>" + "===FINISHED prefetch===");
            stopSelf();
        }
    }

    public void downloadLink(String linkSpeak) {
        log.d("log>>>" + "downloadLink:" + linkSpeak);
        if(TextUtils.isEmpty(linkSpeak)) {
            return;
        }
        File fileTo = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
        if (fileTo.exists()) {
            log.d("log>>>" + "downloadLink exist");
            return;
        }

        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(linkSpeak));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                File fileOut = new File(ResourceManager.getInstance().folderAudio, File.separator + "down_temp");

                OutputStream out = new FileOutputStream(fileOut, false);
                byte []buffer = new byte[1024];
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
