package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;

public class HeroSavedDto extends AObject {
    public List<HeroEntry> listHeroes = new ArrayList<>();

    public HeroSavedDto() {
    }

    public HeroSavedDto(List<HeroEntry> listHeroes) {
        this.listHeroes = listHeroes;
    }
}
