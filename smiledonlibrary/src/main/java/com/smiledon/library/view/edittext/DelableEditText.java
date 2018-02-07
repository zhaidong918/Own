package com.smiledon.library.view.edittext;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;


/**
 * Created by ZhaiDong on 2017/7/14.
 */

public class DelableEditText extends EditText {

    private Context mContext;

    private Drawable imgAble;
    private Drawable imgInable;


    public DelableEditText(Context context) {
       super(context);
        init(context);
    }

    public DelableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DelableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mContext = context;

        imgAble = context.getResources().getDrawable(android.R.drawable.ic_delete);
        imgInable = context.getResources().getDrawable(android.R.drawable.screen_background_light_transparent);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        drawableLeft();
    }

    private void drawableLeft(){
        if(length() > 0){
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgAble, null);
        }
        else{
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgInable, null);
        }
    }

    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgAble != null
                && event.getAction() == MotionEvent.ACTION_UP) {
            int eventX = (int) event.getRawX();
            int eventY = (int) event.getRawY();

            Rect rect = new Rect();
            getGlobalVisibleRect(rect);
            rect.left = rect.right - 50;
            if(rect.contains(eventX, eventY))
                setText("");
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }
}
