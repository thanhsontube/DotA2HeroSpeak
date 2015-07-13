package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.twotoasters.jazzylistview.JazzyHelper;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterTop;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroManager;
import son.nt.dota2.htmlcleaner.role.Roles;
import son.nt.dota2.htmlcleaner.role.RolesLoader;
import son.nt.dota2.loader.HeroBgLoader;
import son.nt.dota2.service.PrefetchService;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.Logger;
import son.nt.dota2.utils.TsLog;
import son.nt.dota2.utils.TsScreen;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
    private HeroData herodata = new HeroData();

    public static final int EFFECT_DEFAULT = JazzyHelper.GROW;
    int currentEffect = EFFECT_DEFAULT;

    public static final String KEY_EFFECT_DEFAULT = "KEY_EFFECT_DEFAULT";


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
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
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        initData();
        herodata = HeroManager.getInstance().getHeroData();
        initLayout(view);
        if (savedInstanceState != null) {
            currentEffect = savedInstanceState.getInt(KEY_EFFECT_DEFAULT, EFFECT_DEFAULT);
            setEffect();
        }
//        controllerLoadBg.load();
        controler.load();
    }
    private void setEffect() {
        adapterTop.getCurrentFragment().jazzyRecyclerViewScrollListener.setTransitionEffect(currentEffect);
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
        public void onFragmentInteraction(Uri uri);
    }

    private void initLayout(View view) {
        tabLayout = (TabLayout) view.findViewById(R.id.home_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.home_view_pager);
        adapterTop = new AdapterTop(getFragmentManager(), herodata);
        viewPager.setAdapter(adapterTop);
        tabLayout.setupWithViewPager(viewPager);
        if (TsScreen.isLandscape(getActivity())) {
            tabLayout.getTabAt(0).setIcon(getResources().getDrawable(R.drawable.ic_str_24));
            tabLayout.getTabAt(1).setIcon(getResources().getDrawable(R.drawable.ic_agi_24));
            tabLayout.getTabAt(2).setIcon(getResources().getDrawable(R.drawable.ic_intel_24));
        }
    }

    private void initData() {
        try {
            File fOut = new File(resource.folderSave, File.separator + "data.zip");
            if (fOut.exists()) {
                herodata = FileUtil.readHeroList(context);
                return;
            }
            InputStream in = context.getAssets().open("data.zip");
            OutputStream out = new FileOutputStream(fOut, false);
            int read;
            byte[] buffer = new byte[1024];
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }

            out.flush();
            out.close();
            in.close();

            //unzip
            boolean isUnzip = FileUtil.unpackZip(resource.folderSave + File.separator, "data.zip");
            if (isUnzip) {
                herodata = FileUtil.readHeroList(context);

            } else {
                Toast.makeText(context, "Sorry, can not initialize data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    //test parse
    Controller controler = new Controller() {
        @Override
        public void load() {
            {
                HttpGet httpGet = new HttpGet(RolesLoader.PATH_ROLES);
                contentManager.load(new RolesLoader(httpGet, false) {
                    @Override
                    public void onContentLoaderStart() {
                        Logger.debug(TAG, ">>>" + "onContentLoaderStart");
                    }

                    @Override
                    public void onContentLoaderSucceed(List<Roles> entity) {
                        Logger.debug(TAG, ">>>" + "onContentLoaderSucceed");
                    }

                    @Override
                    public void onContentLoaderFailed(Throwable e) {
                        Logger.error(TAG, ">>>" + "onContentLoaderFailed:" + e.toString());
                    }
                });
            }
        }
    };

    HeroData bgHeroData;
    TsLog log = new TsLog(TAG);
    Controller controllerLoadBg = new Controller() {
        @Override
        public void load() {
            HttpGet httpGet = new HttpGet("http://dota2.gamepedia.com/Model_pictures");
            contentManager.load(new HeroBgLoader(httpGet, false) {
                @Override
                public void onContentLoaderStart() {
                    log.d("log>>>" + "controllerLoadBg start");
                }

                @Override
                public void onContentLoaderSucceed(HeroData entity) {
                    log.d("log>>>" + "controllerLoadBg success :" + entity.listHeros.size());

                    bgHeroData = entity;
                    try {
//                        FileUtil.saveHeroList(context, herodata);
                        context.startService(new Intent(context, PrefetchService.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    log.e("log>>>" + "controllerLoadBg fail:" + e);
                }
            });
        }
    };

}
