package com.tianlunte.pulltoexpandlistview.model;

/**
 * Created by wangqingyun on 4/7/15.
 */
public class RestaurantObject {

    private String mName;
    private int mDrawable;

    public RestaurantObject(String name, int drawable) {
        mName = name;
        mDrawable = drawable;
    }

    public String getName() {
        return mName;
    }

    public int getDrawable() {
        return mDrawable;
    }

}
