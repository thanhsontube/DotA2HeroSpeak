package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.apache.http.client.methods.HttpGet;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterPlayPlist;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.PlayListDto;
import son.nt.dota2.loader.PlayListLoader;
import son.nt.dota2.utils.TsLog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlayListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlayListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlayListFragment extends AFragment {

    private static final String TAG = "PlayListFragment";
    TsLog log = new TsLog(TAG);
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView recyclerView;
    private AdapterPlayPlist adapter;
    private List<PlayListDto> list;
    private StaggeredGridLayoutManager layoutManager;
    public static PlayListFragment newInstance(String param1, String param2) {
        PlayListFragment fragment = new PlayListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public PlayListFragment() {
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
        return inflater.inflate(R.layout.fragment_play_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener();
        controllerLoadPlayList.load();
    }

    private void initData() {
        list = new ArrayList<>();
    }

    private void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.playlist_recycle_view);
        recyclerView.setHasFixedSize(false);
        layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterPlayPlist(context, list);
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        adapter.setOnCallback(new AdapterPlayPlist.IAdapterCallback() {
            @Override
            public void onClick(int position, PlayListDto playListDto) {

            }
        });
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    int position = 0;

    Controller controllerLoadPlayList = new Controller() {
        @Override
        public void load() {
            String link = String.format(MsConst.FB_FETCH,context.getResources().getStringArray(R.array.play_list)[position] );
            HttpGet httpGet = new HttpGet(link);
            contentManager.load(new PlayListLoader(httpGet, false) {
                @Override
                public void onContentLoaderStart() {
                    log.d("log>>>" + "LoadPlayList Start");
                }

                @Override
                public void onContentLoaderSucceed(PlayListDto entity) {
                    log.d("log>>>" + "LoadPlayList onContentLoaderSucceed :" + entity.videoCount);
                    entity.playListId = context.getResources().getStringArray(R.array.play_list)[position];
                    list.add(entity);
                    adapter.notifyDataSetChanged();
                    position ++;
                    if (position < context.getResources().getStringArray(R.array.play_list).length) {
                        controllerLoadPlayList.load();
                    }

                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    log.e("log>>>" + "LoadPlayList onContentLoaderFailed:" + e.toString());
                    position ++;
                    if (position < context.getResources().getStringArray(R.array.play_list).length) {
                        controllerLoadPlayList.load();
                    }

                }
            });
        }
    };

}
