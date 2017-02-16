package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.PublishRequireData;
import com.xiaojia.daniujia.domain.resp.PublishRequireVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.RequirementPublishFragment;

/**
 * Created by Administrator on 2017/1/4.
 */
public class RequirementPublishControl extends BaseControl<RequirementPublishFragment> {

    public void initRequireData(PublishRequireData data) {
        String url = Config.WEB_API_SERVER + "/demands";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("consultantName", data.getConsultantName());
        builder.add("budget", data.getBudget());
        builder.add("desc", data.getDesc());
        builder.add("serviceDay", data.getServiceDay());
        builder.add("serviceCycle", data.getServiceCycle());
        builder.add("writeName", data.getWriteName());
        builder.add("cityCode", data.getCityCode());
        builder.add("deadline", String.valueOf(data.getDeadline()));

        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<PublishRequireVo>() {
            @Override
            public void onResponse(PublishRequireVo requireListVo) {
                if (requireListVo.getReturncode().equals("SUCCESS")) {
                    getFragment().publishSuccess(requireListVo);
                } else {
                    showShortToast(requireListVo.getReturnmsg());
                }
            }

        },builder);
    }

    public void changeRequireData(PublishRequireData data,int requireId) {
        String url = Config.WEB_API_SERVER + "/demands/" + requireId;
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("consultantName", data.getConsultantName());
        builder.add("budget", data.getBudget());
        builder.add("desc", data.getDesc());
        builder.add("serviceDay", data.getServiceDay());
        builder.add("serviceCycle", data.getServiceCycle());
        builder.add("writeName", data.getWriteName());
        builder.add("cityCode", data.getCityCode());
        builder.add("deadline", String.valueOf(data.getDeadline()));

        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<PublishRequireVo>() {
            @Override
            public void onResponse(PublishRequireVo requireListVo) {
                getFragment().publishSuccess(requireListVo);

            }

        },builder);
    }
}
