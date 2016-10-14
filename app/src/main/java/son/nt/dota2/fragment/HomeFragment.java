package son.nt.dota2.fragment;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.HomePagerAdapter;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.comments.ChatDialog;
import son.nt.dota2.utils.TsGaTools;
import son.nt.dota2.utils.TsScreen;

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
        adapterTop = new HomePagerAdapter(getFragmentManager());
        viewPager.setAdapter(adapterTop);
        tabLayout.setupWithViewPager(viewPager);
        if (TsScreen.isLandscape(getActivity())) {
            tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_str_24));
            tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_agi_24));
            tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_intel_24));
        }

        fabChat = (FloatingActionButton) view.findViewById(R.id.btn_chat_home);
        fabChat.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_chat_home: {
                TsGaTools.trackPages(MsConst.TRACK_CHAT);
                FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
                Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
                if (f != null) {
                    ft.remove(f);
                }
                ChatDialog dialog = ChatDialog.newInstance();
                ft.add(dialog, "chat");
                ft.commit();
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
