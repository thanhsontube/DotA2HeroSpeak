package son.nt.dota2.fragment;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.apache.http.client.methods.HttpGet;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterSpeak;
import son.nt.dota2.base.BaseFragment;
import son.nt.dota2.base.Controller;
import son.nt.dota2.dto.HeroData;
import son.nt.dota2.dto.HeroDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.loader.HeroSpeakLoader;
import son.nt.dota2.loader.MediaLoader;
import son.nt.dota2.utils.FileUtil;
import son.nt.dota2.utils.FilterLog;

public class MainFragment extends BaseFragment {
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

    private OnFragmentInteractionListener mListener;

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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            heroDto = (HeroDto) getArguments().getSerializable(ARG_PARAM2);
        }

        mParam1 = linkPaSpeak;
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
        try {
            HeroDto dto = FileUtil.readHeroSpeak(context, heroDto.name);
            if (dto != null) {
                heroDto = dto;
                list.clear();
                list.addAll(heroDto.listSpeaks);
                adapter.notifyDataSetChanged();
            } else {
                controllerLoadSpeak.load();
            }
        } catch(Exception e) {

        }

    }

    private void initData() {

    }

    private void initLayout(View view) {
        listview = (ListView) view.findViewById(R.id.main_listview);
        adapter = new AdapterSpeak(context, list);
        listview.setAdapter(adapter);
    }

    private void initListener() {
        listview.setOnItemClickListener(itemClick);
    }

    AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SpeakDto dto = list.get(position);
            if (dto.isTitle) {
                return;
            }
            File folderPath = new File(Environment.getExternalStorageDirectory(), "/00-save/");
            if (!folderPath.exists()) {
                folderPath.mkdirs();
            }
            String linkSpeak = dto.link;
            File file = new File(folderPath, File.separator + createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
            if (file.exists()) {
                log.d("log>>>" + "File exist");
                MediaPlayer player = MediaPlayer.create(context, Uri.parse(file.getPath()));
                player.start();
            } else {
                loadSpeak(linkSpeak);
            }
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    Controller controllerLoadSpeak = new Controller() {
        @Override
        public void load() {
            {
                String pathSpeak = String.format("http://dota2.gamepedia.com/%s_responses",heroDto.name);
                log.d("log>>>" + "pathSpeak:" + pathSpeak);
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        log.d("log>>>" + "list speaks:" + heroDto.listSpeaks.size());
                        list.clear();
                        list.addAll(heroDto.listSpeaks);
                        adapter.notifyDataSetChanged();
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
                        File file = new File(entity.getParent(), File.separator + createPathFromUrl(linkSpeak).replace(".mp3", ".dat"));
                        Log.v(TAG, "log>>>" + "file:" + file.getPath());
                        entity.renameTo(file);
                        MediaPlayer player = MediaPlayer.create(context, Uri.parse(file.getPath()));
                        player.start();
                    } catch (Exception e) {
                        // TODO: handle exception
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


}
