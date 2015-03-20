package son.nt.dota2.dto;

/**
 * Created by 4210047 on 3/20/2015.
 */
public class LeftDrawerDto {
    public String text;
    public int drawableId;
    public boolean isSelected = false;
    public boolean isHeader = false;
    public String heroName;
    public String heroUrl;

    public LeftDrawerDto(String text) {
        this.text = text;
    }

    public LeftDrawerDto(String text, boolean isSelected) {
        this.text = text;
        this.isSelected = isSelected;
    }
}
