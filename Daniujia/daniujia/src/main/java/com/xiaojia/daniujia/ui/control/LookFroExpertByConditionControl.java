package com.xiaojia.daniujia.ui.control;

import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.ExpertInformationVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.LookFroExpertByConditionFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/12/15.
 */
public class LookFroExpertByConditionControl extends BaseControl<LookFroExpertByConditionFragment> {

    public void getExpertInfo(LookFroExpertByConditionFragment.LocalSearchCondition data, boolean byVoice) {
        if (data == null){
            return;
        }

        String url = Config.WEB_API_SERVER_V3 + "/experts/search";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (!TextUtils.isEmpty(data.getKeyword())){
            builder.add("keyword",data.getKeyword());
        }
        builder.add("pageNum",String.valueOf(getFragment().getPageIndex()));
        builder.add("pageCount",String.valueOf(LookFroExpertByConditionFragment.PAGE_SIZE));
        builder.add("cityId",String.valueOf(data.getCityId()));
        builder.add("provinceId",String.valueOf(data.getProvinceId()));
        if (!TextUtils.isEmpty(data.getIndustryParentId())) {
            builder.add("industryParentId",data.getIndustryParentId());
        }
        builder.add("industryId",String.valueOf(data.getIndustryId()));
        if (!TextUtils.isEmpty(data.getMainDirectionId())){
            builder.add("mainDirectionId",data.getMainDirectionId());
        }
        builder.add("subDirectionId",String.valueOf(data.getSubDirectionId()));
        builder.add("sort",data.getSort());
        builder.add("byVoice", byVoice ? "true" : "false");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(url, new OkHttpClientManager.ResultCallback<ExpertInformationVo>() {

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "classifications---onFail---errorMsg---" + errorMsg);
                getFragment().stopRefresh();
            }

            @Override
            public void onResponse(ExpertInformationVo result) {
                getFragment().onReceiveExpertInfo(result);

            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                showShortToast("网络超时");
                LogUtil.d("ZLOVE", "classifications---onError---e---" + e.toString());
                getFragment().stopRefresh();
            }
        },builder);
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
}
