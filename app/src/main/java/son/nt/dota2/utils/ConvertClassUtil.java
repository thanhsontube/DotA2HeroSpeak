package son.nt.dota2.utils;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.dto.AbilitySoundDto;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.home.HeroBasicDto;
import son.nt.dota2.dto.story.StoryDto;
import son.nt.dota2.dto.story.StoryFireBaseDto;
import son.nt.dota2.dto.story.StoryPartDto;
import son.nt.dota2.saved_class.AbilitySoundDtoSaved;
import son.nt.dota2.saved_class.FileAbilityList;
import son.nt.dota2.saved_class.FileHeroBasicList;
import son.nt.dota2.saved_class.FileResponseList;
import son.nt.dota2.saved_class.HeroBasicDtoSaved;
import son.nt.dota2.saved_class.HeroResponsesDtoSaved;

/**
 * Created by sonnt on 12/9/16.
 */

public class ConvertClassUtil {

    public static StoryFireBaseDto createStoryFireBaseDto(StoryDto s) {
        StoryFireBaseDto dto = new StoryFireBaseDto(s.getStoryId(), s.getUserId(), s.getUsername(), s.getUserPicture()
                , s.getTitle(), s.getCreatedTime(), s.getContents());
        List<StoryPartDto> list = new ArrayList<>();
        for (StoryPartDto d : s.getContents()) {
            list.add(d);
        }

        dto.setContents(list);
        return dto;
    }

    public static List<HeroBasicDto> createHeroBasicDto(FileHeroBasicList file) {

        List<HeroBasicDto> list = new ArrayList<>();
        if (file == null || file.mList == null) {
            return list;
        }

        for (HeroBasicDtoSaved d : file.mList) {
            list.add(new HeroBasicDto(d.heroId, d.no, d.priority, d.name, d.fullName, d.avatar, d.heroIcon, d.welcomeVoice,
                    d.attribute, d.roles, d.description, d.avatar2, d.href, d.group, d.bgLink));
        }
        return list;

    }

    public static List<AbilitySoundDto> createAbi(FileAbilityList file) {

        List<AbilitySoundDto> list = new ArrayList<>();
        if (file == null || file.mList == null) {
            return list;
        }

        for (AbilitySoundDtoSaved d : file.mList) {
            list.add(new AbilitySoundDto(d.isUltimate, d.id, d.abiNo, d.abiHeroID, d.abiName, d.abiImage, d.abiSound,
                    d.abiDescription, d.abiNotes));
        }
        return list;

    }

    public static List<HeroResponsesDto> createResponse(FileResponseList file) {

        List<HeroResponsesDto> list = new ArrayList<>();
        if (file == null || file.mList == null) {
            return list;
        }
        HeroResponsesDto dto;
        for (HeroResponsesDtoSaved d : file.mList) {
            dto = new HeroResponsesDto(d.no, d.heroId, d.heroName, d.heroIcon, d.voiceGroup,
                    d.toHeroId, d.toHeroIcon, d.toHeroName, d.text, d.link,
                    d.linkArcana, d.sub, d.position, d.itemId, d.title, d.image, d.group,
                    d.duration, d.totalLike, d.totalComments, d.isPlaying, d.isFavorite,
                    d.isLiked);
            list.add(dto);
        }
        return list;

    }
}
