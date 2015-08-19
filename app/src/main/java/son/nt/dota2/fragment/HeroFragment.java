package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterPagerHero;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.customview.KenBurnsView;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.VoiceSpinnerItem;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.OttoBus;

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
    String heroID;

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
            heroID = heroEntry.heroId;
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hero, container, false);
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
        titles.add("Voice");
        listFragments.add(VoiceFragment.newInstance(heroEntry.heroId));
//        titles.add("Ability");
//        listFragments.add(AbilityFragment.newInstance(heroEntry.heroId));
//        titles.add("Introduce");
//        listFragments.add(IntroFragment.newInstance(heroEntry.heroId));
//
//        titles.add("Comments");
//        listFragments.add(ChatFragment.newInstance(heroEntry.heroId));

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
        listKenburns.clear();
        listKenburns.add(heroEntry.bgLink);
        listKenburns.add(heroEntry.bgLink);
        updateKenBurns();
//        coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.hero_coordinator);
        appBarLayout = (AppBarLayout) view.findViewById(R.id.hero_appbarlayout);
        toolbar = (Toolbar) view.findViewById(R.id.hero_toolbar);
        tabLayout = (TabLayout) view.findViewById(R.id.hero_tablayout);
        pager = (ViewPager) view.findViewById(R.id.hero_pager);
        pager.setAdapter(adapter);
        tabLayout.setupWithViewPager(pager);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.hero_fab);
        getSaveActivity().setSupportActionBar(toolbar);
        setupSpinner();
    }

    @Override
    public void initListener() {
        getSafeActionBar().setTitle(heroEntry.name);
        getSafeActionBar().setDisplayShowTitleEnabled(true);

        getSafeActionBar().setDisplayShowHomeEnabled(true);
        getSafeActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == android.R.id.home) {
                    getActivity().finish();
                }
                return true;
            }
        });

    }

    private void updateKenBurns() {
        kenBurnsView.setResourceUrl(listKenburns);
        kenBurnsView.startLayoutAnimation();
    }

    VoiceSpinnerAdapter adapterSpinner;
    List<VoiceSpinnerItem> listSpinner;

    private void setupSpinner() {

        AObject heroSpeak = null;
        try {
            heroSpeak = FileUtil.getObject(getActivity(), "voice_" +heroID);
        } catch ( Exception e ) {


        }
        Logger.debug(TAG, ">>>" + "setupSpinner:" + heroSpeak);
        if (heroSpeak != null) {
            Logger.debug(TAG, ">>>" + "heroSpeak != null");
            HeroSpeakSaved heroSpeakSaved = (HeroSpeakSaved) heroSpeak;

            listSpinner = new ArrayList<>();
            listSpinner.add(new VoiceSpinnerItem("ALL", true));
            for (SpeakDto d : heroSpeakSaved.listSpeaks) {
                if (d.isTitle) {
                    listSpinner.add(new VoiceSpinnerItem(d.text.replace("_", " ").trim()));
                }
            }

            adapterSpinner = new VoiceSpinnerAdapter(getActivity(), listSpinner);

            View spinnerContainer = LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_spiner, toolbar, false);
            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            toolbar.addView(spinnerContainer, lp);

            TextView toolbarTitle = (TextView) spinnerContainer.findViewById(R.id.toolbar_title);
            toolbarTitle.setText(heroEntry.fullName);

            AppCompatSpinner spinner = (AppCompatSpinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
            spinner.setAdapter(adapterSpinner);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> spinner, View view, int position, long itemId) {
                    for (VoiceSpinnerItem p : listSpinner) {
                        p.setIsSelected(false);
                    }
                    listSpinner.get(position).setIsSelected(true);
                    adapterSpinner.notifyDataSetChanged();
                    OttoBus.post(listSpinner.get(position));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

        }


    }

    private class VoiceSpinnerAdapter extends BaseAdapter {
        List<VoiceSpinnerItem> list;
        Context context;

        public VoiceSpinnerAdapter(Context context, List<VoiceSpinnerItem> list) {
            this.list = list;
            this.context = context;

        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Holder holder;
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.row_spinner_toolbar, parent, false);
                holder = new Holder();
                holder.txtTitle = (TextView) v.findViewById(R.id.row_spinner_text);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }
            holder.txtTitle.setText(list.get(position).getGroup());
            holder.txtTitle.setTextColor(getResources().getColor(R.color.md_red_500));
            return v;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Holder holder;
            if (v == null) {
                LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = layoutInflater.inflate(R.layout.row_spinner_toolbar, parent, false);
                holder = new Holder();
                holder.txtTitle = (TextView) v.findViewById(R.id.row_spinner_text);
                holder.txtTitle.setTextColor(getResources().getColor(R.color.md_red_500));
                holder.imgSelected = (ImageView) v.findViewById(R.id.row_spinner_img);
                v.setTag(holder);
            } else {
                holder = (Holder) v.getTag();
            }

            holder.txtTitle.setText(list.get(position).getGroup());
            if (list.get(position).isSelected()) {
                holder.imgSelected.setVisibility(View.VISIBLE);
            } else {
                holder.imgSelected.setVisibility(View.GONE);

            }
            return v;
        }

        class Holder {
            TextView txtTitle;
            ImageView imgSelected;
        }
    }
}
