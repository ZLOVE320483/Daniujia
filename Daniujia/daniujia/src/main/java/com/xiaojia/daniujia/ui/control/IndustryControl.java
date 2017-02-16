package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVo;
import com.xiaojia.daniujia.domain.resp.NovationByTopicVoExtends;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.IndustryFragment;

/**
 * Created by Administrator on 2016/5/27.
 */
public class IndustryControl extends BaseControl<IndustryFragment>{

    public int pageNum = 1;
    private String url;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData(boolean isFirst, int mainDirectionId, String subDirectionCode) {
        url = Config.WEB_API_SERVER_V3 + "/consultants/by/direction/" + mainDirectionId + "/" + subDirectionCode + "/" + pageNum;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(isFirst, url, new OkHttpClientManager.ResultCallback<NovationByTopicVo>() {
            @Override
            public void onResponse(NovationByTopicVo result) {
                getFragment().refreshData(result);
            }
        });
    }

    public void onLoadMore(boolean isFirst, int mainDirectionId, String subDirectionCode) {
        ++pageNum;
        initData(isFirst, mainDirectionId, subDirectionCode);
    }

    public void onRefresh(boolean isFirst, int mainDirectionId, String subDirectionCode) {
        pageNum = 1;
        initData(isFirst, mainDirectionId, subDirectionCode);
    }
}
