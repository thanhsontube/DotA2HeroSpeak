package son.nt.dota2.comments;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.base.BaseActivity;
import son.nt.dota2.data.HeroRepository;

public class CmtsHistoryActivity extends BaseActivity implements CmtsHisotyContract.View {

    CmtsHisotyContract.Presenter mPresenter;

    @BindView(R.id.story_list_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.home_toolbar)
    Toolbar mToolbar;

    AdapterCmtsHistory mAdapter;


    @Override
    protected int provideLayoutResID() {
        return R.layout.activity_cmts_history;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new CmtsHistoryPresenter(this, new HeroRepository());

        setupToolbar();

        mAdapter = new AdapterCmtsHistory(this, new ArrayList<>(0), null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getCmts();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Comments");
    }

    @Override
    public void showList(List<FullCmtsDto> fullCmtsDtos) {
        mAdapter.setData(fullCmtsDtos);
    }
}
