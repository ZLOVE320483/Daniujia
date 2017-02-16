package com.xiaojia.daniujia.ui.frag;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IntroduceFragment extends Fragment implements View.OnClickListener {
	private ViewPagerAdapter mPagerAdapter;
	private EdgeEffectCompat mRightEdge;
	private int[] mPagerResIds = new int[] { R.drawable.icon_guide_page_1, R.drawable.icon_guide_page_2, R.drawable.icon_guide_page_3};
	private int[] mGuide = new int[]{R.string.guide_one,R.string.guide_two,R.string.guide_three};
	private int[] mGuideBold = new int[]{R.string.guide_bold_one,R.string.guide_bold_two,R.string.guide_bold_three};

	private int lastVersionCode = 0;
	private int curVersionCode = 0;
	private LinearLayout mLlPoint;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frag_introduce, container, false);
		lastVersionCode = SysUtil.getIntPref(PrefKeyConst.PREF_VERSION_CODE);
		curVersionCode =SysUtil.getVerCode();
		ViewPager viewPager = (ViewPager) view.findViewById(R.id.introduces_view_pager);
		mLlPoint = (LinearLayout) view.findViewById(R.id.introduces_point);

		List<View> pagerInnerViews = new ArrayList<>();

		for (int i = 0; i < mPagerResIds.length; i++) {
			View guideView = inflater.inflate(R.layout.frag_guide, container, false);
			ImageView iv = (ImageView) guideView.findViewById(R.id.guide_icon);
			Button bt = (Button) guideView.findViewById(R.id.right_now_do_it);
			TextView tvOne = (TextView) guideView.findViewById(R.id.text_one);
			TextView tvTow = (TextView) guideView.findViewById(R.id.text_two);
			tvTow.setText(mGuide[i]);
			tvOne.setText(mGuideBold[i]);

			if (i == mPagerResIds.length -1){
				bt.setVisibility(View.VISIBLE);
				bt.setOnClickListener(this);
			} else {
				bt.setVisibility(View.INVISIBLE);
			}

			iv.setImageResource(mPagerResIds[i]);
			iv.setScaleType(ScaleType.CENTER_CROP);
			if (i == mPagerResIds.length - 1) {// 最后一个页面，点击进入程序
				iv.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {// 1.首次进入 2.从‘设置’->‘欢迎页’
						if (SysUtil.getBooleanPref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, true) || curVersionCode > lastVersionCode) {
							SysUtil.savePref(PrefKeyConst.PREF_VERSION_CODE, curVersionCode);
							startActivity(new Intent(getActivity(), ActMain.class));
						}
						SysUtil.savePref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, false);
						getActivity().finish();
					}
				});
			}

			pagerInnerViews.add(guideView);

			ImageView ivPoint = new ImageView(getActivity());
			ivPoint.setImageResource(R.drawable.icon_default);
			LinearLayout.LayoutParams layoutParams =
					new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			if (i != 0){
				layoutParams.setMargins(ScreenUtils.dip2px(21),0,0,0);
			}
			ivPoint.setLayoutParams(layoutParams);
			mLlPoint.addView(ivPoint);

		}

		drawPoint(0);

		mPagerAdapter = new ViewPagerAdapter(pagerInnerViews);
		viewPager.setAdapter(mPagerAdapter);
		
		try {
			Field rightEdgeField = viewPager.getClass().getDeclaredField("mRightEdge");
			if (rightEdgeField != null) {
				rightEdgeField.setAccessible(true);
				mRightEdge = (EdgeEffectCompat) rightEdgeField.get(viewPager);
			}
		} catch (Exception e) {
			LogUtil.e("ZLOVE", e);
		}
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				drawPoint(position);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				if (mRightEdge != null && !mRightEdge.isFinished()) {// 到了最后一张并且还继续拖动
					if (SysUtil.getBooleanPref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, true) || curVersionCode > lastVersionCode) {
						SysUtil.savePref(PrefKeyConst.PREF_VERSION_CODE, curVersionCode);
						startActivity(new Intent(getActivity(), ActMain.class));
					}
					SysUtil.savePref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, false);
					getActivity().finish();
				}
			}
		});
		return view;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.right_now_do_it){
			if (SysUtil.getBooleanPref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, true) || curVersionCode > lastVersionCode) {
				SysUtil.savePref(PrefKeyConst.PREF_VERSION_CODE, curVersionCode);
				startActivity(new Intent(getActivity(), ActMain.class));
			}
			SysUtil.savePref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, false);
			getActivity().finish();
		}
	}

	/*
	 * ViewPager适配器
	 */
	public class ViewPagerAdapter extends PagerAdapter {
		private List<View> views;

		public ViewPagerAdapter(List<View> views) {
			this.views = views;
		}

		@Override
		public int getCount() {
			if (views != null) {
				return views.size();
			}
			return 0;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return (arg0 == arg1);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			((ViewPager) container).addView(views.get(position), 0);
			return views.get(position);
		}
	}

	private void drawPoint(int selected){
		int points = mLlPoint.getChildCount();

		if (selected >= points)
			return;

		for (int i = 0; i < points; i++) {
			if (mLlPoint.getChildAt(i) instanceof ImageView){
				ImageView iv = (ImageView) mLlPoint.getChildAt(i);
				if (i == selected){
					iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_selected));
				} else {
					iv.setImageDrawable(getResources().getDrawable(R.drawable.icon_default));
				}

			}
		}
	}

}
