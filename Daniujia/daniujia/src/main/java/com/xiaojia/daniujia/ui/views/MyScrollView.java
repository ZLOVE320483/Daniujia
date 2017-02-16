package com.xiaojia.daniujia.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScrollView extends ViewGroup {

	public MyScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyScrollView(Context context) {
		super(context);
		init();
	}

	private Scroller myScroller;

	private void init() {
		
		myScroller = new Scroller(getContext());
		
		gestureDetector = new GestureDetector(getContext(),new GestureDetector.SimpleOnGestureListener(){
			public boolean onScroll(MotionEvent e1, MotionEvent e2,
					float distanceX, float distanceY) {
				scrollBy((int)distanceX, 0);
				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		});
	}
	

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
	private int interDownX;
	private int interDownY;
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		boolean result = false;
		
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			interDownX = (int) event.getX();
			interDownY = (int) event.getY();
			
			gestureDetector.onTouchEvent(event);
			downX = (int) event.getX();
			
			break;
		case MotionEvent.ACTION_MOVE:
			
			int moveX = (int) event.getX();
			int moveY = (int) event.getY();
			
			int disX = Math.abs(interDownX-moveX); 
			int disY = Math.abs(interDownY-moveY); 
			
			if(disX > disY && disX>10){ 
				result = true;
			}
			
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;
		}
		return result;
	}
	
	
	private int currIndex;
	
	private GestureDetector gestureDetector;
	
	private int downX;
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		gestureDetector.onTouchEvent(event);
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			
			downX = (int) event.getX();
			
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			
			int upX = (int) event.getX();
			
			int tempIndex = currIndex; 
			
			if(downX - upX > getWidth()/2){
				tempIndex++;
				
			}else if(upX - downX > getWidth()/2){
				tempIndex -- ;
			}
			
			moveToDest(tempIndex);
			
			break;
		}
		return true;
	}

	public void moveToDest(int tempIndex) {
		if(tempIndex<0){
			tempIndex = 0;
		}
		if(tempIndex > getChildCount()-1){
			tempIndex = getChildCount()-1;
		}
		currIndex = tempIndex;
		
		if(pagerChangedListener!=null){
			pagerChangedListener.onSelectChanged(currIndex);
		}
		
		
		int distanctX = getWidth()*currIndex - getScrollX(); 
		myScroller.startScroll(getScrollX(), 0, distanctX, 0);
		invalidate();
	}
	
	@Override
	public void computeScroll() {
		super.computeScroll();
		
		boolean isRunning = myScroller.computeScrollOffset(); 
		if(isRunning){
			int currX = (int) myScroller.getCurrX(); 
			
			scrollTo(currX, 0);
			
			invalidate();
		}
	}
	
	
	
	
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		
		
		int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
		int modeWidth = MeasureSpec.getMode(widthMeasureSpec); 
		for(int i=0;i<getChildCount();i++){
			View view = getChildAt(i);
			
			view.measure(widthMeasureSpec, heightMeasureSpec);
			
		}
		
	}
	

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		
		for(int i=0;i<getChildCount();i++){
			View view = getChildAt(i);
		}
	}
	
	private IOnPagerChangedListener pagerChangedListener;
	public void setOnPagerChangedListener(IOnPagerChangedListener pagerChangedListener){
		this.pagerChangedListener = pagerChangedListener;
	}
	
	public interface IOnPagerChangedListener{
		
		void onSelectChanged(int currIndex);
	}
}
