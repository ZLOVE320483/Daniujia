package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.RequireListVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.UserRequirementFragment;

/**
 * Created by Administrator on 2017/1/3.
 */
public class UserRequirementControl extends BaseControl<UserRequirementFragment> {

    public void initRequireData(boolean isShowProgress,String type,int pagerNumber,int pagerIndex , final boolean isFresh) {
        String url = Config.WEB_API_SERVER + "/user/demands?type=" + type + "&cursor=" + pagerIndex + "&count=" + pagerNumber;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(isShowProgress,url, new OkHttpClientManager.ResultCallback<RequireListVo>() {
            @Override
            public void onResponse(RequireListVo requireListVo) {
                getFragment().stopListRefresh();
                getFragment().setRequireData(requireListVo,isFresh);

            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                getFragment().stopListRefresh();
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                getFragment().stopListRefresh();
            }
        });
    }
}
