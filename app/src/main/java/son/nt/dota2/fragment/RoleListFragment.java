package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.twotoasters.jazzylistview.JazzyHelper;
import com.twotoasters.jazzylistview.recyclerview.JazzyRecyclerViewScrollListener;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSearchHero;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.provider.SearchableProvider;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RoleListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RoleListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RoleListFragment extends AFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String role;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    AdapterSearchHero adapterSearchHero;
    List<HeroEntry> list = new ArrayList<>();

    CoordinatorLayout coordinatorLayout;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    public static RoleListFragment newInstance(String role) {
        RoleListFragment fragment = new RoleListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, role);
        fragment.setArguments(args);
        return fragment;
    }

    public RoleListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            role = getArguments().getString(ARG_PARAM1);
            role.trim();
            list.clear();
            list.addAll(HeroManager.getInstance().getHeroesByRole(role));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_searchable, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSafeActionBar().setTitle(role + " Heroes");
        initLayout(view);
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
        public void onFragmentInteraction(Uri uri);
    }

    private void initLayout (View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.search_rcv);
        recyclerView.setHasFixedSize(true);
        jazzyRecyclerViewScrollListener = new JazzyRecyclerViewScrollListener();
        jazzyRecyclerViewScrollListener.setTransitionEffect(JazzyHelper.CURL);
        recyclerView.addOnScrollListener(jazzyRecyclerViewScrollListener);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterSearchHero = new AdapterSearchHero(getActivity(), list);
        recyclerView.setAdapter(adapterSearchHero);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.search_coordinator_layout);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_search_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                SearchableProvider.clearHistory(getActivity());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
