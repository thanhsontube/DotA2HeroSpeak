package son.nt.dota2.story.choose_hero;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSearchHero;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.ottobus_entry.GoAddSimpleStory;
import son.nt.dota2.utils.OttoBus;

public class ChooseHeroActivity extends BaseActivity implements ChooseHeroContract.View {

    @BindView(R.id.choose_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.search_keyword)
    EditText mSearch;

    AdapterSearchHero adapterSearchHero;

    ChooseHeroContract.Presenter mPresenter;

    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_choose_hero;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new ChooseHeroPresenter(this, new HeroRepository());
        adapterSearchHero = new AdapterSearchHero(this, new ArrayList<>(0), mSearchHeroCallback);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapterSearchHero);

        mSearch.addTextChangedListener(mTextWatcher);

        mPresenter.getAllHeroList();
    }

    @Override
    public void showList(List<HeroBasicDto> list) {
        adapterSearchHero.setData(list);
    }

    AdapterSearchHero.IAdapterSearchHeroCallback mSearchHeroCallback = heroBasicDto -> {
        OttoBus.post(new GoAddSimpleStory(heroBasicDto));
        finish();
    };

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
