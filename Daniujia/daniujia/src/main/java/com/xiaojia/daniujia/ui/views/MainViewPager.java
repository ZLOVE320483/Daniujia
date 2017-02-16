package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MainViewPager extends ViewPager {
	private boolean isScrollAllowed = false;
	public MainViewPager(Context context){
		super(context);
	}
	
	public MainViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent arg0) {
		if(!isScrollAllowed){
			return false;
		}
		return super.onTouchEvent(arg0);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent arg0) {
		if(!isScrollAllowed){
			return false;
		}
		return super.onInterceptTouchEvent(arg0);
	}
	
	@Override
	public void setCurrentItem(int item) {
		super.setCurrentItem(item, false);
	}
	
	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		super.setCurrentItem(item, smoothScroll);
	}
	
}
