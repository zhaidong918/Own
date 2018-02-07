package com.smiledon.library.view.datepicker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日历月视图
 * @author XZ
 */
public class MonthView extends View implements ValueAnimator.AnimatorUpdateListener {
    private TextPaint mTextPaint;
    private Scroller mScroller;

    private CalendarBiz mCalendarBiz;

    private OnPageChangeListener onPageChangeListener;
    private OnSizeChangedListener onSizeChangedListener;

    private int sizeBase;
    private int lastPointX;
    private int lastMoveX;
    private int width;
    private int criticalWidth;
    private int index;
    private int lastMonth, currentMonth, nextMonth;
    private int lastYear, currentYear, nextYear;
    private int currentDay;
    private int animZoomOut1, animZoomIn1, animZoomOut2;
    private int circleRadius;
    private int colorMain = 0xFFE95344;

    private float textSizeGregorian, textSizeLunar;
    private float offsetYLunar;

    private boolean isLunarShow = true;

    private EventType mEventType = EventType.SINGLE;

    private Map<Integer, List<Region>> calendarRegion = new HashMap<Integer, List<Region>>();
    private Region[][] mRegion = new Region[6][7];
    private Map<String, BGCircle> circlesAppear = new HashMap<String, BGCircle>();
    private Map<String, BGCircle> circlesDisappear = new HashMap<String, BGCircle>();
    private List<String> dateSelected = new ArrayList<String>();

    public enum EventType {
        SINGLE, MULTIPLE
    }

    public interface OnPageChangeListener {
        void onMonthChange(int month);

        void onYearChange(int year);
    }

    public interface OnSizeChangedListener {
        void onSizeChanged(int size);
    }

    public MonthView(Context context) {
        this(context, null);
    }

