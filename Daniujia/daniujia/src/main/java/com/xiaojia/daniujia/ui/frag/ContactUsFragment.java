package com.xiaojia.daniujia.ui.frag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.control.ContactUsControl;
import com.xiaojia.daniujia.ui.views.StillScrollEditText;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ContactUsFragment extends BaseFragment implements View.OnClickListener {
    private LinearLayout mLlPhone;
    private Button mBtCommit;
    private StillScrollEditText mEtSuggestion;
    private EditText mEtPhone;
    private ContactUsControl control;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ContactUsControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_contact_us;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("联系我们");
        mBtCommit = (Button) view.findViewById(R.id.commit);
        mLlPhone = (LinearLayout) view.findViewById(R.id.contact_ll_phone);
        mEtSuggestion = (StillScrollEditText) view.findViewById(R.id.contact_suggestion);
        mEtPhone = (EditText) view.findViewById(R.id.contact_et_phone);

        mBtCommit.setOnClickListener(this);

        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            mLlPhone.setVisibility(View.GONE);
            view.findViewById(R.id.id_line_top_three).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mBtCommit) {
            doCommit();
        }
    }

    private void doCommit() {
        if (TextUtils.isEmpty(mEtSuggestion.getText().toString().trim())) {
            showShortToastForce("请填写您的意见和建议");
            return;
        }

        if (mEtSuggestion.getText().toString().trim().length() < 5) {
            showShortToastForce("您填写内容不能少于5个字数");
            return;
        }

        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            control.submitSuggestionForLogin(mEtSuggestion.getText().toString().trim());

        } else {
            if (TextUtils.isEmpty(mEtPhone.getText().toString().trim())) {
                showShortToastForce("请填写你的联系方式");
                return;
            }

            if (!PatternUtil.isPhoneNum(mEtPhone.getText().toString().trim())) {
                showShortToastForce("联系方式格式不正确");
                return;
            }

            control.submitSuggestionNotLogin(mEtSuggestion.getText().toString().trim()
                    , mEtPhone.getText().toString().trim());
        }

    }

    public void commitSuccess() {
        showShortToastForce("提交成功");
        finishActivity();
    }
}
