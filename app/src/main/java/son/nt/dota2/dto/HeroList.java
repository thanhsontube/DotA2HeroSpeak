package son.nt.dota2.dto;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.base.AObject;

/**
 * Created by Sonnt on 7/23/15.
 */
public class HeroList extends AObject{
    private List<HeroEntry> listHeroes = new ArrayList<>();

    public List<HeroEntry> getListHeroes() {
        return listHeroes;
    }

    public void setListHeroes(List<HeroEntry> listHeroes) {
        this.listHeroes = listHeroes;
    }

    public String[] groups = new String[]{"STRENGTH", "AGILITY", "INTELLIGENCE"};


}
