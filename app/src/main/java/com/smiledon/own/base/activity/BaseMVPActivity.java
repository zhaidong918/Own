package com.smiledon.own.base.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.smiledon.own.base.BasePresenter;
import com.smiledon.own.base.IBaseActivity;
import com.smiledon.own.base.IBaseModel;
import com.smiledon.own.utils.ToastUtils;


/**
 * Created by Horrarndoo on 2017/4/6.
 * <p>
 * Mvp Activity基类
 */
public abstract class BaseMVPActivity<P extends BasePresenter, M extends IBaseModel> extends
        BaseActivity implements IBaseActivity {
    /**
     * presenter 具体的presenter由子类确定
     */
    protected P mPresenter;

    /**
     * model 具体的model由子类确定
     */
    private M mIMode;

    /**
     * 初始化数据
     * <p>
     * 子类可以复写此方法初始化子类数据
     */
    protected void initData() {
        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mIMode = (M) mPresenter.getModel();
            if (mIMode != null) {
                mPresenter.attachMV(mIMode, this);
            }
            //Logger.d("attach M V success.");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachMV();
            //Logger.d("detach M V success.");
        }
    }

    @Override
    public void showLoadingDialog(String msg) {
        showProgressDialog(msg);
    }

    @Override
    public void dismissLoadingDialog() {
        hideProgressDialog();
    }

    @Override
    public void showToast(String msg) {
        ToastUtils.showToast(msg);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        startActivity(clz, bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        startActivityForResult(clz, bundle, requestCode);
    }

    @Override
    public void back() {
//        super.onBackPressedSupport();
    }
}
