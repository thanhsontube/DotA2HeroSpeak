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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterRcvHome;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.dto.GalleryDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsScreen;

public class HeroListFragment extends AFragment {
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = HeroListFragment.class.getSimpleName();
    //    private HeroList heroList;
    private AdapterRcvHome adapterHome;
    private List<HeroBasicDto> listHero = new ArrayList<>();

    private String group = "Str";
    RecyclerView recyclerView;
    StaggeredGridLayoutManager mLayoutMng;

    private OnFragmentInteractionListener mListener;

    public static HeroListFragment newInstance(String group) {
        HeroListFragment fragment = new HeroListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM2, group);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OttoBus.register(this);
        if (getArguments() != null) {
            group = getArguments().getString(ARG_PARAM2);

        }
    }

    private void getList() {
//        if (group.equals(MsConst.GROUP_STR)) {
//            listHero = HeroManager.getInstance().getStrHeroes();
//        } else if (group.equals(MsConst.GROUP_AGI)) {
//            listHero = HeroManager.getInstance().getAgiHeroes();
//        } else if (group.equals(MsConst.GROUP_INTEL)) {
//            listHero = HeroManager.getInstance().getIntelHeroes();
//        }

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference();
        Query query = reference.child(HeroBasicDto.class.getSimpleName()).orderByChild("group").equalTo(group);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener = new ValueEventListener()
    {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot)
        {
            List<HeroBasicDto> list = new ArrayList<>();
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren())
            {
                HeroBasicDto post = postSnapshot.getValue(HeroBasicDto.class);
                list.add(post);
            }


            adapterHome.setData(list);
            recyclerView.scrollToPosition(0);
        }

        @Override
        public void onCancelled(DatabaseError databaseError)
        {

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
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

        getList();

    }


    private void initLayout(View view) {
        adapterHome = new AdapterRcvHome(getContext());
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        final int row = TsScreen.isLandscape(getActivity()) ? 4 : 3;
        mLayoutMng = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutMng);
        recyclerView.setAdapter(adapterHome);
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
        adapterHome.notifyDataSetChanged();
    }

}
