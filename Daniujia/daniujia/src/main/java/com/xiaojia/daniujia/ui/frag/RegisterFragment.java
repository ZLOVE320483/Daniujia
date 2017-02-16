package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiaojia.daniujia.Const;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.ui.control.RegisterControl;
import com.xiaojia.daniujia.ui.views.VerifyCodeDialog;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.PatternUtil;

/**
 * Created by ZLOVE on 2016/4/29.
 */
public class RegisterFragment extends BaseFragment implements View.OnClickListener, VerifyCodeDialog.VerifyCodeListener {

    private RegisterControl registerControl;

    private EditText etPhone;
    private Button btnGetVerifyCode;
    private EditText etVerifyCode;
    private EditText etUserName;
    private EditText etPassword;
    private ImageView ivShowHidePwd;
    private Button btnRegister;

    private VerifyCodeDialog dialog;

    private boolean showPwd = false;
    private String phone;
    private String verifyCode;
    private String userName;
    private String password;
    private View protocolView;

    private GetVerifyCodeTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerControl = new RegisterControl();
        setControl(registerControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_register;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("注册");
        etPhone = (EditText) view.findViewById(R.id.id_phone);
        btnGetVerifyCode = (Button) view.findViewById(R.id.id_get_verify_code);
        etVerifyCode = (EditText) view.findViewById(R.id.id_verify_code);
        etUserName = (EditText) view.findViewById(R.id.id_user_name);
        etPassword = (EditText) view.findViewById(R.id.id_password);
        btnRegister = (Button) view.findViewById(R.id.id_register);
        protocolView = view.findViewById(R.id.protocol_container);
        ivShowHidePwd = (ImageView) view.findViewById(R.id.show_pwd);

        btnGetVerifyCode.setOnClickListener(this);
        btnGetVerifyCode.setClickable(false);
        btnRegister.setOnClickListener(this);
        protocolView.setOnClickListener(this);
        ivShowHidePwd.setOnClickListener(this);

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
                    btnGetVerifyCode.setClickable(true);
                    btnGetVerifyCode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_green_solid));
                } else {
                    btnGetVerifyCode.setClickable(false);
                    btnGetVerifyCode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_grey_solid));
                }
            }
        });

        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    String text = etUserName.getText().toString();
                    if (TextUtils.isEmpty(text)){
                        showShortToast("用户名不能为空");
                    } else if (PatternUtil.containsChinese(text)) {
                        showShortToast("用户名不能包含中文");
                    } else if(text.length() < 4 || text.length() > 14){
                        showShortToast("用户名为4-14位字符，可使用字母、数字等");
                    }
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnGetVerifyCode) {
            phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                showShortToast("手机号不能为空");
                return;
            }
            if (!PatternUtil.isPhoneNum(phone)) {
                showShortToast("手机号不合法");
                return;
            }
            registerControl.checkMobileAndGetImgCode(phone);
        } else if (v == btnRegister) {
            doRegister();
        } else if (v == protocolView) {
            Intent policyI = new Intent(getActivity(), WebActivity.class);
            policyI.putExtra(ExtraConst.EXTRA_WEB_TITLE, getString(R.string.dnj_disclaimer));
            policyI.putExtra(ExtraConst.EXTRA_WEB_URL, Const.URL_DNJ_POLICY);
            startActivity(policyI);
        } else if (v == ivShowHidePwd) {
            if (showPwd) {
                showPwd = false;
                ivShowHidePwd.setImageResource(R.drawable.ic_hide_pwd);
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            } else {
                showPwd = true;
                ivShowHidePwd.setImageResource(R.drawable.ic_show_pwd);
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            }
        }
    }

    private void doRegister() {
        phone = etPhone.getText().toString().trim();
        verifyCode = etVerifyCode.getText().toString().trim();
        userName = etUserName.getText().toString().trim();
        password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            showShortToast("输入手机号");
            return;
        }

        if (TextUtils.isEmpty(verifyCode)) {
            showShortToast("验证码不能为空");
            return;
        }

        if (TextUtils.isEmpty(userName)) {
            showShortToast("用户名不能为空");
            return;
        }

        if (userName.length() < 4 || userName.length() > 14){
            showShortToast("用户名长度在4-14位之间");
            return;
        }

        if (PatternUtil.containsChinese(userName)) {
            showShortToast("用户名不能包含中文");
            return;
        }

        if(!PatternUtil.isRegisterString(userName)){
            showShortToast("用户名只能包含字母、数字、下划线");
            return;
        }

        if (userName.startsWith("_") || userName.endsWith("_")){
            showShortToast("用户名开头或结尾不能有下划线");
            return;
        }

        if (password.length() < 6 || password.length() > 14) {
            showShortToast("请输入6-14位密码");
            return;
        }
        registerControl.doRegister(userName, password, phone, verifyCode);
    }

    public void showImgCodeDialog(String imageData) {
        if (dialog == null) {
            dialog = new VerifyCodeDialog(getActivity(), this, imageData);
        } else {
            dialog.setImageData(imageData);
        }
        dialog.showdialog();
    }

    class GetVerifyCodeTimer extends CountDownTimer {

        public GetVerifyCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnGetVerifyCode.setText("重新获取");
            btnGetVerifyCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnGetVerifyCode.setClickable(false);
            btnGetVerifyCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }

    public void registerSuccess() {
        showShortToast("注册成功,登录中...");
        Intent intent = new Intent();
        finishActivity(intent);
    }

    @Override
    public void confirmAction(String code) {
        if (timer == null) {
            timer = new GetVerifyCodeTimer(60 * 1000, 1000);
        }
        timer.start();
        registerControl.getVerifyCode(phone, code);
    }

    @Override
    public void changeCodeAction() {
        registerControl.checkMobileAndGetImgCode(phone);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}
