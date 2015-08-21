package son.nt.dota2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.otto.Subscribe;
import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.MainActivity;
import son.nt.dota2.adapter.AdapterRcvHome;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.dto.GalleryDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsGaTools;
import son.nt.dota2.utils.TsLog;
import son.nt.dota2.utils.TsScreen;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeroListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeroListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroListFragment extends AFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HeroListFragment";
    public static final int EFFECT_DEFAULT = JazzyHelper.GROW;
    int currentEffect = EFFECT_DEFAULT;

    public static final String KEY_EFFECT_DEFAULT = "KEY_EFFECT_DEFAULT";
    //    private HeroList heroList;
    private AdapterRcvHome adapterHome;
    private List<HeroEntry> listHero = new ArrayList<>();

    private String group = "Str";
    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;
    private OnFragmentInteractionListener mListener;
    TsLog log = new TsLog(TAG);

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
//            heroList = (HeroList) getArguments().getSerializable(ARG_PARAM1);
            group = getArguments().getString(ARG_PARAM2);
            getList();

        }
    }

    private void getList () {
        if (group.equals(MsConst.GROUP_STR)) {
            listHero = HeroManager.getInstance().getStrHeroes();
        } else if (group.equals(MsConst.GROUP_AGI)) {
            listHero = HeroManager.getInstance().getAgiHeroes();
        } else if (group.equals(MsConst.GROUP_INTEL)) {
            listHero = HeroManager.getInstance().getIntelHeroes();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hero_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log.d("log>>>" + "onViewCreated heroList");
        initData();
        initLayout(view);
        initListener();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_EFFECT_DEFAULT, currentEffect);
    }

    private void setEffect() {
        jazzyRecyclerViewScrollListener.setTransitionEffect(currentEffect);
    }

    private void initData() {
//        listHero.clear();
//        listHero.addAll(getListHeros(group));
    }

    private void initLayout(View view) {
        adapterHome = new AdapterRcvHome(context, listHero);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        setEffect();
        recyclerView.addOnScrollListener(jazzyRecyclerViewScrollListener);
        int row = 2;
        if (TsScreen.isLandscape(getActivity())) {
            row = 4;
        }
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(row, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(adapterHome);

    }

    AdapterView.OnItemClickListener onClickGrid = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TsGaTools.trackPages("/" + listHero.get(position).name);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("data", listHero.get(position));
            startActivity(intent);
        }
    };

    private void initListener() {

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);

        void heroSelected(HeroEntry HeroEntry);
    }


    @Subscribe
    public void updateAdapter (GalleryDto galleryDto) {
        getList();
        adapterHome.notifyDataSetChanged();
    }



}
