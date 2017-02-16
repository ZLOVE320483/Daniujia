/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xiaojia.daniujia.ui.views.refreshscroll;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.xiaojia.daniujia.ui.views.refreshscroll.PullToRefreshBase.Mode;
import com.xiaojia.daniujia.ui.views.refreshscroll.PullToRefreshBase.Orientation;
import com.xiaojia.daniujia.R;

public class RotateLoadingLayout extends LoadingLayout {
	private AnimationDrawable refreshAnim;

	public RotateLoadingLayout(Context context, Mode mode, Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);
		ivLoading.setImageResource(R.drawable.anim_refresh);
		refreshAnim = (AnimationDrawable) ivLoading.getDrawable();

	}

	public void onLoadingDrawableSet(Drawable imageDrawable) {

	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
	}

	@Override
	protected void refreshingImpl() {
		ivRefresh.setVisibility(GONE);
		ivLoading.setVisibility(VISIBLE);
		refreshAnim.start();
	}

	@Override
	protected void resetImpl() {
		if (refreshAnim != null) {
			refreshAnim.stop();

		}

		if (ivLoading != null){
			ivLoading.clearAnimation();
		}
	}

	@Override
	protected void pullToRefreshImpl() {
		// NO-OP
		ivRefresh.setVisibility(VISIBLE);
		ivLoading.setVisibility(GONE);
	}

	@Override
	protected void releaseToRefreshImpl() {
		// NO-OP
	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.drawable.list_refresh_logo;
	}

}
