package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SysUtil;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends AbsBaseActivity {

	private int userId = 0;
	private int orderId = 0;
	private int flag = 0;
	private int demandId = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if (intent != null) {
			Uri uri = intent.getData();
			if (uri != null) {
				String userIdStr = Uri.parse(uri.toString()).getQueryParameter("userId");
				if (!TextUtils.isEmpty(userIdStr)) {
					userId = Integer.valueOf(userIdStr);
				}
				String orderIdStr = Uri.parse(uri.toString()).getQueryParameter("orderId");
				if (!TextUtils.isEmpty(orderIdStr)) {
					orderId = Integer.valueOf(orderIdStr);
				}
				String flagStr = Uri.parse(uri.toString()).getQueryParameter("flag");
				if (!TextUtils.isEmpty(flagStr)) {
					flag = Integer.valueOf(flagStr);
				}
				String demandIdStr = Uri.parse(uri.toString()).getQueryParameter("demandId");
				if (!TextUtils.isEmpty(demandIdStr)) {
					demandId = Integer.valueOf(demandIdStr);
				}
				LogUtil.d("ZLOVE", "Splash---userId---" + userId + ",---orderId---" + orderId + ",---flag---" + flag);
			}
		}

		boolean enteredFirstTime = SysUtil.getBooleanPref(PrefKeyConst.EXTRA_IS_FIRST_ENTERED, true);
		int lastVersionCode = SysUtil.getIntPref(PrefKeyConst.PREF_VERSION_CODE);
		int curVersionCode = SysUtil.getVerCode();
		if (enteredFirstTime || curVersionCode - lastVersionCode >= 2) {// 首次进入，展示引导页
			setContentView(R.layout.view_first_enter);
		} else {
			setContentView(R.layout.act_splash);
			ImageView iv = (ImageView) findViewById(R.id.iv_welcome);
			float screenWidth = ScreenUtils.getDisplayMetrics(SplashActivity.this).widthPixels;
			float imgHeight = screenWidth / 750f * 724f;
			ViewGroup.LayoutParams ivLayout = iv.getLayoutParams();
			ivLayout.height = (int)imgHeight;
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(SplashActivity.this, ActMain.class);
					intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, userId);
					intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
					intent.putExtra(IntentKey.INTENT_KEY_CHAT_USER_IDENTITY, flag);
					intent.putExtra(IntentKey.INTENT_KEY_DEMAND_ID, demandId);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					finish();
				}
			}, 2000);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}
}
