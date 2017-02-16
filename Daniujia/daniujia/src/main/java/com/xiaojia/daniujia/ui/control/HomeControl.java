package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.HomeNewBeanVo;
import com.xiaojia.daniujia.domain.resp.ShowActRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.HomeFragment;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by Administrator on 2016/4/25
 */
public class HomeControl extends BaseControl<HomeFragment> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

//    public void initData(boolean needLoading) {
//        String url = Config.WEB_API_SERVER + "/index";
//        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(needLoading,url, new OkHttpClientManager.ResultCallback<HomeBeanVo>() {
//            @Override
//            public void onResponse(HomeBeanVo homeBeanVo) {
//                getFragment().refreshData(homeBeanVo);
//            }
//
//            @Override
//            public void onFail(String errorMsg) {
//                super.onFail(errorMsg);
//                LogUtil.d("ZLOVE", "errorMsg---" + errorMsg);
//                getFragment().stopRefresh();
//            }
//
//            @Override
//            public void onError(Request paramRequest, Exception e) {
//                super.onError(paramRequest, e);
//                getFragment().stopRefresh();
//            }
//        });
//    }

    public void initHomeData(boolean isShowProgress) {
        String url = Config.WEB_API_SERVER + "/homepage";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(isShowProgress,url, new OkHttpClientManager.ResultCallback<HomeNewBeanVo>() {
            @Override
            public void onResponse(HomeNewBeanVo homeBeanVo) {
                getFragment().refreshData(homeBeanVo);

            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                getFragment().stopRefresh();

            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                getFragment().stopRefresh();
            }
        });
    }

    public void requestShowAct() {
        String url = Config.WEB_API_SERVER + "/device/activity";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("deviceNo", ApplicationUtil.getDeviceId(getAttachedActivity()));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(false, url, new OkHttpClientManager.ResultCallback<ShowActRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(ShowActRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().showActDialog(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "requestShowAct---onFail---" + errorMsg);
            }
        }, builder);
    }
}
