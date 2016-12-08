package son.nt.dota2.story;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.realm.Realm;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.story.add_simple_story.AddSimpleStoryActivity;

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

        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(StoryPartDto.class);
        realm.commitTransaction();
        realm.close();

        mPresenter = new StoryPresenter(this, new HeroRepository());
        mAdapter = new AdapterCreateStory(new ArrayList<>(), this, mICreateStoryListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.createAddList();
    }

    AdapterCreateStory.ICreateStoryListener mICreateStoryListener = new AdapterCreateStory.ICreateStoryListener() {
        @Override
        public void onAddLeftClick() {
            AddSimpleStoryActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_LEFT);
        }

        @Override
        public void onAddRightClick() {
            AddSimpleStoryActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_RIGHT);
        }

        @Override
        public void onAddMiddleClick() {
            AddSimpleStoryActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_MIDDLE);
        }
    };

    @Override
    public void showAddList(List<StoryPartDto> dtos) {
        mAdapter.setData(dtos);
    }
}
