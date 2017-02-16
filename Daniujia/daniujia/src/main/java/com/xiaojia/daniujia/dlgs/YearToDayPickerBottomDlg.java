package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.wheelview.OnWheelChangedListener;
import com.xiaojia.daniujia.ui.views.wheelview.WheelView;
import com.xiaojia.daniujia.ui.views.wheelview.adapters.ArrayWheelAdapter;
import com.xiaojia.daniujia.ui.views.wheelview.adapters.WheelViewAdapter;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class YearToDayPickerBottomDlg extends BaseBottomDialog implements View.OnClickListener, OnWheelChangedListener {
    private WheelView mWheelYear, mWheelMonth, mWheelDay;
    private String[] array_years;
    private String[] array_months;
    private String[] array_day31, array_day30, array_day29, array_day28;

    private String currentYear = "1985";// 默认年份
    private String currentMonth;
    private String currentDay;

    public YearToDayPickerBottomDlg(Activity context, String initYear, String initMonth, String initDay) {
        super(context, SysUtil.inflate(R.layout.dlg_bottom_choose_year_month_day));
        LogUtil.d("zp_test",initYear + "-" + initMonth + "-" + initDay);
        if (initYear != null && initMonth != null && initDay != null) {
            currentYear = initYear;
            currentDay = formatMonth(initDay);
            currentMonth = formatMonth(initMonth);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mWheelYear = (WheelView) mView.findViewById(R.id.wv_year);
        mWheelMonth = (WheelView) mView.findViewById(R.id.wv_month);
        mWheelDay = (WheelView) mView.findViewById(R.id.wv_day);

        mWheelYear.setVisibleItems(7);// 设置可见条目数量
        mWheelMonth.setVisibleItems(7);
        mWheelDay.setVisibleItems(7);

        mWheelYear.addChangingListener(this);
        mWheelMonth.addChangingListener(this);
        mWheelDay.addChangingListener(this);
        mView.findViewById(R.id.confirm).setOnClickListener(this);
        mView.findViewById(R.id.cancel).setOnClickListener(this);

        initYears();
        initMonths();
        initDay();
        mWheelYear.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_years));
        mWheelMonth.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_months));
        getDays(currentYear, currentMonth);
        init();
    }

    private void init() {
        List<String> yearList = Arrays.asList(array_years);
        if (yearList.contains(currentYear)) {
            int index = yearList.indexOf(currentYear);
            mWheelYear.setCurrentItem(index);
        }
        List<String> monthList = Arrays.asList(array_months);

        if (monthList.contains(currentMonth)) {
            int index = monthList.indexOf(currentMonth);
            mWheelMonth.setCurrentItem(index);
        }

        if (mWheelDay.getViewAdapter() instanceof ArrayWheelAdapter) {
            ArrayWheelAdapter wheelDay = (ArrayWheelAdapter) mWheelDay.getViewAdapter();
            List<String> dayList = Arrays.asList((String[]) wheelDay.getWheelData());
            if (dayList.contains(currentDay)) {
                mWheelDay.setCurrentItem(dayList.indexOf(currentDay));
            }
        }

    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mWheelYear) {
            int yearIndex = mWheelYear.getCurrentItem();
            currentYear = array_years[yearIndex];
            mWheelMonth.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_months));
            currentMonth = array_months[0];
            getDays(currentYear, currentMonth);
            mWheelMonth.setCurrentItem(0);
            mWheelDay.setCurrentItem(0);
        } else if (wheel == mWheelMonth) {
            int monthIndex = mWheelMonth.getCurrentItem();
            currentMonth = array_months[monthIndex];
            getDays(currentYear, currentMonth);
            mWheelDay.setCurrentItem(0);
        }

        currentDay = getDayText(mWheelDay.getViewAdapter(),
                mWheelDay.getCurrentItem()).toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                if (mClickListener != null) {
                    Bundle b = new Bundle();
                    String[] arr = new String[]{currentYear, formatMonth(currentMonth), currentDay};
                    b.putStringArray(ExtraConst.EXTRA_DATE, arr);
                    mClickListener.onOK(b);
                }
                break;
            case R.id.cancel:
                break;
        }
        dismiss();
    }

    private void initYears() {
        array_years = new String[getYearAfter20() - getYear()];
        for (int i = 0; i < array_years.length; i++) {
            array_years[i] = String.valueOf(getYear() + i);
        }
    }

    private void initMonths() {
        int monthSize = 12;
        monthSize++;
        array_months = new String[12];
        array_months[0] = "01";

        for (int i = 1; i < monthSize; i++) {
            if (i < 12) {
                array_months[i] = formatMonth(i + 1 + "");
            }
        }
    }

    private void initDay() {
        array_day31 = new String[31];
        array_day30 = new String[30];
        array_day29 = new String[29];
        array_day28 = new String[28];

        for (int i = 0; i < 31; i++) {
            array_day31[i] = formatMonth(String.valueOf(i + 1));
        }

        for (int i = 0; i < 30; i++) {
            array_day30[i] = formatMonth(String.valueOf(i + 1));
        }

        for (int i = 0; i < 29; i++) {
            array_day29[i] = formatMonth(String.valueOf(i + 1));
        }

        for (int i = 0; i < 28; i++) {
            array_day28[i] = formatMonth(String.valueOf(i + 1));
        }
    }

    private String formatMonth(String month) {
        if (month.length() == 1) {
            return "0" + month;
        }
        return month;
    }

    private void getDays(String year, String month) {
        if (!PatternUtil.isMonthNum(month))
            return;

        int cMonth = Integer.parseInt(month);
        int tempYear = Integer.parseInt(year);

        LogUtil.d("zp_test", "month: " + cMonth);
        switch (cMonth) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                mWheelDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_day31));
                break;
            case 2:
                if (tempYear % 4 == 0) {
                    mWheelDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_day29));
                } else {
                    mWheelDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_day28));
                }
                break;

            default:
                mWheelDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_day30));
                break;

        }

        // 个数的判断是为了在选择今年本月的日期时，不让其再次进入以下代码，再次截取数组长度
        if (tempYear == getYear() && cMonth == getMonth() && mWheelDay.getViewAdapter().getItemsCount() >= 28){
            int day = getDay();
            if (mWheelDay.getViewAdapter() instanceof ArrayWheelAdapter){
                String[] data = (String[]) ((ArrayWheelAdapter)mWheelDay.getViewAdapter()).getWheelData();
                String[] dataNew = new String[data.length - day + 1];
                int j = 0;
                for (int i = day - 1; i < data.length; i++) {
                    dataNew[j++] = data[i];
                }
                mWheelDay.setViewAdapter(new ArrayWheelAdapter<>(mActivity, dataNew));
            }

        }
    }

    private CharSequence getDayText(WheelViewAdapter adapter, int index) {
        if (adapter instanceof ArrayWheelAdapter)
            return ((ArrayWheelAdapter) adapter).getItemText(index);
        return "";
    }

    public int getYear() {// 当前年份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR);
    }

    public int getYearAfter20() {// 当前年份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.YEAR) + 20;
    }

    public int getMonth() {// 当前月份
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.MONTH) + 1;
    }

    public int getDay() {// 当前日
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DAY_OF_MONTH);
    }

    public String getTimeString(){
        return currentYear + "-" + currentMonth + "-" + currentDay;
    }

}
