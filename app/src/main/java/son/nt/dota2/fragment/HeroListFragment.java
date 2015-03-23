package son.nt.dota2.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.activity.MainActivity;
import son.nt.dota2.adapter.AdapterHeroList;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.utils.FilterLog;
import son.nt.dota2.utils.TsGaTools;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeroListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeroListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroListFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "HeroListFragment";

    // TODO: Rename and change types of parameters
    private HeroData herodata;
    private GridView gridView;
    private AdapterHeroList adapter;
    private List<HeroDto> listHero = new ArrayList<>();
    private String group;

    private OnFragmentInteractionListener mListener;
    FilterLog log = new FilterLog(TAG);

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
    }

    private void initData() {
        listHero.clear();
        listHero.addAll(getListHeros(group));
    }
    private void initLayout(View view) {
        gridView = (GridView) view.findViewById(R.id.top_grid_view);
        adapter = new AdapterHeroList(context, listHero);
        gridView.setAdapter(adapter);
    }
    AdapterView.OnItemClickListener onClickGrid = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TsGaTools.trackPages("/" + listHero.get(position).name);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("data",listHero.get(position));
            startActivity(intent);
        }
    };

    private void initListener() {
        gridView.setOnItemClickListener(onClickGrid);
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    private List<HeroDto> getListHeros(String group) {
        List<HeroDto> list = new ArrayList<>();
        if (group.equals(MsConst.GROUP_STR)) {
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_STR)) {
                    list.add(dto);
                }
            }
        } else if (group.equals(MsConst.GROUP_AGI) ){
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_AGI)) {
                    list.add(dto);
                }
            }
        } else if (group.equals(MsConst.GROUP_INTEL) ){
            for (HeroDto dto : herodata.listHeros) {
                if (dto.group.equals(MsConst.GROUP_INTEL)) {
                    list.add(dto);
                }
            }
        }
        return list;

    }

}
