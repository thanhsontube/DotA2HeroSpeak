package son.nt.dota2.musicPack;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import io.realm.Realm;
import io.realm.RealmResults;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.base.MediaItem;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.dto.musicPack.MusicPackSoundDto;
import son.nt.dota2.dto.musicPack.MusicPackSoundRealm;
import son.nt.dota2.ottobus_entry.GoAdapterMusicPackDetail;
import son.nt.dota2.ottobus_entry.GoPlayer;
import son.nt.dota2.service.DownloadService;
import son.nt.dota2.service.PlayService;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.NetworkUtils;

public class MusicPackDetailsActivity extends ASafeActivity implements View.OnClickListener {
    private static final String TAG = MusicPackDetailsActivity.class.getSimpleName();
    private static final String DATA = "DATA";
    @Bind(R.id.music_pack_detail_rcv)
    RecyclerView mRecyclerView;

    @Bind(R.id.kenburn)
    KenBurnsView2 kenBurnsView2;

    @Bind(R.id.player_play)
    ImageView mImgPlay;

    @Bind(R.id.txt_media_name)
    TextView mTxtName;

    @Bind(R.id.txt_media_group)
    TextView mTxtGroup;

    @Bind(R.id.btn_fav)
    ImageView mImgFav;

    private AdapterMusicPackDetail mAdapter;
    private MusicPackDto mMusicPackDto;

    DownloadService downloadService;
    boolean isBind = false;

    private PlayService mPlayService;
    private Realm mRealm;


    public static void startActivity(Context context, MusicPackDto dto) {
        Intent intent = new Intent(context, MusicPackDetailsActivity.class);
        intent.putExtra(DATA, dto);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        mRealm = Realm.getDefaultInstance();
        //listener
        /**
         * @see #onClick
         */
        mImgPlay.setOnClickListener(this);
        mImgFav.setOnClickListener(this);

        mAdapter = new AdapterMusicPackDetail(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setAdapter(mAdapter);

        if (getIntent() == null) {
            finish();
            return;

        }
        mMusicPackDto = (MusicPackDto) getIntent().getSerializableExtra(DATA);
        if (mMusicPackDto == null || mMusicPackDto.getList() == null) {
            return;
        }
        setupToolbar(toolbar, -1, mMusicPackDto.getName());
        kenBurnsView2.setResourceUrl(mMusicPackDto.getHref(), false);

        mAdapter.setData(mMusicPackDto.getList());
        bindService(DownloadService.getIntent(this), serviceConnectionPrefetchAudio, Service.BIND_AUTO_CREATE);
        if (mPlayService == null) {

            bindService(PlayService.getIntent(this), serviceConnectionPlayer, Service.BIND_AUTO_CREATE);
        }

        initView();

    }


    private void initView() {
        mTxtName.setText(mMusicPackDto.getName());
        mTxtGroup.setText("" + mMusicPackDto.getList().size() + " tracks");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
        if (downloadService != null) {
            downloadService.isQuit = true;
        }
        if (downloadService != null) {
            unbindService(serviceConnectionPrefetchAudio);
        }

        if (mPlayService != null) {
            unbindService(serviceConnectionPlayer);
        }
    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_music_pack_details;
    }

    ServiceConnection serviceConnectionPrefetchAudio = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            startPrefetch();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            downloadService = null;
        }
    };

    ServiceConnection serviceConnectionPlayer = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayService.LocalBinder binder = (PlayService.LocalBinder) service;
            mPlayService = binder.getService();
            isBind = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = false;
            mPlayService = null;
        }
    };

    private void startPrefetch() {
        Logger.debug(TAG, ">>>" + "startPrefetch isBind" + isBind);
        if (NetworkUtils.isConnected(getApplicationContext())) {
            downloadService.addLinkMusicPack(mMusicPackDto.getList());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.player_play: {
                if (mPlayService != null) {
                    if (mPlayService.getList().isEmpty()) {
                        mPlayService.setCurrentList(mAdapter.mValues);
                        mPlayService.playSong(0, false);

                    } else {

                        mPlayService.togglePlay();
                    }
                }
                break;
            }
            case R.id.btn_fav: {
                if (mPlayService == null) {
                    return;
                }
                checkAndAddFav();


                break;
            }
        }
    }

    private void checkAndAddFav() {
        MusicPackSoundDto musicPackSoundDto = mMusicPackDto.getList().get(mPlayService.currentPosition);
        if (musicPackSoundDto == null) {
            return;
        }
        MusicPackSoundRealm doCheckDto = mRealm.where(MusicPackSoundRealm.class).equalTo("itemId", musicPackSoundDto.getItemId()).findFirst();
        mRealm.beginTransaction();
        if (doCheckDto == null) {
            MusicPackSoundRealm musicPackSoundRealm = mRealm.createObject(MusicPackSoundRealm.class);
            musicPackSoundRealm.createDb(musicPackSoundDto);
            mImgFav.setImageResource(R.drawable.ic_fav_light_on);
        } else {
            doCheckDto.deleteFromRealm();
            mImgFav.setImageResource(R.drawable.ic_fav_light);
        }
        mRealm.commitTransaction();


    }

    @Subscribe
    public void itemClick(GoAdapterMusicPackDetail goAdapterMusicPackDetail) {
        if (!isSafe()) {
            return;
        }
        if (mPlayService == null) {
            return;
        }
        mPlayService.setCurrentList(mAdapter.mValues);
        mPlayService.playSong(goAdapterMusicPackDetail.getPosition(), false);
    }

    @Subscribe
    public void commandFromServiceMedia(GoPlayer goPlayer) {
        switch (goPlayer.command) {
            case GoPlayer.DO_PAUSE:
                mImgPlay.setImageResource(R.drawable.icon_played);
                break;
            case GoPlayer.DO_PLAY:
                mImgPlay.setImageResource(R.drawable.icon_paused);
                if (goPlayer.mediaItem != null) {
                    updateMediaWidget(goPlayer.mediaItem);
                }
                break;
        }
        mAdapter.setNewPos(goPlayer.pos);
    }

    private void updateMediaWidget(MediaItem mediaItem) {
        if (!isSafe()) {
            return;
        }
        RealmResults<MusicPackSoundRealm> list = mRealm.where(MusicPackSoundRealm.class).equalTo("group", mMusicPackDto.getName()).findAll();

        mImgFav.setImageResource(R.drawable.ic_fav_light);
        for (MusicPackSoundRealm d : list) {
            if (d.getItemId().equals(mediaItem.getItemId())) {
                mImgFav.setImageResource(R.drawable.ic_fav_light_on);
                break;
            }
        }


        mTxtName.setText(mediaItem.getTitle());
        mTxtGroup.setText(mediaItem.getGroup());

    }

}
