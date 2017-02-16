package com.xiaojia.daniujia.ui.control;

import android.database.Cursor;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.OrderDetailRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.ui.frag.ExpertOrderDetailFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/26.
 */
public class ExpertOrderDetailControl extends BaseControl<ExpertOrderDetailFragment> {

    public void requestOrderDetail(int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/need/serve/detail/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + orderId;
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

    public void expertConfirmOrderRequest(final int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/confirm";
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
                LogUtil.d("ZLOVE", "expertConfirmOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void expertFinishOrderRequest(final int orderId) {
        String url = Config.WEB_API_SERVER + "/user/order/terminate";
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
                LogUtil.d("ZLOVE", "expertFinishOrderRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void judgeConversation(String target) {
        String sql = "select * from _conversation where _from = ?";
        Cursor cursor = DatabaseManager.getInstance().doQueryAction(sql, new String[]{target});
        if (cursor == null || !cursor.moveToFirst()) {
            JSONObject object = new JSONObject();
            try {
                object.put("target", target);
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "JSONException---" + e.toString());
            }
            MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, object);
        }
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

    public void callServer(int orderId, int userId) {
        String url = Config.WEB_API_SERVER + "/user/pstn/call";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("orderId", String.valueOf(orderId));
        builder.add("to_user", String.valueOf(userId));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    LogUtil.d("ZLOVE", "Call Success");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "callServer---errorMsg---" + errorMsg);
            }
        }, builder);
    }
}
