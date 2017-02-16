package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.EntrepreneurshipGuideFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/4/28.
 */
public class EntrepreneurshipGuideControl extends BaseControl<EntrepreneurshipGuideFragment> {

    public int pageNum = 1;
    private String url;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData(boolean needShow, int mainDirectionId, String subDirectionCode, String mainDirectionCode) {
        if (ExtraConst.FROM_MY_COLLECTION.equals(getFragment().fromWhere)) {
            url = Config.WEB_API_SERVER_V3 + "/user/consultants/favorite/new/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + mainDirectionCode + "/" + pageNum;
            OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(needShow, url, new OkHttpClientManager.ResultCallback<NovationByTopicVo>() {
                @Override
                public void onResponse(NovationByTopicVo result) {
                    getFragment().refreshData(result);
                }
            });
        } else if (ExtraConst.FROM_ENTER_GUIDE.equals(getFragment().fromWhere) ) {
            url = Config.WEB_API_SERVER_V3 + "/consultants/by/direction/" + mainDirectionId + "/" + subDirectionCode + "/" + pageNum;

            OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(needShow, url, new OkHttpClientManager.ResultCallback<NovationByTopicVo>() {
                @Override
                public void onResponse(NovationByTopicVo result) {
                    getFragment().refreshData(result);
                }
            });
        }
    }

    public void onLoadMore(boolean needShow, int mainDirectionId, String subDirectionCode, String mainDirectionCode) {
        ++pageNum;
        initData(needShow, mainDirectionId, subDirectionCode, mainDirectionCode);
    }

    public void onRefresh(boolean needShow, int mainDirectionId, String subDirectionCode, String mainDirectionCode) {
        pageNum = 1;
        initData(needShow, mainDirectionId, subDirectionCode, mainDirectionCode);
    }
}
