package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.HomeNewBeanVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.HomeNewFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/4/25
 */
public class HomeNewControl extends BaseControl<HomeNewFragment> {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData(final boolean isShowProgress) {
        String url = Config.WEB_API_SERVER + "/homepage";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(isShowProgress,url, new OkHttpClientManager.ResultCallback<HomeNewBeanVo>() {
            @Override
            public void onResponse(HomeNewBeanVo homeBeanVo) {
                getFragment().initData(homeBeanVo,isShowProgress);

            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                if (getFragment() != null && getFragment().getHomeScrollView() != null){
                    getFragment().getHomeScrollView().onRefreshComplete();
                }

            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                if (getFragment() != null && getFragment().getHomeScrollView() != null){
                    getFragment().getHomeScrollView().onRefreshComplete();
                }
            }
        });
    }

    public void requestUserInfo(final boolean isNeedGo) {
        String url = Config.WEB_API_SERVER + "/user/center/info/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<UserCenterRespVo>() {
            @Override
            public void onResponse(UserCenterRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    LogUtil.d("zptest","thread: " + Thread.currentThread().getName());
                    getFragment().goToCenterInfo(result,isNeedGo);
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

}
