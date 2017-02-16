package com.xiaojia.daniujia.ui.act;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.dlgs.AlertBigMsgDlg;
import com.xiaojia.daniujia.domain.resp.UserProfileRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/5/23.
 */
public class ActProfileEdited extends AbsBaseActivity implements View.OnFocusChangeListener, View.OnClickListener {

    private EditText account_profile;
    private TextView tvExample;

    private String example;
    private AlertBigMsgDlg alertMsgDlg;
    private CommonDialog modifyServiceDialog = null;
    private String tempProfile;

    private CommonDialog.ConfirmAction listener = new CommonDialog.ConfirmAction() {
        @Override
        public void confirm() {
            setResult(Activity.RESULT_OK);
            finish();
        }

        @Override
        public void cancel() {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edited);
        account_profile = (EditText) findViewById(R.id.account_profile);
        account_profile.setOnFocusChangeListener(this);
        TextView next = (TextView) findViewById(R.id.title_right);
        next.setVisibility(View.VISIBLE);
        next.setText("保存");
        next.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.blue_text));
        next.setOnClickListener(this);
        TextView title = (TextView) findViewById(R.id.id_title);
        title.setText("个人简介");
        ImageView id_back = (ImageView) findViewById(R.id.id_back);
        id_back.setOnClickListener(this);
        tvExample = (TextView) findViewById(R.id.id_example);
        tvExample.setOnClickListener(this);

        if (getIntent().hasExtra(ExtraConst.EXTRA_PROFILE)) {
            String profile = getIntent().getStringExtra(ExtraConst.EXTRA_PROFILE);
            account_profile.setText(profile);
        }

        getPersonProfileRequest();
        getExampleRequest();
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            account_profile.setHint("");
            account_profile.setTextColor(getResources().getColor(R.color.first_text_color));
        } else {
            account_profile.setHint(R.string.account_profile_new);
            account_profile.setTextColor(getResources().getColor(R.color.third_text_color));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_back) {
            finish();
        } else if (v.getId() == R.id.title_right) {
            String profile = account_profile.getText().toString().trim();
            if (TextUtils.isEmpty(profile)) {
                if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2) {
                    showShortToast("您是专家，个人简介不能修改为空");
                } else {
                    showShortToast("您正在申请成为专家，个人简介不能修改为空");
                }
                return;
            }

            if (!TextUtils.isEmpty(tempProfile) && tempProfile.equals(profile)) {
                finish();
            } else {
                String url = Config.WEB_API_SERVER_V3 + "/user/profile/update";
                FormEncodingBuilder builder = new FormEncodingBuilder();
                builder.add("userId", "" + SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID));
                builder.add("profile", profile);
                OkHttpClientManager.getInstance(this).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        if (modifyServiceDialog == null) {
                            modifyServiceDialog = new CommonDialog(ActProfileEdited.this, listener,
                                    "提示", "已收到您的个人简介修改申请，我们会尽快进行审核", true);
                        }
                        modifyServiceDialog.showdialog();
                    }
                }, builder);
            }

        } else if (v.getId() == R.id.id_example) {
            if (alertMsgDlg == null) {
                alertMsgDlg = new AlertBigMsgDlg(this);
            }
            alertMsgDlg.initDlg(R.drawable.ic_question_blue, "个人简介", example);
            alertMsgDlg.show();
        }
    }

    private void getPersonProfileRequest() {
        String url = Config.WEB_API_SERVER_V3 + "/become/profile";
        OkHttpClientManager.getInstance(this).getWithToken(true, url, new OkHttpClientManager.ResultCallback<UserProfileRespVo>() {
            @Override
            public void onResponse(UserProfileRespVo result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.getProfile())) {
                    tempProfile = result.getProfile();
                    account_profile.setText(result.getProfile());
                }
            }
        });
    }

    private void getExampleRequest() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/become_user_profile";
        OkHttpClientManager.getInstance(this).getWithPlatform(false, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                if (TextUtils.isEmpty(result)) {
                    return;
                }
                result = result.replace("\\n", "n");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    example = jsonObject.getString("tipContent");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
