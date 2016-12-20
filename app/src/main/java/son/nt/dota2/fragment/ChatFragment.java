package son.nt.dota2.fragment;

import com.squareup.otto.Subscribe;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.comments.AdapterCmts;
import son.nt.dota2.comments.CommentDto;
import son.nt.dota2.ottobus_entry.GoAdapterCmt;
import son.nt.dota2.ottobus_entry.GoChatFragment;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.OttoBus;

public class ChatFragment extends AbsFragment {
    private static final String ARG_PARAM1 = "param1";

    private String heroID;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    AdapterCmts adapterCmts;
    List<CommentDto> listValues = new ArrayList<>();

    View viewLoading;
    TextView viewRefresh;
    SwipeRefreshLayout swipeRefreshLayout;
    ServiceMedia serviceMedia;
    View chatLL;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            ServiceMedia.LocalBinder localBinder = (ServiceMedia.LocalBinder) binder;
            serviceMedia = localBinder.getService();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceMedia = null;

        }
    };


    public static ChatFragment newInstance(String param1) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            heroID = getArguments().getString(ARG_PARAM1);
        }
        getActivity().bindService(ServiceMedia.getIntentService(getActivity()), serviceConnection, Service.BIND_AUTO_CREATE);
        OttoBus.register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
        OttoBus.unRegister(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.chat_layout, container, false);
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
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {


    }

    @Override
    public void initLayout(View view) {
        chatLL = view.findViewById(R.id.chat_ll);
        chatLL.setBackgroundColor(getResources().getColor(R.color.transparent));
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapterCmts = new AdapterCmts(getActivity(), listValues);
        recyclerView.setAdapter(adapterCmts);

        viewLoading = view.findViewById(R.id.chat_loading);
        viewRefresh = (TextView) view.findViewById(R.id.chat_refresh);

        viewRefresh = (TextView) view.findViewById(R.id.chat_refresh);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.chat_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int topRowVerticalPosition = (recyclerView == null || recyclerView.getChildCount() == 0) ? 0
                        : recyclerView.getChildAt(0).getTop();
                swipeRefreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    @Override
    public void initListener() {
        viewRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

    }

    private void getData() {

    }

    @Subscribe
    public void getDataAdapter(GoAdapterCmt dto) {
        if (serviceMedia != null) {
            serviceMedia.play(dto.link, dto.heroID);
        }
    }

    //sub update the view after user comment
    @Subscribe
    public void updateListCmt(GoChatFragment dto) {
        if (dto.heroID.equals(heroID)) {
            getData();
        }
    }
}
