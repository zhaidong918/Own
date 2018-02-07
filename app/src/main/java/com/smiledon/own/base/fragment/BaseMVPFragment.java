package com.smiledon.own.base.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.smiledon.own.base.BasePresenter;
import com.smiledon.own.base.IBaseFragment;
import com.smiledon.own.base.IBaseModel;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.utils.ToastUtils;

/**
 * Created by Horrarndoo on 2017/9/6.
 * <p>
 * Mvp Fragment基类
 * <p>
 * 实现IBaseView方法、绑定butterknife
 */

public abstract class BaseMVPFragment<P extends BasePresenter, M extends IBaseModel> extends
        BaseFragment implements IBaseFragment {
    public P mPresenter;
    public M mIMode;

    /**
     * 在监听器之前把数据准备好
     */
    public void initData() {
        super.initData();

        mPresenter = (P) initPresenter();
        if (mPresenter != null) {
            mIMode = (M) mPresenter.getModel();
            if (mIMode != null) {
                mPresenter.attachMV(mIMode, this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachMV();
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
        ToastUtils.showToast(mContext, msg, Toast.LENGTH_SHORT);
    }

    @Override
    public void back() {
        this.onBackPressedSupport();
    }

    @Override
    public void startNewFragment(@NonNull Fragment supportFragment) {
//        start(supportFragment);
    }

    @Override
    public void startNewFragmentWithPop(@NonNull Fragment supportFragment) {
//        startWithPop(supportFragment);
    }

    @Override
    public void startNewFragmentForResult(@NonNull Fragment supportFragment, int
            requestCode) {
//        startForResult(supportFragment, requestCode);
    }

    @Override
    public void popToFragment(Class<?> targetFragmentClass, boolean includeTargetFragment) {
//        popTo(targetFragmentClass, includeTargetFragment);
    }

    @Override
    public void hideKeybord() {
//        hideSoftInput();
    }

    @Override
    public void setOnFragmentResult(int ResultCode, Bundle data) {
//        setFragmentResult(ResultCode, data);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz) {
        ((BaseActivity) mActivity).startActivity(clz);
    }

    @Override
    public void startNewActivity(@NonNull Class<?> clz, Bundle bundle) {
        ((BaseActivity) mActivity).startActivity(clz, bundle);
    }

    @Override
    public void startNewActivityForResult(@NonNull Class<?> clz, Bundle bundle, int requestCode) {
        ((BaseActivity) mActivity).startActivityForResult(clz, bundle, requestCode);
    }

    @Override
    public boolean isVisiable() {
//        return isSupportVisible();
        return false;
    }

    @Override
    public Activity getBindActivity() {
        return mActivity;
    }
}