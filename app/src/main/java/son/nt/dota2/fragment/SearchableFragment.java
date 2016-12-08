package son.nt.dota2.fragment;

import android.app.Activity;
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

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSearchHero;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.data.IHeroRepository;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.provider.SearchableProvider;


public class SearchableFragment extends AFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String query;

    private OnFragmentInteractionListener mListener;

    RecyclerView recyclerView;
    AdapterSearchHero adapterSearchHero;
    List<HeroBasicDto> list = new ArrayList<>();

    CoordinatorLayout coordinatorLayout;

    IHeroRepository mRepository;

    Subscription subscription;


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
        }

        mRepository = new HeroRepository();
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
        mListener = null;
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDetach();
    }


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    private void initLayout(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.search_rcv);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapterSearchHero = new AdapterSearchHero(getActivity(), list, null);
        recyclerView.setAdapter(adapterSearchHero);
        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.search_coordinator_layout);
        doSearch(query);
    }

    public void doSearch(String query) {
        setTitle("Search for:" + query);
        list.clear();
//        for (HeroEntry dto : HeroManager.getInstance().listHeroes) {
//            if (dto.name.toLowerCase().contains(query.toLowerCase()) ||
//                    dto.fullName.toLowerCase().contains(query.toLowerCase())) {
//                list.add(dto);
//            }
//        }

        subscription = mRepository.searchHero(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<HeroBasicDto>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<HeroBasicDto> listData) {
                        if (!listData.isEmpty()) {
                            SearchableProvider.saveQuery(getActivity(), query);
                            list.clear();
                            list.addAll(listData);

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
                        } else if (coordinatorLayout.findViewWithTag("not-found") != null) {
                            coordinatorLayout.removeView(coordinatorLayout.findViewWithTag("not-found"));
                        }
                    }
                });

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
