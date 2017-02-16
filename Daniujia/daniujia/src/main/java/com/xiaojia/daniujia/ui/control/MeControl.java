package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.ChildShareRespVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.MeFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/5/9.
 */
public class MeControl extends BaseControl<MeFragment> {

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

    public void showKnowledgeShareIcon(){
        String url = Config.WEB_API_SERVER + "/init";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(false, url, new OkHttpClientManager.ResultCallback<ChildShareRespVo>() {
            @Override
            public void onResponse(ChildShareRespVo result) {
                getFragment().onSetKnowledgeState(result);
            }
        });
    }

//    public void getKnowledgeIconState(){
//        String url = Config.WEB_API_SERVER + "/init";
//        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<ChildShareRespVo>() {
//            @Override
//            public void onResponse(ChildShareRespVo result) {
//                getFragment().onSetKnowledgeState(result);
//            }
//        });
//    }

}
