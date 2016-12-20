package son.nt.dota2.story;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import son.nt.dota2.MsConst;
import son.nt.dota2.MyApplication;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.di.component.app.AppComponent;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.story.search_sound.SearchSoundActivity;
import timber.log.Timber;

public class CreateStoryActivity extends BaseActivity implements StoryContract.View {

    @BindView(R.id.story_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.story_name)
    EditText mStoryNameEdt;

    @BindView(R.id.story_save)
    View saveView;

    AdapterCreateStory mAdapter;

    StoryContract.Presenter mPresenter;

    //    @Inject
    FirebaseUser mFirebaseUser;


    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_create_story;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        inject();
        setupToolbar();

        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.delete(StoryPartDto.class);
        realm.commitTransaction();
        realm.close();

        saveView.setVisibility(View.GONE);

        mPresenter = new StoryPresenter(this, new HeroRepository());
        mAdapter = new AdapterCreateStory(new ArrayList<>(), this, mICreateStoryListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);


    }

    private void setupToolbar() {
        setSupportActionBar(ButterKnife.findById(this, R.id.home_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Create");
    }

    private void inject() {
        AppComponent appComponent = MyApplication.get(this).getAppComponent();
        if (appComponent != null) {

            appComponent.inject(this);
        } else {
            Timber.d(">>>" + "How can NULL ????");
        }
    }

    @OnClick(R.id.story_save)
    public void onSaveClick() {
        mPresenter.saveStory(mStoryNameEdt.getText().toString(), mFirebaseUser);
    }

    @OnClick(R.id.create_story_stop)
    public void onStopClick() {
        mPresenter.stopStory();
    }

    @OnClick(R.id.create_story_play)
    public void onPlayClick() {
        mPresenter.playStory(mStoryNameEdt.getText().toString(), mFirebaseUser);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mFirebaseUser == null) {
            Toast.makeText(this, "Need login", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        mPresenter.createAddList();
    }

    AdapterCreateStory.ICreateStoryListener mICreateStoryListener = new AdapterCreateStory.ICreateStoryListener() {
        @Override
        public void onAddLeftClick() {
            SearchSoundActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_LEFT);
        }

        @Override
        public void onAddRightClick() {
            SearchSoundActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_RIGHT);
        }

        @Override
        public void onAddMiddleClick() {
            SearchSoundActivity.start(CreateStoryActivity.this, MsConst.TYPE_SOUND_MIDDLE);
        }
    };

    @Override
    public void showAddList(List<StoryPartDto> dtos) {
        if (dtos != null && dtos.size() > 1) {
            saveView.setVisibility(View.VISIBLE);
        }
        mAdapter.setData(dtos);
    }

    @Override
    public void doFinish() {
        Toast.makeText(this, "Save successful, your story will be pushed in Story List", Toast.LENGTH_SHORT).show();
        finish();
    }
}
