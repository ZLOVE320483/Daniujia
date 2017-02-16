package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.FormEncodingBuilder;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.AlertBigMsgDlg;
import com.xiaojia.daniujia.domain.resp.UserProfileRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActApplyExpertComplete;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/28.
 */
public class StepThirdToBeExpertNew extends BaseFragment implements View.OnClickListener {

    private EditText content;

    private String example;
    private AlertBigMsgDlg alertMsgDlg;
    private String fromWhere = "";

    @Override
    protected int getInflateLayout() {
        return R.layout.fg_step3_to_be_expert_new;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(IntentKey.FROM_WHERE)) {
            fromWhere = intent.getStringExtra(IntentKey.FROM_WHERE);
        }
        setBackButton(view.findViewById(R.id.id_back));
        view.findViewById(R.id.title_right).setOnClickListener(this);
        TextView title = (TextView) view.findViewById(R.id.id_title);
        title.setText("个人简介");

        ImageView step3 = (ImageView) view.findViewById(R.id.step3);

        view.findViewById(R.id.example).setOnClickListener(this);
        step3.setImageResource(R.drawable.personal_profile_white);
        content = (EditText) view.findViewById(R.id.content);

        getPersonProfileRequest();
        getExampleRequest();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_right:
               saveDate();
                break;
            case R.id.example:
                if (alertMsgDlg == null) {
                    alertMsgDlg = new AlertBigMsgDlg(getActivity());
                }
                alertMsgDlg.initDlg(R.drawable.ic_question_blue, "个人简介", example);
                alertMsgDlg.show();
                break;
        }
    }

    private void saveDate() {
        if (TextUtils.isEmpty(content.getText().toString().trim())) {
            showShortToast("请编辑个人简介");
            return;
        }
        notifyServer();
        String url = Config.WEB_API_SERVER_V3 + "/user/profile/update";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("userId", String.valueOf(SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)));
        builder.add("profile", content.getText().toString().trim());
        OkHttpClientManager.getInstance(getActivity()).postWithToken(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                Intent intent = new Intent(new Intent(getActivity(), ActApplyExpertComplete.class));
                intent.putExtra(IntentKey.FROM_WHERE, fromWhere);
                startActivity(intent);
            }
        }, builder);
    }

    private void notifyServer() {
        String url = Config.WEB_API_SERVER + "/user/notify/server";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("flag", "1");
        OkHttpClientManager.getInstance(getActivity()).postWithToken(false, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
            }
        },builder);
    }

    private void getPersonProfileRequest() {
        String url = Config.WEB_API_SERVER_V3 + "/become/profile";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(true, url, new OkHttpClientManager.ResultCallback<UserProfileRespVo>() {
            @Override
            public void onResponse(UserProfileRespVo result) {
                if (result == null) {
                    return;
                }
                if (!TextUtils.isEmpty(result.getProfile())) {
                    content.setText(result.getProfile());
                }
            }
        });
    }

    private void getExampleRequest() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/become_user_profile";
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(false, url, new OkHttpClientManager.ResultCallback<String>() {
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
