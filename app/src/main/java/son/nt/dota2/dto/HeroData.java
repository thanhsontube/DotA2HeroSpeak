package son.nt.dota2.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HeroData implements Serializable{
    public List<HeroDto> listHeros;
    public int status = 0;

    public HeroData() {
        listHeros = new ArrayList<HeroDto>();
    }
}
