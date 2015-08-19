package son.nt.dota2.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.melnykov.fab.ObservableScrollView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.AbilityItemAffectDto;
import son.nt.dota2.dto.AbilityLevelDto;
import son.nt.dota2.dto.AbilityNotesDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.dto.save.SaveHeroAbility;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;

public class AbilityFragment extends AbsFragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = AbsFragment.class.getSimpleName();
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private List<AbilityDto> listAbilities = new ArrayList<>();
    private String heroId;
    private LayoutInflater inflater;
    private TextView txtAbility, txtAffects, txtDamage;
    private TextView txtDescription;

    private LinearLayout viewItemAffects;
    private LinearLayout viewAbilityPerLevel;

    private TextView txtNotes;

    ServiceMedia serviceMedia;

    com.melnykov.fab.FloatingActionButton fabMuted;

    public static AbilityFragment newInstance(String heroID) {
        AbilityFragment fragment = new AbilityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, heroID);
        fragment.setArguments(args);
        return fragment;
    }

    public AbilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getArguments() != null) {
            heroId = getArguments().getString(ARG_PARAM1);
        }
//        getSafeActionBar().setTitle(heroEntry.name);
        getActivity().bindService(ServiceMedia.getIntentService(getActivity()), serviceConnection, Service.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(serviceConnection);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ability, container, false);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {


    }

    @Override
    public void initLayout(View view) {
        fabMuted = (com.melnykov.fab.FloatingActionButton) view.findViewById(R.id.ability_muted);
        ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.ability_scroll_view);
        fabMuted.attachToScrollView(scrollView);

        tabLayout = (TabLayout) view.findViewById(R.id.ability_tab_layout);
        txtAbility = (TextView) view.findViewById(R.id.ability_abi);
        txtAffects = (TextView) view.findViewById(R.id.ability_aff);
        txtDamage = (TextView) view.findViewById(R.id.ability_damage);
        txtDescription = (TextView) view.findViewById(R.id.ability_description);
        txtNotes = (TextView) view.findViewById(R.id.ability_txt_notes);
        viewItemAffects = (LinearLayout) view.findViewById(R.id.ability_items_affects);
        viewAbilityPerLevel = (LinearLayout) view.findViewById(R.id.ability_level);


        HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
        if (heroEntry == null || heroEntry.listAbilities.size() == 0) {
            return;
        }

        for (AbilityDto dto : heroEntry.listAbilities) {
            View vTab = inflater.inflate(R.layout.tab_ability, null);
            ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
            TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
            txt.setText(dto.name);
            Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
            TabLayout.Tab tab = tabLayout.newTab().setCustomView(vTab);
            tabLayout.addTab(tab);
        }
        update(0);

    }

    private void update(int position) {
        HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
        if (heroEntry == null || heroEntry.listAbilities.size() == 0) {
            return;
        }

        AbilityDto dto = heroEntry.listAbilities.get(position);
        txtAbility.setText(heroEntry.listAbilities.get(position).ability);
        txtAffects.setText(heroEntry.listAbilities.get(position).affects);
        txtDamage.setText(heroEntry.listAbilities.get(position).damage);
        txtDescription.setText(heroEntry.listAbilities.get(position).description);
        txtDescription.setVisibility(TextUtils.isEmpty(heroEntry.listAbilities.get(position).description) ? View.GONE : View.VISIBLE);


        StringBuilder notes = new StringBuilder();
        for (AbilityNotesDto s : dto.listNotes) {
            notes.append(s.notes);
            notes.append("\n\r\n\r");
        }

        txtNotes.setText(notes.toString());

        viewItemAffects.removeAllViews();
        for (AbilityItemAffectDto d : dto.listItemAffects) {
            View row = inflater.inflate(R.layout.row_item_affects, null);
            ((TextView) row.findViewById(R.id.row_text)).setText(d.text);
            if (!TextUtils.isEmpty(d.alt)) {
                ((TextView) row.findViewById(R.id.alt)).setVisibility(View.VISIBLE);
                ((TextView) row.findViewById(R.id.alt)).setText(d.alt);
            } else {
                ((TextView) row.findViewById(R.id.alt)).setVisibility(View.GONE);
            }
            ImageView src = (ImageView) row.findViewById(R.id.src);
            Glide.with(src.getContext()).load(d.src).fitCenter().into(src);
            if (!TextUtils.isEmpty(d.text)) {
                viewItemAffects.addView(row);
            }
        }

        viewAbilityPerLevel.removeAllViews();
        for (AbilityLevelDto d : dto.listAbilityPerLevel) {
            View row = inflater.inflate(R.layout.row_ability_level, null);
            TextView txtLevelName = (TextView) row.findViewById(R.id.level_name);
            TextView txtLevel1 = (TextView) row.findViewById(R.id.level1_1);
            TextView txtLevel2 = (TextView) row.findViewById(R.id.level1_2);
            TextView txtLevel3 = (TextView) row.findViewById(R.id.level1_3);
            TextView txtLevel4 = (TextView) row.findViewById(R.id.level1_4);

            txtLevel1.setTextColor(getResources().getColor(R.color.md_white_1000));
            txtLevel2.setTextColor(getResources().getColor(R.color.md_white_1000));
            txtLevel3.setTextColor(getResources().getColor(R.color.md_white_1000));
            txtLevel4.setTextColor(getResources().getColor(R.color.md_white_1000));

            txtLevelName.setText(d.name);

            if (TextUtils.isEmpty(d.name)) {
                txtLevel1.setTextColor(getResources().getColor(R.color.md_yellow_A700));
                txtLevel2.setTextColor(getResources().getColor(R.color.md_yellow_A700));
                txtLevel3.setTextColor(getResources().getColor(R.color.md_yellow_A700));
                txtLevel4.setTextColor(getResources().getColor(R.color.md_yellow_A700));
            }

            if (d.name.toLowerCase().contains("cooldown")) {
                txtLevel1.setTextColor(getResources().getColor(R.color.md_green_A700));
                txtLevel2.setTextColor(getResources().getColor(R.color.md_green_A700));
                txtLevel3.setTextColor(getResources().getColor(R.color.md_green_A700));
                txtLevel4.setTextColor(getResources().getColor(R.color.md_green_A700));
            }

            if (d.name.contains("Mana")) {
                txtLevel1.setTextColor(getResources().getColor(R.color.md_blue_A700));
                txtLevel2.setTextColor(getResources().getColor(R.color.md_blue_A700));
                txtLevel3.setTextColor(getResources().getColor(R.color.md_blue_A700));
                txtLevel4.setTextColor(getResources().getColor(R.color.md_blue_A700));
            }

            if (d.name.toLowerCase().contains("damage")) {
                txtLevel1.setTextColor(getResources().getColor(R.color.md_red_A700));
                txtLevel2.setTextColor(getResources().getColor(R.color.md_red_A700));
                txtLevel3.setTextColor(getResources().getColor(R.color.md_red_A700));
                txtLevel4.setTextColor(getResources().getColor(R.color.md_red_A700));
            }


            if (d.list.size() >=4) {

                txtLevel1.setText(d.list.get(0));
                txtLevel2.setText(d.list.get(1));
                txtLevel3.setText(d.list.get(2));
                txtLevel4.setText(d.list.get(3));
            }
            if (dto.isUltimate) {
                txtLevel4.setVisibility(View.GONE);
            } else {
                txtLevel4.setVisibility(View.VISIBLE);
            }

            viewAbilityPerLevel.addView(row);
        }
    }

    @Override
    public void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
                if (heroEntry != null && heroEntry.listAbilities.size() > 0 && !TextUtils.isEmpty(heroEntry.listAbilities.get(tab.getPosition()).sound)) {
                    try {
                        if (fabMuted.getTag().equals("normal")) {

                            serviceMedia.play(heroEntry.listAbilities.get(tab.getPosition()).sound, heroId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                update(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                HeroEntry heroEntry = HeroManager.getInstance().getHero(heroId);
                if (!TextUtils.isEmpty(heroEntry.listAbilities.get(tab.getPosition()).sound)) {
                    if (fabMuted.getTag().equals("normal")) {

                        serviceMedia.play(heroEntry.listAbilities.get(tab.getPosition()).sound, heroId);
                    }
                }
            }
        });
        getAbilityData();


        fabMuted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fabMuted.getTag().equals("muted")) {
                    fabMuted.setTag("normal");
                    fabMuted.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_up_white_24dp));
                } else {
                    fabMuted.setTag("muted");
                    fabMuted.setImageDrawable(getResources().getDrawable(R.drawable.ic_volume_off_white_24dp));
                }
            }
        });

    }

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

    private void getData1() {
                for (HeroEntry p : HeroManager.getInstance().getStrHeroes()) {
                    AObject abiObject = null;
                    try {
                        abiObject = FileUtil.getAbilityObject(getActivity(), p.heroId);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    if (abiObject != null) {
                        SaveHeroAbility saveHeroAbility = (SaveHeroAbility) abiObject;
                        HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.clear();
                        HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.addAll(saveHeroAbility.listAbility);
                        Logger.debug(TAG, ">>>" + "withAbility hero:" + saveHeroAbility.listAbility.size());
                        listAbilities.clear();
                        listAbilities.addAll(HeroManager.getInstance().getHero(heroId).listAbilities);
                        tabLayout.removeAllTabs();
                        for (AbilityDto dto : listAbilities) {
                            View vTab = inflater.inflate(R.layout.tab_ability, null);
                            ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
                            TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
                            txt.setText(dto.name);
                            Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
                            TabLayout.Tab tab = tabLayout.newTab().setCustomView(vTab);
                            tabLayout.addTab(tab);
                        }
                        for (int i = 0; i < listAbilities.size(); i++) {
                            update(i);
                        }

                    } else {

                        HTTPParseUtils.getInstance().withAbility(p.heroId);
                    }
                }
    }

    private void getDataAgi() {
        for (HeroEntry p : HeroManager.getInstance().getIntelHeroes()) {
            AObject abiObject = null;
            try {
                abiObject = FileUtil.getAbilityObject(getActivity(), p.heroId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (abiObject != null) {
                SaveHeroAbility saveHeroAbility = (SaveHeroAbility) abiObject;
                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.clear();
                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.addAll(saveHeroAbility.listAbility);
                Logger.debug(TAG, ">>>" + "withAbility hero:" + saveHeroAbility.listAbility.size());
                listAbilities.clear();
                listAbilities.addAll(HeroManager.getInstance().getHero(heroId).listAbilities);
                tabLayout.removeAllTabs();
                for (AbilityDto dto : listAbilities) {
                    View vTab = inflater.inflate(R.layout.tab_ability, null);
                    ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
                    TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
                    txt.setText(dto.name);
                    Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
                    TabLayout.Tab tab = tabLayout.newTab().setCustomView(vTab);
                    tabLayout.addTab(tab);
                }
                for (int i = 0; i < listAbilities.size(); i++) {
                    update(i);
                }

            } else {

                HTTPParseUtils.getInstance().withAbility(p.heroId);
            }
        }
    }

    private void getAbilityData() {

        final HeroEntry p = HeroManager.getInstance().getHero(heroId);
        try {

            AObject abiObject = FileUtil.getAbilityObject(getActivity(), p.heroId);
            if (abiObject != null) {
                SaveHeroAbility saveHeroAbility = (SaveHeroAbility) abiObject;
                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.clear();
                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.addAll(saveHeroAbility.listAbility);
                Logger.debug(TAG, ">>>" + "withAbility hero:" + saveHeroAbility.listAbility.size());
                listAbilities.clear();
                listAbilities.addAll(HeroManager.getInstance().getHero(heroId).listAbilities);
                tabLayout.removeAllTabs();
                for (AbilityDto dto : listAbilities) {
                    View vTab = inflater.inflate(R.layout.tab_ability, null);
                    ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
                    TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
                    txt.setText(dto.name);
                    Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
                    TabLayout.Tab tab = tabLayout.newTab().setCustomView(vTab);
                    tabLayout.addTab(tab);
                }
                for (int i = 0; i < listAbilities.size(); i++) {
                    update(i);
                }

            } else {

                HTTPParseUtils.getInstance().withAbility(p.heroId);
                HTTPParseUtils.getInstance().setCallback(new HTTPParseUtils.IParseCallBack() {
                    @Override
                    public void onFinish() {
                        try {
                            AObject abiObject = FileUtil.getAbilityObject(getActivity(), p.heroId);
                            if (abiObject != null) {
                                SaveHeroAbility saveHeroAbility = (SaveHeroAbility) abiObject;
                                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.clear();
                                HeroManager.getInstance().getHero(saveHeroAbility.heroID).listAbilities.addAll(saveHeroAbility.listAbility);
                                Logger.debug(TAG, ">>>" + "withAbility hero:" + saveHeroAbility.listAbility.size());
                                listAbilities.clear();
                                listAbilities.addAll(HeroManager.getInstance().getHero(heroId).listAbilities);
                                tabLayout.removeAllTabs();
                                for (AbilityDto dto : listAbilities) {
                                    View vTab = inflater.inflate(R.layout.tab_ability, null);
                                    ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
                                    TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
                                    txt.setText(dto.name);
                                    Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
                                    TabLayout.Tab tab = tabLayout.newTab().setCustomView(vTab);
                                    tabLayout.addTab(tab);
                                }
                                for (int i = 0; i < listAbilities.size(); i++) {
                                    update(i);
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "getAbilityData error:" + e.toString());

        }
    }
}
