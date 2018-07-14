package com.smiledon.library.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import java.text.NumberFormat;



public class NumberDigitsTextWatcher implements TextWatcher {

    private EditText mEditText;
    private int mBeforeDotLength;
    private int mAfterDotLength;

    private NumberFormat mNumberFormat;
    private OnTextChangedListener mOnTextChangedListener;

    public NumberDigitsTextWatcher(EditText editText, int beforeDotLength, int afterDotLength, OnTextChangedListener onTextChangedListener) {

        this.mEditText = editText;
        this.mBeforeDotLength = beforeDotLength;
        this.mAfterDotLength = afterDotLength;
        this.mOnTextChangedListener = onTextChangedListener;
        this.mNumberFormat = NumberFormat.getNumberInstance();
        mNumberFormat.setGroupingUsed(false);
        mNumberFormat.setMaximumFractionDigits(afterDotLength);

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {

        boolean isAlert = false;
        String value = s.toString();

        //没有小数位的时候替换掉小数点
        if (mAfterDotLength == 0 && value.contains(".")) {
            value = value.replace(".", "");
            isAlert = true;
        }
        //如果第一个是.就补全
        if (value.startsWith(".")) {
            value = "0" + value;
            isAlert = true;
        }

        //去掉前面重复的0
        if (value.startsWith("0") && value.indexOf(".") != 1) {
            try {
                value = mNumberFormat.format(Double.valueOf(value));
            }
            catch (Exception e){
                Toast.makeText(mEditText.getContext(), "Set EditText inputType to Decimal", Toast.LENGTH_LONG).show();
                value = "0";
            }
            isAlert = true;

        }


        if (value.contains(".")) {

            String[] values = value.split("\\.");

            //有小数点  整数位数大于约定 或 小数位数有内容 并且大于约定
            if (values[0].length() > mBeforeDotLength
                    || (values.length > 1 && values[1].length() > mAfterDotLength)) {

                String beforeDot = values[0];
                //截取小数点前面的位数
                if (values[0].length() > mBeforeDotLength) {
                    beforeDot = values[0].substring(0, mBeforeDotLength);
                }

                String afterDot = ".";

                //截取小数点后面的位数
                if (values.length > 1) {

                    afterDot = values[1];

                    if (values[1].length() > mAfterDotLength) {
                        afterDot = values[1].substring(0, mAfterDotLength);
                    }
                }

                value = beforeDot + "." + afterDot;
                isAlert = true;
            }
        }
        else{
            //不含有小数点且整数位数大于约定
            if (value.length() > mBeforeDotLength) {
                value = value.substring(0, mBeforeDotLength);
                isAlert = true;
            }
        }

        if (isAlert) {
            mEditText.removeTextChangedListener(this);
            mEditText.setText(value);
            mEditText.setSelection(mEditText.getText().length());
            mEditText.addTextChangedListener(this);
        }

        if(mOnTextChangedListener != null)
            mOnTextChangedListener.afterTextChanged(value);
    }

    public interface OnTextChangedListener{
        void afterTextChanged(String value);
    }
}