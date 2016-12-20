package son.nt.dota2.story.search_sound;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.hero.hero_fragment.AdapterFragmentSound;

public class SearchSoundActivity extends BaseActivity implements SearchSoundContract.View {

    @BindView(R.id.choose_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_keyword)
    EditText mSearch;

    @BindView(R.id.des_txt)
    EditText mDescriptionView;

    AdapterFragmentSound mAdapter;

    SearchSoundContract.Presenter mPresenter;

    String mSide;

    public static void start(Activity context, String side) {
        Intent intent = new Intent(context, SearchSoundActivity.class);
        intent.putExtra("data", side);
        context.startActivity(intent);
    }

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_search_sound;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupToolbar();
        mPresenter = new SearchSoundPresenter(this, new HeroRepository());
        mAdapter = new AdapterFragmentSound(this, new ArrayList<>(0));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mSearch.addTextChangedListener(mTextWatcher);

        mSide = getIntent().getStringExtra("data");
        if (mSide.equals(MsConst.TYPE_SOUND_MIDDLE)) {
            mSearch.setVisibility(View.GONE);
        }
        mPresenter.setSide(mSide);
        mPresenter.getSomeSounds();
    }

    private void setupToolbar() {
        setSupportActionBar(ButterKnife.findById(this, R.id.home_toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("");
    }

    @OnClick(R.id.search_sound_select)
    public void onSelectedSound() {
        HeroResponsesDto heroResponsesDto = mAdapter.getSelected();
        if (heroResponsesDto == null && !mSide.equals(MsConst.TYPE_SOUND_MIDDLE)) {
            Toast.makeText(this, "Have to choose at least 1 item", Toast.LENGTH_SHORT).show();
            return;
        }

        mPresenter.wrapSimpleStory(mDescriptionView.getText().toString(), heroResponsesDto);
//        OttoBus.post(new GoAddASound(heroResponsesDto));
//        finish();
    }

    @Override
    public void showListData(List<HeroResponsesDto> list) {
        mAdapter.setData(list);
    }

    @Override
    public void closeActivity() {
        finish();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            Observable.just(s.toString())
                    .debounce(750L, TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(s1 -> {
                        mPresenter.setFilter(s1);
                        mPresenter.search();
                    });

        }
    };
}
