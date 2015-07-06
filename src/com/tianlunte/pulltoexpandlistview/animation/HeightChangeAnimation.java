package com.tianlunte.pulltoexpandlistview.animation;

import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by wangqingyun on 29/6/15.
 */
public class HeightChangeAnimation extends Animation {
    private View mAnimatedView;
    private int mStartHeight;
    private int mEndHeight;

    public HeightChangeAnimation(View view, int duration, int startH, int endH) {
        setDuration(duration);
        mAnimatedView = view;
        mStartHeight = startH;
        mEndHeight = endH;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);

        if (interpolatedTime < 1.0f) {
            mAnimatedView.getLayoutParams().height = mStartHeight + (int) ((mEndHeight-mStartHeight) * interpolatedTime);
            mAnimatedView.requestLayout();
        } else {
            mAnimatedView.getLayoutParams().height = mEndHeight;
            mAnimatedView.requestLayout();
        }
    }
}