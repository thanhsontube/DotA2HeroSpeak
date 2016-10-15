package son.nt.dota2.fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.squareup.otto.Subscribe;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
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
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsScreen;

public class HeroListFragment extends AFragment implements IHeroListPage, HeroListContract.View {
    private static final String EXTRA_GROUP = "EXTRA_GROUP";
    private static final String TAG = HeroListFragment.class.getSimpleName();

    @Inject
    HeroListContract.Presenter mPresenter;
    //    private HeroList heroList;
    private HeroListAdapter mAdapter;
    private List<HeroBasicDto> listHero = new ArrayList<>();

    private String mGroup = "Str";
    RecyclerView recyclerView;
    StaggeredGridLayoutManager mLayoutMng;

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

    private void getList() {
//        if (mGroup.equals(MsConst.GROUP_STR)) {
//            listHero = HeroManager.getInstance().getStrHeroes();
//        } else if (mGroup.equals(MsConst.GROUP_AGI)) {
//            listHero = HeroManager.getInstance().getAgiHeroes();
//        } else if (mGroup.equals(MsConst.GROUP_INTEL)) {
//            listHero = HeroManager.getInstance().getIntelHeroes();
//        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(HeroBasicDto.class.getSimpleName()).orderByChild("mGroup").equalTo(mGroup);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            List<HeroBasicDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                list.add(post);
            }
            mAdapter.setData(list);
            recyclerView.scrollToPosition(0);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

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

//        getList();

    }


    private void initLayout(View view) {
        mAdapter = new HeroListAdapter(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        final int row = TsScreen.isLandscape(getActivity()) ? 4 : 3;
        mLayoutMng = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutMng);
        recyclerView.setAdapter(mAdapter);
    }

    AdapterView.OnItemClickListener onClickGrid = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {

        }
    };

    private void initListener() {

    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void heroSelected(HeroEntry HeroEntry);
    }

    @Subscribe
    public void updateAdapter(GalleryDto galleryDto) {
        getList();
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
        mAdapter.setData(heroBasicDtoList);
    }
}
