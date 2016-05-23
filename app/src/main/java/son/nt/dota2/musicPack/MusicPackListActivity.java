package son.nt.dota2.musicPack;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.ottobus_entry.GoAdapterMusicPackHome;

public class MusicPackListActivity extends ASafeActivity {
    @Bind(R.id.music_pack_home_rcv)
    RecyclerView mRecyclerView;

    private AdapterMusicPackHome mAdapter;

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

        findViewById(R.id.test_click_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPParseUtils.getInstance().withMusicPacksList();
            }
        });
    }

    @Subscribe
    public void getData (GoAdapterMusicPackHome goAdapterMusicPackHome)
    {
        mAdapter.setData(goAdapterMusicPackHome.list);
    }

}
