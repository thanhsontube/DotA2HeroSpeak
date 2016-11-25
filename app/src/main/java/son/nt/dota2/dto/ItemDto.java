package son.nt.dota2.dto;

import io.realm.RealmObject;

/**
 * Created by sonnt on 11/25/16.
 */

public class ItemDto extends RealmObject {
    int itemNo;
    String itemId;
    String itemName;
    String itemIcon;
    String itemGroup;
    String itemHref;
    int itemPrice;

    public ItemDto() {
    }

    public ItemDto(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public int getItemNo() {
        return itemNo;
    }

    public void setItemNo(int itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemIcon() {
        return itemIcon;
    }

    public void setItemIcon(String itemIcon) {
        this.itemIcon = itemIcon;
    }

    public String getItemGroup() {
        return itemGroup;
    }

    public void setItemGroup(String itemGroup) {
        this.itemGroup = itemGroup;
    }

    public String getItemHref() {
        return itemHref;
    }

    public void setItemHref(String itemHref) {
        this.itemHref = itemHref;
    }

    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
}