    public MonthView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MonthView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);

        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG | Paint.DEV_KERN_TEXT_FLAG);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mScroller = new Scroller(context);

        Calendar calendar = Calendar.getInstance();

        currentYear = calendar.get(Calendar.YEAR);
        currentMonth = calendar.get(Calendar.MONTH) + 1;
        currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        mCalendarBiz = new CalendarBiz(index, currentYear, currentMonth);
        computeDate();
        buildCalendarRegion();
    }

    public void showToday(){
        initDefaultDate();
    }

    // 画出当前这天
    private void initDefaultDate(){
        for (int i = 0; i < mRegion.length; i++) {
            for (int j = 0; j < mRegion[i].length; j++) {
                Region region = mRegion[i][j];
                String day = mCalendarBiz.getGregorianCreated().get(index)[i][j];
                if (null == mCalendarBiz.getGregorianCreated().get(index)[i][j]) {
                    continue;
                }
                // 今天
                if (day.equalsIgnoreCase(currentDay+"")) {
                    String date = currentYear + "-" +currentMonth+"-"+currentDay;
                    dateSelected.add(date);
                    BGCircle circle = createCircle(region.getBounds().centerX() + index * sizeBase, region.getBounds().centerY());
                    getShowAnim(circle).start();
                    circlesAppear.put(date, circle);
                }
            }
        }
    }

    public void update(int year,int month,int day){
        index = 0;
        calendarRegion.clear();
        circlesAppear.clear();
        circlesDisappear.clear();
        dateSelected.clear();
        this.currentYear = year;
        this.currentMonth = month + 1;
        this.currentDay = day;
        mCalendarBiz = null;
        mCalendarBiz = new CalendarBiz(index, currentYear, currentMonth);
        computeDate();
        buildCalendarRegion();
        initDefaultDate();
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
        if (null != this.onPageChangeListener) {
            this.onPageChangeListener.onYearChange(currentYear);
            this.onPageChangeListener.onMonthChange(currentMonth);
        }
    }

    public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener) {
        this.onSizeChangedListener = onSizeChangedListener;
    }

    public List<String> getDateSelected() {
        return dateSelected;
    }

    public void setLunarShow(boolean isLunarShow) {
        this.isLunarShow = isLunarShow;
        invalidate();
    }

    public void setColorMain(int colorMain) {
        this.colorMain = colorMain;
        invalidate();
    }

    public void setEventType(EventType type){
        this.mEventType = type;
    }

    private void computeDate() {
        nextYear = lastYear = currentYear;
        nextMonth = currentMonth + 1;
        lastMonth = currentMonth - 1;

        if (null != onPageChangeListener) {
            onPageChangeListener.onYearChange(currentYear);
            onPageChangeListener.onMonthChange(currentMonth);
        }
        if (currentMonth == 12) {
            nextYear++;

            mCalendarBiz.buildSolarTerm(nextYear);
            nextMonth = 1;
        }
        if (currentMonth == 1) {
            lastYear--;

            mCalendarBiz.buildSolarTerm(lastYear);
            lastMonth = 12;
        }
    }

    private void buildCalendarRegion() {
        if (!calendarRegion.containsKey(index)) {
            List<Region> regions = new ArrayList<Region>();
            calendarRegion.put(index, regions);
        }
    }

    private String[][] gregorianToLunar(String[][] gregorian, int year, int month) {
        String[][] lunar = new String[6][7];
        for (int i = 0; i < gregorian.length; i++) {
            for (int j = 0; j < gregorian[i].length; j++) {
                String str = gregorian[i][j];
                if (null == str) {
                    str = "";
                } else {
                    str = mCalendarBiz.gregorianToLunar(year, month, Integer.valueOf(str));
                }
                lunar[i][j] = str;
            }
        }
        return lunar;
    }

    private BGCircle createCircle(float x, float y) {
        OvalShape circle = new OvalShape();
        circle.resize(0, 0);
        ShapeDrawable drawable = new ShapeDrawable(circle);
        BGCircle circle1 = new BGCircle(drawable);
        circle1.setX(x);
        circle1.setY(y);
        drawable.getPaint().setColor(0xFFDCDCDC);
        return circle1;
    }

    private void defineContainRegion(int x, int y) {
        for (int i = 0; i < mRegion.length; i++) {
            for (int j = 0; j < mRegion[i].length; j++) {
                Region region = mRegion[i][j];

                if (null == mCalendarBiz.getGregorianCreated().get(index)[i][j]) {
                    continue;
                }
                if (region.contains(x, y)) {
                    // 获取日历的当前区域
                    List<Region> regions = calendarRegion.get(index);
                    // 如果包含此区域
                    if (regions.contains(region)) {
                        regions.remove(region);
                    } else {
                        regions.add(region);
                    }
                    final String date = currentYear + "-" + currentMonth + "-" + mCalendarBiz.getGregorianCreated().get(index)[i][j];

                    switch (mEventType) {
                        case SINGLE:
                            // 选择的列表如果包含此选择的
                            if(dateSelected.contains(date)){
//							dateSelected.remove(date);
//							dismissCircle(date);
//							circlesAppear.remove(date);
                            }else{ // 如果不包含
                                // 已经有一个内容了
                                if(dateSelected.size() > 0){
                                    dismissCircle(dateSelected.get(0));
                                    circlesAppear.remove(dateSelected.get(0));
                                    dateSelected.clear();
                                    circlesAppear.clear();

                                    dateSelected.add(date);
                                    BGCircle circle = createCircle(region.getBounds().centerX() + index * sizeBase, region.getBounds().centerY());
                                    getShowAnim(circle).start();
                                    circlesAppear.put(date, circle);
                                }else{
                                    dateSelected.add(date);
                                    BGCircle circle = createCircle(region.getBounds().centerX() + index * sizeBase, region.getBounds().centerY());
                                    getShowAnim(circle).start();
                                    circlesAppear.put(date, circle);
                                }
                            }
                            break;

                        case MULTIPLE:
                            if (dateSelected.contains(date)) {
                                dateSelected.remove(date);
                                dismissCircle(date);
                                circlesAppear.remove(date);
                            } else {
                                dateSelected.add(date);
                                BGCircle circle = createCircle(region.getBounds().centerX() + index * sizeBase, region.getBounds().centerY());
                                getShowAnim(circle).start();
                                circlesAppear.put(date, circle);
                            }
                            break;
                    }


                }
            }
        }
    }

    // 销毁选择的
    private void dismissCircle(final String date){
        BGCircle circle = circlesAppear.get(date);
        if(circle == null){return;}
        ValueAnimator animScale = ObjectAnimator.ofInt(circle, "radius", circleRadius, 0);
        animScale.setDuration(250);
        animScale.setInterpolator(new AccelerateInterpolator());
        animScale.addUpdateListener(this);
        animScale.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                circlesDisappear.remove(date);
            }
        });
        animScale.start();
        circlesDisappear.put(date, circle);
    }

    private AnimatorSet getShowAnim(BGCircle circle){
        ValueAnimator animScale1 = ObjectAnimator.ofInt(circle, "radius", 0, animZoomOut1);
        animScale1.setDuration(250);
        animScale1.setInterpolator(new DecelerateInterpolator());
        animScale1.addUpdateListener(this);

        ValueAnimator animScale2 = ObjectAnimator.ofInt(circle, "radius", animZoomOut1, animZoomIn1);
        animScale2.setDuration(100);
        animScale2.setInterpolator(new AccelerateInterpolator());
        animScale2.addUpdateListener(this);

        ValueAnimator animScale3 = ObjectAnimator.ofInt(circle, "radius", animZoomIn1, animZoomOut2);
        animScale3.setDuration(150);
        animScale3.setInterpolator(new DecelerateInterpolator());
        animScale3.addUpdateListener(this);

        ValueAnimator animScale4 = ObjectAnimator.ofInt(circle, "radius", animZoomOut2, circleRadius);
        animScale4.setDuration(50);
        animScale4.setInterpolator(new AccelerateInterpolator());
        animScale4.addUpdateListener(this);

        AnimatorSet animSet = new AnimatorSet();
        animSet.playSequentially(animScale1, animScale2, animScale3, animScale4);

        return animSet;
    }

    public void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(dx, dy);
    }

    private void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(mScroller.getFinalX(), mScroller.getFinalY(), dx, dy, 500);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastPointX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int totalMoveX = (int) (lastPointX - event.getX()) + lastMoveX;
                smoothScrollTo(totalMoveX, 0);
                break;
            case MotionEvent.ACTION_UP:
                if (Math.abs(lastPointX - event.getX()) > 10) {
                    if (lastPointX > event.getX()) {
                        if (Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            index++;
                            currentMonth = (currentMonth + 1) % 13;

                            if (currentMonth == 0) {
                                currentMonth = 1;
                                currentYear++;

                                mCalendarBiz.buildSolarTerm(currentYear);
                            }
                            computeDate();
                            if (null != onPageChangeListener) {
                                onPageChangeListener.onMonthChange(currentMonth);
                                onPageChangeListener.onYearChange(currentYear);
                            }
                            buildCalendarRegion();
                        }
                        smoothScrollTo(width * index, 0);
                        lastMoveX = width * index;
                    } else if (lastPointX < event.getX()) {
                        if (Math.abs(lastPointX - event.getX()) >= criticalWidth) {
                            index--;
                            currentMonth = (currentMonth - 1) % 12;
                            if (currentMonth == 0) {
                                currentMonth = 12;
                                currentYear--;
                                mCalendarBiz.buildSolarTerm(currentYear);
                            }
                            computeDate();
                            if (null != onPageChangeListener) {
                                onPageChangeListener.onMonthChange(currentMonth);
                                onPageChangeListener.onYearChange(currentYear);
                            }
                            buildCalendarRegion();
                        }
                        smoothScrollTo(width * index, 0);
                        lastMoveX = width * index;
                    }
                } else {
                    defineContainRegion((int) event.getX(), (int) event.getY());
                }
                break;
        }
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        } else {
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);

        String[][] currentGregorian = mCalendarBiz.getGregorianCreated().get(index);
        setMeasuredDimension(measureWidth, (int) (measureWidth * 6 / 7F));
