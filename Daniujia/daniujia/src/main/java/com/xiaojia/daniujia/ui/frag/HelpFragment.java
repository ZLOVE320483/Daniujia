package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.Const;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.UpdateVersionRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.ui.act.AboutDnjActivity;
import com.xiaojia.daniujia.ui.act.ActContactUs;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.utils.SysUtil;

/**
 * Created by ZLOVE on 2016/4/28.
 */
public class HelpFragment extends BaseFragment implements View.OnClickListener {

    private View aboutView;
    private ImageView ivNewVersion;
    private View helpView;
    private View contactView;

    private boolean hasNewVersion = false;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_help;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        ((TextView) view.findViewById(R.id.id_title)).setText("帮助");

        aboutView = view.findViewById(R.id.id_about);
        ivNewVersion = (ImageView) view.findViewById(R.id.newversion);
        helpView = view.findViewById(R.id.id_help);
        contactView = view.findViewById(R.id.id_contact);

        aboutView.setOnClickListener(this);
        helpView.setOnClickListener(this);
        contactView.setOnClickListener(this);

        doRequestVersionInfo();
    }

    @Override
    public void onClick(View v) {
        if (v == aboutView) {
            Intent intent = new Intent(getActivity(), AboutDnjActivity.class);
            intent.putExtra(IntentKey.INTENT_KEY_HAS_NEW_VERSION, hasNewVersion);
            startActivity(intent);
        } else if (v == helpView) {
            Intent feedbackI = new Intent(getActivity(), WebActivity.class);
            feedbackI.putExtra(ExtraConst.EXTRA_WEB_TITLE, getString(R.string.help_feedback));
            feedbackI.putExtra(ExtraConst.EXTRA_WEB_URL, Const.URL_HELP_AND_FEEDBACK);
            startActivity(feedbackI);
        } else if (v == contactView) {
            Intent policyI = new Intent(getActivity(), ActContactUs.class);
            startActivity(policyI);
        }
    }

    private void doRequestVersionInfo() {
        final String curVersion = SysUtil.getVerName();
        String url = Config.WEB_API_SERVER + "/android/latestVersion/" + curVersion;

        OkHttpClientManager.getInstance(getActivity()).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<UpdateVersionRespVo>() {
            @Override
            public void onResponse(UpdateVersionRespVo result) {
                if (result == null) {
                    return;
                }
                if (result.getReturncode().equals("SUCCESS")) {
                    if (result.getUpdateFlag() == 0) {
                        ivNewVersion.setVisibility(View.GONE);
                        hasNewVersion = false;
                        return;
                    }
                    if (!result.getLatestVersion().equals(curVersion)) {
                        ivNewVersion.setVisibility(View.VISIBLE);
                        hasNewVersion = true;
                    }
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
}
