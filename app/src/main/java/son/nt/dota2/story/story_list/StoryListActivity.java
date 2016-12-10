package son.nt.dota2.story.story_list;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;

public class StoryListActivity extends BaseActivity implements StoryListContract.View {

    @BindView(R.id.story_list_rcv)
    RecyclerView mRecyclerView;

    AdapterStoryList mAdapter;

    StoryListContract.Presenter mPresenter;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_story_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new StoryListPresenter(this, new HeroRepository());

        mAdapter = new AdapterStoryList(this, new ArrayList<>(0), mIAdapterStoryListCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getStoryList();


    }

    AdapterStoryList.IAdapterStoryListCallback mIAdapterStoryListCallback = new AdapterStoryList.IAdapterStoryListCallback() {
        @Override
        public void onAdapterHeroClick(StoryFireBaseDto StoryDto) {

        }
    };

    @Override
    public void showAddList(List<StoryFireBaseDto> storyDtos) {
        mAdapter.setData(storyDtos);
    }
}
