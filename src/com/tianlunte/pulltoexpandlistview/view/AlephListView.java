package com.tianlunte.pulltoexpandlistview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import com.tianlunte.pulltoexpandlistview.adapter.AlephListAdapter;
import com.tianlunte.pulltoexpandlistview.animation.HeightChangeAnimation;
import com.tianlunte.pulltoexpandlistview.controller.AlephActivity;
import com.tianlunte.pulltoexpandlistview.model.RestaurantObject;
import com.tianlunte.pulltoexpandlistview.utils.CommUtils;

/**
 * Created by wangqingyun on 4/7/15.
 */
public class AlephListView extends ListView implements View.OnTouchListener {

    private AlephActivity mActivity;
    private List<RestaurantObject> mList;
    private AlephListAdapter mAdapter;
    private boolean showingLess;
    private float mDownY;
    private List<Boolean> mSlideBackFinished;
    private boolean mSlideBackFinishedB;

    public AlephListView(Context context) {
        super(context);
    }

    public AlephListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlephListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setupList(AlephActivity activity, List<RestaurantObject> list) {
        mActivity = activity;
        mList = list;
    }

    public void showLess() {
        showingLess = true;

        List<RestaurantObject> list2 = new ArrayList<>();
        list2.add(mList.get(0));
        list2.add(mList.get(1));

        mAdapter = new AlephListAdapter(mActivity, list2, true, 2);
        setAdapter(mAdapter);

        setOnTouchListener(this);
    }

    public void showMore() {
        showingLess = false;

        mAdapter = new AlephListAdapter(mActivity, mList, false, getFitNumber());
        setAdapter(mAdapter);
    }

    private int getFitNumber() {
        int avail = CommUtils.getScreenHeight(mActivity) - mActivity.getStatusBarHeight();
        int num = (int) (((avail * 1.0) / CommUtils.measureExpandedListItemHeight(mActivity)) + 0.9999);
        return num;
    }

    @Override
    public boolean onTouch(View view, MotionEvent mv) {
        if(showingLess) {
            switch (mv.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mDownY = mv.getRawY();
                    break;
                case MotionEvent.ACTION_MOVE:
                case MotionEvent.ACTION_UP:
                    if (isPullingDown(mDownY, mv.getRawY())) {
                        mDownY = 0;
                        showMore();
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    private boolean isPullingDown(float yDown, float yUp) {
        if((yUp-yDown) > ViewConfiguration.get(mActivity).getScaledTouchSlop()) {
            return true;
        } else {
            return false;
        }
    }

    public void slideBackToLess() {
        final int lastPos = getLastVisiblePosition();
        final int firstPos = getFirstVisiblePosition();
        initSlidebackFinished(firstPos, lastPos);
        mAdapter.startSlideBack();

        for(int position=getFirstVisiblePosition()+1; position>=getFirstVisiblePosition();position--) {
            final View convertView = getChildAt(position-firstPos);
            final int pos = position;
            HeightChangeAnimation animationH = new HeightChangeAnimation(convertView,
                    AlephListAdapter.ANIM_DURATION,
                    CommUtils.measureExpandedListItemHeight(mActivity),
                    CommUtils.measureCollapsedListItemHeight(mActivity));
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(animationH);
            animation.setDuration(AlephListAdapter.ANIM_DURATION);
            animation.setStartOffset(25*(lastPos-position));
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    isSlideFinished(lastPos-pos);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            convertView.startAnimation(animation);
        }
        for(int position=getLastVisiblePosition();position>=getFirstVisiblePosition()+2;position--) {
            final View convertView = getChildAt(position-firstPos);
            final int pos = position;
            TranslateAnimation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (float) 0.0,
                    Animation.RELATIVE_TO_PARENT, (float)-1.0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0);
            AlphaAnimation animation2 = new AlphaAnimation(1, (float)0.1);
            HeightChangeAnimation animationH = new HeightChangeAnimation(convertView,
                    AlephListAdapter.ANIM_DURATION,
                    CommUtils.measureExpandedListItemHeight(mActivity),
                    CommUtils.measureCollapsedListItemHeight(mActivity));
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(animation1);
            animation.addAnimation(animation2);
            animation.addAnimation(animationH);
            animation.setDuration(AlephListAdapter.ANIM_DURATION);
            animation.setStartOffset(25*(lastPos-position));
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    isSlideFinished(lastPos-pos);
                    TranslateAnimation animation5 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (float) -1.0,
                            Animation.RELATIVE_TO_PARENT, (float)-1.0, Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0);
                    animation5.setDuration(1000000);
                    animation5.setStartOffset(0);
                    convertView.startAnimation(animation5);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            convertView.startAnimation(animation);
        }
    }

    private void initSlidebackFinished(int st, int en) {
        mSlideBackFinishedB = false;
        mSlideBackFinished = new ArrayList<>();
        for(int i=st;i<=en;i++) {
            mSlideBackFinished.add(false);
        }
    }

    private boolean isSlideFinished(int pos) {
        if(mSlideBackFinishedB) { return true; }

        mSlideBackFinished.set(pos, true);

        for(int i=0;i<mSlideBackFinished.size();i++) {
            if(!mSlideBackFinished.get(i)) {
                return false;
            }
        }
        mSlideBackFinishedB = true;

        showLess();

        return true;
    }

}
