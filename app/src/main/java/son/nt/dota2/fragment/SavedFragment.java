package son.nt.dota2.fragment;

import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.hero.hero_fragment.AdapterFragmentSound;
import son.nt.dota2.ottobus_entry.GoSaved;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsLog;


public class SavedFragment extends AFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SavedFragment";


    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private AdapterFragmentSound adapter;

    private List<HeroResponsesDto> listSave = new ArrayList<>();

    TsLog log = new TsLog(TAG);

    private OnFragmentInteractionListener mListener;


    public static SavedFragment newInstance(String param1, String param2) {
        SavedFragment fragment = new SavedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SavedFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        context.bindService(new Intent(context, ServiceMedia.class), serviceConnectionMedia, Service.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSafeActionBar().setTitle("My Playlist");
        OttoBus.register(this);
        initData();
        initLayout(view);
        updateLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        listSave.clear();
        adapter.notifyDataSetChanged();

    }

    private void initLayout (View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.saved_recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterFragmentSound(context, listSave);
        recyclerView.setAdapter(adapter);

    }

    private void initData() {


    }

    private void updateLayout() {

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

    public interface OnFragmentInteractionListener {
         void onFragmentInteraction(Uri uri);
         void onSavedItemLongClick (SpeakDto dto);
    }

    //MEDIA MUSIC service
    private ServiceMedia mediaService;
    ServiceConnection serviceConnectionMedia = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ServiceMedia.LocalBinder binder = (ServiceMedia.LocalBinder) service;
            mediaService = binder.getService();
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

    @Override
    public void onDestroy() {
        OttoBus.unRegister(this);
        super.onDestroy();
        if (mediaService != null) {
            context.unbindService(serviceConnectionMedia);
        }
    }
    @Subscribe
    public void onUpdate (GoSaved dto) {
        listSave.clear();
        adapter.notifyDataSetChanged();
    }
}
