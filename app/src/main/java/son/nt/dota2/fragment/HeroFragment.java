package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterPagerHero;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.customview.KenBurnsView;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HeroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HeroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HeroFragment extends AbsFragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "HeroFragment";

    // TODO: Rename and change types of parameters
    private HeroEntry heroEntry;

    private OnFragmentInteractionListener mListener;

    private AdapterPagerHero adapter;
    private List<android.support.v4.app.Fragment> listFragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    public FloatingActionButton floatingActionButton;

    KenBurnsView kenBurnsView;
    private List<String> listKenburns = new ArrayList<>();

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HeroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HeroFragment newInstance(HeroEntry param1) {
        HeroFragment fragment = new HeroFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HeroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            heroEntry = (HeroEntry) getArguments().getSerializable(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hero, container, false);
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {
        Logger.debug(TAG, ">>>" + "initData:" + heroEntry);
        if (heroEntry != null) {
            Logger.debug(TAG, ">>>" + "initData with:" + heroEntry.heroId);
        }
        titles.clear();
        listFragments.clear();
        titles.add("Introduce");
        listFragments.add(IntroFragment.newInstance(heroEntry.heroId));
        titles.add("Ability");
        listFragments.add(AbilityFragment.newInstance(heroEntry.heroId));
        titles.add("Voice");
        listFragments.add( VoiceFragment.newInstance(heroEntry.heroId));

        adapter = new AdapterPagerHero(getSafeFragmentManager(), listFragments, titles);

    }

    CoordinatorLayout coordinatorLayout;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;

    @Override
    public void initLayout(View view) {
        //kenburns
        kenBurnsView = (KenBurnsView) view.findViewById(R.id.hero_ken_burns);
        int[] ids = new int[]{R.mipmap.ken2, R.mipmap.ken2};
        kenBurnsView.setResourceIds(ids);
        kenBurnsView.startLayoutAnimation();

        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.hero_coordinator);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.hero_appbarlayout);
        toolbar = (Toolbar) view.findViewById(R.id.hero_toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.hero_tablayout);
        pager = (ViewPager) view.findViewById(R.id.hero_pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.hero_fab);

        getKenBurnsImage();
    }

    @Override
    public void initListener() {

    }


    private void getKenBurnsImage () {
        Logger.debug(TAG, ">>>" + "getKenBurnsImage");
        try {
            ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Dota2BgDto");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> list, ParseException e) {
                    listKenburns.clear();
                    for (ParseObject p : list) {
                        String s = p.getString("link");
                        listKenburns.add(s);

                    }
                    updateKenBurns();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateKenBurns() {
        kenBurnsView.setResourceUrl(listKenburns);
        kenBurnsView.startLayoutAnimation();
    }
}
