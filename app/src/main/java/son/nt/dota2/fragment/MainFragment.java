package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSpeak;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.customview.blur.Blur;
import son.nt.dota2.customview.blur.ImageUtils;
import son.nt.dota2.dialog.DialogSetting;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.HeroSpeakLoader;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.service.DownloadService;
import son.nt.dota2.service.ServiceMedia;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.FilterLog;
import son.nt.dota2.utils.NetworkUtils;

public class MainFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = "MainFragment";
    FilterLog log = new FilterLog(TAG);
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    String linkPaSpeak = "http://dota2.gamepedia.com/Phantom_Assassin_responses";
    String linkLunaSpeak = "http://dota2.gamepedia.com/Luna_responses";
    String linkVoidSpeak = "http://dota2.gamepedia.com/Faceless_Void_responses";

    private ListView listview;
    private List<SpeakDto> list = new ArrayList<>();
    private AdapterSpeak adapter;
    private HeroDto heroDto;

    private String heroName;

    private ImageView btnPaused;
    private ImageView btnPlayed;
    private TextView txtPos;


    // Blur
    private static final int LIMIT_TRANSFORM_STOP_VIEW = 200;
    private static final int LIMIT_TRANSFORM_BLUR_VIEW = 600;
    public static final int BACKGROUND_SHIFT = 100;

    public static final int DISTANCE = 512;
    private ImageView blurredImageView;
    private ImageView nonBlurImageView;

    private ScrollView scrollView;

    private int screenHeightShift;

    private File fileNormal;
    private File fileBlur;

    private OnFragmentInteractionListener mListener;

    Drawable drawable;

    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, HeroDto param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putSerializable(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        log.d("log>>>" + "===============MAIN onCreate--------------");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            heroDto = (HeroDto) getArguments().getSerializable(ARG_PARAM2);
            heroName = heroDto.name;
            log.d("log>>>" + "hero:" + heroName + ";background link:" + heroDto.bgLink);
            context.bindService(new Intent(context, DownloadService.class), serviceConnection, Service.BIND_AUTO_CREATE);
            context.bindService(new Intent(context, ServiceMedia.class), serviceConnectionMedia, Service.BIND_AUTO_CREATE);
        }
        mParam1 = linkPaSpeak;



