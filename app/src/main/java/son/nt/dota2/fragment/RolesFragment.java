package son.nt.dota2.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterRoles;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.save.SaveRoles;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.utils.FileUtil;


public class RolesFragment extends AbsFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    List<RoleDto> list = new ArrayList<>();
    AdapterRoles adapterRoles;

    public static RolesFragment newInstance(String param1, String param2) {
        RolesFragment fragment = new RolesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RolesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        getSafeActionBar().setTitle("Hero Roles");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_roles, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        update();
    }

    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {
        adapterRoles = new AdapterRoles(getActivity(), list);
    }

    @Override
    public void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.roles_rcv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterRoles);
    }

    @Override
    public void initListener() {

    }

    private void update() {
        try
        {
            AObject aObject = FileUtil.getObject(getActivity(), SaveRoles.class.getSimpleName());
            if (aObject != null) {
                SaveRoles saveRoles = (SaveRoles) aObject;
                list.clear();
                list.addAll(saveRoles.list);
                adapterRoles.notifyDataSetChanged();
                return;
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery(RoleDto.class.getSimpleName());
            query.addAscendingOrder("no");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> l, ParseException e) {
                    if (e != null) {
                        return;
                    }
                    list.clear();
                    RoleDto dto;
                    for (ParseObject p :l) {
                        dto = new RoleDto();
                        dto.name = p.getString("name");
                        dto.slogan = p.getString("slogan");
                        dto.linkIcon = p.getString("linkIcon");
                        list.add(dto);
                    }
                    try {
                        FileUtil.saveObject(getActivity(),new SaveRoles(list),SaveRoles.class.getSimpleName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                    adapterRoles.notifyDataSetChanged();
                }
            });

        } catch (Exception e) {

        }

    }
}
