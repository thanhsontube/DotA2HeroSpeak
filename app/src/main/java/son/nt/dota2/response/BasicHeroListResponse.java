package son.nt.dota2.response;

import java.util.List;

import son.nt.dota2.dto.home.HeroBasicDto;

/**
 * Created by sonnt on 10/14/16.
 */

public class BasicHeroListResponse extends DefaultResponse{
    List<HeroBasicDto> mHeroBasicDtoList;

    public BasicHeroListResponse(List<HeroBasicDto> heroBasicDtoList) {
        mHeroBasicDtoList = heroBasicDtoList;
    }

    public List<HeroBasicDto> getHeroBasicDtoList() {
        return mHeroBasicDtoList;
    }
}
