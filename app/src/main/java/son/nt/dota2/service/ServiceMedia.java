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
import son.nt.dota2.adapter.AdapterVoice;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.utils.PreferenceUtil;
import son.nt.dota2.utils.TsLog;

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
    public String heroID;

    private boolean isPlayAndStopOne = false;
    AdapterVoice adapterVoice;
    public void setAdapterVoice (AdapterVoice adapterVoice, String heroID) {
        this.adapterVoice = adapterVoice;
        this.heroID = heroID;
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
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    playNextVideo();
                }
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                if (!isPlayAndStopOne) {
                    playNextVideo();
                }
                return false;
            }
        });
    }

    public void playNextVideo() {
        repeatMode = MsConst.RepeatMode.getMode(PreferenceUtil.getPreference(this, MsConst.KEY_REPEAT, 1));
        switch (repeatMode) {
            case MODE_ONE:
                playSong(currentPosition, heroID);
                break;
            case MODE_OFF:
                currentPosition++;
                if (currentPosition != list.size()) {
                    playSong(currentPosition, heroID);
                }
                break;
            case MODE_ON:
                currentPosition++;
                if (currentPosition == list.size()) {
                    currentPosition = 0;
                }
                playSong(currentPosition, heroID);
                break;

            default:
                break;
        }


    }

    public void playSong (int index, String heroID, boolean isItemClick) {
        isPlayAndStopOne = isItemClick;
        playSong(index, heroID);
    }



    //TODO play song at index
    public void playSong(int index, String heroID) {
        currentPosition = index;
        try {
            player.reset();
            if (list.size() >= currentPosition) {
                if (adapterVoice != null) {
                    if (currentPosition > 0) {
                        adapterVoice.getItem(prePos).isPlaying = false;
                    }
                    adapterVoice.getItem(currentPosition).isPlaying = true;
                    adapterVoice.notifyDataSetChanged();

                }

                if(txtPos != null) {
                    txtPos.setText("(" + currentPosition + ")");
                }
                prePos = currentPosition;
                File file = new File(ResourceManager.getInstance().getPathAudio(list.get(index).link, heroID));
                if (file.exists()) {
                    player.setDataSource(file.getPath());
                    player.prepare();
                    player.start();
                } else {
                    loadSpeak(list.get(index).link, heroID);
                }


            }
        } catch (Exception e) {
            log.e("log>>>" + "MediaService error play song:" + currentPosition + ":" + e.toString());
            if (!isPlayAndStopOne) {
                playNextVideo();
            }
        }
    }

//    public void playSong2(int index) {
//        currentPosition = index;
//        try {
//            player.reset();
//            if (list.size() >= currentPosition) {
//                if (adapter != null) {
//                    if (currentPosition > 0) {
//                        adapter.getItem(prePos).isPlaying = false;
//                    }
//                    adapter.getItem(currentPosition).isPlaying = true;
//                    adapter.notifyDataSetChanged();
//
//                }
//
//                if(txtPos != null) {
//                    txtPos.setText("(" + currentPosition + ")");
//                }
//                prePos = currentPosition;
//                File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(list.get(index).count).replace(".mp3", ".dat"));
//                if (file.exists()) {
//                    player.setDataSource(file.getPath());
//                    player.prepare();
//                    player.start();
//                } else {
//                    loadSpeak(list.get(index).count);
//                }
//
//
//            }
//        } catch (Exception e) {
//            log.e("log>>>" + "MediaService error play song:" + currentPosition + ":" + e.toString());
//            if (!isPlayAndStopOne) {
//                playNextVideo();
//            }
//        }
//    }

    public void play() {
        isPlayAndStopOne = false;
        playSong(currentPosition, heroID);
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
        currentPosition = 0;
        prePos = 0;
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

    private void loadSpeak(final String linkSpeak, final String heroID) {
        try {

            HttpGet httpGet = new HttpGet(linkSpeak);

            MediaLoader dataLoader = new MediaLoader(httpGet, false) {

                @Override
                public void onContentLoaderSucceed(File entity) {
                    Log.v(TAG, "log>>>" + "onContentLoaderSucceed:" + entity.getPath());
                    try {
                        File file = new File(ResourceManager.getInstance().getPathAudio(linkSpeak, heroID));
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

    public void play (String link, String heroID) {
        isPlayAndStopOne = true;
        try {
            File file = new File(ResourceManager.getInstance().getPathAudio(link, heroID));
            if (file.exists()) {
                player.reset();
                player.setDataSource(file.getPath());
                player.prepare();
                player.start();
            } else {
                loadSpeak(link, heroID);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public void playSingleLink (String count) {
//        isPlayAndStopOne = true;
//        try {
//            File file = new File(ResourceManager.getInstance().folderAudio, File.separator + FileUtil.createPathFromUrl(count).replace(".mp3", ".dat"));
//            if (file.exists()) {
//                player.reset();
//                player.setDataSource(file.getPath());
//                player.prepare();
//                player.start();
//            } else {
//                loadSpeak(count);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }




}
