package com.xiaojia.daniujia.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.utils.LogUtil;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	private static final String TAG = "wxpay";
	private IWXAPI wxApi;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		wxApi = WXAPIFactory.createWXAPI(this, Config.WXPAY_APP_ID);
		wxApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		wxApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
		// called when a request is built from bundle
		Log.i("lk", req.openId);
	}

	@Override
	public void onResp(BaseResp resp) {
		// called when a response is built from bundle
		LogUtil.d("ZLOVE", "onWXPayFinish, errCode = " + resp.errCode + "---errStr---" + resp.errStr);
		int code = resp.errCode;
		switch (code) {
		case 0:// 成功
			MsgHelper.getInstance().sendMsg(InsideMsg.PAY_WEIXIN_SUCCESS);
			break;
		case -1:// 错误
			MsgHelper.getInstance().sendMsg(InsideMsg.PAY_WEIXIN_FAIL);
			break;
		case -2:// 用户取消
			break;
		}
		finish();
		// if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
		// }
	}
}