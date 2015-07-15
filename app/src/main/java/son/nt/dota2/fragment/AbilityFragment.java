package son.nt.dota2.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.AbilityDto;
import son.nt.dota2.dto.HeroEntry;

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
        if (getArguments() != null) {
            heroEntry = (HeroEntry) getArguments().getSerializable(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        getSafeActionBar().setTitle(heroEntry.name);
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
        if (heroEntry == null || heroEntry.listAbilities.size() == 0) {
            return;
        }

        for (AbilityDto dto : heroEntry.listAbilities) {
            TabLayout.Tab tab =  tabLayout.newTab().setText(dto.name);
            tabLayout.addTab(tab);

        }

    }

    @Override
    public void initListener() {

    }
}
