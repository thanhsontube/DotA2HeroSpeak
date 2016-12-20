package son.nt.dota2.dto;

/**
 * Created by sonnt on 11/24/16.
 */

public class CircleFeatureDto {
    private String name;
    private int icon;
    private boolean isSelected;

    public CircleFeatureDto() {
    }

    public CircleFeatureDto(String name, int icon, boolean isSelected) {
        this.name = name;
        this.icon = icon;
        this.isSelected = isSelected;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
