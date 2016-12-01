package son.nt.dota2.fragment;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import son.nt.dota2.R;
import son.nt.dota2.activity.HeroActivity;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.HeroTabFragment;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.HeroSpeakSaved;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.dto.VoiceSpinnerItem;
import son.nt.dota2.hero.hero_fragment.AdapterFragmentSkills;
import son.nt.dota2.hero.hero_fragment.AdapterFragmentSound;
import son.nt.dota2.hero.hero_fragment.HeroFragmentPresenter;
import son.nt.dota2.hero.hero_fragment.HeroResponseContract;
import son.nt.dota2.service.DownloadService;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.NetworkUtils;
import son.nt.dota2.utils.OttoBus;
import timber.log.Timber;


public class SwipeHeroFragment extends HeroTabFragment implements HeroResponseContract.View {

    private static final String TAG = SwipeHeroFragment.class.getSimpleName();
    private static final String EXTRA_HERO_ID = "EXTRA_HERO_ID";

    //    private HeroEntry heroEntry;
    DownloadService downloadService;
    boolean isBind = false;
    public FloatingActionButton floatingActionButton;
    String heroID;
    Spinner spinner;
    AppBarLayout appBarLayout;
    Toolbar toolbar;
    TabLayout tabLayout;

    private HeroResponseContract.Presenter mPresenter;

    @BindView(R.id.hero_rcv)
    RecyclerView mRecyclerView;

    @BindView(R.id.skill_rcv)
    RecyclerView mRecyclerViewSkills;

    @BindView(R.id.arcana_voice)
    SwitchCompat mSwitchCompatArcanaVoice;

    private AdapterFragmentSound mAdapter;
    private AdapterFragmentSkills mAdapterSkills;

    private ActionBar mSafeActionBar;

    private String mHeroId;

    public static SwipeHeroFragment newInstance(String heroId) {
        SwipeHeroFragment fragment = new SwipeHeroFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_HERO_ID, heroId);
        fragment.setArguments(args);
        return fragment;
    }

    public SwipeHeroFragment() {
        // Required empty public constructor
//        getActivity().bindService(new Intent(getActivity(), DownloadService.class), serviceConnectionPrefetchAudio, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onPageSelected() {
        Timber.d(">>>" + "onPageSelected");
        final HeroActivity activity = (HeroActivity) getActivity();
        activity.setSoundsList(mPresenter.getSoundsList());
        if (activity.tab.equals("sound")) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerViewSkills.setVisibility(View.GONE);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mRecyclerViewSkills.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageUnSelected() {

    }

    @Override
    public void onDestroy() {
        if (downloadService != null) {
            downloadService.isQuit = true;
            getActivity().unbindService(serviceConnectionPrefetchAudio);
            downloadService = null;
        }
        super.onDestroy();
    }

    @Override
    protected int provideLayoutResID() {
        return R.layout.fragment_swipe_hero;
    }
    //prefetch all audio

    ServiceConnection serviceConnectionPrefetchAudio = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            mPresenter.setFetchServiceBind(true);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = true;
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHeroId = getArguments().getString(EXTRA_HERO_ID);
//        setHasOptionsMenu(true);
        mPresenter = new HeroFragmentPresenter(this, new HeroRepository());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        mPresenter.fetchBasicHeroFromHeroId(mHeroId);
        mPresenter.getAbi();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    protected ActionBar getSafeActionBar() {
        if (mSafeActionBar == null) {
            mSafeActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        }
        return mSafeActionBar;
    }


    public void initLayout(View view) {
//        if (heroEntry == null) {
//            return;
//        }
//        listKenburns.clear();
//        listKenburns.add(heroEntry.bgLink);
//        listKenburns.add(heroEntry.bgLink);

//        spinner = (Spinner) view.findViewById(R.id.hero_spinner);
////        updateKenBurns();
//        appBarLayout = (AppBarLayout) view.findViewById(R.id.hero_appbarlayout);
//        toolbar = (Toolbar) view.findViewById(R.id.hero_toolbar);
//        tabLayout = (TabLayout) view.findViewById(R.id.hero_tablayout);
////        pager = (ViewPager) view.findViewById(R.id.hero_pager);
////        adapter = new AdapterPagerHero(getFragmentManager(), new ArrayList<>());
////        pager.setAdapter(adapter);
////        tabLayout.setupWithViewPager(pager);
//        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.hero_fab);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        setupSpinner();

//        recyclerView = (RecyclerView) view.findViewById(R.id.voice_recycleview);
        LinearLayoutManager linearLayoutManagerSkill = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerViewSkills.setLayoutManager(linearLayoutManagerSkill);
        mRecyclerViewSkills.setHasFixedSize(true);
        mAdapterSkills = new AdapterFragmentSkills(getActivity(), new ArrayList<>());
        mRecyclerViewSkills.setAdapter(mAdapterSkills);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new AdapterFragmentSound(getActivity(), new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);

        mSwitchCompatArcanaVoice.setOnCheckedChangeListener((buttonView, isChecked) -> mAdapter.setArcana(isChecked));
    }

    public void initListener() {
        getSafeActionBar().setTitle(mHeroId);
        getSafeActionBar().setDisplayShowTitleEnabled(true);

        getSafeActionBar().setDisplayShowHomeEnabled(true);
        getSafeActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == android.R.id.home) {
                getActivity().finish();
            }
            return true;
        });

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
//            toolbar.setTitle(heroEntry.fullName);

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

    @Override
    public void showHeroSoundsList(List<HeroResponsesDto> list) {
        Timber.d(">>>" + "showHeroSoundsList:" + list.size() + ";ID:" + mHeroId);
        mAdapter.setData(list);
    }

    @Override
    public void addDataToDownload(List<HeroResponsesDto> heroResponsesDtos, String heroID) {
        if (!NetworkUtils.isConnected(getActivity())) {
            return;
        }

//        downloadService.addLinkDto2(heroResponsesDtos, heroID);
    }

    @Override
    public void updateArcana(boolean arcana) {
        mAdapter.setArcana(arcana);
        if (arcana) {
            mSwitchCompatArcanaVoice.setVisibility(View.VISIBLE);
            mSwitchCompatArcanaVoice.setChecked(true);
        } else {
            mSwitchCompatArcanaVoice.setVisibility(View.GONE);
        }
    }

    @Override
    public void updateAbi(List<AbilitySoundDto> data) {
        mAdapterSkills.setData(data);
    }
}
