package com.xiaojia.daniujia.ui.control;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.CouponVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.CouponFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/5/3.
 */
public class CouponControl extends BaseControl<CouponFragment> {

    public void getCouponsRequest(boolean needShow, boolean isHistory, int pageNum) {
        String url = Config.WEB_API_SERVER + "/user/gift/list/"+ SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) +"/"+ (isHistory ? 2 : 1) +"/" + pageNum;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(needShow, url, new OkHttpClientManager.ResultCallback<CouponVo>() {

            @Override
            public void onResponse(CouponVo result) {
                getFragment().setData(result);
            }
        });
    }
}
