package son.nt.dota2.service;

import com.squareup.otto.Subscribe;

import org.apache.http.client.methods.HttpGet;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.ResourceManager;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.heroSound.ISound;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.musicPack.MusicPackDetailsActivity;
import son.nt.dota2.ottobus_entry.GoPlayer;
import son.nt.dota2.ottobus_entry.GoVoice;
import son.nt.dota2.service.notification.INotification;
import son.nt.dota2.service.notification.NotificationImpl;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.PreferenceUtil;
import timber.log.Timber;

public class PlayService2 extends Service implements MediaServiceContract.Controller {

    private static final String TAG = PlayService2.class.getSimpleName();
    private static final int NOTIFICATION_ID = 101;
    private MediaPlayer mPlayer;
    private LocalBinder mBinder = new LocalBinder();


    private List<? extends ISound> list = new ArrayList<>();

    private MsConst.RepeatMode repeatMode = MsConst.RepeatMode.MODE_OFF;
    public int currentPosition = 0;
    public int prePos = 0;

    private boolean isPlayAndStopOne = false;

    private MediaServiceContract.Presenter mPresenter;

    private INotification mNotification;

    public void setCurrentList(List<? extends ISound> list) {
        this.list = list;
    }

    public static Intent getIntentService(Context context) {
        return new Intent(context, PlayService2.class);
    }

    @Override
    public void onCreate() {
        Timber.d(">>>" + "onCreate");
        super.onCreate();
        OttoBus.register(this);
        createMediaPlayerIfNeeded();

        mPresenter = new MediaServicePresenterImpl(this);

        mNotification = new NotificationImpl(this, this);

        IntentFilter filter = new IntentFilter();
        filter.addAction(NotificationImpl.ACTION_NEXT);
        filter.addAction(NotificationImpl.ACTION_PREV);
        filter.addAction(NotificationImpl.ACTION_PLAY);
        filter.addAction(NotificationImpl.ACTION_CLOSE);
        registerReceiver(notificationReceiver, filter);

    }

    @Override
    public void onDestroy() {
        Timber.d(">>>" + "onDestroy");
        OttoBus.unRegister(this);
        releaseMediaPlayer();
        super.onDestroy();
    }

    @Override
    public void releaseMediaPlayer() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void togglePlay() {
        if (mPlayer == null) {
            OttoBus.post(new GoPlayer(GoPlayer.DO_PAUSE, currentPosition));
            return;
        }

        if (mPlayer.isPlaying()) {
            OttoBus.post(new GoPlayer(GoPlayer.DO_PAUSE, currentPosition));
            mPlayer.pause();
            return;
        }
        OttoBus.post(new GoPlayer(GoPlayer.DO_PLAY, currentPosition));
        mPlayer.start();

    }

