package son.nt.dota2.comments;

import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/17/16.
 */

public class FullCmtsDto {
    CmtsDto mCmtsDto;
    ISound mSound;

    public FullCmtsDto() {
    }

    public CmtsDto getCmtsDto() {
        return mCmtsDto;
    }

    public void setCmtsDto(CmtsDto cmtsDto) {
        mCmtsDto = cmtsDto;
    }

    public ISound getSound() {
        return mSound;
    }

    public void setSound(ISound sound) {
        mSound = sound;
    }
}
