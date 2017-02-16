package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.RegisterFragment;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZLOVE on 2016/5/4.
 */
public class RegisterControl extends BaseControl<RegisterFragment> {

    public static final String CHECK_MOBILE_AND_GET_IMG_CODE_URL = Config.WEB_API_SERVER + "/imageCaptcha/";
    public static final String GET_VERIFY_CODE_URL = Config.WEB_API_SERVER + "/mobilecheck";
    public static final String REGISTER_URL =  Config.WEB_API_SERVER + "/register";

    public void checkMobileAndGetImgCode(String mobile) {
        String url = CHECK_MOBILE_AND_GET_IMG_CODE_URL + mobile + "?imageType=register";
        OkHttpClientManager.getInstance(getAttachedActivity()).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject object = new JSONObject(result);
                    if (object != null) {
                        String returnCode = object.getString("returncode");
                        if (!returnCode.equals("SUCCESS")) {
                            showShortToast(object.getString("errmsg"));
                        } else {
                            String imgData = object.getString("imageData");
                            getFragment().showImgCodeDialog(imgData);
                        }
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

    public void getVerifyCode(String phone, String code) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", phone);
        builder.add("captcha", code);
        builder.add("flag", "1");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(false, GET_VERIFY_CODE_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.d("ZLOVE", "getVerifyCodeRes---" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object != null) {
                        String returnCode = object.getString("returncode");
                        if (!returnCode.equals("SUCCESS")) {
                            showShortToast(object.getString("errmsg"));
                        } else {
                            showShortToast("验证码已发送");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, builder);
    }

    public void doRegister(final String userName, final String password, String phoneNO, String verifyCode) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("username", userName);
        builder.add("password", password);
        builder.add("mobile", phoneNO);
        builder.add("verifycode", verifyCode);
        builder.add("source", "2");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(REGISTER_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                SysUtil.savePref(PrefKeyConst.PREF_ACCOUNT, userName);
                SysUtil.savePref(PrefKeyConst.PREF_PASSWORD, password);
                getFragment().registerSuccess();
            }
        }, builder);
    }

}
