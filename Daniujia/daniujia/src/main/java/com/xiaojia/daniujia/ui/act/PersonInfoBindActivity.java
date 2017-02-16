package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.managers.OkHttpClientManager.ResultCallback;
import com.xiaojia.daniujia.ui.views.VerifyCodeDialog;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

public class PersonInfoBindActivity extends AbsBaseActivity implements OnClickListener, VerifyCodeDialog.VerifyCodeListener, TagAliasCallback {

    public static final String CHECK_MOBILE_AND_GET_IMG_CODE_URL = Config.WEB_API_SERVER + "/imageCaptcha/";

    private View titleView, userNamePwdContainer;
    private EditText etPhone, etVercode, etUsername, etPassword;
    private Button btnComplete, btnGetVercode;
    private GetVerifyCodeTimer mTimeCount;
    private String qquid, weibouid, wxopenid, unionid, head;
    private int userexist = -1;

    private VerifyCodeDialog dialog;
    private String bindType = "qq";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_person_info_bind);
        Intent data = getIntent();
        if (getIntent() != null) {
            qquid = data.getStringExtra("qquid");
            weibouid = data.getStringExtra("weibouid");
            wxopenid = data.getStringExtra("wxopenid");
            unionid = data.getStringExtra("unionid");
            head = data.getStringExtra("socialimage");
        }
        if (!TextUtils.isEmpty(qquid)) {
            bindType = "qq";
        } else if (!TextUtils.isEmpty(weibouid)) {
            bindType = "weibo";
        } else if (!TextUtils.isEmpty(wxopenid)) {
            bindType = "wechat";
        }
        initView();
        mTimeCount = new GetVerifyCodeTimer(60 * 1000, 1000);
    }

    private void initView() {
        titleView = findViewById(R.id.layout_title);
        userNamePwdContainer = findViewById(R.id.user_name_pwd_container);
        ImageView iv_back = (ImageView) titleView.findViewById(R.id.iv_title_back);
        iv_back.setOnClickListener(this);
        TextView title = (TextView) titleView.findViewById(R.id.tv_title);
        title.setText("绑定个人信息");
        etPhone = (EditText) findViewById(R.id.et_bind_phone);
        etVercode = (EditText) findViewById(R.id.et_bind_verifycode);
        etUsername = (EditText) findViewById(R.id.et_bind_username);
        etPassword = (EditText) findViewById(R.id.id_password);
        btnComplete = (Button) findViewById(R.id.btn_bind_complete);
        btnGetVercode = (Button) findViewById(R.id.btn_bind_vercode);
        btnGetVercode.setClickable(false);
        btnComplete.setOnClickListener(onClickListener);
        btnGetVercode.setOnClickListener(onClickListener);

        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String tmpPhone = s.toString().trim();
                if (tmpPhone.length() >= 11) {
                    btnGetVercode.setClickable(true);
                    btnGetVercode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_green_solid));
                } else {
                    btnGetVercode.setClickable(false);
                    btnGetVercode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_grey_solid));
                }
            }
        });
    }

    private OnClickListener onClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            String phone = etPhone.getText().toString().trim();
            String verCode = etVercode.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            if (v == btnComplete) {
                if (userexist == 0) {
                    String checkInfo = PatternUtil.checkUser(username, password);
                    if (!TextUtils.isEmpty(checkInfo)) {
                        showShortToast(checkInfo);
                    } else {
                        doRealThirdLogin(phone, verCode, username, password);
                    }
                } else {
                    doRealThirdLogin(phone, verCode, "", "");
                }
            } else if (v == btnGetVercode) {
                checkMobileAndGetImgCode(phone);
            }
        }
    };

    private void doRequestVercode(String phone, String code) {
        if (!PatternUtil.isPhoneNum(phone)) {
            showShortToast("请填写正确的手机号");
            return;
        }
        String url = Config.WEB_API_SERVER_V3 + "/mobilecheck";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", phone);
        builder.add("captcha", code);
        builder.add("flag", "3");
        OkHttpClientManager.getInstance(activity).postWithPlatform(url, new ResultCallback<String>() {

            @Override
            public void onResponse(String result) {
                /**
                 * userisexists Int 用户是否存在（flag=3） mobileisregist int
                 * 手机号是否已注册（flag =1） 0 未注册 1已注册/重置密码 message String 返回信息
                 */
                mTimeCount.start();
                try {
                    JSONObject json = new JSONObject(result);
                    userexist = json.getInt("userisexists");
                    if (userexist == 0) {
                        userNamePwdContainer.setVisibility(View.VISIBLE);
                    } else {
                        userNamePwdContainer.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, builder);
    }

    private void doRealThirdLogin(String phone, String verCode, String username, String password) {
        if (!PatternUtil.isPhoneNum(phone)) {
            showShortToast("请填写正确的手机号");
            return;
        }
        if (TextUtils.isEmpty(verCode)) {
            showShortToast("验证码不能为空");
        }
        String url = Config.WEB_API_SERVER + "/third/mobile/bind";
        /**
         * userisexist string 用户是否存在（验证码接口flag=3返回） mobile String 电话号码
         * verifycode String 验证码 username String 用户名（userisexist=1时不用传） qquid
         * String Qq返回uid weibouid String Weibo返回uid wxopenid String 微信返回openid
         * socialimage String 第三方社交头像 unionid String 微信unionid 存在就传
         *type openId
         */
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("mobile", phone);
        builder.add("verifyCode", verCode);
        builder.add("username", username);
        builder.add("password", password);
        if (!TextUtils.isEmpty(qquid)) {
            builder.add("type", "qq");
            builder.add("openId", qquid);
        }
        if (!TextUtils.isEmpty(weibouid)) {
            builder.add("type", "weibo");
            builder.add("openId", weibouid);
        }
        if (!TextUtils.isEmpty(wxopenid)) {
            builder.add("type", "wechat");
            builder.add("openId", wxopenid);
        }
        builder.add("unionId", TextUtils.isEmpty(unionid) ? "" : unionid);
        builder.add("accessToken", "");
        builder.add("imgUrl", head == null ? "" : head);
        OkHttpClientManager.getInstance(activity).postWithPlatform(url, new ResultCallback<UserInfo>() {

            @Override
            public void onError(Request paramRequest, Exception e) {
                LogUtil.d("ZLOVE", "bind---e---" + e.toString());
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "bind---errorMsg---" + errorMsg);
                super.onFail(errorMsg);
            }

            @Override
            public void onResponse(UserInfo result) {
                LogUtil.d("ZLOVE", "bind---result---" + result.toString());
                SysUtil.savePref(PrefKeyConst.PREF_ACCOUNT, result.getUsername());
                SysUtil.savePref(PrefKeyConst.PREF_PASSWORD, result.getPassword());
                SysUtil.saveUserInfo(result);
                JPushInterface.setAlias(PersonInfoBindActivity.this, result.getUsername(), PersonInfoBindActivity.this);
                setResult(RESULT_OK);
                finish();
            }
        }, builder);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    class GetVerifyCodeTimer extends CountDownTimer {

        public GetVerifyCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnGetVercode.setText("重新获取");
            btnGetVercode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnGetVercode.setClickable(false);
            btnGetVercode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mTimeCount != null) {
            mTimeCount.cancel();
        }
    }

    public void checkMobileAndGetImgCode(String mobile) {
        String url = CHECK_MOBILE_AND_GET_IMG_CODE_URL + mobile + "?imageType=thirdBind&bindType=" + bindType;
        OkHttpClientManager.getInstance(this).getWithPlatform(url, new OkHttpClientManager.ResultCallback<String>() {
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
                            showImgCodeDialog(imgData);
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


    public void showImgCodeDialog(String imageData) {
        if (dialog == null) {
            dialog = new VerifyCodeDialog(this, this, imageData);
        } else {
            dialog.setImageData(imageData);
        }
        dialog.showdialog();
    }

    @Override
    public void confirmAction(String code) {
        String phone = etPhone.getText().toString().trim();
        doRequestVercode(phone, code);
    }

    @Override
    public void changeCodeAction() {
        String phone = etPhone.getText().toString().trim();
        checkMobileAndGetImgCode(phone);
    }

    @Override
    public void gotResult(int i, String s, Set<String> set) {
        if (i == 0) {
            LogUtil.d("JPUSH", "设置别名成功");
        }
    }
}
