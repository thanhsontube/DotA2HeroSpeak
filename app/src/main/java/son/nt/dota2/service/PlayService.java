package son.nt.dota2.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.base.MediaItem;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.musicPack.MusicPackDetailsActivity;
import son.nt.dota2.utils.PreferenceUtil;

public class PlayService extends Service {

    private static final String TAG = PlayService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 101;
    private MediaPlayer player;
    private LocalBinder mBinder = new LocalBinder();

    private List<MediaItem> list = new ArrayList<>();
    private Notification mNotification;


    private String source;
    //    public List<MusicPackSoundDto> list = new ArrayList<MusicPackSoundDto>();
    private MsConst.RepeatMode repeatMode = MsConst.RepeatMode.MODE_OFF;
    public int currentPosition = 0;
    public int prePos = 0;
    public String heroID;

    private boolean isPlayAndStopOne = false;

    public static Intent getIntent(Context context) {
        return new Intent(context, PlayService.class);
    }

    public void setCurrentList(List<MediaItem> list) {
        this.list = list;
    }


    public static Intent getIntentService(Context context) {
        return new Intent(context, PlayService.class);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        initController();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
    }

    public class LocalBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
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

    public void playSong(int index, boolean isItemClick) {
        isPlayAndStopOne = isItemClick;
        playSong(index);
    }


    //TODO play song at index
    public void playSong(int index) {
        currentPosition = index;
        try {
            Intent intent = new Intent(this, MusicPackDetailsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 102, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            mNotification = new NotificationCompat.Builder(this).setTicker(list.get(currentPosition).getTitle())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(list.get(currentPosition).getTitle())
                    .setContentText(list.get(currentPosition).getGroup())
                    .setContentInfo(getString(R.string.app_name))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(false)
                    .setOngoing(true)
                    .build();
            startForeground(NOTIFICATION_ID, mNotification);
            player.reset();
            if (list.size() >= currentPosition) {
//                if (adapterVoice != null) {
//                    if (currentPosition > 0) {
//                        adapterVoice.getItem(prePos).setPlaying(false);
//                    }
//                    adapterVoice.getItem(currentPosition).setPlaying(true);
//                    adapterVoice.notifyDataSetChanged();
//
//                }

                prePos = currentPosition;
                File file = new File(ResourceManager.getInstance().getPathMusicPack(list.get(index).getmUrl()));
                if (file.exists()) {
                    player.setDataSource(file.getPath());
                    player.prepare();
                    player.start();
                } else {
                    loadSpeak(list.get(index).getmUrl(), heroID);
                }


            }
        } catch (Exception e) {
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

    public void pause() {
        isPlayAndStopOne = true;
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
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


    public MediaPlayer getPlayer() {
        return player;
    }


    private void loadSpeak(final String linkSpeak, final String heroID) {
        try {

            HttpGet httpGet = new HttpGet(linkSpeak);

            MediaLoader dataLoader = new MediaLoader(httpGet, false) {

                @Override
                public void onContentLoaderSucceed(File entity) {
                    Log.v(TAG, "log>>>" + "onContentLoaderSucceed:" + entity.getPath());
                    try {
                        File file = new File(ResourceManager.getInstance().getPathMusicPack(linkSpeak));
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

    public void play(String link, String heroID) {
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


}
