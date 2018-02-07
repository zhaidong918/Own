package com.smiledon.library.view.datepicker;


/**
 * The interface of DatePicker{@link cn.aigestudio.datepicker.views.DatePicker}
 * All of method you can invoke will be defined here.
 * @author XZ
 */
public interface IPick {
    /**
     * Set the callback interface when date picked.
     */
    void setOnDateSelected(OnDateSelected onDateSelected);

    /**
     * Set the primary colour of DatePicker{@link cn.aigestudio.datepicker.views.DatePicker}
     * By default,DatePicker's tone consists of four colors black-white-gray and primary colour,
     * you can use this method set the primary colour.
     */
    void setColor(int color);

    /**
     * �����Ƿ���ʾũ��
     * Set is lunar display.
     */
    void isLunarDisplay(boolean display);
    
    /**
     * ����������ѡ�����
     * @param type
     */
    void setEventType(MonthView.EventType type);
}
