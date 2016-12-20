package son.nt.dota2.hero.hero_fragment;

import java.util.List;

import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;

/**
 * Created by sonnt on 11/7/16.
 */

public class HeroResponseContract {
    public interface View  {

        void showHeroSoundsList(List<HeroResponsesDto> list);

        void addDataToDownload(List<HeroResponsesDto> heroResponsesDtos, String heroID);

        void updateArcana(boolean arcana);

        void updateAbi(List<AbilitySoundDto> data);
    }

    public interface Presenter {

        void getHeroSounds();

        void fetchBasicHeroFromHeroId(String heroId);

        void setFetchServiceBind(boolean binded);

        List <HeroResponsesDto> getSoundsList ();

        void getAbi ();

        void downloadFetch();

        void searchSound(String keyword);
    }
}
