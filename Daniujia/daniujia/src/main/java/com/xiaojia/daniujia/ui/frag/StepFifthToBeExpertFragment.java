package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by Administrator on 2016/5/18.
 */
public class StepFifthToBeExpertFragment extends BaseFragment implements View.OnClickListener {

    private TextView tvTitle;
    private TextView tvFifth;
    private TextView tvTip;
    private TextView tvTipDesc;
    private boolean isModify = false;

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step5_to_be_expert;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY)) {
            isModify = intent.getBooleanExtra(IntentKey.INTENT_KEY_TO_BE_EXPERT_IS_MODIFY, false);
        }

        setBackButton(view.findViewById(R.id.id_back));
         tvTitle = (TextView) view.findViewById(R.id.id_title);

        tvFifth = (TextView) view.findViewById(R.id.complete_tv);
        tvTip = (TextView) view.findViewById(R.id.tip);
        tvTipDesc = (TextView) view.findViewById(R.id.tip_desc);

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2) {
            tvTitle.setText("修改完成");
            tvTip.setText("修改完成");
            tvFifth.setText("修改完成");
            tvTipDesc.setText("您的资料已修改成功,涉及到服务产品价格修改的部分,\n我们将人工审核,并告知您审核结果");
        } else {
            if (isModify) {
                tvTitle.setText("修改完成");
                tvTip.setText("修改完成");
                tvFifth.setText("修改完成");
                tvTipDesc.setText("您的资料已修改成功,我们的审核正在进行中,\n请耐心等待");
            } else {
                tvTitle.setText("申请完成");
            }
        }

        Button btn = (Button) view.findViewById(R.id.confirm);
        btn.setOnClickListener(this);
        notifyServer();
    }

    private void notifyServer() {
        String url = Config.WEB_API_SERVER + "/user/notify/server";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("flag", "1");
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
            }
        },builder);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.confirm) {
            startActivity(new Intent(getActivity(), ActMain.class));
            finishActivity();
        }
    }
}
