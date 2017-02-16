package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActCommitSuccess;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.PatternUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/10/26.
 */
public class AskQuestionFragment extends BaseFragment implements View.OnClickListener {
    private EditText mEtContent;
    private EditText mEtPhone;
    private Button mBtSubmit;

    @Override
    protected int getInflateLayout() {
        return R.layout.act_ask_question;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.iv_title_back));
        ((TextView)view.findViewById(R.id.tv_title)).setText("提问");
        mEtContent = (EditText) view.findViewById(R.id.id_user_request);
        mEtPhone = (EditText) view.findViewById(R.id.id_user_phone);
        mBtSubmit = (Button) view.findViewById(R.id.submit);

        mBtSubmit.setOnClickListener(this);


        if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)){
            mEtPhone.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit){
            if (TextUtils.equals(mEtContent.getText().toString().trim(),"")){
                showShortToast("描述问题不能为空");
                return;
            }

            if (mEtPhone != null && mEtPhone.getVisibility() == View.VISIBLE) {
                if (TextUtils.equals(mEtPhone.getText().toString().trim(),"")){
                    showShortToast("手机号不能为空");
                    return;
                }

                if (!PatternUtil.isPhoneNum(mEtPhone.getText().toString().trim())) {
                    showShortToast("手机号不合法");
                    return;
                }
            }

            submitSearchRequest(mEtPhone.getText().toString().trim(),mEtContent.getText().toString().trim());

        }
    }

    public void submitSearchRequest(String mobile, String content) {
        String url = Config.WEB_API_SERVER + "/feedback";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("contact", mobile);
        builder.add("content", content);
        builder.add("type", "question");
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            OkHttpClientManager.getInstance(getActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest,e);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(BaseRespVo baseRespVo) {
                    if (baseRespVo.getReturncode().equals("SUCCESS")) {
                        commitSuccess();
                    }
                }

                @Override
                public void onFail(String errorMsg) {
                    LogUtil.d("ZLOVE", "submitSearchRequest---onFail---" + errorMsg);
                }
            }, builder);
        } else {
            OkHttpClientManager.getInstance(getActivity()).postWithPlatform(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
                @Override
                public void onError(Request paramRequest, Exception e) {
                    super.onError(paramRequest,e);
                    e.printStackTrace();
                }

                @Override
                public void onResponse(BaseRespVo baseRespVo) {
                    if (baseRespVo.getReturncode().equals("SUCCESS")) {
                        commitSuccess();
                    }
                }

                @Override
                public void onFail(String errorMsg) {
                    showShortToast("提交失败");
                }
            }, builder);
        }

    }

    private void commitSuccess(){
        Intent intent = new Intent(getActivity(), ActCommitSuccess.class);
        startActivity(intent);
        finishActivity();
    }
}
