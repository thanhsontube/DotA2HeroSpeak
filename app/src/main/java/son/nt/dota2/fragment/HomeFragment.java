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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.twotoasters.jazzylistview.JazzyHelper;

import java.util.Random;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterTop;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.comments.ChatDialog;
import son.nt.dota2.utils.TsScreen;


public class HomeFragment extends AFragment {
    public static final String TAG = "HomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ViewPager viewPager;
    TabLayout tabLayout;
    AdapterTop adapterTop;

    public static final int EFFECT_DEFAULT = JazzyHelper.ZIPPER;
    int currentEffect = EFFECT_DEFAULT;

    public static final String KEY_EFFECT_DEFAULT = "KEY_EFFECT_DEFAULT";


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        currentEffect = new Random().nextInt(14);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initLayout(view);
        if (savedInstanceState != null) {
            currentEffect = savedInstanceState.getInt(KEY_EFFECT_DEFAULT, EFFECT_DEFAULT);
            setEffect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        currentEffect = new Random().nextInt(14);
        setEffect();
    }

    private void setEffect() {
        try {
            adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(currentEffect);
        } catch (Exception e) {
            e.printStackTrace();
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


    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    FloatingActionButton fabChat;
    private void initLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        adapterTop = new AdapterTop(getFragmentManager());
        viewPager.setAdapter(adapterTop);
        tabLayout.setupWithViewPager(viewPager);
        if (TsScreen.isLandscape(getActivity())) {
            tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_str_24));
            tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_agi_24));
            tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_intel_24));
        }

        fabChat = (FloatingActionButton) view.findViewById(R.id.btn_chat_home);
        fabChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getSafeFragmentManager().beginTransaction();
                Fragment f = getSafeFragmentManager().findFragmentByTag("chat");
                if (f != null) {
                    ft.remove(f);
                }
                ChatDialog dialog = ChatDialog.newInstance();
                ft.add(dialog, "chat");
                ft.commit();
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_effect, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_0:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(0);
                currentEffect = 0;
                break;
            case R.id.action_1:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(1);
                currentEffect = 1;
                break;
            case R.id.action_2:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(2);
                currentEffect = 2;
                break;
            case R.id.action_3:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(3);
                currentEffect = 3;
                break;
            case R.id.action_4:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(4);
                currentEffect = 4;
                break;
            case R.id.action_5:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(5);
                currentEffect = 5;
                break;
            case R.id.action_6:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(6);
                currentEffect = 6;
                break;
            case R.id.action_7:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(7);
                currentEffect = 7;
                break;
            case R.id.action_8:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(8);
                currentEffect = 8;
                break;
            case R.id.action_9:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(9);
                currentEffect = 9;
                break;
            case R.id.action_10:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(10);
                currentEffect = 10;
                break;
            case R.id.action_11:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(11);
                currentEffect = 11;
                break;
            case R.id.action_12:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(12);
                currentEffect = 12;
                break;
            case R.id.action_13:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(13);
                currentEffect = 13;
                break;
            case R.id.action_14:
                adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(14);
                currentEffect = 14;
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_EFFECT_DEFAULT, currentEffect);
    }
}
