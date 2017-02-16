package com.xiaojia.daniujia.ui.act;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;

import android.os.Bundle;

@ContentView(R.layout.view_first_enter)
public class IntroduceActivity extends AbsBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ViewUtils.inject(this);
	}
}
