package com.tianlunte.pulltoexpandlistview.controller;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.tianlunte.pulltoexpandlistview.R;
import com.tianlunte.pulltoexpandlistview.model.RestaurantObject;
import com.tianlunte.pulltoexpandlistview.utils.CommUtils;
import com.tianlunte.pulltoexpandlistview.view.AlephListView;

/**
 * Created by wangqingyun on 1/7/15.
 */
public class AlephActivity extends Activity {

    private AlephListView mListView;
    private RelativeLayout.LayoutParams mListViewParams;

    private List<RestaurantObject> mRestaurantsList;

    private AlephListViewExpandedReceiver mAlephExpandedReceiver;

    private RelativeLayout mTopBar;
    private RelativeLayout mBtmBar;

    private Button mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aleph);

        mListView = (AlephListView)findViewById(R.id.aleph_listview);

        mTopBar = (RelativeLayout)findViewById(R.id.aleph_top_bar);
        mBtmBar = (RelativeLayout)findViewById(R.id.aleph_btm_bar);

        mBtnBack = (Button)findViewById(R.id.aleph_back_arrow);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfold();
            }
        });

        mRestaurantsList = new ArrayList<>();
        mRestaurantsList.add(new RestaurantObject("KFC", R.drawable.kfc));
        mRestaurantsList.add(new RestaurantObject("StarBucks", R.drawable.starbucks));
        mRestaurantsList.add(new RestaurantObject("Burger King", R.drawable.burger_king));
        mRestaurantsList.add(new RestaurantObject("Chipotle", R.drawable.chipotle));
        mRestaurantsList.add(new RestaurantObject("Jimmy Monkey", R.drawable.jimmy_monkey));
        mRestaurantsList.add(new RestaurantObject("McDonalds", R.drawable.mcdonalds));
        mRestaurantsList.add(new RestaurantObject("Pizza Hut", R.drawable.pizza_hut));
        mRestaurantsList.add(new RestaurantObject("Wendy's", R.drawable.wendys));
        mRestaurantsList.add(new RestaurantObject("KFC 2", R.drawable.kfc));
        mRestaurantsList.add(new RestaurantObject("StarBucks 2", R.drawable.starbucks));
        mRestaurantsList.add(new RestaurantObject("Burger King 2", R.drawable.burger_king));
        mRestaurantsList.add(new RestaurantObject("Chipotle 2", R.drawable.chipotle));
        mRestaurantsList.add(new RestaurantObject("Jimmy Monkey 2", R.drawable.jimmy_monkey));
        mRestaurantsList.add(new RestaurantObject("McDonalds 2", R.drawable.mcdonalds));
        mRestaurantsList.add(new RestaurantObject("Pizza Hut 2", R.drawable.pizza_hut));
        mRestaurantsList.add(new RestaurantObject("Wendy's 2", R.drawable.wendys));


        mListView.setupList(this, mRestaurantsList);
        mListView.showLess();
    }

    @Override
    public void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(AlephListViewExpandedReceiver.ACTION);
        mAlephExpandedReceiver = new AlephListViewExpandedReceiver();
        registerReceiver(mAlephExpandedReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();

        unregisterReceiver(mAlephExpandedReceiver);
    }

    public int getStatusBarHeight() {
        Rect rectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        int statusBarHeight = rectangle.top;

        return statusBarHeight;
    }

    public void uponListViewExpanded() {
        mListViewParams = (RelativeLayout.LayoutParams)mListView.getLayoutParams();
        ValueAnimator animListV = ValueAnimator.ofInt(0, CommUtils.getPxFromDp(this, 45));
        animListV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mListViewParams.topMargin = (Integer) valueAnimator.getAnimatedValue();
                mListViewParams.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                mListView.setLayoutParams(mListViewParams);
            }
        });
        //
        ObjectAnimator animTopBar = ObjectAnimator.ofFloat(mTopBar, "translationY", -CommUtils.getPxFromDp(this, 45), 0);
        ObjectAnimator animBtmBar = ObjectAnimator.ofFloat(mBtmBar, "translationY", CommUtils.getPxFromDp(this, 45), 0);

        ArrayList<Animator> animations = new ArrayList<>();

        animations.add(animListV);
        animations.add(animTopBar);
        animations.add(animBtmBar);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setStartDelay(50);
        set.playTogether(animations);
        set.start();
    }

    public void unfold() {
        mListViewParams = (RelativeLayout.LayoutParams)mListView.getLayoutParams();
        ValueAnimator animListV = ValueAnimator.ofInt(CommUtils.getPxFromDp(this, 45), 0);
        animListV.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mListViewParams.topMargin = (Integer) valueAnimator.getAnimatedValue();
                mListViewParams.bottomMargin = (Integer) valueAnimator.getAnimatedValue();
                mListView.setLayoutParams(mListViewParams);
            }
        });
        //
        ObjectAnimator animTopBar = ObjectAnimator.ofFloat(mTopBar, "translationY", 0, -CommUtils.getPxFromDp(this, 45));
        ObjectAnimator animBtmBar = ObjectAnimator.ofFloat(mBtmBar, "translationY", 0, CommUtils.getPxFromDp(this, 45));

        ArrayList<Animator> animations = new ArrayList<>();

        animations.add(animListV);
        animations.add(animTopBar);
        animations.add(animBtmBar);

        AnimatorSet set = new AnimatorSet();
        set.setDuration(500);
        set.setStartDelay(0);
        set.playTogether(animations);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {}
            @Override
            public void onAnimationEnd(Animator animation) {
                new AsyncTask<String, Void, Void>() {
                    @Override
                    protected Void doInBackground(String... params) {
                        try {
                            Thread.sleep(50);
                        } catch(Exception e) {}
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mListView.slideBackToLess();
                            }
                        });
                        return null;
                    }
                }.execute();
            }
            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });

        set.start();
    }

    public class AlephListViewExpandedReceiver extends BroadcastReceiver {
        public static final String ACTION = "aleph.alephactivity.listviewexpandedreceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            uponListViewExpanded();
        }
    }

}