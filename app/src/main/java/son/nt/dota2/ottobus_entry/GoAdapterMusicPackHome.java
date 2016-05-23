package son.nt.dota2.ottobus_entry;

import java.util.List;

import son.nt.dota2.dto.musicPack.MusicPackDto;

/**
 * Created by sonnt on 5/23/16.
 */
public class GoAdapterMusicPackHome {
    public List<MusicPackDto> list;

    public GoAdapterMusicPackHome(List<MusicPackDto> entity) {
        this.list = entity;
    }
}
