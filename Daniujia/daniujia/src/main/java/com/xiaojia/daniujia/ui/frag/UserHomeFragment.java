package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.UserHomeRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;

/**
 * Created by ZLOVE on 2016/5/31.
 */
public class UserHomeFragment extends BaseFragment {

    private RoundedImageView ivAvatar;
    private TextView tvName;
    private TextView tvCorpName;
    private TextView tvCorpPosition;
    private TextView tvCareerYear;

    private View personInfoContainer;
    private TextView tvGender;
    private TextView tvCity;

    private int userId = 0;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_user_home;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHAT_USER_ID)) {
                userId = intent.getIntExtra(IntentKey.INTENT_KEY_CHAT_USER_ID, 0);
            }
        }

        setBackButton(view.findViewById(R.id.id_back));
        ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
        tvName = (TextView) view.findViewById(R.id.id_name);
        tvCorpName = (TextView) view.findViewById(R.id.id_corp);
        tvCorpPosition = (TextView) view.findViewById(R.id.id_position);
        tvCareerYear = (TextView) view.findViewById(R.id.id_career_year);

        personInfoContainer = view.findViewById(R.id.person_info_container);
        tvGender = (TextView) view.findViewById(R.id.gender);
        tvCity = (TextView) view.findViewById(R.id.city);

        requestExpertInfo();
    }

    public void requestExpertInfo() {
        String url = Config.WEB_API_SERVER + "/common/user/detail/" + userId;
        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<UserHomeRespVo>() {
            @Override
            public void onResponse(UserHomeRespVo result) {
                if (result.getReturncode().equals("SUCCESS")) {
                    setExpertData(result);
                } else {
                    showShortToast(result.getReturnmsg());
                }
            }

            @Override
            public void onError(Request paramRequest, Exception e) {
                super.onError(paramRequest, e);
            }

            @Override
            public void onFail(String errorMsg) {
                super.onFail(errorMsg);
            }
        });
    }

    private void setExpertData(UserHomeRespVo data) {
        if (data == null) {
            return;
        }
        UserHomeRespVo.BasicInfo basicInfo = data.getBasic();
        if (!TextUtils.isEmpty(basicInfo.getImgurl())) {
            Picasso.with(getActivity()).load(basicInfo.getImgurl()).resize(ScreenUtils.dip2px(70), ScreenUtils.dip2px(70)).into(ivAvatar);
        } else {
            Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(70), ScreenUtils.dip2px(70)).into(ivAvatar);
        }
        tvName.setText(basicInfo.getName());
        tvCorpName.setText(basicInfo.getCompany());
        tvCorpPosition.setText(basicInfo.getPosition());
        tvCareerYear.setText(String.valueOf(basicInfo.getWorkage()) + "年工作经验");
        int gender = basicInfo.getGender();

        if (gender == 0) {
            tvGender.setText("未选");
            tvGender.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        } else if (gender == 1) {
            tvGender.setText("男");
        } else if (gender == 2) {
            tvGender.setText("女");
        }

        if (TextUtils.isEmpty(basicInfo.getCityName())) {
            tvCity.setText("未选");
            tvCity.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        } else {
            tvCity.setText(basicInfo.getCityName());
        }
    }

}
