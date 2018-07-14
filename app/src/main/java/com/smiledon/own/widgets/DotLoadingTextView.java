package com.smiledon.own.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

import com.smiledon.own.helper.RxHelper;
import com.smiledon.own.utils.LogUtil;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * 带点点点的加载动画
 * Created by 95867 on 2018/4/8.
 */

public class DotLoadingTextView extends TextView{

    private static final String DOT = ".";
    private static final String DOTS = "...";

    private String mContent;

    private Disposable mDisposable;

    public DotLoadingTextView(Context context) {
        this(context, null);
    }

    public DotLoadingTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotLoadingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        String content = getText().toString();

        if (content.endsWith(DOTS)) {
            startLoadingAnim(content);
        }
    }

    private void startLoadingAnim(String content) {

        mContent = content.substring(0, content.length() - 3);

        mDisposable = Observable.interval(0, 500, TimeUnit.MILLISECONDS)
                .map(aLong -> {
                    int length = (int) (aLong % 4);
                    String dots = "";
                    for (int i = 0; i < length; i++) {
                        dots += DOT;
                    }
                    return dots;
                })
                .compose(RxHelper.rxSchedulerHelper())
                .subscribe(dots -> {
//                    LogUtil.d("还在的打印中.....");
                    setText(mContent + dots);
                });
    }

    public void stopLoadingAnim() {
        if(mDisposable != null)
            mDisposable.dispose();
    }
}
