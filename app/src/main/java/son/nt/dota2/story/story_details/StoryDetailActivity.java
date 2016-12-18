package son.nt.dota2.story.story_details;

import org.parceler.Parcels;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.customview.UserAddStoryView;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.story.cmts.StoryCmtsDialog;

public class StoryDetailActivity extends BaseActivity implements StoryDetailContract.View {

    StoryDetailContract.Presenter mPresenter;

    @BindView(R.id.story_list_detail_rcv)
    RecyclerView mRecyclerView;
    AdapterStoryDetail mAdapter;

    @BindView(R.id.user_add_story_view)
    UserAddStoryView mUserAddStoryView;


    @OnClick(R.id.story_detail_close)
    public void onCloseClick() {
        finish();
    }




    public static void start(Activity activity, StoryFireBaseDto storyID) {
        Intent intent = new Intent(activity, StoryDetailActivity.class);
        intent.putExtra("data", Parcels.wrap(storyID));
        activity.startActivity(intent);
    }


    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_story_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StoryDetailPresenter(this, new HeroRepository());
        mPresenter.setStory(Parcels.unwrap(getIntent().getParcelableExtra("data")));

        mAdapter = new AdapterStoryDetail(new ArrayList<>(), this, null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
        setupActionBar();

        mPresenter.filterData();
    }

    private void setupActionBar () {
        setSupportActionBar(ButterKnife.findById(this, R.id.home_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @OnClick(R.id.play_a_story)
    public void onPlayStory() {
        mPresenter.playStory();
    }

    @OnClick(R.id.stop_a_story)
    public void onStopStory() {
        mPresenter.stopStory();
    }

    @OnClick(R.id.story_detail_cmts)
    public void onCommendsClick() {
        StoryCmtsDialog storyCmtsDialog = StoryCmtsDialog.newInstance(mPresenter.getStoryId());
        storyCmtsDialog.show(getSupportFragmentManager(), StoryCmtsDialog.class.getSimpleName());

    }

    @Override
    public void showList(List<StoryPartDto> contents) {
        mAdapter.setData(contents);
    }

    @Override
    public void updateUserView(String title, long createdTime) {
        mUserAddStoryView.setData(title, createdTime);
    }
}
