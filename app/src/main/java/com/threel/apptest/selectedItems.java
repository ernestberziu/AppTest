package com.threel.apptest;

public class selectedItems {
    String index;
    String backgroundColor;

    public selectedItems(String index, String backgroundColor) {
        this.index = index;
        this.backgroundColor = backgroundColor;
    }
    public selectedItems() {
    }
    public String getIndex() {
        return index;
    }
    public void setIndex(String index) {
        this.index = index;
    }
    public String getBackgroundColor() {
        return backgroundColor;
    }
    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

}
