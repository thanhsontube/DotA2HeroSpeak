package son.nt.dota2.ottobus_entry;

import son.nt.dota2.dto.SpeakDto;

/**
 * Created by Sonnt on 8/10/15.
 */
public class GoLoginDto {
    public boolean isLogin;
    public SpeakDto speakDto;

    public GoLoginDto(boolean isLogin) {
        this.isLogin = isLogin;
    }
}
