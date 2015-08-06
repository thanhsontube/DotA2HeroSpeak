package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.squareup.otto.Subscribe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterVoice;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.service.DownloadService;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.NetworkUtils;
import son.nt.dota2.utils.OttoBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VoiceFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VoiceFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VoiceFragment extends AbsFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "VoiceFragment";

    private String heroID;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private View autoPlay;

    public static VoiceFragment newInstance(String param1) {
        VoiceFragment fragment = new VoiceFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public VoiceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            heroID = getArguments().getString(ARG_PARAM1);
        }
        OttoBus.register(this);
        getActivity().bindService(ServiceMedia.getIntentService(getActivity()), serviceConnectionMedia, Service.BIND_AUTO_CREATE);
        getActivity().bindService(DownloadService.getIntent(getActivity()), serviceConnectionPrefetchAudio, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OttoBus.unRegister(this);
        getActivity().unbindService(serviceConnectionMedia);
        getActivity().unbindService(serviceConnectionPrefetchAudio);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_voice, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {

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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {

    }

    private RecyclerView recyclerView;
    private AdapterVoice adapter;
    private List<SpeakDto> list = new ArrayList<>();

    @Override
    public void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.voice_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setHasFixedSize(true);

        adapter = new AdapterVoice(getActivity(), list);
        recyclerView.setAdapter(adapter);

        autoPlay = view.findViewById(R.id.voice_fab);

    }

    @Override
    public void initListener() {

        autoPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaService != null) {
                    if (mediaService.getPlayer().isPlaying()) {

                        mediaService.pause();
                        ((ImageButton)autoPlay).setImageResource(R.mipmap.icon_played);
                    } else {
                        mediaService.play();
                        ((ImageButton)autoPlay).setImageResource(R.mipmap.icon_paused);
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!recyclerView.canScrollVertically(1)) {
//                    onScrolledToEnd();
                } else if (dy < 0) {
//                    onScrolledUp();
                    //hide
//                    ((HeroActivity)getActivity()).floatingActionButton.setVisibility(View.GONE);
                } else if (dy > 0) {
//                    onScrolledDown();
                    //show
//                    ((HeroActivity)getActivity()).floatingActionButton.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        try {
            AObject heroSpeak = FileUtil.getObject(getActivity(), heroID);
            if (heroSpeak != null) {
                Logger.debug(TAG, ">>>" + "heroSpeak != null");
                HeroSpeakSaved heroSpeakSaved = (HeroSpeakSaved) heroSpeak;
                HeroManager.getInstance().getHero(heroID).listSpeaks.clear();
                HeroManager.getInstance().getHero(heroID).listSpeaks.addAll(heroSpeakSaved.listSpeaks);

                list.clear();
                list.addAll(heroSpeakSaved.listSpeaks);
                adapter.notifyDataSetChanged();
                isLoaded = true;
                startPrefetch();
            } else {
                HTTPParseUtils.getInstance().withVoices(heroID);
                HTTPParseUtils.getInstance().setCallback(new HTTPParseUtils.IParseCallBack() {
                    @Override
                    public void onFinish() {
                        Logger.debug(TAG, ">>>" + "withVoices onFinish");
                        list.clear();
                        List<SpeakDto> mList = HeroManager.getInstance().getHero(heroID).listSpeaks;

//                        for (SpeakDto d : mList) {
//                            Logger.debug(TAG, ">>>" + "Text:" + d.text + ";img:" + d.rivalImage);
//                        }
                        if (mediaService != null) {
                            mediaService.setAdapterVoice(adapter, heroID);
                            mediaService.setListData(mList);
                        }
                        list.addAll(mList);
                        adapter.notifyDataSetChanged();
                        isLoaded = true;
                        startPrefetch();
                    }
                });
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }


    DownloadService downloadService;
    boolean isBind = false;
    boolean isLoaded = false;
    //MEDIA MUSIC service
    private ServiceMedia mediaService;
    ServiceConnection serviceConnectionMedia = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceMedia.LocalBinder binder = (ServiceMedia.LocalBinder) service;
            mediaService = binder.getService();
            prepareMedia();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mediaService.stop();
                mediaService = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void prepareMedia() {
        mediaService.setAdapterVoice(adapter, heroID);
        mediaService.setListData(HeroManager.getInstance().getHero(heroID).listSpeaks);

    }

    @Subscribe
    public void listeningAndPlay(SpeakDto speakDto) {
        if (mediaService != null) {
//            mediaService.playSingleLink(speakDto.link);
            mediaService.playSong(speakDto.position, speakDto.heroId, true);
        }

    }

    //prefetch all audio

    ServiceConnection serviceConnectionPrefetchAudio = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            isBind = true;
            startPrefetch();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = true;
        }
    };

    private void startPrefetch() {
        Logger.debug(TAG, ">>>" + "startPrefetch isBind" + isBind + ";isLoaded:" + isLoaded);
        if (isBind && isLoaded && NetworkUtils.isConnected(getActivity())) {
            downloadService.addLinkDto(list, heroID);
        }
    }
}
