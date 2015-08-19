package son.nt.dota2.comments;

import android.app.Dialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.CommentManager;
import son.nt.dota2.R;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.ottobus_entry.GoAdapterCmt;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.NetworkUtils;
import son.nt.dota2.utils.OttoBus;

/**
 * Created by Sonnt on 8/12/15.
 */
public class ChatDialog extends DialogFragment {
    RecyclerView recyclerView;
    AdapterCmts adapterCmts;
    List<CommentDto> listValues = new ArrayList<>();
    View viewLoading;
    TextView viewRefresh;
    SwipeRefreshLayout swipeRefreshLayout;

    ServiceMedia serviceMedia;
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
    public static ChatDialog newInstance() {
        ChatDialog f = new ChatDialog();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().bindService(ServiceMedia.getIntentService(getActivity()), serviceConnection, Service.BIND_AUTO_CREATE);
        OttoBus.register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
        OttoBus.unRegister(this);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.chat_layout, null);
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycle_view);
        viewLoading = view.findViewById(R.id.chat_loading);
        viewRefresh = (TextView) view.findViewById(R.id.chat_refresh);
        recyclerView.setVisibility(View.VISIBLE);
        viewRefresh.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        listValues.clear();
        listValues.addAll(CommentManager.getInstance().listCmts);


        adapterCmts = new AdapterCmts(getActivity(), listValues);
        recyclerView.setAdapter(adapterCmts);

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        if (listValues.size() > 3) {

            recyclerView.smoothScrollToPosition(listValues.size() -2);
        }
        if (listValues.size() == 0) {

        getData();
        }
        return alertDialog;

    }

    private void getData() {
        recyclerView.setVisibility(View.GONE);
        viewRefresh.setVisibility(View.GONE);
        viewLoading.setVisibility(View.VISIBLE);
        if (!NetworkUtils.isConnected(getActivity())) {
            recyclerView.setVisibility(View.GONE);
            viewRefresh.setVisibility(View.VISIBLE);
            viewRefresh.setText("No Network Connection !\n\r" +
                    " Click to reload");
            viewLoading.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(CommentDto.class.getSimpleName());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                CommentDto commentDto;
                List<CommentDto> listCmts = new ArrayList<CommentDto>();
                for (ParseObject p : list) {

                    String message = p.getString("message");
                    String fromID = p.getString("fromID");
                    String fromName = p.getString("fromName");
                    long createTime = p.getLong("createTime");
                    String image = p.getString("fromImage");

                    String heroText = p.getString("heroText");
                    String heroLink = p.getString("heroLink");
                    String heroID = p.getString("heroID");
                    String heroGroup = p.getString("heroGroup");

                    commentDto = new CommentDto();
                    commentDto.setMessage(message);
                    commentDto.setFromID(fromID);
                    commentDto.setFromName(fromName);
                    commentDto.setImage(image);
                    commentDto.setCreateTime(createTime);

                    SpeakDto speakDto = new SpeakDto();
                    speakDto.heroId = heroID;
                    speakDto.text = heroText;
                    speakDto.voiceGroup = heroGroup;
                    speakDto.link = heroLink;

                    commentDto.setSpeakDto(speakDto);
                    listCmts.add(commentDto);
                }
                swipeRefreshLayout.setRefreshing(false);
                if (listCmts.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    viewRefresh.setVisibility(View.VISIBLE);
                    viewRefresh.setText("No comment on this Hero :( \n\r Click to reload");
                    viewLoading.setVisibility(View.GONE);
                } else {

                    listValues.clear();
                    listValues.addAll(listCmts);
                    adapterCmts.notifyDataSetChanged();

                    CommentManager.getInstance().listCmts.clear();
                    CommentManager.getInstance().listCmts.addAll(listValues);

                    recyclerView.setVisibility(View.VISIBLE);
                    viewRefresh.setVisibility(View.GONE);
                    viewLoading.setVisibility(View.GONE);
                }
                if (listValues.size() > 3) {

                    recyclerView.smoothScrollToPosition(listValues.size() -2);
                }


            }
        });
    }

    @Subscribe
    public void getDataAdapter (GoAdapterCmt dto) {
        if (serviceMedia != null) {
            serviceMedia.play(dto.link, dto.heroID);
        }
    }
}
