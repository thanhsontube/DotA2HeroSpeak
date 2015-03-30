/*
 * Copyright 2012 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package son.nt.dota2.youtube;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.WindowManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.adapter.AdapterVideoList;
import son.nt.dota2.dto.PlayListDto;
import son.nt.dota2.dto.YoutubeVideoDto;

/**
 * A simple YouTube Android API demo application which shows how to create a simple application that
 * shows a YouTube Video in a {@link com.google.android.youtube.player.YouTubePlayerFragment}.
 * <p/>
 * Note, this sample app extends from {@link YouTubeFailureRecoveryActivity} to handle errors, which
 * itself extends {@link com.google.android.youtube.player.YouTubeBaseActivity}. However, you are not required to extend
 * {@link com.google.android.youtube.player.YouTubeBaseActivity} if using {@link com.google.android.youtube.player.YouTubePlayerFragment}s.
 */
public class WidgetVideoYoutubeActivity extends YouTubeFailureRecoveryActivity {

    private String youtubeLink;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private PlayListDto playListDto;
    private AdapterVideoList adapter;
    private List<YoutubeVideoDto> list = new ArrayList<>();
    YouTubePlayer player;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        this.setFinishOnTouchOutside(false);
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_widget_video_youtube);
        playListDto = (PlayListDto) getIntent().getSerializableExtra("youtube");
        youtubeLink = playListDto.list.get(0).videoId;
        list.clear();
        list.addAll(playListDto.list);
        adMob();
//        String link = getIntent().getStringExtra("youtube");
//        if (!TextUtils.isEmpty(link) && link.contains("youtube.com")) {
//            link = link.substring(link.indexOf("v=")+2);
//            if(link.contains("&")) {
//                link = link.substring(0, link.indexOf("&"));
//            }
//
//            youtubeLink = link;
//
//        } else {
//            youtubeLink = link;
//        }
        Log.v("", "log>>>" + "onCreate:" + youtubeLink);


        YouTubePlayerFragment youTubePlayerFragment =
                (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        youTubePlayerFragment.initialize(DeveloperKey.DEVELOPER_KEY, this);
        initLayout();
    }

    private void initLayout() {
        recyclerView = (RecyclerView) findViewById(R.id.player_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AdapterVideoList(this, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnCallback(new AdapterVideoList.IAdapterCallback() {
            @Override
            public void onClick(int position, YoutubeVideoDto dto) {
                player.loadPlaylist(playListDto.playListId, getIndexFromVideoId(dto), 0);
            }
        });

    }

    private int getIndexFromVideoId(YoutubeVideoDto dto) {
        for (int i = 0; i < list.size(); i++) {
            if (dto.videoId.equals(list.get(i).videoId)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player,
                                        boolean wasRestored) {
        this.player = player;
        Log.v("", "log>>>" + "onInitializationSuccess :" + youtubeLink);
//        YouTubePlayer.PlayerStyle style = YouTubePlayer.PlayerStyle.MINIMAL;
//        player.setPlayerStyle(style);
        if (!wasRestored) {
//            player.loadVideo(youtubeLink);
            player.loadPlaylist(playListDto.playListId);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

    private void adMob() {
        //ad mob
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice("C5C5650D2E6510CF583E2D3D94B69B57")
//                .addTestDevice("224370EA280CB464C7C922F369F77C69")
                .build();

        //my s3
        mAdView.loadAd(adRequest);
    }

}
