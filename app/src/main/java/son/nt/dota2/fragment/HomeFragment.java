package son.nt.dota2.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.HomePagerAdapter;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.comments.CmtsHistoryActivity;
import son.nt.dota2.utils.TsGaTools;

public class HomeFragment extends AFragment implements View.OnClickListener {
    public static final String TAG = HomeFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;

    ViewPager viewPager;
    TabLayout tabLayout;
    HomePagerAdapter adapterTop;
    FloatingActionButton fabChat;


    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);

    }


    private void initLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        List<HeroListFragment> list = new ArrayList<>();

        list.add(HeroListFragment.newInstance(MsConst.GROUP_STR));
        list.add(HeroListFragment.newInstance(MsConst.GROUP_AGI));
        list.add(HeroListFragment.newInstance(MsConst.GROUP_INTEL));

        adapterTop = new HomePagerAdapter(getFragmentManager(), list);
        viewPager.setAdapter(adapterTop);
        viewPager.setOffscreenPageLimit(list.size());
        tabLayout.setupWithViewPager(viewPager);
        fabChat = (FloatingActionButton) view.findViewById(R.id.btn_chat_home);
        fabChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat_home: {
                TsGaTools.trackPages(MsConst.TRACK_CHAT);
//                FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
//                Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
//                if (f != null) {
//                    ft.remove(f);
//                }
//                ChatDialog dialog = ChatDialog.newInstance();
//                ft.add(dialog, "chat");
//                ft.commit();

                ActivityCompat.startActivity(getActivity(), new Intent(getActivity(), CmtsHistoryActivity.class), null);
                break;
            }
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
