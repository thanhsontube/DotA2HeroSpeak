package son.nt.dota2.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSpeak;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.HeroSpeakLoader;
import son.nt.dota2.utils.FilterLog;

public class MainFragment extends BaseFragment {
    private static final String TAG = "MainFragment";
    FilterLog log = new FilterLog(TAG);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String linkPaSpeak = "http://dota2.gamepedia.com/Phantom_Assassin_responses";
    String linkLunaSpeak = "http://dota2.gamepedia.com/Luna_responses";
    String linkVoidSpeak = "http://dota2.gamepedia.com/Faceless_Void_responses";

    private ListView listview;
    private List<SpeakDto> list = new ArrayList<>();
    private AdapterSpeak adapter;

    private OnFragmentInteractionListener mListener;

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.d("log>>>" + "===============MAIN onCreate--------------");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mParam1 = linkPaSpeak;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener();
        controllerLoadSpeak.load();
    }

    private void initData() {

    }

    private void initLayout(View view) {
        listview = (ListView) view.findViewById(R.id.main_listview);
        adapter = new AdapterSpeak(context, list);
        listview.setAdapter(adapter);
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    Controller controllerLoadSpeak = new Controller() {
        @Override
        public void load() {
            {
                HttpGet httpGet = new HttpGet(mParam1);
                contentManager.load(new HeroSpeakLoader(httpGet, true) {
                    @Override
                    public void onContentLoaderStart() {
                        log.d("log>>>" + "onContentLoaderStart");
                    }

                    @Override
                    public void onContentLoaderSucceed(HeroData entity) {
                        log.d("log>>>" + "onContentLoaderSucceed :" + entity.listHeros.size());
                        HeroDto heroDto = entity.listHeros.get(0);
                        log.d("log>>>" + "list speaks:" + heroDto.listSpeaks.size());
                        list.clear();
                        list.addAll(heroDto.listSpeaks);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onContentLoaderFailed(Throwable e) {
                        log.e("log>>>" + "onContentLoaderFailed:" + e);
                    }
                });
            }
        }
    };


}
