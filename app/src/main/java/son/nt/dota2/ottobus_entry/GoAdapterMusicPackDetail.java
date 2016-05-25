package son.nt.dota2.ottobus_entry;

import son.nt.dota2.dto.musicPack.MusicPackSoundDto;

/**
 * Created by sonnt on 5/25/16.
 */
public class GoAdapterMusicPackDetail {
    int position;
    MusicPackSoundDto musicPackSoundDto;

    public GoAdapterMusicPackDetail(int position, MusicPackSoundDto musicPackSoundDto) {
        this.position = position;
        this.musicPackSoundDto = musicPackSoundDto;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public MusicPackSoundDto getMusicPackSoundDto() {
        return musicPackSoundDto;
    }

    public void setMusicPackSoundDto(MusicPackSoundDto musicPackSoundDto) {
        this.musicPackSoundDto = musicPackSoundDto;
    }
}
