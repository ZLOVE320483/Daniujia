package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.control.ForgetPwdControl;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.PatternUtil;

/**
 * Created by ZLOVE on 2016/5/4.
 */
public class ForgetPwdFragment extends BaseFragment implements View.OnClickListener {

    private ForgetPwdControl forgetPwdControl;
    private EditText etPhone;
    private Button btnGetVerCode;
    private EditText etVerifyCode;
    private EditText etPassword;
    private EditText etConfirmPwd;
    private Button btnComplete;

    private String phone;
    private String verCode;
    private String password;
    private String confirmPwd;

    private GetVerifyCodeTimer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        forgetPwdControl = new ForgetPwdControl();
        setControl(forgetPwdControl);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_forget_pwd;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("忘记密码");

        etPhone = (EditText) view.findViewById(R.id.id_phone);
        btnGetVerCode = (Button) view.findViewById(R.id.id_get_verify_code);
        btnGetVerCode.setClickable(false);
        etVerifyCode = (EditText) view.findViewById(R.id.id_verify_code);
        etPassword = (EditText) view.findViewById(R.id.id_password);
        etConfirmPwd = (EditText) view.findViewById(R.id.id_confirm_password);
        btnComplete = (Button) view.findViewById(R.id.id_complete);

        btnGetVerCode.setOnClickListener(this);
        btnComplete.setOnClickListener(this);

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
                    btnGetVerCode.setClickable(true);
                    btnGetVerCode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_green_solid));
                } else {
                    btnGetVerCode.setClickable(false);
                    btnGetVerCode.setBackground(ApplicationUtil.getApplicationContext().getResources().getDrawable(R.drawable.shape_common_grey_solid));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btnGetVerCode) {
            phone = etPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                showShortToast("手机号不能为空");
                return;
            }
            if (!PatternUtil.isPhoneNum(phone)) {
                showShortToast("手机号不合法");
                return;
            }
            forgetPwdControl.getVerCode(phone);
            if (timer == null) {
                timer = new GetVerifyCodeTimer(60 * 1000, 1000);
            }
            timer.start();
        } else if (v == btnComplete) {
            doSubmit();
        }
    }

    private void doSubmit() {
        phone = etPhone.getText().toString().trim();
        verCode = etVerifyCode.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        confirmPwd = etConfirmPwd.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            showShortToast("手机号不能为空");
            return;
        }
        if (TextUtils.isEmpty(verCode)) {
            showShortToast("验证码不能为空");
            return;
        }
        if (password.length() < 6 || password.length() > 14) {
            showShortToast("请输入6-14位密码");
            return;
        }
        if (!password.equals(confirmPwd)) {
            showShortToast("两次密码不一致");
            return;
        }

        forgetPwdControl.resetPassword(phone, verCode, password, confirmPwd);
    }

    class GetVerifyCodeTimer extends CountDownTimer {

        public GetVerifyCodeTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onFinish() {//计时完毕时触发
            btnGetVerCode.setText("重新获取");
            btnGetVerCode.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {//计时过程显示
            btnGetVerCode.setClickable(false);
            btnGetVerCode.setText(millisUntilFinished / 1000 + "秒");
        }
    }
}
