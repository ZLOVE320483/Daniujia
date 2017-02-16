package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.UserOrderDetailFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/5/26.
 */
public class UserOrderDetailControl extends BaseControl<UserOrderDetailFragment> {

    public void requestOrderDetail(int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/my/detail/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + orderId;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<OrderDetailRespVo>() {
            @Override
            public void onResponse(OrderDetailRespVo result) {
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

    public void cancelOrderRequest(final int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/cancel";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    requestOrderDetail(orderId);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "cancelOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void modifyQuesOrProfile(int orderId, String content, int modifyType) {
        String url = Config.WEB_API_SERVER + "/user/order/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        if (modifyType == 1) {
            builder.add("profile", content);
        } else if (modifyType == 2) {
            builder.add("quesDesc", content);
        }
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast("更新成功");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "modifyQuesOrProfile---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void getKnowledgeIconState(){
        String url = Config.WEB_API_SERVER + "/init";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<ChildShareRespVo>() {
            @Override
            public void onResponse(ChildShareRespVo result) {
                getFragment().onSetKnowledgeState(result);
            }
        });
    }
}
