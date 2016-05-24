package son.nt.dota2.ottobus_entry;

import java.util.List;

import son.nt.dota2.dto.musicPack.MusicPackSoundDto;

/**
 * Created by sonnt on 5/23/16.
 */
public class GoAdapterMusicPackDetails {
    public List<MusicPackSoundDto> list;

    public GoAdapterMusicPackDetails(List<MusicPackSoundDto> entity) {
        this.list = entity;
    }
}
