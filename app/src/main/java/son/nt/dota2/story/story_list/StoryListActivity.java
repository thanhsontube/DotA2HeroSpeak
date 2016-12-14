package son.nt.dota2.story.story_list;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.story.CreateStoryActivity;

public class StoryListActivity extends BaseActivity implements StoryListContract.View {

    @BindView(R.id.story_list_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;

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
        setupToolbar();

        mAdapter = new AdapterStoryList(this, new ArrayList<>(0), mIAdapterStoryListCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getStoryList();


    }

    @OnClick(R.id.fab_add)
    public void addNewStory () {
        ActivityCompat.startActivity(this,new Intent(this, CreateStoryActivity.class), null);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Story");
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