    public void downloadCurrent() {
        try {
            ISound mediaItem = list.get(currentPosition);
            File file = new File(ResourceManager.getInstance().getPathMusicPack(mediaItem.getLink()));
            if (!file.exists()) {
                Toast.makeText(getApplicationContext(), "Sorry, Can not download this file!", Toast.LENGTH_SHORT).show();
                return;
            }
            String link = mediaItem.getGroup() + "_" + mediaItem.getTitle() + "_" + currentPosition;
            FileUtil.copyFile(file.getPath(), ResourceManager.getInstance().getPathDownloadMusicPack(link));
            Toast.makeText(getApplicationContext(), "Downloaded at:" + ResourceManager.getInstance().getPathDownloadMusicPack(link), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public class LocalBinder extends Binder {
        public PlayService2 getService() {
            return PlayService2.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void createMediaPlayerIfNeeded() {
        if (mPlayer != null) return;

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(mp -> {

            //sleep 500ms
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .observeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            mPresenter.completeSound();

//                                if (!isPlayAndStopOne) {
//
//                                    playNextVideo();
//                                }
                        }
                    });

        });

        mPlayer.setOnErrorListener((mp, what, extra) -> {
            mPresenter.errorPlaySound();
//            if (!isPlayAndStopOne) {
//                playNextVideo();
//            }
            return false;
        });
    }

    @Override
    public void switchPlay(int status) {
        Timber.d(">>>" + "switchPlay:" + status);
    }

    @Override
    public void setSoundsSource(int type, List<? extends ISound> list) {
        Timber.d(">>>" + "setSoundsSource type:" + type + ";list:" + (list != null ? list.size() : "NULL"));
        mPresenter.setSoundsSource(type, list);
    }

    @Override
    public void showOnNotification(ISound dto) {
        mNotification.setData(dto);
        mNotification.doPlay();
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
        if (list == null || list.isEmpty()) {
            return;
        }
        playSong(index);
    }


    //TODO play song at index
    public void playSong(int index) {
        currentPosition = index;
        try {

            mPlayer.reset();
            if (list.size() >= currentPosition) {
                prePos = currentPosition;
                File file = new File(ResourceManager.getInstance().getPathMusicPack(list.get(index).getLink()));
                if (file.exists()) {
                    mPlayer.setDataSource(file.getPath());
                    mPlayer.prepare();
                    mPlayer.start();
                    /**
                     * @see MusicPackDetailsActivity#commandFromServiceMedia
                     */
//                    OttoBus.post(new GoPlayer(GoPlayer.DO_PLAY, list.get(currentPosition), currentPosition));
                } else {
                    loadMedia(list.get(index).getLink());
                }


            }
        } catch (Exception e) {
            if (!isPlayAndStopOne) {
                playNextVideo();
            }
        }
    }

    @Override
    public void setCurrentIndex(int currentIndex) {
        mPresenter.setCurrentIndex(currentIndex);
    }

    @Override
    public void play() {
        isPlayAndStopOne = false;
        playSong(currentPosition);
//        mPlayer.start();
    }

    @Override
    public void pause() {
        isPlayAndStopOne = true;
        if (mPlayer != null) {
            mPlayer.pause();
        }
    }

    @Override
    public void playOffline(String dataSource) {
        try {
            createMediaPlayerIfNeeded();
            pause();
            mPlayer.reset();
            mPlayer.setDataSource(dataSource);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Timber.e(">>>" + "playOffline:" + e);
        }
    }

    @Override
    public void playOnline(String link) {
        try {
            createMediaPlayerIfNeeded();
            pause();
            mPlayer.reset();
            mPlayer.setDataSource(link);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Timber.e(">>>" + "playOffline:" + e);
        }

    }

    @Override
    public void downloadSound(String link, String des) {

    }

    public List<? extends ISound> getList() {
        return list;
    }


    private void loadMedia(final String linkUrl) {
        try {

            HttpGet httpGet = new HttpGet(linkUrl);

            MediaLoader dataLoader = new MediaLoader(httpGet, false) {

                @Override
                public void onContentLoaderSucceed(File entity) {
                    Log.v(TAG, "log>>>" + "onContentLoaderSucceed:" + entity.getPath());
                    try {
                        File file = new File(ResourceManager.getInstance().getPathMusicPack(linkUrl));
                        Log.v(TAG, "log>>>" + "file:" + file.getPath());
                        entity.renameTo(file);
                        mPlayer.setDataSource(file.getPath());
                        mPlayer.prepare();
                        mPlayer.start();
//                        OttoBus.post(new GoPlayer(GoPlayer.DO_PLAY, list.get(currentPosition), currentPosition));
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
                mPlayer.reset();
                mPlayer.setDataSource(file.getPath());
                mPlayer.prepare();
                mPlayer.start();
            } else {
                loadMedia(link);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(NotificationImpl.ACTION_PLAY)) {
                play();
            } else if (action.equals(NotificationImpl.ACTION_NEXT)) {

//                playAtPos(currentPos ++);
            } else if (action.equals(NotificationImpl.ACTION_PREV)) {
//                playAtPos(currentPos --);
            } else if (action.equals(NotificationImpl.ACTION_CLOSE)) {
//                relaxResource(true);
//                if (mediaPlayer != null && mediaPlayer.isPlaying())
//                {
//                    mediaPlayer.stop();
//                    mediaPlayer.reset();
//                    mediaPlayer.release();
//                    mediaPlayer = null;
//                }
//               stopForeground(true);
            }
        }
    };

    @Subscribe
    public void onGetAdapterSwipeFragmentClick(GoVoice dto) {
//        play(dto.getLink(), dto.getHeroId());
        mPresenter.playSelectedSound(dto.mHeroResponsesDto, dto.arcana);
    }


}
