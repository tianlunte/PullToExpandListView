package com.tianlunte.pulltoexpandlistview.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.tianlunte.pulltoexpandlistview.R;
import com.tianlunte.pulltoexpandlistview.animation.HeightChangeAnimation;
import com.tianlunte.pulltoexpandlistview.controller.AlephActivity;
import com.tianlunte.pulltoexpandlistview.model.RestaurantObject;
import com.tianlunte.pulltoexpandlistview.utils.CommUtils;

/**
 * Created by wangqingyun on 1/7/15.
 */
public class AlephListAdapter extends BaseAdapter {
    public static final int ANIM_DURATION = 500;

    private Context mContext;
    private LayoutInflater mInflater;
    private List<RestaurantObject> mList;

    private boolean mShowCollapse;
    private int mFitNum;
    private List<Boolean> mFinished;
    private boolean mFinishedB;
    private boolean mIsSlidingBack;

    public AlephListAdapter(Context context, List<RestaurantObject> list, boolean showCollapse, int fitNum) {
        mContext  = context;
        mInflater = LayoutInflater.from(context);
        mList = list;
        mShowCollapse = showCollapse;
        mFitNum = fitNum;

        mFinished = new ArrayList<>();
        for(int i=0;i<mList.size() && i<mFitNum;i++) {
            mFinished.add(false);
        }
        mFinishedB = false;
        mIsSlidingBack = false;
    }

    public boolean ifAllFinished() {
        if(mFinishedB) {
            return true;
        }

        for(int i=0;i<mFinished.size();i++) {
            if(!mFinished.get(i).booleanValue()) {
                return false;
            }
        }

        mFinishedB = true;

        mContext.sendBroadcast(new Intent(AlephActivity.AlephListViewExpandedReceiver.ACTION));

        return true;
    }

    public void startSlideBack() {
        mIsSlidingBack = true;
    }

    // refer to http://www.javacodegeeks.com/2013/09/android-viewholder-pattern-example.html for viewHolder
    public View getView(final int position, View convertView, ViewGroup root) {

        if(mShowCollapse) {
            convertView = mInflater.inflate(R.layout.aleph_list_item_collapse, null);
            ((TextView) (convertView.findViewById(R.id.aleph_item_collapse_name))).setText(mList.get(position).getName());
        } else {
            convertView = mInflater.inflate(R.layout.aleph_list_item_expand, null);
            ((ImageView) (convertView.findViewById(R.id.aleph_item_expand_img))).setImageResource(mList.get(position).getDrawable());
            ((TextView) (convertView.findViewById(R.id.aleph_item_expand_name))).setText(mList.get(position).getName());
        }

        if(!mShowCollapse && !mIsSlidingBack) {
            if (position <= 1 && !ifAllFinished()) {
                HeightChangeAnimation animationH = new HeightChangeAnimation(convertView,
                        ANIM_DURATION, CommUtils.measureCollapsedListItemHeight(mContext),
                        CommUtils.measureExpandedListItemHeight(mContext));
                animationH.setDuration(ANIM_DURATION);
                animationH.setStartOffset(0);
                convertView.startAnimation(animationH);
                animationH.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {}
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mFinished.set(position, true);
                        ifAllFinished();
                    }
                    @Override
                    public void onAnimationRepeat(Animation animation) {}
                });
                //
                ImageView img = (ImageView)convertView.findViewById(R.id.aleph_item_expand_img);
                TextView tvPeople = (TextView)convertView.findViewById(R.id.aleph_item_expand_txt_people);
                AlphaAnimation animationImgAlpha = new AlphaAnimation((float)0.1, 1);
                animationImgAlpha.setDuration(ANIM_DURATION);
                animationImgAlpha.setStartOffset(25*(position-2));
                img.startAnimation(animationImgAlpha);
            } else if (position < mFitNum && !mFinished.get(position)) {
                TranslateAnimation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (float) -1.0,
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                        Animation.RELATIVE_TO_PARENT, 0);
                AlphaAnimation animation2 = new AlphaAnimation((float) 0.5, 1);
                HeightChangeAnimation animationH = new HeightChangeAnimation(convertView,
                        ANIM_DURATION, CommUtils.measureCollapsedListItemHeight(mContext),
                        CommUtils.measureExpandedListItemHeight(mContext));
                AnimationSet animation = new AnimationSet(false);
                animation.addAnimation(animation1);
                animation.addAnimation(animation2);
                animation.addAnimation(animationH);
                animation.setDuration(ANIM_DURATION);
                animation.setStartOffset(25 * (position - 2));
                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        // this.view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        mFinished.set(position, true);
                        ifAllFinished();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
                convertView.startAnimation(animation);
            } else {
                if(ifAllFinished()) {
                    if(null!=convertView.getTag()) {
                        AnimationSet anim = (AnimationSet)convertView.getTag();
                        anim.cancel();
                    }
                } else {
                    TranslateAnimation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (float) -1.0,
                            Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                            Animation.RELATIVE_TO_PARENT, 0);
                    AlphaAnimation animation2 = new AlphaAnimation((float) 0, 1);
                    AnimationSet animation = new AnimationSet(false);
                    animation.addAnimation(animation1);
                    animation.addAnimation(animation2);
                    animation.setDuration(100000);
                    animation.setStartOffset(0);
                    convertView.setTag(animation);
                    convertView.startAnimation(animation);
                }
            }
        } else if(mIsSlidingBack) {
            TranslateAnimation animation1 = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, (float) -1.0,
                    Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                    Animation.RELATIVE_TO_PARENT, 0);
            AlphaAnimation animation2 = new AlphaAnimation((float)0, (float)0.1);
            AnimationSet animation = new AnimationSet(true);
            animation.addAnimation(animation1);
            animation.addAnimation(animation2);
            animation.setDuration(100000000);
            animation.setStartOffset(0);
            convertView.startAnimation(animation);
        }

        return convertView;
    }

    public int getCount() {
        return mList.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return position;
    }

}
