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

import butterknife.Bind;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.service.DownloadService;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.NetworkUtils;

public class MusicPackDetailsActivity extends ASafeActivity {
    private static final String TAG = MusicPackDetailsActivity.class.getSimpleName();
    private static final String DATA = "DATA";
    @Bind(R.id.music_pack_detail_rcv)
    RecyclerView mRecyclerView;

    @Bind(R.id.kenburn)
    KenBurnsView2 kenBurnsView2;

    private AdapterMusicPackDetail mAdapter;
    private MusicPackDto mMusicPackDto;

    DownloadService downloadService;
    boolean isBind = false;


    public static void startActivity(Context context, MusicPackDto dto) {
        Intent intent = new Intent(context, MusicPackDetailsActivity.class);
        intent.putExtra(DATA, dto);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);

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
        if (mMusicPackDto == null) {
            return;
        }
        setupToolbar(toolbar, -1, mMusicPackDto.getName());
        kenBurnsView2.setResourceUrl(mMusicPackDto.getHref(), false);
        mAdapter.setData(mMusicPackDto.getList());
        bindService(DownloadService.getIntent(this), serviceConnectionPrefetchAudio, Service.BIND_AUTO_CREATE);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadService != null) {
            downloadService.isQuit = true;
        }
        if (serviceConnectionPrefetchAudio != null) {
            unbindService(serviceConnectionPrefetchAudio);
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
            isBind = true;
            startPrefetch();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = true;
        }
    };

    private void startPrefetch() {
        Logger.debug(TAG, ">>>" + "startPrefetch isBind" + isBind);
        if (isBind && NetworkUtils.isConnected(getApplicationContext())) {
            downloadService.addLinkMusicPack(mMusicPackDto.getList());
        }
    }

}
