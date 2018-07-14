package com.smiledon.own.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MotionEvent;
import android.view.View;

import com.smiledon.own.R;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityNestScrollBinding;
import com.smiledon.own.databinding.ActivityNestScrollItemBinding;
import com.smiledon.own.ui.adapter.BaseBindingAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * file description
 *
 * @auther ZhaiDong
 * @date 2018/7/3 10:48
 */

public class NestScrollActivity extends BaseActivity {

    ActivityNestScrollBinding mBinding;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_nest_scroll);
        return mBinding.getRoot();
    }

    int count = 0;

    @Override
    protected void initView(Bundle savedInstanceState) {

        mBinding.bounceTv.setOnClickListener(v -> mBinding.bounceTv.setText(count += 1548.165));

    }


    class Entry {

        public Entry left;
        public Entry right;
        public Entry parent;

    }

    /*
                    x
                   / \
                  A   y
                     / \
                    B   C
     */

    public void rotateLeft(Entry x) {

        if (x != null) {

            Entry y = x.right;  //  y
            //x.right 指向y.left
            //y.left.parent 指向 x
            x.right = y.left;   //  B
            if (y.left != null)
                y.left.parent = x; //

            y.parent = x.parent;
            if (x.parent.left == x)
                x.parent.left = y;
            else
                x.parent.right = y;

            y.left = x;
            x.parent = y;
        }
    }

    /*
                    x
                   / \
                  y   A
                 / \ / \
                    B   C
     */
    public void rotate(Entry x) {

        Entry y = x.left;

        x.left = y.right;
        y.right.parent = x;

        y.parent = x.parent;
        if (x.parent.left == x) {
            x.parent.left = y;
        }
        else{
            x.parent.right = y;
        }

        y.right = x;
        x.parent = y;





    }



}
