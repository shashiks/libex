package com.shaksoni.libex;

/**
 * Created by shaksoni on 9/23/17.
 */

public class SlideMenu {


    private String title;
    private int icon;

public SlideMenu(){}

    public SlideMenu( int icon, String title) {
        this.title = title;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
