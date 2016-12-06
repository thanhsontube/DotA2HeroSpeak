package son.nt.dota2.story;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.dto.story.StoryDto;

public class CreateStoryActivity extends BaseActivity implements StoryContract.View {

    @BindView(R.id.story_rcv)
    RecyclerView mRecyclerView;

    AdapterCreateStory mAdapter;

    StoryContract.Presenter mPresenter;


    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_create_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPresenter = new StoryPresenter(this);
        mAdapter = new AdapterCreateStory(new ArrayList<>(), this, mICreateStoryListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.createAddList();
    }

    AdapterCreateStory.ICreateStoryListener mICreateStoryListener = new AdapterCreateStory.ICreateStoryListener() {
        @Override
        public void onAddLeftClick() {
            Intent intent = new Intent(getApplicationContext(), AddSimpleStoryActivity.class);
            startActivity(new Intent(intent));
        }

        @Override
        public void onAddRightClick() {
            Intent intent = new Intent(getApplicationContext(), AddSimpleStoryActivity.class);
            startActivity(new Intent(intent));
        }

        @Override
        public void onAddMiddleClick() {
            Intent intent = new Intent(getApplicationContext(), AddSimpleStoryActivity.class);
            startActivity(new Intent(intent));
        }
    };

    @Override
    public void showAddList(List<StoryDto> dtos) {
        mAdapter.setData(dtos);
    }
}