//        if (null == currentGregorian[4][0]) {
//            setMeasuredDimension(measureWidth, (int) (measureWidth * 4 / 7F));
//        } else if (null == currentGregorian[5][0]) {
//            setMeasuredDimension(measureWidth, (int) (measureWidth * 5 / 7F));
//        } else {
//            setMeasuredDimension(measureWidth, (int) (measureWidth * 6 / 7F));
//        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        width = w;
//        height = h;

        criticalWidth = (int) (1F / 5F * width);

        sizeBase = width;

        if (null != onSizeChangedListener) {
            onSizeChangedListener.onSizeChanged(sizeBase);
        }
        int sizeCell = (int) (sizeBase / 7F);
        // TODO 选择圆的半径
        circleRadius = sizeCell;
        animZoomOut1 = (int) (sizeCell * 1.02F);
        animZoomIn1 = (int) (sizeCell * 0.8F);
        animZoomOut2 = (int) (sizeCell * 1.01F);

        textSizeGregorian = sizeBase / 20F;
        mTextPaint.setTextSize(textSizeGregorian);

        float gregorianH = mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top;

        textSizeLunar = sizeBase / 35F;
        mTextPaint.setTextSize(textSizeLunar);

        float lunarH = mTextPaint.getFontMetrics().bottom - mTextPaint.getFontMetrics().top;

        offsetYLunar = (((Math.abs(mTextPaint.ascent() + mTextPaint.descent())) / 2) + lunarH / 2 + gregorianH / 2)
                * 3F / 4F;

        for (int i = 0; i < mRegion.length; i++) {
            for (int j = 0; j < mRegion[i].length; j++) {
                Region region = new Region();
                region.set((j * sizeCell), (i * sizeCell), sizeCell + (j * sizeCell), sizeCell + (i * sizeCell));
                mRegion[i][j] = region;
            }
        }
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCircle(canvas);
        drawMonths(canvas);
    }

    private void drawCircle(Canvas canvas) {
        for (String s : circlesDisappear.keySet()) {
            BGCircle circle = circlesDisappear.get(s);
            drawBGCircle(canvas, circle);
        }
        for (String s : circlesAppear.keySet()) {
            BGCircle circle = circlesAppear.get(s);
            drawBGCircle(canvas, circle);
        }
    }

    private void drawBGCircle(Canvas canvas, BGCircle circle) {
        canvas.save();
        canvas.translate(circle.getX() - circle.getRadius() / 2, circle.getY() - circle.getRadius() / 2);
        circle.getShape().getShape().resize(circle.getRadius(), circle.getRadius());
        circle.getShape().draw(canvas);
        canvas.restore();
    }

    private void drawMonths(Canvas canvas) {
        drawMonth(canvas, (index - 1) * sizeBase, lastYear, lastMonth);
        drawMonth(canvas, index * sizeBase, currentYear, currentMonth);
        drawMonth(canvas, (index + 1) * sizeBase, nextYear, nextMonth);
    }

    private void drawMonth(Canvas canvas, float offsetX, int year, int month) {
        canvas.save();
        canvas.translate(offsetX, 0);

        int current = (int) (offsetX / sizeBase);
        mTextPaint.setTextSize(textSizeGregorian);
        mTextPaint.setColor(Color.BLACK);

        String[][] gregorianCurrent = mCalendarBiz.getGregorianCreated().get(current);

        if (null == gregorianCurrent) {
            gregorianCurrent = mCalendarBiz.buildGregorian(year, month);
        }
        for (int i = 0; i < gregorianCurrent.length; i++) {
            for (int j = 0; j < gregorianCurrent[i].length; j++) {
                String str = gregorianCurrent[i][j];
                if (null == str) {
                    str = "";
                }
                canvas.drawText(str, mRegion[i][j].getBounds().centerX(),
                        mRegion[i][j].getBounds().centerY(), mTextPaint);
            }
        }
        if (isLunarShow) {
            String[][] lunarCurrent = mCalendarBiz.getLunarCreated().get(current);
            if (null == lunarCurrent) {
                lunarCurrent = gregorianToLunar(gregorianCurrent, year, month);
            }
            mTextPaint.setTextSize(textSizeLunar);
            for (int i = 0; i < lunarCurrent.length; i++) {
                for (int j = 0; j < lunarCurrent[i].length; j++) {
                    String str = lunarCurrent[i][j];
                    if (str.contains(" ")) {
                        str.trim();
                        mTextPaint.setColor(colorMain);
                    } else {
                        mTextPaint.setColor(Color.GRAY);
                    }
                    canvas.drawText(str, mRegion[i][j].getBounds().centerX(), mRegion[i][j].getBounds().centerY() +
                            offsetYLunar, mTextPaint);
                }
            }
            mCalendarBiz.getLunarCreated().put(current, lunarCurrent);
        }
        mCalendarBiz.getGregorianCreated().put(current, gregorianCurrent);
        canvas.restore();
    }
}
