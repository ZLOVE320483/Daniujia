package com.xiaojia.daniujia.dlgs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.wheelview.OnWheelChangedListener;
import com.xiaojia.daniujia.ui.views.wheelview.WheelView;
import com.xiaojia.daniujia.ui.views.wheelview.adapters.ArrayWheelAdapter;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class DatePickerBottomDlg extends BaseBottomDialog implements View.OnClickListener, OnWheelChangedListener {

	private WheelView mWheelYear, mWheelMonth;
	private String[] array_years;
	private String[] array_months;
	private String [] arrayMonthWithNow;
	private String currentYear = "1985";// 默认年份
	private String currentMonth;
	private boolean showNow = true;
	private String initMonth;
	private String  realYear;

	public DatePickerBottomDlg(Activity context, String initYear, String initMonth) {
		super(context, SysUtil.inflate(R.layout.dlg_bottom_choose_birth));
		realYear = getYear()+"";
		if (initYear != null && initMonth != null) {
			currentYear = initYear;
			if (!realYear.equals(initYear)) {
				this.initMonth = formatMonth(initMonth);
				currentMonth = initMonth;
			}else{
				this.initMonth = formatMonth(initMonth);
				currentMonth = initMonth;
			}
		}
	}

	public void initYearMonth(String initYear, String initMonth) {
		if (initYear != null && initMonth != null) {
			currentYear = initYear;
			this.initMonth = formatMonth(initMonth);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWheelYear = (WheelView) mView.findViewById(R.id.wv_year);
		mWheelMonth = (WheelView) mView.findViewById(R.id.wv_month);

		mWheelYear.setVisibleItems(7);// 设置可见条目数量
		mWheelMonth.setVisibleItems(7);

		mWheelYear.addChangingListener(this);
		mWheelMonth.addChangingListener(this);
		mView.findViewById(R.id.confirm).setOnClickListener(this);
		mView.findViewById(R.id.cancel).setOnClickListener(this);

		initYears();
		initMonths();
		mWheelYear.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_years));
		if (showNow && realYear.equals(currentYear)) {
			//指定为显示至今 且当前为2016年
			mWheelMonth.setViewAdapter(new ArrayWheelAdapter<>(mActivity, arrayMonthWithNow));
		}else{
			mWheelMonth.setViewAdapter(new ArrayWheelAdapter<>(mActivity, array_months));
		}
		init();
	}

	private void init() {
		List<String> yearList = Arrays.asList(array_years);
		if (yearList.contains(currentYear)) {
			int index = yearList.indexOf(currentYear);
			mWheelYear.setCurrentItem(index);
		}
		List<String> monthList = Arrays.asList(array_months);
		List<String> monthListWithNow = Arrays.asList(arrayMonthWithNow);

		if (monthListWithNow.contains(currentMonth) && realYear.equals(currentYear)) {
			int index = monthListWithNow.indexOf(initMonth);
			mWheelMonth.setCurrentItem(index);
		}else if (monthList.contains(initMonth)) {
			int index = monthList.indexOf(initMonth);
			mWheelMonth.setCurrentItem(index);
		}
	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == mWheelYear) {
			int yearIndex = mWheelYear.getCurrentItem();
			currentYear = array_years[yearIndex];
			if (realYear.equals(currentYear)) {
				mWheelMonth.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, arrayMonthWithNow));
				currentMonth = arrayMonthWithNow[0];
			}else{
				mWheelMonth.setViewAdapter(new ArrayWheelAdapter<String>(mActivity, array_months));
				currentMonth = array_months[0];
			}
			mWheelMonth.setCurrentItem(0);
		} else if (wheel == mWheelMonth) {
			int monthIndex = mWheelMonth.getCurrentItem();
			if (realYear.equals(currentYear) && showNow) {
				currentMonth = arrayMonthWithNow[monthIndex];
			}else {
				currentMonth = array_months[monthIndex];
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm:
			if (mClickListener != null) {
				Bundle b = new Bundle();
				String[] arr = new String[] { currentYear, formatMonth(currentMonth) };
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
		int minYear = 1950;
		array_years = new String[getYear() - minYear];
		for (int i = getYear(), k = 0; i > minYear; i--, k++) {
			array_years[getYear() - minYear-1-k] = i + "";
		}
	}

	private void initMonths() {
		int k = 1;
		int monthSize = 12;
		monthSize++;
		arrayMonthWithNow = new String[getMonth()+1];
		arrayMonthWithNow[0] = "至今";
		array_months = new String[12];
		array_months[0] = "01";

		for (int i = 1; i < monthSize; i++) {
			if (showNow && i < getMonth()+1) {
				arrayMonthWithNow[i] = formatMonth(k++ + "");
			}
			if (i < 12) {
				array_months[i] = formatMonth(i + 1 +"");
			}
		}
	}
	
	private String formatMonth(String month) {
		if (month.length() == 1) {
			return "0" + month;
		}
		return month;
	}

	public int getYear() {// 当前年份
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	public int getMonth() {// 当前月份
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH) + 1;
	}

}
