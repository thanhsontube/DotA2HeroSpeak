package son.nt.dota2.musicPack;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.squareup.otto.Subscribe;

import java.io.IOException;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.dto.save.SaveMusicPack;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.musicPack.fav.MusicPackFavActivity;
import son.nt.dota2.utils.FileUtil;

public class MusicPackListActivity extends ASafeActivity {
    @BindView(R.id.music_pack_home_rcv)
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
        setupToolbar(toolbar, -1, "Dota 2 Music Pack");

        mAdapter = new AdapterMusicPackHome(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        // Create a custom SpanSizeLookup where the first item spans both columns
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return position == 0 || position == 1 ? 2 : 1;
            }
        });


        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        try {
            SaveMusicPack saveMusicPack = (SaveMusicPack) FileUtil.getMusicPackObject(this);
            if (saveMusicPack != null) {
                mAdapter.setData(saveMusicPack.list);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.test_click_1).setVisibility(View.GONE);
        findViewById(R.id.test_click_1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HTTPParseUtils.getInstance().withMusicPacksDetails2();
            }
        });

    }


    @Subscribe
    public void getData(SaveMusicPack goAdapterMusicPackHome) {
        mAdapter.setData(goAdapterMusicPackHome.list);
    }

    @Subscribe
    public void itemClick(MusicPackDto musicPackDto) {
        if (!isSafe() || musicPackDto == null) {
            return;
        }
        if (TextUtils.isEmpty(musicPackDto.getLinkDetails())) {
            MusicPackFavActivity.startActivity(this);
        } else {

            MusicPackDetailsActivity.startActivity(this, musicPackDto);
        }
    }

}
