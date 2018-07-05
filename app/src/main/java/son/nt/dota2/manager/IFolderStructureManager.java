package son.nt.dota2.manager;

/**
 * Created by sonnt on 2/15/17.
 */

public interface IFolderStructureManager {

    String getInternalFolder();

    String getExternalFolder();

    String getHeroBasicFolderPath();

    String getHeroAbiFolderPath();

    String getHeroVoiceFolderPath();

    String getHeroBasicFile(String heroId);
}
