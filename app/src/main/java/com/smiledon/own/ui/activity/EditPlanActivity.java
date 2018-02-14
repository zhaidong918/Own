package com.smiledon.own.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.smiledon.own.R;
import com.smiledon.own.app.OwnConfig;
import com.smiledon.own.base.activity.BaseActivity;
import com.smiledon.own.databinding.ActivityEditPlanBinding;
import com.smiledon.own.service.model.Plan;
import com.smiledon.own.utils.ResourcesUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 编辑任务页面
 *
 * @author zhaidong
 * @date 2018/2/13 9:18
 */

public class EditPlanActivity extends BaseActivity {

    private final String YYYY_MM_DD = "yyyy-MM-dd";
    private final String HH_MM = "HH:mm";

    private ActivityEditPlanBinding mBinding;
    private ArrayAdapter timeAdapter;
    private String[] timeQuantums;

    private ArrayAdapter planTypeAdapter;
    private String[] planTypes;

    private ArrayAdapter importanceAdapter;
    private String[] importances;

    private ArrayAdapter instancyAdapter;
    private String[] instancys;

    private Plan mPlan;

    @Override
    protected View createContentView() {
        mBinding = inflate(R.layout.activity_edit_plan);
        return mBinding.getRoot();
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

        mBinding.planDateTv.setText(new SimpleDateFormat(YYYY_MM_DD).format(new Date()));

        initData();
        initEvent();
    }

    private void initData() {

        mPlan = new Plan();

        List<String> timeQuantum = new ArrayList<>();
        StringBuilder builder;
        for (int i = 0; i < 24; i++) {
            builder = new StringBuilder();
            if(i < 10) builder.append("0");
            builder.append(i).append(":");
            for (int j = 0; j < 2; j++) {
                builder.delete(3, builder.length());
                if(j*30 < 10) builder.append("0");
                builder.append(j*30);
                timeQuantum.add(builder.toString());
            }
        }
        timeQuantums = new String[48];
        timeAdapter = getArrayAdapter(timeQuantum.toArray(timeQuantums));
        //设置下拉列表的风格,simple_spinner_dropdown_item是android系统自带的样式，等会自定义修改
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mBinding.timeSpinner.setAdapter(timeAdapter);

        planTypes = ResourcesUtils.getStringArray(R.array.plan_type);
        planTypeAdapter = getArrayAdapter(planTypes);
        mBinding.typeSpinner.setAdapter(planTypeAdapter);

        importances = ResourcesUtils.getStringArray(R.array.plan_importance);
        importanceAdapter = getArrayAdapter(importances);
        mBinding.importanceSpinner.setAdapter(importanceAdapter);

        instancys = ResourcesUtils.getStringArray(R.array.plan_instancy);
        instancyAdapter = getArrayAdapter(instancys);
        mBinding.instancySpinner.setAdapter(instancyAdapter);
    }

    private ArrayAdapter getArrayAdapter(String[] data) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, android.R.layout.simple_dropdown_item_1line, data);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    private void initEvent() {
        RxTextView.textChanges(mBinding.planEt)
                .subscribe(charSequence ->
                        mBinding.addPlanBtn.setEnabled(!TextUtils.isEmpty(charSequence)));

        mBinding.planDateTv.setOnClickListener(v -> {
            showDateDialog();
        });
    }

    private DatePickerDialog datePickerDialog;

    private void showDateDialog() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            if (datePickerDialog == null) {
                datePickerDialog = new DatePickerDialog(this);
                datePickerDialog.setOnDateSetListener((view, year, month, dayOfMonth) -> {

                    StringBuilder builder = new StringBuilder();
                    builder.append(year);
                    builder.append("-");
                    if(month + 1 < 10) builder.append(0);
                    builder.append(month + 1);
                    builder.append("-");
                    if(dayOfMonth < 10) builder.append(0);
                    builder.append(dayOfMonth);


                    mBinding.planDateTv.setText(builder.toString());
                });
            }

            datePickerDialog.show();
        }
    }

    /**
     * 保存当前计划
     * @param view
     */
    public void addPlan(View view) {

        //保存完成类型
        mPlan.setIs_complete(OwnConfig.Plan.TabOne.NOT_COMPLETE);

        //保存计划类型
        switch (mBinding.typeSpinner.getSelectedItemPosition()) {
            case 0:
                mPlan.setType(OwnConfig.Plan.TabThree.TYPE_LIFT);
                break;
            case 1:
                mPlan.setType(OwnConfig.Plan.TabThree.TYPE_WORK);
                break;
        }
        //保存计划重要紧急程度
        int importance = mBinding.importanceSpinner.getSelectedItemPosition();
        int instancy = mBinding.instancySpinner.getSelectedItemPosition();
        if (importance == 0 && instancy == 0)
            mPlan.setLevel(OwnConfig.Plan.TabTwo.NOW);
        else if (importance == 0 && instancy == 1)
            mPlan.setLevel(OwnConfig.Plan.TabTwo.IMPORTANCE);
        else if (importance == 1 && instancy == 0)
            mPlan.setLevel(OwnConfig.Plan.TabTwo.INSTANCY);
        else if (importance == 1 && instancy == 1)
            mPlan.setLevel(OwnConfig.Plan.TabTwo.OTHER);

        //保存目标时间
        savePlanDate();

        //保存创建时间
        mPlan.setCreate_date(new Date());

        //保存计划内容
        mPlan.setPlan(mBinding.planEt.getText().toString().trim());

        mPlan.save();

        finish();
    }

    private void savePlanDate() {
        StringBuilder builder = new StringBuilder();
        builder.append(mBinding.planDateTv.getText().toString());
        builder.append(OwnConfig.BLANK);
        builder.append(timeQuantums[mBinding.timeSpinner.getSelectedItemPosition()]);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(YYYY_MM_DD + HH_MM);
        try {
            mPlan.setDate(simpleDateFormat.parse(builder.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



}
