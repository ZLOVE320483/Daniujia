package com.xiaojia.daniujia.domain.resp;

public class WeixinOrderVo {
	/**
	 * out_trade_no string 订单号 appid string 应用ID noncestr string 随机字符串 package
	 * string 固定值”Sign=WXPay” partnerid string 商户ID prepayid string 预支付会话号
	 * timestamp int 时间戳 sign string 签名
	 */
	public String out_trade_no;
	public String appid;
	public String noncestr;
	public String partnerid;
	public String prepayid;
	public int timestamp;
	public String sign;
}
