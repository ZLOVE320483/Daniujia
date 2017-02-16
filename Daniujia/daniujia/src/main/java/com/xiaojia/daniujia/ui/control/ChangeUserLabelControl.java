package com.xiaojia.daniujia.ui.control;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.UserTag;
import com.xiaojia.daniujia.domain.resp.AddUserLabelVo;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.domain.resp.UserLabelVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ChangeUserLabelFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZLOVE on 2016/5/10.
 */
public class ChangeUserLabelControl extends BaseControl<ChangeUserLabelFragment> {

    public void requestUserLabel(int functionId) {
        String url = Config.WEB_API_SERVER + "/user/my/tags/and/common/tags/" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID) + "/" + functionId;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<UserLabelVo>() {
            @Override
            public void onResponse(UserLabelVo result) {
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

    public void addLabelRequest(int functionId, String labelName) {
        String url = Config.WEB_API_SERVER + "/user/tag/add";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("functionId", String.valueOf(functionId));
        builder.add("tagName", labelName);
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<AddUserLabelVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "addLabelRequest---onError---" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(AddUserLabelVo addUserLabelVo) {
                getFragment().addLabelResp(addUserLabelVo);
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "addLabelRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void saveUserLabelRequest(int functionId, List<UserLabelVo.MyTagInfo> tagInfoList) {
        List<UserTag> tagIds = new ArrayList<>();
        for (UserLabelVo.MyTagInfo info : tagInfoList) {
            UserTag tag = new UserTag();
            tag.setTagId(info.getTagId());
            tagIds.add(tag);
        }

        String url = Config.WEB_API_SERVER + "/user/tag/save";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("functionId", String.valueOf(functionId));
        builder.add("tagIds", JSON.toJSONString(tagIds));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "saveUserLabelRequest---onError---" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    getFragment().saveLabelSuccess();
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "saveUserLabelRequest---onFail---" + errorMsg);
            }
        }, builder);
    }
}
