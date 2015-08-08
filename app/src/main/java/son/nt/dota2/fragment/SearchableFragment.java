package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

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
 * {@link SearchableFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchableFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchableFragment extends AFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String query;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    AdapterSearchHero adapterSearchHero;
    List<HeroEntry> list = new ArrayList<>();

    CoordinatorLayout coordinatorLayout;
    JazzyRecyclerViewScrollListener jazzyRecyclerViewScrollListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchableFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchableFragment newInstance(String param1, String param2) {
        SearchableFragment fragment = new SearchableFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SearchableFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            query = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        doSearch(query);
    }

    public void doSearch(String query) {
        setTitle("Search for:" + query);
        list.clear();
        for (HeroEntry dto : HeroManager.getInstance().listHeroes) {
            if (dto.name.toLowerCase().contains(query.toLowerCase())||
                    dto.fullName.toLowerCase().contains(query.toLowerCase())) {
                list.add(dto);
            }
        }
        if(!list.isEmpty()) {
            SearchableProvider.saveQuery(getActivity(), query);
        }
        adapterSearchHero.notifyDataSetChanged();

        recyclerView.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
        if (list.isEmpty()) {
            TextView textView = new TextView(getActivity());
            textView.setText(getString(R.string.not_found_hero));
            textView.setTag("not-found");
            textView.setTextColor(getResources().getColor(R.color.black));
            textView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            coordinatorLayout.addView(textView);
        } else if (coordinatorLayout.findViewWithTag("not-found") != null){
            coordinatorLayout.removeView(coordinatorLayout.findViewWithTag("not-found"));
        }
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
