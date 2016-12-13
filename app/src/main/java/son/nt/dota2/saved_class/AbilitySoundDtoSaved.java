package son.nt.dota2.saved_class;

import com.google.gson.Gson;

import son.nt.dota2.MsConst;
import son.nt.dota2.base.AObject;
import son.nt.dota2.dto.heroSound.ISound;

/**
 * Created by sonnt on 12/1/16.
 */

public class AbilitySoundDtoSaved extends AObject implements ISound {

    public String id;

    public int abiNo;
    public String abiHeroID; //Mirana
    public String abiName; //Starstorm
    public String abiImage; // No targer
    public String abiSound; //enemies
    public String abiDescription; //magical
    public String abiNotes; //magical
    public boolean isUltimate = false;

    @Override
    public String getTitle() {
        return abiName;
    }

    @Override
    public String getLink() {
        return abiSound;
    }

    @Override
    public String getArcanaLink() {
        return null;
    }

    @Override
    public String getImage() {
        return abiImage;
    }

    @Override
    public String getGroup() {
        return null;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public int getSoundType() {
        return MsConst.TYPE_ABILITY_SOUND;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
