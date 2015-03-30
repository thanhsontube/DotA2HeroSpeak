package son.nt.dota2.loader;

import org.apache.http.client.methods.HttpUriRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.PlayListDto;
import son.nt.dota2.dto.YoutubeVideoDto;
import son.nt.dota2.loader.base.JsonLoaderBase;
import son.nt.dota2.utils.FilterLog;

/**
 * Created by 4210047 on 3/30/2015.
 */
public abstract class PlayListLoader extends JsonLoaderBase<PlayListDto> {
    private static final String TAG = "PlayListLoader";
    FilterLog log = new FilterLog(TAG);

    public PlayListLoader (HttpUriRequest httpUriRequest, boolean isUseCache) {
        super (httpUriRequest, isUseCache);
    }
    @Override
    protected PlayListDto handleJson(JSONObject json) throws IOException, JSONException {
        try {
            log.d("log>>>" + "Json:" + json.toString());
            JSONObject joFeed = json.getJSONObject("feed");
            JSONObject joTitle = joFeed.getJSONObject("title");
            String title = joTitle.getString("$t");

            JSONObject joMediaGroup = joFeed.getJSONObject("media$group");
            JSONArray joArrMediaThumbnail = joMediaGroup.getJSONArray("media$thumbnail");
            JSONObject joThumbnail = joArrMediaThumbnail.getJSONObject(2);

            String thumbnail = joThumbnail.getString("url");

            JSONArray joArrEntry = joFeed.getJSONArray("entry");

            int videoCount = joArrEntry.length();

            YoutubeVideoDto dto;
            List<YoutubeVideoDto> list = new ArrayList<>();
            list.clear();

            for (int i = 0; i < videoCount; i ++) {
                JSONObject joTube = joArrEntry.getJSONObject(i);
                JSONObject joPublished = joTube.getJSONObject("published");
                String publishedTube = joPublished.getString("$t");

                String titleTube = joTube.getJSONObject("title").getString("$t");
                JSONObject joMediaGroupTube = joTube.getJSONObject("media$group");
                JSONArray joArrMediaThumbnaiTube = joMediaGroupTube.getJSONArray("media$thumbnail");
                JSONObject joThumbnailTube = joArrMediaThumbnaiTube.getJSONObject(2);

                String thumbnailTube = joThumbnailTube.getString("url");

                int duration = joMediaGroupTube.getJSONObject("yt$duration").getInt("seconds");
                String videoId = joMediaGroupTube.getJSONObject("yt$videoid").getString("$t");

                String author = joTube.getJSONArray("author").getJSONObject(0).getJSONObject("name").getString("$t");


                dto = new YoutubeVideoDto();
                dto.published = publishedTube;
                dto.title = titleTube;
                dto.thumbnail = thumbnailTube;
                dto.duration = duration;
                dto.author = author;
                dto.videoId = videoId;
                list.add(dto);
            }
            PlayListDto playListDto = new PlayListDto(title, thumbnail, videoCount);
            playListDto.list = list;
            return playListDto;
        } catch (Exception e) {
            log.e("log>>>" + "Error parse video entry:" + e.toString());
        }
        return null;
    }
}
