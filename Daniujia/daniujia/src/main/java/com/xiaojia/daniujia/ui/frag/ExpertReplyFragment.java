package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/5/24.
 */
public class ExpertReplyFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvUserComment;
    private EditText etReply;
    private TextView tvCancel;
    private TextView tvSave;

    private int orderId;
    private String userComment;
    private String replyContent;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_reply;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_USER_COMMENT)) {
                userComment = intent.getStringExtra(IntentKey.INTENT_KEY_USER_COMMENT);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("专家回复");
        tvUserComment = (TextView) view.findViewById(R.id.user_comment);
        etReply = (EditText) view.findViewById(R.id.id_expert_reply);
        tvCancel = (TextView) view.findViewById(R.id.id_cancel);
        tvSave = (TextView) view.findViewById(R.id.id_save);

        tvUserComment.setText(userComment);
        tvCancel.setOnClickListener(this);
        tvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == tvCancel) {
            finishActivity();
        } else if (v == tvSave) {
            replyContent = etReply.getText().toString().trim();
            if (TextUtils.isEmpty(replyContent)) {
                showShortToast("请输入您的回复");
                return;
            }
            expertReplyRequest();
        }
    }

    private void expertReplyRequest() {
        String url = Config.WEB_API_SERVER + "/user/order/reply";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        builder.add("replyContent", replyContent);
        OkHttpClientManager.getInstance(getActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    Intent intent = new Intent();
                    finishActivity(intent);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "expertReplyRequest---onFail---" + errorMsg);
            }
        }, builder);
    }
}
