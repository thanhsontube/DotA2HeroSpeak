package son.nt.dota2.musicPack;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.Bind;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.dto.save.SaveMusicPack;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.service.PlayService;
import son.nt.dota2.utils.FileUtil;

public class MusicPackListActivity extends ASafeActivity {
    @Bind(R.id.music_pack_home_rcv)
    RecyclerView mRecyclerView;

    private AdapterMusicPackHome mAdapter;
    private PlayService mPlayService;

    @Override
    public int getContentViewID() {
        return R.layout.activity_music_pack_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setupToolbar(toolbar, -1, null);

        mAdapter = new AdapterMusicPackHome(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        try {
            SaveMusicPack saveMusicPack = (SaveMusicPack) FileUtil.getMusicPackObject(this);
            if (saveMusicPack != null)
            {
                mAdapter.setData(saveMusicPack.list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.test_click_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPParseUtils.getInstance().withMusicPacksList();
            }
        });

        startService(PlayService.getIntentService(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(PlayService.getIntentService(this));
    }

    @Subscribe
    public void getData (SaveMusicPack goAdapterMusicPackHome)
    {
        mAdapter.setData(goAdapterMusicPackHome.list);
    }

    @Subscribe
    public void itemClick (MusicPackDto musicPackDto)
    {
        MusicPackDetailsActivity.startActivity(this, musicPackDto);
    }

}
