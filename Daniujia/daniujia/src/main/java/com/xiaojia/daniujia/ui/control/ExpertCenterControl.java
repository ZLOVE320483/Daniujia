package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.ExpertCenterRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ExpertCenterFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/5/13.
 */
public class ExpertCenterControl extends BaseControl<ExpertCenterFragment> {

    public void requestExpertCenterInfo(boolean needDialog) {
        String url = Config.WEB_API_SERVER + "/user/consultant/center/stat/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(needDialog, url, new OkHttpClientManager.ResultCallback<ExpertCenterRespVo>() {
            @Override
            public void onResponse(ExpertCenterRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().setExpertCenterData(result);
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
}
