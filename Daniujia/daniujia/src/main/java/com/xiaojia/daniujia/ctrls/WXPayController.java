package com.xiaojia.daniujia.ctrls;

import android.app.Activity;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.PayOrderRespVo;
import com.xiaojia.daniujia.domain.resp.WeixinOrderVo;
import com.xiaojia.daniujia.domain.resp.WxPayRetVo;
import com.xiaojia.daniujia.utils.LogUtil;

public class WXPayController {
	private IWXAPI wxApi;

	public WXPayController(Activity act) {
		wxApi = WXAPIFactory.createWXAPI(act, null);
		wxApi.registerApp(Config.WXPAY_APP_ID);
	}

	public boolean isWXAppInstalled() {
		return wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI();
	}

	public void pay(WxPayRetVo wxpayRetVo) {
		// 生成签名参数
		PayReq payReq = new PayReq();
		payReq.appId = wxpayRetVo.appid;
		payReq.partnerId = wxpayRetVo.partnerid;
		payReq.prepayId = wxpayRetVo.prepayid;
		payReq.packageValue = "Sign=WXPay";
		payReq.nonceStr = wxpayRetVo.noncestr;
		payReq.timeStamp = wxpayRetVo.timestamp + "";
		payReq.sign = wxpayRetVo.sign;
		// 发起支付
		boolean sentSuccess = wxApi.sendReq(payReq);
		LogUtil.d("wxpay", "send response to wechat app:" + sentSuccess);
	}
	
	public boolean pay(WeixinOrderVo order){
		PayReq payReq = new PayReq();
		payReq.appId = order.appid;
		payReq.partnerId = order.partnerid;
		payReq.prepayId = order.prepayid;
		payReq.packageValue = "Sign=WXPay";
		payReq.nonceStr = order.noncestr;
		payReq.timeStamp = order.timestamp + "";
		payReq.sign = order.sign;
		// 发起支付
		boolean sentSuccess = wxApi.sendReq(payReq);
		return sentSuccess;
	}

	public boolean pay(PayOrderRespVo order){
		LogUtil.d("ZLOVE", "PayOrderRespVo---" + order.toString());

		PayReq payReq = new PayReq();
		payReq.appId = order.getAppId();
		payReq.partnerId = order.getPartnerId();
		payReq.prepayId = order.getPrepayId();
		payReq.packageValue = "Sign=WXPay";
		payReq.nonceStr = order.getNonceStr();
		payReq.timeStamp = String.valueOf(order.getTimestamp());
		payReq.sign = order.getSign();
		// 发起支付
		boolean sentSuccess = wxApi.sendReq(payReq);
		return sentSuccess;
	}

}
