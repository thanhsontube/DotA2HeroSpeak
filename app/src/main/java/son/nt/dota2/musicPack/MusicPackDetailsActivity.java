package son.nt.dota2.musicPack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.squareup.otto.Subscribe;

import butterknife.Bind;
import son.nt.dota2.R;
import son.nt.dota2.base.ASafeActivity;
import son.nt.dota2.customview.KenBurnsView2;
import son.nt.dota2.dto.musicPack.MusicPackDto;
import son.nt.dota2.ottobus_entry.GoAdapterMusicPackDetails;

public class MusicPackDetailsActivity extends ASafeActivity {
    private static final String DATA = "DATA";
    @Bind(R.id.music_pack_detail_rcv)
    RecyclerView mRecyclerView;

    @Bind(R.id.kenburn)
    KenBurnsView2 kenBurnsView2;

    private AdapterMusicPackDetail mAdapter;
    private MusicPackDto mMusicPackDto;

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

        if (getIntent() != null) {
            mMusicPackDto = (MusicPackDto) getIntent().getSerializableExtra(DATA);
            if (mMusicPackDto == null)
            {
                return;
            }
            setupToolbar(toolbar, -1, mMusicPackDto.getName());
            kenBurnsView2.setResourceUrl(mMusicPackDto.getHref(), false);
//            HTTPParseUtils.getInstance().withMusicPacksDetails(mMusicPackDto.getLinkDetails());
            mAdapter.setData(mMusicPackDto.getList());

        }


    }

    @Override
    public int getContentViewID() {
        return R.layout.activity_music_pack_details;
    }

    @Subscribe
    public void getData (GoAdapterMusicPackDetails goAdapterMusicPackDetails)
    {
        mAdapter.setData(goAdapterMusicPackDetails.list);
    }
}
