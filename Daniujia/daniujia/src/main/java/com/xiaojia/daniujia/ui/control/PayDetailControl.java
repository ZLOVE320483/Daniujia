package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.PayBalanceCouponVo;
import com.xiaojia.daniujia.domain.resp.PayOrderRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.PayDetailFragment;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/5/31.
 */
public class PayDetailControl extends BaseControl<PayDetailFragment> {

    public void getBalanceCouponRequest() {
        String url = Config.WEB_API_SERVER + "/user/balance/coupon";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<PayBalanceCouponVo>() {
            @Override
            public void onResponse(PayBalanceCouponVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }

    public void createPayOrderRequest(int pMethod, int orderId, float balance, int giftId) {
        String url = Config.WEB_API_SERVER + "/user/order/pay/do";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("pMethod", String.valueOf(pMethod));
        builder.add("orderId", String.valueOf(orderId));
        if (balance == 0.0f) {
            builder.add("balance", String.valueOf(0));
        } else {
            builder.add("balance", String.valueOf(balance));
        }
        builder.add("giftId", String.valueOf(giftId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<PayOrderRespVo>() {

            @Override
            public void onResponse(PayOrderRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setCreateOrderResp(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "createPayOrderRequest---onFail---" + errorMsg);
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }
        }, builder);
    }
}
