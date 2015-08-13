package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.comments.AdapterCmts;
import son.nt.dota2.comments.CommentDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.utils.NetworkUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends AbsFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String heroID;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    AdapterCmts adapterCmts;
    List<CommentDto> listValues = new ArrayList<>();

    View viewLoading;
    TextView viewRefresh;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ChatFragment.
     */
    // TODO: Rename and change types and number of parameters
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chat_layout, container, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

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

    @Override
    public void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.chat_recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        adapterCmts = new AdapterCmts(getActivity(), listValues);
        recyclerView.setAdapter(adapterCmts);

        viewLoading = view.findViewById(R.id.chat_loading);
        viewRefresh = (TextView) view.findViewById(R.id.chat_refresh);

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
//        listValues.clear();
//        listValues.addAll(ChatHistoryManager.getInstance().getHeroHistory(heroID));
//        if (listValues.size() > 0) {
//            adapterCmts.notifyDataSetChanged();
//            return;
//        }

//        ChatHistoryManager.getInstance().getHistory(null);


        recyclerView.setVisibility(View.GONE);
        viewRefresh.setVisibility(View.GONE);
        viewLoading.setVisibility(View.VISIBLE);
        if (!NetworkUtils.isConnected(getActivity())) {
            recyclerView.setVisibility(View.GONE);
            viewRefresh.setVisibility(View.VISIBLE);
            viewRefresh.setText("No Network Connection !\n\r" +
                    " Click to reload");
            viewLoading.setVisibility(View.GONE);
        }

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(CommentDto.class.getSimpleName());
        query.whereEqualTo("heroID", heroID);
        query.setLimit(200);
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

                    String heroText = p.getString("heroText");
                    String heroLink = p.getString("heroLink");
                    String heroID = p.getString("heroID");
                    String heroGroup = p.getString("heroGroup");

                    commentDto = new CommentDto();
                    commentDto.setMessage(message);

                    commentDto.setFromID(fromID);
                    commentDto.setFromName(fromName);
                    commentDto.setCreateTime(createTime);

                    SpeakDto speakDto = new SpeakDto();
                    speakDto.heroId = heroID;
                    speakDto.text = heroText;
                    speakDto.voiceGroup = heroGroup;
                    speakDto.link = heroLink;

                    commentDto.setSpeakDto(speakDto);
                    listCmts.add(commentDto);
                }
                if (listCmts.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    viewRefresh.setVisibility(View.VISIBLE);
                    viewRefresh.setText("No comment on this Hero :( \n\r Click to reload");
                    viewLoading.setVisibility(View.GONE);
                } else {

                    listValues.clear();
                    listValues.addAll(listCmts);
                    adapterCmts.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    viewRefresh.setVisibility(View.GONE);
                    viewLoading.setVisibility(View.GONE);
                }


            }
        });
    }

//    @Subscribe
//    public void getChatHistory (GoChatManager dto) {
//        listValues.clear();
//        listValues.addAll(ChatHistoryManager.getInstance().getHeroHistory(heroID));
//        if (listValues.size() > 0) {
//            adapterCmts.notifyDataSetChanged();
//        }
//    }
}