//        drawable = getResources().getDrawable(R.color.red_400);
//        drawable.setAlpha(255);
//        getActivity().getActionBar().setBackgroundDrawable(drawable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initLayout(view);
        initListener();
        updateLayout();
        try {
            HeroDto dto = FileUtil.readHeroSpeak(context, heroDto.name);
            if (dto != null) {
                heroDto = dto;
                list.clear();
                list.addAll(heroDto.listSpeaks);
                adapter.notifyDataSetChanged();
                isLoaded = true;
                prepareMedia();

                if (NetworkUtils.isConnected(context)) {
                    startPrefetch();
                }
            } else {
                controllerLoadSpeak.load();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initData() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        screenHeightShift = displayMetrics.heightPixels + BACKGROUND_SHIFT;
//        log.d("log>>>" + "screenHeightShift:" + screenHeightShift);

        if (heroDto.bgLink != null) {
            fileBlur = new File(resource.folderBlur, File.separator + createPathFromUrl(heroDto.bgLink));
        }

    }

    private void initLayout(View view) {
        //blur image
        blurredImageView = (ImageView) view.findViewById(R.id.blured_image);
        nonBlurImageView = (ImageView) view.findViewById(R.id.orginal_image);
        scrollView = (ScrollView) view.findViewById(R.id.main_scroll_view);

        listview = (ListView) view.findViewById(R.id.main_listview);
        adapter = new AdapterSpeak(context, list);
        adapter.setOnIAdapterListener(adapterListener);
        listview.setAdapter(adapter);

        btnPaused = (ImageView) view.findViewById(R.id.main_btn_paused);
        btnPlayed = (ImageView) view.findViewById(R.id.main_btn_played);
        btnPaused.setOnClickListener(this);
        btnPlayed.setOnClickListener(this);
        txtPos = (TextView) view.findViewById(R.id.main_txt_pos);
    }

    private void updateLayout() {
//        if(actionBar != null) {
//            actionBar.setTitle(heroDto.name);
//        }
        txtPos.setVisibility(View.GONE);
        btnPaused.setVisibility(View.GONE);
        btnPlayed.setVisibility(View.GONE);
        setViewHeight(nonBlurImageView, screenHeightShift);
        setViewHeight(blurredImageView, screenHeightShift);
        if (aq.getCachedFile(heroDto.bgLink) != null) {
            aq.id(nonBlurImageView).image(heroDto.bgLink, true, true);
            loadBlurImage();
        } else {
            aq.id(nonBlurImageView).image(heroDto.bgLink, true, true);
            aq.id(blurredImageView).image(heroDto.bgLink, true, true);
        }
    }


    public void setViewHeight(View v, int height) {
        ViewGroup.LayoutParams params = v.getLayoutParams();
        params.height = height;
        v.setLayoutParams(params);
    }


    private void initListener() {
        listview.setOnItemClickListener(itemClick);
        listview.setOnScrollListener(onScrollListener);
    }

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SpeakDto dto = list.get(position);
            if (dto.isTitle) {
                return;
            }
            btnPaused.setVisibility(View.GONE);
            btnPlayed.setVisibility(View.VISIBLE);
            txtPos.setVisibility(View.GONE);
            mediaService.playSong(position, true);
//            String linkSpeak = dto.link;
//            File file = new File(resource.folderAudio, File.separator + createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
//            if (file.exists()) {
//                log.d("log>>>" + "File audio exist");
//                MediaPlayer player = MediaPlayer.create(context, Uri.parse(file.getPath()));
//                player.start();
//                mediaService.playSong(position);
//            } else {
//                loadSpeak(linkSpeak);
//            }
        }
    };

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

    Controller controllerLoadSpeak = new Controller() {
        @Override
        public void load() {
            {
                String pathSpeak = String.format("http://dota2.gamepedia.com/%s_responses", heroDto.name);
                log.d("log>>>" + "pathSpeak:" + pathSpeak);
                if (pathSpeak.contains("Natures_Prophet")) {
                    pathSpeak = pathSpeak.replace("Natures", "Nature's");
                    log.d("log>>>" + "pathSpeak 2:" + pathSpeak);
                }
                HttpGet httpGet = new HttpGet(pathSpeak);
                contentManager.load(new HeroSpeakLoader(httpGet, true) {
                    @Override
                    public void onContentLoaderStart() {
                        log.d("log>>>" + "onContentLoaderStart");
                    }

                    @Override
                    public void onContentLoaderSucceed(HeroData entity) {
                        log.d("log>>>" + "onContentLoaderSucceed :" + entity.listHeros.size());
                        heroDto.listSpeaks.clear();
                        heroDto.listSpeaks.addAll(entity.listHeros.get(0).listSpeaks);
                        try {
                            FileUtil.saveHeroSpeak(context, heroDto, heroDto.name);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        log.d("log>>>" + "list speaks:" + heroDto.listSpeaks.size());
                        list.clear();
                        list.addAll(heroDto.listSpeaks);
                        adapter.notifyDataSetChanged();
                        isLoaded = true;
                        startPrefetch();
                        prepareMedia();
                    }

                    @Override
                    public void onContentLoaderFailed(Throwable e) {
                        log.e("log>>>" + "onContentLoaderFailed:" + e);
                    }
                });
            }
        }
    };

    private void loadSpeak(final String linkSpeak) {
        try {

            HttpGet httpGet = new HttpGet(linkSpeak);

            MediaLoader dataLoader = new MediaLoader(httpGet, false) {

                @Override
                public void onContentLoaderSucceed(File entity) {
                    Log.v(TAG, "log>>>" + "onContentLoaderSucceed:" + entity.getPath());
                    try {
                        File file = new File(resource.folderAudio, File.separator + createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
                        Log.v(TAG, "log>>>" + "file:" + file.getPath());
                        entity.renameTo(file);
                        MediaPlayer player = MediaPlayer.create(context, Uri.parse(file.getPath()));
                        player.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onContentLoaderStart() {
                    Log.v(TAG, "log>>>" + "onContentLoaderStart");
                }

                @Override
                public void onContentLoaderFailed(Throwable e) {
                    Log.v(TAG, "log>>>" + "onContentLoaderFailed");
                }
            };

            contentManager.load(dataLoader);
        } catch (Exception e) {
        }
    }

    private String createPathFromUrl(String url) {
        String path = url.replaceAll("[|?*<\":>+\\[\\]/']", "_");
        return path;
    }

    int lastY = -1;

    AbsListView.OnScrollListener onScrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            int scrollY = getScrollY();

//            final int headerHeight = DISTANCE;
//            final float ratio = (float) Math.min(Math.max(scrollY, 0), headerHeight) / headerHeight;
//            final int newAlpha = (int) (ratio * 255);
//            drawable.setAlpha(newAlpha);
//            lastY = scrollY;


            if (scrollY < DISTANCE && scrollY >= 0) {
                float val = scrollY * (1f / DISTANCE);
                blurredImageView.setAlpha(val);
            }

            //fade stop
            if (scrollY < LIMIT_TRANSFORM_BLUR_VIEW * 2 && scrollY >= 0) {
                scrollView.scrollTo(0, scrollY / 3);
            }
        }
    };

    public int getScrollY() {
        View c = listview.getChildAt(0);
        if (c == null) {
            return 0;
        }

        int firstVisiblePosition = listview.getFirstVisiblePosition();
        int top = c.getTop();
        return -top + firstVisiblePosition * c.getHeight();
    }

    private void loadBlurImage() {
        if (fileBlur != null && fileBlur.exists()) {
            log.d("log>>>" + "BLUR Image exists");
            aq.id(blurredImageView).image(fileBlur, 0);
            aq.id(nonBlurImageView).image(heroDto.bgLink, true, true);
        } else {

            //Create a new BlurImage and save
//                File file = new File(pathCampaignImage);
            aq.image(heroDto.bgLink, true, true, 0, 0, new BitmapAjaxCallback() {

                @Override
                public void callback(String url, ImageView iv, Bitmap bm, AjaxStatus status) {
                    new SyncBlurBitmap().execute(bm);
                }

            });
        }
    }

    class SyncBlurBitmap extends AsyncTask<Bitmap, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(Bitmap... params) {
            try {
                return Blur.fastblur(context, params[0], 20);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            //display norImage
            aq.id(nonBlurImageView).image(heroDto.bgLink, true, true);
            if (bitmap == null) {
                log.d("log>>>" + "cre4ate blur bitmap error");
                return;
            }
            ImageUtils.storeImage(bitmap, fileBlur);
            log.d("log>>>" + "sync blur Save Success");
            aq.id(blurredImageView).image(bitmap);
        }
    }

    //prefetch all audio

    DownloadService downloadService;
    boolean isBind = false;
    boolean isLoaded = false;
    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownloadService.LocalBinder binder = (DownloadService.LocalBinder) service;
            downloadService = binder.getService();
            isBind = true;
            startPrefetch();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind = true;
        }
    };

    private void startPrefetch() {
        if (isBind && isLoaded) {
            downloadService.addLinkDto(list);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (downloadService != null) {
            downloadService.isQuit = true;
            context.unbindService(serviceConnection);
        }
        if (mediaService != null) {
            mediaService.stop();
            context.unbindService(serviceConnectionMedia);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                DialogSetting dialog = DialogSetting.newInstance("Save", "Cancel");
                dialog.setCancelable(true);
                dialog.setOnIDialogListener(new DialogSetting.IDialogListener() {
                    @Override
                    public void onIDialogCancel(MsConst.RepeatMode repeatMode, int currentVolume) {
//                        Toast.makeText(context, "Mode:" + repeatMode, Toast.LENGTH_SHORT).show();

                    }
                });
                dialog.show(ft, "setting");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //MEDIA MUSIC service
    private ServiceMedia mediaService;
    ServiceConnection serviceConnectionMedia = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            log.d("log>>>" + "MediaService serviceConnectionMedia Connected");
            ServiceMedia.LocalBinder binder = (ServiceMedia.LocalBinder) service;
            mediaService = binder.getService();
            prepareMedia();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            try {
                mediaService.stop();
                mediaService = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void prepareMedia() {
        if (mediaService != null && isLoaded) {
            mediaService.setAdapter(adapter);
            mediaService.setActionBar(txtPos, heroName);
            mediaService.setListData(heroDto.listSpeaks);
            btnPlayed.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_btn_paused:
                btnPaused.setVisibility(View.GONE);
                btnPlayed.setVisibility(View.VISIBLE);
                mediaService.pause();
                txtPos.setVisibility(View.GONE);
                break;
            case R.id.main_btn_played:
                btnPaused.setVisibility(View.VISIBLE);
                btnPlayed.setVisibility(View.GONE);
                mediaService.play();
                txtPos.setVisibility(View.VISIBLE);
                break;
        }
    }

    AdapterSpeak.IAdapterListener adapterListener = new AdapterSpeak.IAdapterListener() {
        @Override
        public void onMenuClick(MsConst.MenuSelect action) {
            switch (action) {
                case FB_SHARE:
                    break;
                case FAVORITE:
                    break;
                case COPY:
                    break;
                case RINGTONE:
                    break;
            }
        }
    };
}
