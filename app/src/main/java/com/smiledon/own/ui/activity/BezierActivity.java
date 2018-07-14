package com.smiledon.own.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityBezierBinding;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/5 17:23
 */

public class BezierActivity extends BaseActivity {

    private static final int BLUE = R.color.blue_color;
    private static final int ORANGE = R.color.orange_color;

    ActivityBezierBinding mBinding;

    private int mThemeColor = BLUE;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_bezier);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mBinding.submitBtn.setOnClickListener(l->{
            mBinding.submitBtn.startSubmit();
            mBinding.loadingRompView.startLoading();
        });

        mBinding.loadView.setOnClickListener(l-> mBinding.loadView.startLoading());

        mBinding.loadOkView.setOnClickListener(l->{
            mBinding.loadOkView.startLoading();

            Observable.timer((long) (Math.random()*3000), TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> mBinding.loadOkView.startLoadingComplete(true));

        });

        mBinding.loadErrorView.setOnClickListener(l->{
            mBinding.loadErrorView.startLoading();

            Observable.timer((long) (Math.random() * 3000), TimeUnit.MILLISECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> mBinding.loadErrorView.startLoadingComplete(false));
        });

        mBinding.switchBtn.setOnClickListener(l->{
//            takeScreenShot();
        });
    }

    private void takeScreenShot() {

        Observable.just(getScreenShot())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> {
                    mBinding.overlayLayout.setBackground(new BitmapDrawable(getResources(), bitmap));

                    if(mThemeColor == BLUE)
                        mThemeColor = ORANGE;
                    else if(mThemeColor == ORANGE)
                        mThemeColor = BLUE;

                    mBinding.topWaveView.setWaveColor(mThemeColor);
                    mBinding.bottomWaveView.setWaveColor(mThemeColor);
                    mBinding.submitBtn.setButtonColor(mThemeColor);

                    startSwitch(mBinding.switchBtn.getWidth()/2, mBinding.switchBtn.getHeight()/2);
                });
    }

    private Bitmap getScreenShot() {
        Bitmap bitmap = Bitmap.createBitmap(mBinding.getRoot().getWidth(),
                mBinding.getRoot().getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        mBinding.getRoot().draw(canvas);
        return bitmap;
    }


    private void startSwitch(int centerX, int centerY) {

        // get the final radius for the clipping circle
        int dx = Math.max(centerX, mBinding.getRoot().getWidth() - centerX);
        int dy = Math.max(centerY, mBinding.getRoot().getWidth() - centerY);
        double finalRadius = Math.hypot(dx, dy);

        // Android native animator
        Animator animator = ViewAnimationUtils.createCircularReveal(mBinding.contentLayout, centerX, centerY, 0f, (float) finalRadius);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(500);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBinding.overlayLayout.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));
            }
        });

        animator.start();
    }
}
