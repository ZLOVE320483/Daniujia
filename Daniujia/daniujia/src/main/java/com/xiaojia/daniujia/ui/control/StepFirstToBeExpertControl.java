package com.xiaojia.daniujia.ui.control;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.BeExpertBaseInfo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.StepFirstToBeExpertNew;

/**
 * Created by ZLOVE on 2016/7/5.
 */
public class StepFirstToBeExpertControl extends BaseControl<StepFirstToBeExpertNew> {

    public void requestBaseInfo() {
        String url = Config.WEB_API_SERVER_V3 + "/become/base/info";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<BeExpertBaseInfo>() {
            @Override
            public void onResponse(BeExpertBaseInfo result) {
                getFragment().refreshDate(result);
            }
        });
    }
}
