package com.smiledon.own.base.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.smiledon.own.app.AppApplication;
import com.smiledon.own.app.AppManager;
import com.smiledon.own.utils.AppUtils;
import com.smiledon.own.utils.StatusBarUtils;
import com.smiledon.own.widgets.WaitPorgressDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;


/**
 *
 * Activity基类
 *
 * @author East Chak
 * @date 2018/1/3 9:48
 */
public abstract class BaseActivity extends RxAppCompatActivity {
    protected AppApplication mApplication;
    protected WaitPorgressDialog mWaitPorgressDialog;
    protected Context mContext;//全局上下文对象
    protected boolean isTransAnim;

    static {
        //5.0以下兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }


    private void init(Bundle savedInstanceState) {
//        setTheme(ThemeUtils.themeArr[SpUtils.getThemeIndex(this)][SpUtils.getNightModel(this) ? 1 : 0]);
        setContentView(createContentView());
        StatusBarUtils.setTransparent(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initData();
        initView(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        mContext = AppUtils.getContext();
        mApplication = AppApplication.getInstance();
        mWaitPorgressDialog = new WaitPorgressDialog(this);
        isTransAnim = true;
    }

    /**
     * 获取当前layouty的布局ID,用于设置当前布局
     * <p>
     * 交由子类实现
     *
     * @return layout Id
     */
    protected abstract View createContentView();

    /**
     * 初始化view
     * <p>
     * 子类实现 控件绑定、视图初始化等内容
     *
     * @param savedInstanceState savedInstanceState
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * final定义，不允许子类重写此方法
     */
    public final <T extends ViewDataBinding> T inflate(int layout_id) {
        return DataBindingUtil.inflate(LayoutInflater.from(this), layout_id, null, false);
    }

    /**
     * 显示提示框
     *
     * @param msg 提示框内容字符串
     */
    protected void showProgressDialog(String msg) {
        mWaitPorgressDialog.setMessage(msg);
        mWaitPorgressDialog.show();
    }

    /**
     * 隐藏提示框
     */
    protected void hideProgressDialog() {
        if (mWaitPorgressDialog != null) {
            mWaitPorgressDialog.dismiss();
        }
    }

    /**
     * [页面跳转]
     *
     * @param clz 要跳转的Activity
     */
    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    public void startActivity(Class<?> clz) {
        startActivity(clz, null);
    }


    /**
     * [携带数据的页面跳转]
     *
     * @param clz    要跳转的Activity
     * @param bundle bundel数据
     */
    public void startActivity(Class<?> clz, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        if (isTransAnim) {
           /* overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                    .activity_start_zoom_out);*/
        }
    }

    public void startActivityForResult(Class<?> clz,int requestCode) {
        startActivityForResult(clz, null, requestCode);
    }

    /**
     * [含有Bundle通过Class打开编辑界面]
     *
     * @param clz         要跳转的Activity
     * @param bundle      bundel数据
     * @param requestCode requestCode
     */
    public void startActivityForResult(Class<?> clz, Bundle bundle,int requestCode) {
        Intent intent = new Intent();
        intent.setClass(this, clz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        if (isTransAnim) {
           /* overridePendingTransition(R.anim.activity_start_zoom_in, R.anim
                    .activity_start_zoom_out);*/
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.ECLAIR)
    @Override
    public void finish() {
        super.finish();
        if (isTransAnim) {
           /* overridePendingTransition(R.anim.activity_finish_trans_in, R.anim
                    .activity_finish_trans_out);*/
        }
    }

    /**
     * 隐藏键盘
     *
     * @return 隐藏键盘结果
     * <p>
     * true:隐藏成功
     * <p>
     * false:隐藏失败
     */
    protected boolean hiddenKeyboard() {
        //点击空白位置 隐藏软键盘
        InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService
                (INPUT_METHOD_SERVICE);
        return mInputMethodManager.hideSoftInputFromWindow(this
                .getCurrentFocus().getWindowToken(), 0);
    }

    protected void initTitleBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //设置toolbar的图标
//        toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white);
        //设置toolbar的图标的点击事件
//        toolbar.setNavigationOnClickListener(new IView.OnClickListener() {
//            @Override
//            public void onClick(IView view) {
//                onBackPressedSupport();
//            }
//        });
    }

    /**
     * 是否使用overridePendingTransition过度动画
     * @return 是否使用overridePendingTransition过度动画，默认使用
     */
    protected boolean isTransAnim() {
        return isTransAnim;
    }

    /**
     * 设置是否使用overridePendingTransition过度动画
     */
    protected void setIsTransAnim(boolean b){
        isTransAnim = b;
    }
}
