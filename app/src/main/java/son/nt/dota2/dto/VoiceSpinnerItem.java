package son.nt.dota2.dto;

/**
 * Created by Sonnt on 8/7/15.
 */
public class VoiceSpinnerItem {
    private String group;
    private boolean isSelected;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public VoiceSpinnerItem(String group) {
        this.group = group;
        this.isSelected = false;
    }

    public VoiceSpinnerItem(String group, boolean isSelected) {
        this.group = group;
        this.isSelected = isSelected;
    }
}
