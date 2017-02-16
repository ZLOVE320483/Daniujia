package com.xiaojia.daniujia.ui.frag;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.BaseRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.ActExpertDirection;
import com.xiaojia.daniujia.ui.act.ActExpertEducationBackground;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertServiceSetting;
import com.xiaojia.daniujia.ui.act.ActExpertWorkExperience;
import com.xiaojia.daniujia.ui.act.ActProfileEdited;
import com.xiaojia.daniujia.ui.act.ActUserBasic;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/6/27.
 */
public class ExpertModifyInfoFragment extends BaseFragment implements View.OnClickListener {
    private static final int SERVICE_SETTING = 0x01;
    private static final int PERSON_INFO = 0x02;
    private View expertHomeView;
    private ImageView ivDivider1;
    private View basicInfoView;
    private View expertDirectionView;
    private View personIntroduceView;
    private View workExperienceView;
    private View educationBackgroundView;
    private View serviceSettingView;

    private ToggleButton tbSwitch;
    private String operation = "close";
    private TextView mTvVertify;
    private TextView mTvServiceChange;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_modify_info;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("修改资料");

        expertHomeView = view.findViewById(R.id.expert_home_container);
        ivDivider1 = (ImageView) view.findViewById(R.id.id_divider_1);
        basicInfoView = view.findViewById(R.id.basic_info_container);
        expertDirectionView = view.findViewById(R.id.expert_direction_container);
        personIntroduceView = view.findViewById(R.id.person_introduce_container);
        workExperienceView = view.findViewById(R.id.work_experience_container);
        educationBackgroundView = view.findViewById(R.id.education_background_container);
        serviceSettingView = view.findViewById(R.id.service_setting_container);
        tbSwitch = (ToggleButton) view.findViewById(R.id.toggle_switch);
        mTvVertify = (TextView) view.findViewById(R.id.frag_expert_home_tv_verify);
        mTvServiceChange = (TextView) view.findViewById(R.id.frag_expert_home_tv_service_verify);

        expertHomeView.setOnClickListener(this);
        basicInfoView.setOnClickListener(this);
        expertDirectionView.setOnClickListener(this);
        personIntroduceView.setOnClickListener(this);
        workExperienceView.setOnClickListener(this);
        educationBackgroundView.setOnClickListener(this);
        serviceSettingView.setOnClickListener(this);

        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 2) {
            expertHomeView.setVisibility(View.GONE);
            ivDivider1.setVisibility(View.GONE);
        }

        tbSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    operation = "close";
                } else {
                    operation = "open";
                }
                requestAnonymousExpert();
            }
        });

        requestIsAnonymous();
    }

    @Override
    public void onClick(View v) {
        if (v == expertHomeView) {
            Intent intent = new Intent(getActivity(), ActExpertHome.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID));
//            if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1) {
//                intent.putExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW, true);
//            }
            startActivity(intent);
        } else if (v == basicInfoView) {
            Intent intent = new Intent(getActivity(), ActUserBasic.class);
            if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1) {
                intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, true);
            }
            startActivity(intent);
        } else if (v == expertDirectionView) {
            Intent intent = new Intent(getActivity(), ActExpertDirection.class);
            startActivity(intent);
        } else if (v == personIntroduceView) {
            Intent intent = new Intent(getActivity(), ActProfileEdited.class);
            startActivityForResult(intent, PERSON_INFO);
        } else if (v == workExperienceView) {
            Intent intent = new Intent(getActivity(), ActExpertWorkExperience.class);
            startActivity(intent);
        } else if (v == educationBackgroundView) {
            Intent intent = new Intent(getActivity(), ActExpertEducationBackground.class);
            startActivity(intent);
        } else if (v == serviceSettingView) {
            Intent intent = new Intent(getActivity(), ActExpertServiceSetting.class);
            startActivityForResult(intent, SERVICE_SETTING);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if ((requestCode == PERSON_INFO || requestCode == SERVICE_SETTING) && resultCode == Activity.RESULT_OK) {
            requestIsAnonymous();
        }
    }

    private void requestIsAnonymous() {
        String url = Config.WEB_API_SERVER_V3 + "/become/anonymous";
        OkHttpClientManager.getInstance(getActivity()).getWithToken(url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onResponse(BaseRespVo result) {
                if (result == null) {
                    return;
                }
                if (result.getReturncode().equals("SUCCESS")) {
                    if (result.getAnonymous() == 0) {
                        tbSwitch.setChecked(true);
                    } else {
                        tbSwitch.setChecked(false);
                    }
                    if (result.getProfile_status() == 0) {
                        mTvVertify.setVisibility(View.VISIBLE);
                    } else {
                        mTvVertify.setVisibility(View.GONE);
                    }

                    if (result.getServiceProductStatus() == 0){
                        mTvServiceChange.setVisibility(View.VISIBLE);
                    } else {
                        mTvServiceChange.setVisibility(View.GONE);
                    }

                } else {
                    showShortToast(result.getReturnmsg());
                }
            }
        });
    }

    private void requestAnonymousExpert() {
        String url = Config.WEB_API_SERVER_V3 + "/become/anonymous/consultant";
        FormEncodingBuilder builder = new FormEncodingBuilder();
        builder.add("operation", operation);

        OkHttpClientManager.getInstance(getActivity()).postWithToken(true, url, new OkHttpClientManager.ResultCallback<BaseRespVo>() {
            @Override
            public void onError(Request paramRequest, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(BaseRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    if (operation.equals("close")) {
                        tbSwitch.setChecked(true);
                    } else {
                        tbSwitch.setChecked(false);
                    }
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onFail(String errorMsg) {
                LogUtil.d("ZLOVE", "requestAnonymousExpert---onFail---" + errorMsg);
            }
        }, builder);
    }
}
