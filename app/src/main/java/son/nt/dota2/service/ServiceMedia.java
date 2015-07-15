package son.nt.dota2.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.adapter.AdapterSpeak;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.TsLog;
import son.nt.dota2.utils.PreferenceUtil;

public class ServiceMedia extends Service {

    private static final String TAG = "ServiceMedia";
    TsLog log = new TsLog(TAG);
    private MediaPlayer player;
    private LocalBinder mBinder = new LocalBinder();
    private String source;
    public List<SpeakDto> list = new ArrayList<SpeakDto>();
    private MsConst.RepeatMode repeatMode = MsConst.RepeatMode.MODE_OFF;
    public int currentPosition = 0;
    public int prePos = 0;

    private boolean isPlayAndStopOne = false;

    public SpeakDto getnextFile() {
        if (list == null || list.size() == 0) {
            return null;
        }
        if (currentPosition == list.size() - 1) {
            return list.get(0);
        }
        return list.get(currentPosition + 1);
    }

    public static Intent getIntentService(Context context) {
        return new Intent(context, ServiceMedia.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        log.v("log>>>" + "ServiceMedia onCreate");
        initController();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        log.e("log>>>" + "ServiceMedia onDestroy");
    }

    public class LocalBinder extends Binder {
        public ServiceMedia getService() {
            return ServiceMedia.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void initController() {
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                if (!isPlayAndStopOne) {
                    playNextVideo();
                }
//                play();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (!isPlayAndStopOne) {
                    playNextVideo();
                }
//                play();
                return false;
            }
        });
    }

    public void playNextVideo() {
        repeatMode = MsConst.RepeatMode.getMode(PreferenceUtil.getPreference(this, MsConst.KEY_REPEAT, 1));
        switch (repeatMode) {
            case MODE_ONE:
                playSong(currentPosition);
                break;
            case MODE_OFF:
                currentPosition++;
                if (currentPosition != list.size()) {
                    playSong(currentPosition);
                }
                break;
            case MODE_ON:
                currentPosition++;
                if (currentPosition == list.size()) {
                    currentPosition = 0;
                }
                playSong(currentPosition);
                break;

            default:
                break;
        }


    }

    public void playSong (int index, boolean isItemClick) {
        isPlayAndStopOne = isItemClick;
        playSong(index);
    }



    //TODO play song at index
    public void playSong(int index) {
        currentPosition = index;
        try {
            player.reset();
            if (list.size() >= currentPosition) {
                if (adapter != null) {
                    if (currentPosition > 0) {
                        adapter.getItem(prePos).isPlaying = false;
                    }
                    adapter.getItem(currentPosition).isPlaying = true;
                    adapter.notifyDataSetChanged();
                }

                if(txtPos != null) {
                    txtPos.setText("(" + currentPosition + ")");
                }
                prePos = currentPosition;
                File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(list.get(index).link).replace(".mp3", ".dat"));
                if (file.exists()) {
                    player.setDataSource(file.getPath());
                    player.prepare();
                    player.start();
                } else {
                    loadSpeak(list.get(index).link);
                }


            }
        } catch (Exception e) {
            log.e("log>>>" + "MediaService error play song:" + currentPosition + ":" + e.toString());
            if (!isPlayAndStopOne) {
                playNextVideo();
            }
        }
    }

    public void play() {
        isPlayAndStopOne = false;
        playSong(currentPosition);
//        player.start();
    }

    public void pause () {
        isPlayAndStopOne = true;
        if (player != null) {
            player.pause();
        }
    }
    public void stop () {
        if (player != null) {
            try {
                player.stop();
                player.release();
                player = null;
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

        }
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setListData(List<SpeakDto> list) {
        log.d("log>>>" + "MediaService setListData:" + list.size());
        this.list.clear();
        this.list.addAll(list);
    }

    public MediaPlayer getPlayer() {
        return player;
    }

    AdapterSpeak adapter;
    TextView txtPos;
    String heroName;
    public void setAdapter (AdapterSpeak adapter) {
        this.adapter = adapter;
    }
    public void setActionBar (TextView txtPos, String heroName) {
        this.txtPos = txtPos;
        this.heroName = heroName;
    }

    private void loadSpeak(final String linkSpeak) {
        try {

            HttpGet httpGet = new HttpGet(linkSpeak);

            MediaLoader dataLoader = new MediaLoader(httpGet, false) {

                @Override
                public void onContentLoaderSucceed(File entity) {
                    Log.v(TAG, "log>>>" + "onContentLoaderSucceed:" + entity.getPath());
                    try {
                        File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
                        Log.v(TAG, "log>>>" + "file:" + file.getPath());
                        entity.renameTo(file);
                        player.setDataSource(file.getPath());
                        player.prepare();
                        player.start();
                    } catch (Exception e) {
                       e.printStackTrace();
                    }
                }

                @Override
                public void onContentLoaderStart() {
                    Log.v(TAG, "log>>>" + "onContentLoaderStart");
                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    Log.v(TAG, "log>>>" + "onContentLoaderFailed");
                }
            };

            ResourceManager.getInstance().getContentManager().load(dataLoader);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play (String link) {
        isPlayAndStopOne = true;
        try {
            File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat"));
            if (file.exists()) {
                player.reset();
                player.setDataSource(file.getPath());
                player.prepare();
                player.start();
            } else {
                loadSpeak(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playSingleLink (String link) {
        isPlayAndStopOne = true;
        try {
            File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(link).replace(".mp3", ".dat"));
            if (file.exists()) {
                player.reset();
                player.setDataSource(file.getPath());
                player.prepare();
                player.start();
            } else {
                loadSpeak(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
