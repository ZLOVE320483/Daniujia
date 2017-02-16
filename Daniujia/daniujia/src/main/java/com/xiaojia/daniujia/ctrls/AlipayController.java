package com.xiaojia.daniujia.ctrls;

import android.app.Activity;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.utils.LogUtil;

public class AlipayController {
	private Activity mActivity;

	public AlipayController(Activity act) {
		mActivity = act;
	}

	public void pay(String payInfo) {
		// 构造PayTask 对象
		PayTask alipay = new PayTask(mActivity);
		// 调用支付接口，获取支付结果
		String result = alipay.pay(payInfo);
		AliPayResult payResult = new AliPayResult(result);
		// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
		String resultInfo = payResult.getResult();
		String resultStatus = payResult.getResultStatus();
		// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
		if (TextUtils.equals(resultStatus, "9000")) {
			MsgHelper.getInstance().sendMsg(InsideMsg.PAY_ALI_SUCCESS);

		} else {
			// 判断resultStatus 为非“9000”则代表可能支付失败
			// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
			LogUtil.e("alipay", resultStatus);
//			if (TextUtils.equals(resultStatus, "8000")) {
//				MainThreadHandler.makeToast("支付结果确认中");
//			} else if (TextUtils.equals(resultStatus, "4000")) {
//				MainThreadHandler.makeLongToast("订单支付失败：" + payResult.getMemo());
//			} else if (TextUtils.equals(resultStatus, "6002")) {
//				MainThreadHandler.makeLongToast("网络连接出错：" + payResult.getMemo());
//			} else if (TextUtils.equals(resultStatus, "6001")) {
//				MainThreadHandler.makeLongToast("用户中途取消：" + payResult.getMemo());
//			}
		}
	}

}