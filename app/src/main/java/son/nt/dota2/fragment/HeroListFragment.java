package son.nt.dota2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.MainActivity;
import son.nt.dota2.adapter.AdapterRcvHome;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
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
    public static final String KEY_EFFECT_DEFAULT = "KEY_EFFECT_DEFAULT";

    private HeroData herodata;
    private AdapterRcvHome adapterHome;
    private List<HeroDto> listHero = new ArrayList<>();
    private String group;

    RecyclerView recyclerView;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    private OnFragmentInteractionListener mListener;
    TsLog log = new TsLog(TAG);
    int currentEffect = EFFECT_DEFAULT;

    public static HeroListFragment newInstance(HeroData herodata, String group) {
        HeroListFragment fragment = new HeroListFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, herodata);
        args.putString(ARG_PARAM2, group);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            herodata = (HeroData) getArguments().getSerializable(ARG_PARAM1);
            group = getArguments().getString(ARG_PARAM2);
        }
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
        log.d("log>>>" + "onViewCreated herodata");
        initData();
        initLayout(view);
        initListener();
        if (savedInstanceState != null) {
            currentEffect = savedInstanceState.getInt(KEY_EFFECT_DEFAULT, EFFECT_DEFAULT);
            setEffect();

        }
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
        listHero.clear();
        listHero.addAll(getListHeros(group));
    }

    private void initLayout(View view) {
        adapterHome = new AdapterRcvHome(context, listHero, new AdapterRcvHome.IAdapter() {
            @Override
            public void onIAdapterItemCLick(HeroDto heroDto, int position) {
                if (mListener != null) {
                    mListener.heroSelected(heroDto);
                }
            }
        });
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
//        gridView.setOnItemClickListener(onClickGrid);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
        void heroSelected (HeroDto heroDto);
    }

    private List<HeroDto> getListHeros(String group) {
        List<HeroDto> list = new ArrayList<>();
        if (group.equals(MsConst.GROUP_STR)) {
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_STR)) {
                    list.add(dto);
                }
            }
        } else if (group.equals(MsConst.GROUP_AGI)) {
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_AGI)) {
                    list.add(dto);
                }
            }
        } else if (group.equals(MsConst.GROUP_INTEL)) {
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_INTEL)) {
                    list.add(dto);
                }
            }
        }
        return list;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_effect, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_0:
                jazzyRecyclerViewScrollListener.setTransitionEffect(0);
                currentEffect = 0;
                break;
            case R.id.action_1:
                jazzyRecyclerViewScrollListener.setTransitionEffect(1);
                currentEffect = 1;
                break;
            case R.id.action_2:
                jazzyRecyclerViewScrollListener.setTransitionEffect(2);
                currentEffect = 2;
                break;
            case R.id.action_3:
                jazzyRecyclerViewScrollListener.setTransitionEffect(3);
                currentEffect = 3;
                break;
            case R.id.action_4:
                jazzyRecyclerViewScrollListener.setTransitionEffect(4);
                currentEffect = 4;
                break;
            case R.id.action_5:
                jazzyRecyclerViewScrollListener.setTransitionEffect(5);
                currentEffect = 5;
                break;
            case R.id.action_6:
                jazzyRecyclerViewScrollListener.setTransitionEffect(6);
                currentEffect = 6;
                break;
            case R.id.action_7:
                jazzyRecyclerViewScrollListener.setTransitionEffect(7);
                currentEffect = 7;
                break;
            case R.id.action_8:
                jazzyRecyclerViewScrollListener.setTransitionEffect(8);
                currentEffect = 8;
                break;
            case R.id.action_9:
                jazzyRecyclerViewScrollListener.setTransitionEffect(9);
                currentEffect = 9;
                break;
            case R.id.action_10:
                jazzyRecyclerViewScrollListener.setTransitionEffect(10);
                currentEffect = 10;
                break;
            case R.id.action_11:
                jazzyRecyclerViewScrollListener.setTransitionEffect(11);
                currentEffect = 11;
                break;
            case R.id.action_12:
                jazzyRecyclerViewScrollListener.setTransitionEffect(12);
                currentEffect = 12;
                break;
            case R.id.action_13:
                jazzyRecyclerViewScrollListener.setTransitionEffect(13);
                currentEffect = 13;
                break;
            case R.id.action_14:
                jazzyRecyclerViewScrollListener.setTransitionEffect(14);
                currentEffect = 14;
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
