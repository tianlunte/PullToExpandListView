package com.tianlunte.pulltoexpandlistview.utils;

import com.tianlunte.pulltoexpandlistview.R;

import android.content.Context;
import android.graphics.Point;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by wangqingyun on 30/6/15.
 */
public class CommUtils  {

    public static int getPxFromDp(Context context, int dp) {
        return dp*CommUtils.getDensity(context)/160;
    }

    public static int getDensity(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        int densityDpi = (int)(metrics.density * 160f);

        return densityDpi;
    }

    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();

        display.getSize(size);
        return size.y;
    }

    public static int measureCollapsedListItemHeight(Context context) {
        RelativeLayout itemLayout = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.aleph_list_item_collapse, null);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        itemLayout.measure(widthMeasureSpec, heightMeasureSpec);
        return itemLayout.getMeasuredHeight();
    }

    public static int measureExpandedListItemHeight(Context context) {
        RelativeLayout itemLayout = (RelativeLayout)LayoutInflater.from(context).inflate(R.layout.aleph_list_item_expand, null);

        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        itemLayout.measure(widthMeasureSpec, heightMeasureSpec);
        return itemLayout.getMeasuredHeight();
    }

}