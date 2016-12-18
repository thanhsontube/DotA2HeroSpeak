package son.nt.dota2.fragment;

import com.squareup.otto.Subscribe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import son.nt.dota2.MsConst;
import son.nt.dota2.MyApplication;
import son.nt.dota2.R;
import son.nt.dota2.adapter.HeroListAdapter;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.di.component.app.AppComponent;
import son.nt.dota2.di.module.herolist.HeroListModule;
import son.nt.dota2.dto.GalleryDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.home.IHeroListPage;
import son.nt.dota2.home.herolist.HeroListContract;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

public class HeroListFragment extends AFragment implements IHeroListPage, HeroListContract.View {
    private static final String EXTRA_GROUP = "EXTRA_GROUP";
    private static final String TAG = HeroListFragment.class.getSimpleName();

    @Inject
    HeroListContract.Presenter mPresenter;
    //    private HeroList heroList;
    private HeroListAdapter mAdapter;

    private String mGroup = "Str";
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public static HeroListFragment newInstance(String group) {
        HeroListFragment fragment = new HeroListFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_GROUP, group);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mGroup = getArguments().getString(EXTRA_GROUP);
        setupDI();
        OttoBus.register(this);
        OttoBus.register(mPresenter);
    }

    private void setupDI() {
        AppComponent appComponent = MyApplication.get(getContext()).getAppComponent();
        appComponent.plus(new HeroListModule(this))
                .inject(this);

    }

    @Override
    public void onDestroy() {
        OttoBus.unRegister(this);
        OttoBus.unRegister(mPresenter);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hero_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        initListener();
        mPresenter.setGroup(mGroup);
        mPresenter.getHeroList();
    }


    private void initLayout(View view) {

        mAdapter = new HeroListAdapter(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void initListener() {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void heroSelected(HeroEntry HeroEntry);
    }

    @Subscribe
    public void updateAdapter(GalleryDto galleryDto) {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public String getGroup() {
        return mGroup;
    }

    @Override
    public String getGroupDisplayName() {
        if (TextUtils.isEmpty(mGroup)) {
            return "";
        }
        if (MsConst.GROUP_STR.equals(mGroup)) {
            return getString(R.string.group_str);
        }
        if (MsConst.GROUP_AGI.equals(mGroup)) {
            return getString(R.string.group_agi);
        }
        if (MsConst.GROUP_INTEL.equals(mGroup)) {
            return getString(R.string.group_intel);
        }
        return null;
    }

    @Override
    public void showHeroList(List<HeroBasicDto> heroBasicDtoList) {
        Logger.debug(TAG, ">>>" + "showHeroList:" + heroBasicDtoList.size());
        mAdapter.setData(heroBasicDtoList);
    }
}
