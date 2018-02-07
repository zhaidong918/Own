package com.smiledon.library.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;

import com.smiledon.library.R;

/**
 * Created by ZhaiDong on 2017/7/13.
 *
 *只能输入整数或者小数的输入框
 * 用于限制小数点位数  以及所输入的最大值
 */
public class NumberDecimalEditText extends EditText {
    
    private static final String TAG = NumberDecimalEditText.class.getSimpleName();

    private int beforePointLength;
    private int afterPointLength;
    private int inputType;

    public NumberDecimalEditText(Context context) {
        super(context);
    }

    public NumberDecimalEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.NumberDecimalEditText);
        beforePointLength = typedArray.getInteger(R.styleable.NumberDecimalEditText_beforePointLength, 0);
        afterPointLength = typedArray.getInteger(R.styleable.NumberDecimalEditText_afterPointLength, 0);


        typedArray.recycle();

        //只能输入数字小数点
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }

    public NumberDecimalEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

//        if(inputType != InputType.TYPE_CLASS_NUMBER
//                && inputType != InputType.TYPE_NUMBER_FLAG_DECIMAL )
//            return;

        String input  = text.toString().trim();

        /** 去掉数字前面多于的0 */
        if(input.startsWith("0")
                && input.length() > 1
                && !".".equals(input.substring(1, 2))){
            setTextAndSelection(input.substring(1));
        }

        if(input.contains(".")){
            int pointPosition = input.lastIndexOf(".");

            /** 小数点开头自动加上0 */
            if(input.startsWith(".")){
                setTextAndSelection("0" + input);
            }

            /** 小数点后的位数大于指定位数 更至指定小数位数 */
            if(input.substring(pointPosition, input.length()).length() - 1 > afterPointLength){
                setTextAndSelection(input.substring(0, pointPosition + 1 + afterPointLength));
            }

            /** 小数点前的位数大于指定位数 更至指定位数 */
            if(input.substring(0, pointPosition).length() > beforePointLength){
                setTextAndSelection(input.substring(0, beforePointLength) + input.substring(pointPosition));
            }
        }
        else{
            if(input.length() > beforePointLength){
                setTextAndSelection(input.substring(0, beforePointLength));
            }
        }
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        String input = getText().toString().trim();

        if(!focused){
            if(input.contains(".")){
                if(input.endsWith(".")){
                    setText(input.substring(0, input.length() - 1 ));
                }
                if(input.length() > 1
                        && input.startsWith(".")){
                    setText("0" + input);
                }
            }
        }
    }

    /**
     * textview设置值以及光标位置
     * @param text
     */
    private void setTextAndSelection(String text){
        setText(text);
        setSelection(text != null ? text.length() : 0);
    }

}
