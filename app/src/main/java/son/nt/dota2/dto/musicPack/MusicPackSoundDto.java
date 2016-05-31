package son.nt.dota2.dto.musicPack;

import son.nt.dota2.base.MediaItem;

/**
 * Created by sonnt on 5/23/16.
 */
public class MusicPackSoundDto extends MediaItem {

    public MusicPackSoundDto() {
        super();
    }

    public MusicPackSoundDto(String name, String link, String o, String g) {
        super(name, link, o, g);
    }

//    public MusicPackSoundRealm createDb() {
//        MusicPackSoundRealm musicPackSoundRealm = new MusicPackSoundRealm();
//        musicPackSoundRealm.itemId = getItemId();
//        musicPackSoundRealm.title = getTitle();
//        musicPackSoundRealm.link = getLink();
//        musicPackSoundRealm.image = getImage();
//        musicPackSoundRealm.group = getGroup();
//        musicPackSoundRealm.duration = getDuration();
//        musicPackSoundRealm.sub = getSub();
//        musicPackSoundRealm.totalLike = getTotalLike();
//        musicPackSoundRealm.totalComments = getTotalComments();
//        musicPackSoundRealm.isPlaying = isPlaying();
//        musicPackSoundRealm.isFavorite = isFavorite();
//        musicPackSoundRealm.isLiked = isLiked();
//        return musicPackSoundRealm;
//    }
}
