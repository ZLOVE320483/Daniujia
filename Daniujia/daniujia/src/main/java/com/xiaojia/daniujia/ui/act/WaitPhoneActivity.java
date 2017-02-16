package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/3/25
 */
public class WaitPhoneActivity  extends AbsBaseActivity{

    private Button btn;

    private int userId = 0;
    private int orderId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_callwait);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_ID)) {
                userId = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
        }

        LogUtil.d("ZLOVE", "userId---" + userId);
        LogUtil.d("ZLOVE", "orderId---" + orderId);

        btn = (Button) findViewById(R.id.back);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                showShortToast("仍在为您接通中...");
            }
        });

        callServer();
    }

    private void callServer() {
        String url = Config.WEB_API_SERVER + "/user/pstn/call";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("orderId", String.valueOf(orderId));
        builder.add("to_user", String.valueOf(userId));
        OkHttpClientManager.getInstance(activity).postWithToken(false, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    LogUtil.d("ZLOVE", "Call Success");
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "callServer---errorMsg---" + errorMsg);
            }
        }, builder);
    }
}
