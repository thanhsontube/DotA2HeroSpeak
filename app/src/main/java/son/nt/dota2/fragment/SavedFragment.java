package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
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

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSaved;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.data.TsSqlite;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.ottobus_entry.GoSaved;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.OttoBus;
import son.nt.dota2.utils.TsLog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SavedFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SavedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SavedFragment extends AFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = "SavedFragment";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;
    private AdapterSaved adapter;

    private List<SpeakDto> listSave = new ArrayList<>();

    TsLog log = new TsLog(TAG);

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SavedFragment.
     */
    // TODO: Rename and change types and number of parameters
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
        initListener();
        updateLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        listSave.clear();
        listSave.addAll(TsSqlite.getInstance().getPlaylist());
        adapter.notifyDataSetChanged();

    }

    private void initLayout (View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.saved_recycle_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterSaved(context, listSave);
        recyclerView.setAdapter(adapter);

    }

    private void initListener() {
        adapter.setOnCallback(new AdapterSaved.IAdapterCallback() {
            @Override
            public void onClick(int position, SpeakDto dto) {
                if (mediaService != null) {
                    mediaService.play(dto.link, dto.heroId);
                }
            }

            @Override
            public void onLongClick(int position, SpeakDto dto) {
                if (mListener != null) {
                    mListener.onSavedItemLongClick(dto);
                }
            }

            @Override
            public void onMenuClick(MsConst.MenuSelect action, int position) {

            }
        });

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
        listSave.addAll(TsSqlite.getInstance().getPlaylist());
        adapter.notifyDataSetChanged();
    }
}
