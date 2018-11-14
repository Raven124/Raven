package com.mediatechindo.wahyu.materialuikit.template.ExtraDesignCategory.LeftMenuIcons;

/**
 * Created by wahyu on 13/10/16.
 */

public class ExtraLeftMenuIconsModel {
    private int iconImage;
    private String menuName = "";
    private boolean isSelected = false;

    public int getIconImage() {
        return iconImage;
    }

    public void setIconImage(int iconImage) {
        this.iconImage = iconImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
