package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.CollectMeRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.CollectMeFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/5/13.
 */
public class CollectMeControl extends BaseControl<CollectMeFragment> {

    public void requestCollectMeInfo(int pageNum) {
        String url = Config.WEB_API_SERVER + "/user/consultant/center/favorites/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + pageNum;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<CollectMeRespVo>() {
            @Override
            public void onResponse(CollectMeRespVo result) {
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
}
