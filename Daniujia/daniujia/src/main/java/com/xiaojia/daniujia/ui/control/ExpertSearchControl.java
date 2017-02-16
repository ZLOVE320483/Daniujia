package com.xiaojia.daniujia.ui.control;

import android.util.Log;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.ExpertSearchHotKeysVo;
import com.xiaojia.daniujia.domain.resp.ExpertSearchVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ExpertSearchFragment;
import com.xiaojia.daniujia.utils.JsonUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class ExpertSearchControl extends BaseControl<ExpertSearchFragment> {

    public void requestHotKeys() {
        String url = Config.WEB_API_SERVER + "/experts/search/hotKeywords";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    SysUtil.savePref(PrefKeyConst.PREF_EXPERT_SEARCH_HOT_LABEL, response);
                    SysUtil.savePref(PrefKeyConst.PREF_CURRENT_TIME, System.currentTimeMillis());
                    ExpertSearchHotKeysVo result = JsonUtil.getCorrectJsonResult(response, ExpertSearchHotKeysVo.class).getData();
                    if (result.getReturncode().equals("SUCCESS")) {
                        getFragment().setHotKey(result);
                    } else {
                        showShortToast(result.getReturnmsg());
                    }
                } catch (Exception e) {
                    LogUtil.d("ZLOVE", "requestHotKeys---onResponse---exception---" + e.toString());
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

    public void searchExpert(String key, int pageNum) {
        String url = Config.WEB_API_SERVER + "/consultants/search";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("keyword", key);
        builder.add("pagenum", String.valueOf(pageNum));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(true, url, new OkHttpClientManager.ResultCallback<ExpertSearchVo>() {
            @Override
            public void onResponse(ExpertSearchVo expertSearchVo) {
                getFragment().setSearchData(expertSearchVo);
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "searchExpert---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void submitSearchRequest(String mobile, String content) {
        String url = Config.WEB_API_SERVER + "/feedback";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", mobile);
        builder.add("content", content);
        builder.add("type", "search");
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
                @Override
                public void onError(Request paramRequest, Exception e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(BaseRespVo baseRespVo) {
                    if (baseRespVo.getReturncode().equals("SUCCESS")) {
                        getFragment().commitSuccess();
                    }
                }

                @Override
                public void onFail(String errorMsg) {
                    LogUtil.d("ZLOVE", "submitSearchRequest---onFail---" + errorMsg);
                }
            }, builder);
        } else {
            OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
                @Override
                public void onError(Request paramRequest, Exception e) {
                    Log.d("ZLOVE", "submitSearchRequest---onError---" + e.getMessage());
                    e.printStackTrace();
                }

                @Override
                public void onResponse(BaseRespVo baseRespVo) {
                    if (baseRespVo.getReturncode().equals("SUCCESS")) {
                        getFragment().commitSuccess();
                    }
                }

                @Override
                public void onFail(String errorMsg) {
                    Log.d("ZLOVE", "submitSearchRequest---onFail---" + errorMsg);
                }
            }, builder);
        }

    }
}
