package com.xiaojia.daniujia.ui.control;

import android.os.Bundle;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.WorkExperienceVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.StepSecondToBeExpertFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/5/24.
 */
public class StepSecondControl extends BaseControl<StepSecondToBeExpertFragment>{

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initData(){
        String url = Config.WEB_API_SERVER_V3 + "/user/careers/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<WorkExperienceVo>() {
            @Override
            public void onResponse(WorkExperienceVo result) {
                getFragment().refreshData(result);
            }
        });
    }
}
