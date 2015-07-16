package son.nt.dota2.fragment;

import android.app.Fragment;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.HeroEntry;
import son.nt.dota2.service.ServiceMedia;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AbilityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AbilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AbilityFragment extends AbsFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private TabLayout tabLayout;
    private List<AbilityDto> listAbilities = new ArrayList<>();
    private HeroEntry heroEntry;
    private LayoutInflater inflater;
    private TextView txtAbility, txtAffects, txtDamage;

    ServiceMedia serviceMedia;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AbilityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AbilityFragment newInstance(HeroEntry param1, String param2) {
        AbilityFragment fragment = new AbilityFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
            heroEntry = (HeroEntry) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getSafeActionBar().setTitle(heroEntry.name);
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
        tabLayout = (TabLayout) view.findViewById(R.id.ability_tab_layout);
        txtAbility = (TextView) view.findViewById(R.id.ability_abi);
        txtAffects = (TextView) view.findViewById(R.id.ability_aff);
        txtDamage = (TextView) view.findViewById(R.id.ability_damage);
        if (heroEntry == null || heroEntry.listAbilities.size() == 0) {
            return;
        }

        for (AbilityDto dto : heroEntry.listAbilities) {
            View vTab = inflater.inflate(R.layout.tab_ability, null);
            ImageView img = (ImageView) vTab.findViewById(R.id.ability_icon);
            TextView txt = (TextView) vTab.findViewById(R.id.ability_text);
            txt.setText(dto.name);
            Glide.with(getActivity()).load(dto.linkImage).fitCenter().into(img);
            TabLayout.Tab tab =  tabLayout.newTab().setCustomView(vTab);
            tabLayout.addTab(tab);
        }
        update(0);

    }

    private void update(int position) {
        if (heroEntry == null || heroEntry.listAbilities.size() == 0) {
            return;
        }
        txtAbility.setText(heroEntry.listAbilities.get(position).ability);
        txtAffects.setText(heroEntry.listAbilities.get(position).affects);
        txtDamage.setText(heroEntry.listAbilities.get(position).damage);
    }

    @Override
    public void initListener() {
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (!TextUtils.isEmpty(heroEntry.listAbilities.get(tab.getPosition()).sound)) {
                    serviceMedia.playSingleLink(heroEntry.listAbilities.get(tab.getPosition()).sound);
                }
                update(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (!TextUtils.isEmpty(heroEntry.listAbilities.get(tab.getPosition()).sound)) {
                    serviceMedia.playSingleLink(heroEntry.listAbilities.get(tab.getPosition()).sound);
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
}
