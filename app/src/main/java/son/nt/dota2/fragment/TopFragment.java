package son.nt.dota2.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.client.methods.HttpGet;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterTop;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.loader.DataLoader;
import son.nt.dota2.utils.FilterLog;


public class TopFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "TopFragment";
    FilterLog log = new FilterLog(TAG);

    private ViewPager pager;
    private AdapterTop adapter;
    private HeroData herodata = new HeroData();
    private View view;
    //titlepage indicator
    TitlePageIndicator indicator;

    public static TopFragment newInstance(String param1, String param2) {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initData();
//        initLayout(view);
//        initListener();
        this.view = view;
        controllerHeroList.load();
    }

    private void initData() {
        herodata = new HeroData();
//        for (int i = 0; i < 50; i++) {
//
//            herodata.listHeros.add(new HeroDto());
//        }

    }

    private void initLayout(View view) {
        pager = (ViewPager) view.findViewById(R.id.pager);
        indicator = (TitlePageIndicator) view.findViewById(R.id.indicator);
        adapter = new AdapterTop(getActivity().getSupportFragmentManager(), herodata);
        pager.setAdapter(adapter);
        initIndicator();
    }

    private void initListener() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    public void initIndicator() {
        indicator.setViewPager(pager);
        indicator.setBackgroundColor(getResources().getColor(android.R.color.background_dark));
        indicator.setTextColor(Color.WHITE);
        indicator.setSelectedBold(true);
        indicator.setSelectedColor(Color.RED);
    }

    Controller controllerHeroList = new Controller() {
        @Override
        public void load() {
            HttpGet httpGet = new HttpGet(mypath.getHerosListPath());
            contentManager.load(new DataLoader(httpGet, true) {
                @Override
                public void onContentLoaderStart() {
                    log.d("log>>>" + "onContentLoaderStart");
                }

                @Override
                public void onContentLoaderSucceed(HeroData entity) {
                    log.d("log>>>" + "onContentLoaderSucceed :" + entity.listHeros.size());
                    herodata.listHeros.clear();
                    herodata.listHeros.addAll(entity.listHeros);
//                    adapter.notifyDataSetChanged();
//                    adapter.update(entity);

//                    initData();
                    initLayout(view);
                    initListener();

                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    log.e("log>>>" + "onContentLoaderFailed:" + e);
                }
            });
        }
    };

}
