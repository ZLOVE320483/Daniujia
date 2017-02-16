package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import com.xiaojia.daniujia.ui.views.orderprogress.RoundProgressBarWidthNumber;
import com.xiaojia.daniujia.utils.LogUtil;

/**
 * Created by ZLOVE on 2016/5/31.
 */
public class UserOrderCommentFragment extends BaseFragment implements View.OnClickListener, View.OnFocusChangeListener {
    private String expertName;
    private int serviceType;
    private String service;
    private int orderId;

    private TextView tvTitle;
    private EditText etComment;
    private View cancelView;
    private View confirmView;
    private TextView tvPlus;
    private TextView tvMinus;
    private RoundProgressBarWidthNumber rpScoreOut;
    private RoundProgressBarWidthNumber rpScoreInner;
    private TextView tvRightSubmit;

    private int score = 10;
    private String commentContent;

    private boolean isAnimationEnd = false;
    private boolean isSubmit = false;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_user_order_comment;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                orderId = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_NAME)) {
                expertName = intent.getStringExtra(IntentKey.INTENT_KEY_EXPERT_NAME);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE)) {
                serviceType = intent.getIntExtra(IntentKey.INTENT_KEY_ORDER_SERVICE_TYPE, 1);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        tvTitle = (TextView) view.findViewById(R.id.id_title);
        if (serviceType == 1) {
            service = "图文约谈";
        } else if (serviceType == 2) {
            service = "电话约谈";
        } else if (serviceType == 3) {
            service = "线下约谈";
        }
        tvTitle.setText(expertName + "的" + service + "评价");

        rpScoreOut = (RoundProgressBarWidthNumber) view.findViewById(R.id.order_progress_out);
        rpScoreInner = (RoundProgressBarWidthNumber) view.findViewById(R.id.order_progress_inner);
        rpScoreOut.setRadiusWidth(10);
        rpScoreInner.setShowTest(false);
        rpScoreInner.setRadiusWidth(14);
        tvPlus = (TextView) view.findViewById(R.id.user_order_tv_plus);
        tvPlus.setOnClickListener(this);
        tvPlus.setClickable(false);
        tvMinus = (TextView) view.findViewById(R.id.user_order_tv_minus);
        tvMinus.setOnClickListener(this);
        etComment = (EditText) view.findViewById(R.id.id_user_comment);
        etComment.setOnFocusChangeListener(this);
        cancelView = view.findViewById(R.id.id_cancel);
        setBackButton(cancelView);
        confirmView = view.findViewById(R.id.id_save);
        confirmView.setOnClickListener(this);

        tvRightSubmit = (TextView) view.findViewById(R.id.id_right);
        tvRightSubmit.setOnClickListener(this);
        tvRightSubmit.setVisibility(View.VISIBLE);
        tvRightSubmit.setText("提交");
    }

    @Override
    public void onClick(View v) {
        if (v == confirmView) {
            commentContent = etComment.getText().toString().trim();
            if (TextUtils.isEmpty(commentContent)) {
                showShortToast("请输入评价内容");
                return;
            }
            if (commentContent.length() < 10) {
                showShortToast("评价内容不少于10字");
                return;
            }
            commentRequest();
        } else if (v == tvMinus) {
            if (isAnimationEnd){
                onMinusClick();
            }
        } else if (v == tvPlus) {
            if (isAnimationEnd){
                onAddClick();
            }

        } else if(v == tvRightSubmit){
            commentContent = etComment.getText().toString().trim();
            if (TextUtils.isEmpty(commentContent)) {
                showShortToast("请输入评价内容");
                return;
            }
            if (commentContent.length() < 10) {
                showShortToast("评价内容不少于10字");
                return;
            }
            isSubmit = true;
            commentRequest();
        }
    }

    private int firstProgress = 0;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LogUtil.d("zptest","handlerOut: " + msg.what);
            if (msg.what == 0){
                LogUtil.d("zptest","firstProgress: " + firstProgress);
                rpScoreOut.setProgress(firstProgress);
                rpScoreInner.setProgress(firstProgress);
                firstProgress += 2;
                if (firstProgress > 100){
                    handler.sendEmptyMessageDelayed(1,20);
                } else {
                    handler.sendEmptyMessageDelayed(0,20);
                }

            } else if (msg.what == 1){
                isAnimationEnd = true;
                rpScoreOut.setMax(10);
                rpScoreInner.setMax(10);

                rpScoreOut.setProgress(10);
                rpScoreInner.setProgress(10);
            }
        }
    };

    public void updateUi(){
        if (score == 10 && !isSubmit) {
            firstProgress = 0;
            rpScoreOut.setMax(100);
            rpScoreInner.setMax(100);
            handler.sendEmptyMessageDelayed(0,300);
        }

    }

    private void onMinusClick() {
        tvPlus.setBackgroundResource(R.drawable.score_plus);
        tvPlus.setClickable(true);
        score--;
        if (score == 1) {
            tvMinus.setBackgroundResource(R.drawable.score_minus_disable);
            tvMinus.setClickable(false);
        }
        rpScoreOut.setProgress(score);
        rpScoreInner.setProgress(score);
//      tvChangeScore.setText(String.valueOf(score));
    }

    private void onAddClick() {
        tvMinus.setBackgroundResource(R.drawable.score_minus);
        tvMinus.setClickable(true);
        score++;
        if (score == 10) {
            tvPlus.setBackgroundResource(R.drawable.score_plus_disable);
            tvPlus.setClickable(false);
        }
        rpScoreOut.setProgress(score);
        rpScoreInner.setProgress(score);
//        tvChangeScore.setText(String.valueOf(score));
//        tvScore.setText(String.valueOf(score));
    }

    private void commentRequest() {
        String url = Config.WEB_API_SERVER + "/user/order/comment";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("orderId", String.valueOf(orderId));
        builder.add("score", String.valueOf(score));
        builder.add("commentContent", commentContent);
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
                    isSubmit = false;
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "commentRequest---onFail---" + errorMsg);
            }
        }, builder);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            etComment.setHint("");
        }else{
            etComment.setHint("留下本次服务评价(不少于10个字)");
        }
    }

}
