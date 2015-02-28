package com.trader.android.app.ui;

public class NavMenuItem {
    String itemText;
    String itemIconText;

    public NavMenuItem(String itemText, String itemIconText) {
        this.itemText = itemText;
        this.itemIconText = itemIconText;
    }

    public String getItemText() { return itemText; }
    public void setItemText(String itemText) { this.itemText = itemText; }
    public String getItemIconText() { return itemIconText; }
    public void setItemIconText(String itemIconText) { this.itemIconText = itemIconText; }
}
