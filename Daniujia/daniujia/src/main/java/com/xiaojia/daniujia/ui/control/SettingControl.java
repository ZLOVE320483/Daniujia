package com.xiaojia.daniujia.ui.control;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.db.DatabaseManager;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.ui.frag.SettingFragment;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONObject;

/**
 * Created by ZLOVE on 2016/5/3.
 */
public class SettingControl extends BaseControl<SettingFragment> {

    public static final String logoutUrl = Config.WEB_API_SERVER + "/user/logout";

    public void logOut() {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        OkHttpClientManager.getInstance(getAttachedActivity()).postWithToken(logoutUrl, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
                MqttUtils.disconnect();
                MqttUtils.handleConnectionLost();
                SysUtil.clearUserInfo();
                SysUtil.removePref(PrefKeyConst.PREF_PASSWORD);
                DatabaseManager.releaseInstance();
                finishActivity();
            }
        }, builder);
    }

}
