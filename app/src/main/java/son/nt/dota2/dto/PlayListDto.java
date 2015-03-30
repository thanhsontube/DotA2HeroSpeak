package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4210047 on 3/30/2015.
 */
public class PlayListDto implements Serializable {

    public String title;
    public String playListId;
    public int videoCount;
    public String author;
    public String thumbnail;
    public List<YoutubeVideoDto> list = new ArrayList<>();

    public PlayListDto(String title,  String thumbnail, int videoCount) {
        this.title = title;
        this.videoCount = videoCount;
        this.thumbnail = thumbnail;
    }
}
