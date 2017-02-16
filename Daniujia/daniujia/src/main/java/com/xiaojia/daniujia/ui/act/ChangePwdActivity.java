package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.domain.resp.ChangePwdResp;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.OkHttpClientManager.ResultCallback;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePwdActivity extends AbsBaseActivity implements OnClickListener {
    private View titleView;
    private EditText etOldPassword, etNewPassword, etPasswordConfirm;
    private Button btnComplete;
    private TextView tvTip, tvName, tvPhone;
    private LinearLayout llPhone, llName;
    private int isThirdLogin = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        initView();
        doRequestAccountInfo();
    }

    private void doRequestAccountInfo() {
        String url = Config.WEB_API_SERVER + "/user/social/password/show/"
                + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID);
        OkHttpClientManager.getInstance(activity).getWithToken(url, new ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    isThirdLogin = json.getInt("issocial");
                    if (isThirdLogin == 0) { // not third login
                        etOldPassword.setVisibility(View.VISIBLE);
                    } else if (isThirdLogin == 1) {
                        String phone = json.getString("mobile");
                        String username = json.getString("username");
                        llName.setVisibility(View.VISIBLE);
                        llPhone.setVisibility(View.VISIBLE);
                        tvTip.setVisibility(View.VISIBLE);
                        tvName.setText(username);
                        tvPhone.setText(phone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void initView() {
        titleView = findViewById(R.id.layout_title);
        ImageView iv_back = (ImageView) titleView.findViewById(R.id.iv_title_back);
        TextView title = (TextView) titleView.findViewById(R.id.tv_title);
        title.setText("修改密码");
        iv_back.setOnClickListener(this);
        etOldPassword = (EditText) findViewById(R.id.old_pwd);
        etNewPassword = (EditText) findViewById(R.id.new_pwd);
        etPasswordConfirm = (EditText) findViewById(R.id.re_pwd);
        btnComplete = (Button) findViewById(R.id.btn_change_complete);
        tvTip = (TextView) findViewById(R.id.tv_change_pwd_tip);
        tvPhone = (TextView) findViewById(R.id.tv_change_pwd_phone);
        tvName = (TextView) findViewById(R.id.tv_change_pwd_name);
        llName = (LinearLayout) findViewById(R.id.ll_change_pwd_name);
        llPhone = (LinearLayout) findViewById(R.id.ll_change_pwd_phone);
        btnComplete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                doUpdatePassword();
            }
        });
    }

    private void doUpdatePassword() {
        String oldPwd, newPwd, rePwd;
        oldPwd = etOldPassword.getText().toString();
        newPwd = etNewPassword.getText().toString();
        rePwd = etPasswordConfirm.getText().toString();
        if (isThirdLogin == 0) {
            if (TextUtils.isEmpty(oldPwd)) {
                showShortToast(R.string.input_old_pwd);
                return;
            } else if (TextUtils.isEmpty(newPwd)) {
                showShortToast(R.string.input_new_pwd);
                return;
            } else if (!newPwd.equals(rePwd)) {
                showShortToast(R.string.twice_pwd_not_equal);
                return;
            }
        } else {
            if (TextUtils.isEmpty(newPwd)) {
                showShortToast(R.string.input_new_pwd);
                return;
            } else if (!newPwd.equals(rePwd)) {
                showShortToast(R.string.twice_pwd_not_equal);
                return;
            }
            oldPwd = "";
        }
        String url = Config.WEB_API_SERVER_V3 + "/user/password/change";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userid", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("oldpassword", oldPwd);
        builder.add("newpassword", rePwd);
        builder.add("repeatpassword", rePwd);
        final String savePwd = rePwd;
        OkHttpClientManager.getInstance(activity).postWithToken(url, new ResultCallback<ChangePwdResp>() {
            @Override
            public void onResponse(ChangePwdResp result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    showShortToast(R.string.pwd_change_success);
                    SysUtil.savePref(PrefKeyConst.PREF_PASSWORD, savePwd);
                    if (!TextUtils.isEmpty(result.getToken())) {
                        SysUtil.savePref(PrefKeyConst.PREF_TOKEN, result.getToken());
                    }
                    finish();
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
                LogUtil.d("ZLOVE", "doUpdatePassword---onError---e---" + e.toString());
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
                LogUtil.d("ZLOVE", "doUpdatePassword---onFail---e---" + errorMsg);
            }
        }, builder);
    }

    @Override
    public void onClick(View v) {
        finish();
    }
}
