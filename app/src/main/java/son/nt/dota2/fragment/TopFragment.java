package son.nt.dota2.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viewpagerindicator.TitlePageIndicator;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Random;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterTop;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.customview.KenBurnsView;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.loader.DataLoader;
import son.nt.dota2.loader.HeroBgLoader;
import son.nt.dota2.service.PrefetchService;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.FilterLog;


public class TopFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG = "TopFragment";
    FilterLog log = new FilterLog(TAG);

    private ViewPager pager;
    private AdapterTop adapter;
    private HeroData herodata = new HeroData();
    private View view;
    private KenBurnsView kenburns;
    //titlepage indicator
    TitlePageIndicator indicator;

    public static TopFragment newInstance(String param1, String param2) {
        TopFragment fragment = new TopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public TopFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_top, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        //kenburns
        kenburns = (KenBurnsView) view.findViewById(R.id.top_ken_burns);
        int[] ids = new int[]{R.mipmap.ken2, R.mipmap.ken2};
        kenburns.setResourceIds(ids);
        kenburns.startLayoutAnimation();
        loadingTask.execute();

//        File file = new File(resource.fileHeroList);
//        if (file.exists()) {
//            try {
//                herodata = FileUtil.readHeroList(context);
//                log.d("log>>>" + "===========Hero list exists size:" + herodata.listHeros.size());
//                initLayout(view);
//                initListener();
//                context.startService(new Intent(context, PrefetchService.class));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        } else {
//            controllerHeroList.load();
//        }
    }

    AsyncTask<Void, Void, Void> loadingTask = new AsyncTask<Void, Void, Void>() {
        @Override
        protected Void doInBackground(Void... params) {
            initData();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (MsConst.IS_HANDSOME) {
                initLayout(view);
                initListener();
            }
        }
    };

    private void initData() {
        try {
            File fOut = new File(resource.folderSave, File.separator + "data.zip");
            if (fOut.exists()) {
                log.d("log>>>" + "initData Exist....");
                herodata = FileUtil.readHeroList(context);
                return;
            }
            log.d("log>>>" + "start copy....");
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
            log.d("log>>>" + "Write file OK");

            //unzip
            boolean isUnzip = FileUtil.unpackZip(resource.folderSave + File.separator, "data.zip");
            if (isUnzip) {
                log.d("log>>>" + "Unzip OK");
                herodata = FileUtil.readHeroList(context);

            } else {
                log.e("log>>>" + "Unzip FAIL");
                Toast.makeText(context, "Sorry, can not initialize data", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.e("log>>>" + "Write file ERROR:" + e.toString());
        }
    }

    private void initLayout(View view) {
        pager = (ViewPager) view.findViewById(R.id.pager);
        indicator = (TitlePageIndicator) view.findViewById(R.id.indicator);
        adapter = new AdapterTop(getActivity().getSupportFragmentManager(), herodata);
        pager.setAdapter(adapter);
        initIndicator();
    }

    private void initListener() {

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

    public void initIndicator() {
        indicator.setViewPager(pager);
        indicator.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        indicator.setTextColor(Color.WHITE);
        indicator.setSelectedBold(true);
        indicator.setSelectedColor(Color.RED);
    }

    Controller controllerHeroList = new Controller() {
        @Override
        public void load() {
            HttpGet httpGet = new HttpGet(mypath.getHerosListPath());
            contentManager.load(new DataLoader(httpGet, true) {
                @Override
                public void onContentLoaderStart() {
                    log.d("log>>>" + "controllerHeroList start");
                }

                @Override
                public void onContentLoaderSucceed(HeroData entity) {
                    log.d("log>>>" + "controllerHeroList Success :" + entity.listHeros.size());
                    herodata.listHeros.clear();
                    herodata.listHeros.addAll(entity.listHeros);
//                    try {
//                        FileUtil.saveHeroList(context, herodata);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    initLayout(view);
//                    initListener();

                    controllerLoadBg.load();
                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    log.e("log>>>" + "controllerHeroList fail:" + e);
                }
            });
        }
    };

    HeroData bgHeroData;
    Controller controllerLoadBg = new Controller() {
        @Override
        public void load() {
            HttpGet httpGet = new HttpGet("http://dota2.gamepedia.com/Model_pictures");
            contentManager.load(new HeroBgLoader(httpGet, true) {
                @Override
                public void onContentLoaderStart() {
                    log.d("log>>>" + "controllerLoadBg start");
                }

                @Override
                public void onContentLoaderSucceed(HeroData entity) {
                    log.d("log>>>" + "controllerLoadBg success :" + entity.listHeros.size());

                    bgHeroData = entity;
                    addBgToList(entity.listHeros);
                    try {
                        FileUtil.saveHeroList(context, herodata);
                        context.startService(new Intent(context, PrefetchService.class));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    initLayout(view);
                    initListener();
                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    log.e("log>>>" + "controllerLoadBg fail:" + e);
                }
            });
        }
    };

    private void addBgToList(List<HeroDto> listBg) {
        String bgName;
        int i = 0;
        for (HeroDto dto : listBg) {
            bgName = dto.bgName;
            HeroDto hero = hasName(bgName);
            if (hero != null) {
                log.d("log>>>" + i + "heroname:" + hero.name);
                i++;
                hero.bgName = bgName;
                hero.bgLink = dto.bgLink;
            }
        }
    }

    private HeroDto hasName(String name) {
        int i = 0;
        for (HeroDto dto : herodata.listHeros) {
            if (dto.name.contains(name)) {
                return dto;
            }
        }
        return null;
    }

    private void updateKensBurns() {
        log.d("log>>>" + "updateKensBurns");
        Random random = new Random();

        File file = new File(resource.folderBlur, File.separator);
        if (!file.exists()) {
            return;
        }
        File[] listFiles = file.listFiles();
        int pos = random.nextInt(listFiles.length);
        file = listFiles[pos];


//        HeroDto dto = herodata.listHeros.get(pos);
//        File file = aq.getCachedFile(dto.bgLink);
        if (file != null) {
            try {
                log.d("log>>>" + "update kenburns OK");
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath(), options);
                kenburns.setResourceBitmap(new Bitmap[]{bitmap, bitmap});
                kenburns.startLayoutAnimation();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            updateKensBurns();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
