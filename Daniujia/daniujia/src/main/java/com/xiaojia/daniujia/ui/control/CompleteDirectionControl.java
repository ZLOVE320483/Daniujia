package com.xiaojia.daniujia.ui.control;

import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.ConcreteDirection;
import com.xiaojia.daniujia.domain.resp.ConcreteDirectionRespVo;
import com.xiaojia.daniujia.domain.resp.DirectionRelativeIndustryRespVo;
import com.xiaojia.daniujia.domain.resp.RelatedIndustry;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.CompleteDirectionFragment;
import com.xiaojia.daniujia.utils.LogUtil;

import java.util.List;

/**
 * Created by ZLOVE on 2016/7/1.
 */
public class CompleteDirectionControl extends BaseControl<CompleteDirectionFragment> {

    public void requestRelativeIndustry(String keyWord) {
        String url = Config.WEB_API_SERVER_V3 + "/become/search/industry/" + keyWord;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(false, url, new OkHttpClientManager.ResultCallback<DirectionRelativeIndustryRespVo>() {
            @Override
            public void onResponse(DirectionRelativeIndustryRespVo result) {
                getFragment().handleSearchResp(result);
            }
        });
    }

    public void requestConcreteDirection(int subId) {
        String url = Config.WEB_API_SERVER_V3 + "/become/concrete/directions/" + subId;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<ConcreteDirectionRespVo>() {
            @Override
            public void onResponse(ConcreteDirectionRespVo result) {
                getFragment().setConcreteDirectionData(result);
            }
        });
    }

    public void addOrUpdateDirectionRequest(int specId, int subId, String writeName, String workage, List<RelatedIndustry> relatedIndustrys, List<ConcreteDirection> concreteDirections) {
        String url = Config.WEB_API_SERVER_V3 + "/become/speciality/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("specId", String.valueOf(specId));
        builder.add("subId", String.valueOf(subId));
        builder.add("writeName", writeName);
        builder.add("workage", workage);
        builder.add("relatedIndustrys", JSON.toJSONString(relatedIndustrys));
        builder.add("concreteDirections", JSON.toJSONString(concreteDirections));

        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast("完善成功");
                    Intent intent = new Intent();
                    finishActivity(intent);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "addOrUpdateDirectionRequest---onFail---" + errorMsg);
            }
        }, builder);
    }
}
