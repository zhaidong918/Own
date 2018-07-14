package com.smiledon.own.base.activity;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.ConnectivityManager;
import android.net.LinkProperties;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.smiledon.own.R;
import com.smiledon.own.app.AppApplication;
import com.smiledon.own.app.AppManager;
import com.smiledon.own.app.RxBus;
import com.smiledon.own.utils.AppUtils;
import com.smiledon.own.utils.LogUtil;
import com.smiledon.own.utils.ToastUtils;
import com.smiledon.own.widgets.WaitProgressDialog;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import io.reactivex.functions.Consumer;


/**
 *
 * Activity基类
 *
 * @author East Chak
 * @date 2018/1/3 9:48
 */
public abstract class BaseActivity extends RxAppCompatActivity {


    protected AppApplication mApplication;

    protected WaitProgressDialog mWaitProgressDialog;

    protected Context mContext;//全局上下文对象

    static {
        //5.0以下兼容vector
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mContext = AppUtils.getContext();
        mApplication = AppApplication.getInstance();
        mWaitProgressDialog = new WaitProgressDialog(this);

        setContentView(createContentView());

        initView(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        registerNetStatusListener();

        initTitle();

    }

    private Toolbar toolbar;
    private TextView textView;
    private int tipsHeight;

    protected void initTitle() {
        toolbar = findViewById(R.id.toolbar);
        textView = findViewById(R.id.tips_tv);

        if (toolbar != null) {
            toolbar.bringToFront();
        }

        if (textView != null) {
            textView.post(() -> tipsHeight = textView.getHeight());
        }
    }

    protected void registerNetStatusListener(){

        //targetSdkVersion在24及以上时可采用此方式监听网络
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest networkRequest = new NetworkRequest.Builder().build();
        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            //存在可用网络
            @Override
            public void onAvailable(Network network) {
                LogUtil.i("network: " + network.toString());
                hideTipsView();
            }

            //个人理解：连接到网络且可用
            @Override
            public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
                super.onCapabilitiesChanged(network, networkCapabilities);
            }

            //不存在可用网络
            @Override
            public void onLost(Network network) {
                LogUtil.i("onLost:" + network.toString());
                showTipsView();
            }

        };

        /*connectivityManager.requestNetwork(networkRequest, networkCallback);
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
        connectivityManager.unregisterNetworkCallback(networkCallback);*/

        RxBus.getIntanceBus().addSubscription(RxBus.Event.NET_STATUS,
                RxBus.getIntanceBus().doSubscribe(boolean.class, (Boolean aBoolean) -> {
                    if (aBoolean) {
                        ToastUtils.showToast("网络恢复全力加载");
                    }
                    else {
                        ToastUtils.showToast("网络点都不给力");
                    }
                }, throwable -> {
                    ToastUtils.showToast(throwable.getMessage());
                }));
    }

    protected void showTipsView() {
        if (textView != null) {
            textView.animate().translationYBy(tipsHeight).setDuration(100).start();
        }
    }

    protected void hideTipsView() {
        if (textView != null) {
            textView.animate().translationYBy(-tipsHeight).setDuration(100).start();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        RxBus.getIntanceBus().unSubscribe(RxBus.Event.NET_STATUS);
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
     * final定义，不允许子类重写此方法
     */
    public View inflateContentView(int layout_id) {
        return LayoutInflater.from(mContext).inflate(layout_id, null);
    }

    /**
     * 显示提示框
     *
     * @param msg 提示框内容字符串
     */
    protected void showProgressDialog(String msg) {
        mWaitProgressDialog.setMessage(msg);
        mWaitProgressDialog.show();
    }

    /**
     * 隐藏提示框
     */
    protected void hideProgressDialog() {
        if (mWaitProgressDialog != null) {
            mWaitProgressDialog.dismiss();
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
    }

}
