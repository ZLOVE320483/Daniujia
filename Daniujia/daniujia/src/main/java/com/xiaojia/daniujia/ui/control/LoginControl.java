package com.xiaojia.daniujia.ui.control;

import android.text.TextUtils;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.LoginSuccessBean;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.LoginFragment;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by ZLOVE on 2016/4/29.
 */
public class LoginControl extends BaseControl<LoginFragment> {

    public static final String loginUrl = Config.WEB_API_SERVER + "/login";
    public static final String socialRegisterUrl = Config.WEB_API_SERVER_V3 + "/third/login";

    public void doLogin(final String account, final String password, final String verCode) {

        if (TextUtils.isEmpty(account) || TextUtils.isEmpty(password)) {
            showShortToast(R.string.login_illeagal);
            return;
        }

        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("username", account);
        builder.add("password", password);
        builder.add("captcha", verCode);
        builder.add("deviceId", ApplicationUtil.getDeviceId(getContext()));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(true, loginUrl, new OkHttpClientManager.ResultCallback<LoginSuccessBean>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "login---onError---" + e.toString());
                e.printStackTrace();
            }

            @Override
            public void onResponse(LoginSuccessBean bean) {
                getFragment().loginSuccess(bean);
            }

            @Override
            public void onFail(String errorMsg) {
                getFragment().loginFail(errorMsg);
                LogUtil.d("ZLOVE", "login---onFail---" + errorMsg);
            }
        }, builder);
    }

    public void doRequestThirdInfo(final Platform platform, final String userId, final String head, final String unionId) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        if (platform.getName().equals(Wechat.NAME)) {
            builder.add("signal", unionId);
        } else {
            builder.add("signal", userId);
        }
        builder.add("socialimage", head);
        builder.add("deviceId", ApplicationUtil.getDeviceId(getContext()));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(socialRegisterUrl, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "---socialRegister---e----" + e.toString());
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "---socialRegister---errorMsg----" + errorMsg);
                super.onFail(errorMsg);
            }

            @Override
            public void onResponse(String result) {
                LogUtil.d("ZLOVE", "---socialRegister---result----" + result);
                getFragment().responseSocialRegister(result, platform, head, userId, unionId);
            }
        }, builder);
    }

    public void checkMobileAndGetImgCode(String userName) {
        String url = Config.WEB_API_SERVER + "/imageCaptcha?imageType=login&username=" + userName;
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object != null) {
                        String imgData = object.getString("imageData");
                        getFragment().showImgCodeDialog(imgData);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "checkMobileAndGetImgCode---errorMsg---" + errorMsg);
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                LogUtil.d("ZLOVE", "checkMobileAndGetImgCode---onError---" + e.toString());
            }
        });
    }
}
