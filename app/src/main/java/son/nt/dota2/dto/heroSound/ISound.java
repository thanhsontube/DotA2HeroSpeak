package son.nt.dota2.dto.heroSound;

/**
 * Created by sonnt on 11/7/16.
 */

public interface ISound {

    //text
    public String getTitle();

    public String getLink();

    public String getArcanaLink();

    public String getImage();

    public String getGroup();

    public boolean isPlaying();


    //type of Sounds
//    public static final int TYPE_HERO_SOUND = 1;
//    public static final int TYPE_MUSIC_PACK = 2;
//    public static final int TYPE_CREATIVE_SOUND = 3;
//    public static final int TYPE_ITEM_SOUND = 4;

    public int getSoundType();

    public String getSavedRootFolder();

    public String getSavedBranchFolder();

}
