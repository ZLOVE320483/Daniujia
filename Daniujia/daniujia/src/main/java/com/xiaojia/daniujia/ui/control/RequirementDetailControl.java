package com.xiaojia.daniujia.ui.control;

import android.database.Cursor;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.RequirementDetailRespVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.ui.frag.RequirementDetailFragment;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZLOVE on 2017/1/4.
 */
public class RequirementDetailControl extends BaseControl<RequirementDetailFragment> {

    public void requestRequirementDetail(int demandId) {
        String url = Config.WEB_API_SERVER + "/demands/" + demandId;
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<RequirementDetailRespVo>() {

                @Override
                public void onResponse(RequirementDetailRespVo result) {
                    getFragment().setRequirementDetailInfo(result);
                }

                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest, e);
                    LogUtil.d("ZLOVE", "requestRequirementDetail---onError---" + e.getMessage());
                }

                @Override
                public void onFail(String errorMsg) {
                    super.onFail(errorMsg);
                    LogUtil.d("ZLOVE", "requestRequirementDetail---errorMsg---" + errorMsg);
                }
            });
        } else {
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<RequirementDetailRespVo>() {

                @Override
                public void onResponse(RequirementDetailRespVo result) {
                    getFragment().setRequirementDetailInfo(result);
                }

                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest, e);
                    LogUtil.d("ZLOVE", "requestRequirementDetail---onError---" + e.getMessage());
                }

                @Override
                public void onFail(String errorMsg) {
                    super.onFail(errorMsg);
                    LogUtil.d("ZLOVE", "requestRequirementDetail---errorMsg---" + errorMsg);
                }
            });
        }

    }

    public void signUpRequirement(final int demandId, final String type) {
        String url = Config.WEB_API_SERVER + "/demand/applicants";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("demandId", String.valueOf(demandId));
        builder.add("type", type);
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    if (type.equals("out")) {
                        showShortToast("已取消");
                    } else {
                        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1 && SysUtil.getIntPref(PrefKeyConst.PREF_APPLY_STEP) == 15) {
                            CommonDialog callTipDialog = new CommonDialog(getAttachedActivity(), null, "温馨提示",
                                    getContext().getResources().getString(R.string.sign_up_checking_tip), "知道了", true);
                            callTipDialog.showdialog();
                        } else {
                            showShortToast("报名成功");
                        }
                    }
                    requestRequirementDetail(demandId);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    public void confirmExpert(final int demandId, int expertId, String content) {
        String url = Config.WEB_API_SERVER + "/demand/applicants/" + expertId;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("demandId", String.valueOf(demandId));
        builder.add("content", content);
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast("已确定");
                    requestRequirementDetail(demandId);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    public void callPhone(int demandId, int expertId, String type) {
        String url = Config.WEB_API_SERVER + "/demand/pstn";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("demandId", String.valueOf(demandId));
        builder.add("expertId", String.valueOf(expertId));
        builder.add("type", String.valueOf(type));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        }, builder);
    }

    public void requestUserInfo() {
        String url = Config.WEB_API_SERVER + "/user/center/info/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<UserCenterRespVo>() {
            @Override
            public void onResponse(UserCenterRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setUserCenterInfo(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                LogUtil.d("ZLOVE", "requestUserInfo----Exception----" + e.toString());
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "requestUserInfo----errorMsg----" + errorMsg);
            }
        });
    }

    public void notifyTobeExpert() {
        String url = Config.WEB_API_SERVER + "/user/beconsultanttip";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
            }
        }, builder);
    }

    public void notifyBecomeExpert() {
        String url = Config.WEB_API_SERVER + "/user/consultant/first/login";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
            }
        }, builder);
    }

    public void judgeConversation(String target) {
        String sql = "select * from _conversation where _target_name = ?";
        Cursor cursor = DatabaseManager.getInstance().doQueryAction(sql, new String[]{target});
        if (cursor == null || !cursor.moveToFirst()) {
            JSONObject object = new JSONObject();
            try {
                object.put("target", target);
                object.put("type", "singleChat");
                object.put("allowEmpty", true);
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "JSONException---" + e.toString());
            }
            MqttUtils.publish_request(MqttServiceConstants.PULL_CONVERSATIONS_REQUEST, object);
        }
    }
}
