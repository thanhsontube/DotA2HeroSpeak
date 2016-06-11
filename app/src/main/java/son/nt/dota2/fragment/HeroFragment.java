package son.nt.dota2.fragment;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
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


public class HeroFragment extends AbsFragment {

    private static final String TAG = HeroFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";

    private HeroEntry heroEntry;

    private OnFragmentInteractionListener mListener;


    private List<android.support.v4.app.Fragment> listFragments = new ArrayList<>();
    private ArrayList<String> titles = new ArrayList<>();
    public FloatingActionButton floatingActionButton;

    KenBurnsView kenBurnsView;
    private List<String> listKenburns = new ArrayList<>();
    String heroID;

    Spinner spinner;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;
    ViewPager pager;
    private AdapterPagerHero adapter;

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
        titles.add("Skills");
        listFragments.add(AbilityFragment.newInstance(heroEntry.heroId));
//        titles.add("BIO");
//        listFragments.add(IntroFragment.newInstance(heroEntry.heroId));
        titles.add("Comments");
        listFragments.add(ChatFragment.newInstance(heroEntry.heroId));

        adapter = new AdapterPagerHero(getSafeFragmentManager(), listFragments, titles);

    }


    @Override
    public void initLayout(View view) {
        //kenburns
        kenBurnsView = (KenBurnsView) view.findViewById(R.id.hero_ken_burns);
        listKenburns.clear();
        listKenburns.add(heroEntry.bgLink);
        listKenburns.add(heroEntry.bgLink);

        spinner = (Spinner) view.findViewById(R.id.hero_spinner);
        updateKenBurns();
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
            heroSpeak = FileUtil.getObject(getActivity(), "voice_" + heroID);
        } catch (Exception e) {


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

//            View spinnerContainer = LayoutInflater.from(getActivity()).inflate(R.layout.toolbar_spiner, toolbar, false);
//            Toolbar.LayoutParams lp = new Toolbar.LayoutParams(
//                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            toolbar.addView(spinnerContainer, lp);

//            TextView toolbarTitle = (TextView) spinnerContainer.findViewById(R.id.toolbar_title);
//            toolbarTitle.setText(heroEntry.fullName);
            toolbar.setTitle(heroEntry.fullName);

//            AppCompatSpinner spinner = (AppCompatSpinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
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
            holder.txtTitle.setTextColor(getResources().getColor(R.color.white));
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
                holder.txtTitle.setTextColor(getResources().getColor(R.color.black));
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
