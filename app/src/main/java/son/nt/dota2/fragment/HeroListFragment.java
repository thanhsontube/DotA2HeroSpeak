package son.nt.dota2.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.squareup.otto.Subscribe;

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

public class HeroListFragment extends AFragment {
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = HeroListFragment.class.getSimpleName();
    //    private HeroList heroList;
    private AdapterRcvHome adapterHome;
    private List<HeroEntry> listHero = new ArrayList<>();

    private String group = "Str";
    RecyclerView recyclerView;
    GridLayoutManager mGridLayoutManager;

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
            group = getArguments().getString(ARG_PARAM2);
            getList();

        }
    }

    private void getList() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hero_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        log.d("log>>>" + "onViewCreated heroList");
        initLayout(view);
        initListener();

    }


    private void initLayout(View view) {
        adapterHome = new AdapterRcvHome(context, listHero);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycle_view);
        recyclerView.setHasFixedSize(true);
        final int row = TsScreen.isLandscape(getActivity()) ? 4 : 2;
        mGridLayoutManager = new GridLayoutManager(getContext(), row);
        recyclerView.setLayoutManager(mGridLayoutManager);
        recyclerView.setAdapter(adapterHome);
    }

    AdapterView.OnItemClickListener onClickGrid = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view,
                                int position, long id) {
            TsGaTools.trackPages("/" + listHero.get(position).name);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("data", listHero.get(position));
            startActivity(intent);
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
