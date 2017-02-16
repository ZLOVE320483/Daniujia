package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.domain.resp.CommonVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ContactUsFragment;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ContactUsControl extends BaseControl<ContactUsFragment> {
    public void submitSuggestionForLogin(String suggestion){
        String url = Config.WEB_API_SERVER_V3 + "/user/feedback";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("name", SysUtil.getPref(PrefKeyConst.PREF_USER_NAME));
        builder.add("content",suggestion);

        OkHttpClientManager.getInstance(getAttachedActivity())
                .postWithToken(true, url, new OkHttpClientManager.ResultCallback<CommonVo>() {
            @Override
            public void onResponse(CommonVo result) {
                getFragment().commitSuccess();
            }
        },builder);
    }

    public void submitSuggestionNotLogin(String suggestion,String phone){
        String url = Config.WEB_API_SERVER_V3 + "/user/feedback";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("content",suggestion);
        builder.add("phonenum",phone);

        OkHttpClientManager.getInstance(getAttachedActivity())
                .postWithPlatform(true, url, new OkHttpClientManager.ResultCallback<CommonVo>() {
            @Override
            public void onResponse(CommonVo result) {
                getFragment().commitSuccess();
            }
        },builder);
    }
}
