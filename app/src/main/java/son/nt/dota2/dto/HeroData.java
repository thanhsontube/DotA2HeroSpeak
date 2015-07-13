package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeroData implements Serializable {
    public List<HeroDto> listHeros = new ArrayList<>();
    public int status = 0;
    //    public String[] groups = new String[]{"Strength", "Agility", "Intelligence"};
    public String[] groups = new String[]{"STRENGTH", "AGILITY", "INTELLIGENCE"};

    public HeroData() {
        listHeros = new ArrayList<HeroDto>();
    }
}
