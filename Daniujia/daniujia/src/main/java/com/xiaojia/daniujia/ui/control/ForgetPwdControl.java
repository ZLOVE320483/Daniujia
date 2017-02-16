package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.frag.ForgetPwdFragment;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ZLOVE on 2016/5/4.
 */
public class ForgetPwdControl extends BaseControl<ForgetPwdFragment> implements DialogManager.ConfirmActionListener {

    public static final String GET_VER_CODE_URL = Config.WEB_API_SERVER + "/mobilecheck";
    public static final String RESET_PWD_URL = Config.WEB_API_SERVER + "/password/reset";

    public void getVerCode(String phone) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", phone);
        builder.add("flag", "2");
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(GET_VER_CODE_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                LogUtil.d("ZLOVE", "getVerifyCodeRes---" + result);
                try {
                    JSONObject object = new JSONObject(result);
                    if (object != null) {
                        String returnCode = object.getString("returncode");
                        if (!returnCode.equals("SUCCESS")) {
                            showShortToast(object.getString("errmsg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, builder);
    }

    public void resetPassword(String phone, String verifyCode, String password, String confirmPassword) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", phone);
        builder.add("verifycode", verifyCode);
        builder.add("newpassword", password);
        builder.add("repeatpassword", confirmPassword);

        OkHttpClientManager.getInstance(getAttachedActivity()).postWithPlatform(RESET_PWD_URL, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String paramT) {
                try {
                    JSONObject object = new JSONObject(paramT);
                    String code = object.optString("returncode");
                    String msg = object.optString("returnmsg");
                    if(code.equalsIgnoreCase("success")){
                        DialogManager.showForgetPwdDialog(getAttachedActivity(), ForgetPwdControl.this);
                    }else{
                        showShortToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, builder);
    }

    @Override
    public void confirm() {
        finishActivity();
    }
}
