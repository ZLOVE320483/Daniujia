package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.squareup.okhttp.Request;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.Const;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.domain.resp.UpdateVersionRespVo;
import com.xiaojia.daniujia.managers.OkHttpClientManager;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.WUtil;

import org.json.JSONException;
import org.json.JSONObject;

@ContentView(R.layout.activity_about_dnj)
public class AboutDnjActivity extends AbsBaseActivity implements DialogManager.UpdateVersionListener {
    @ViewInject(R.id.id_about_desc)
    private TextView tvAboutDesc;
    @ViewInject(R.id.act_about_tv_version)
    private TextView tvVersion;
    @ViewInject(R.id.cur_version)
    private View curVersionView;

    private String updateUrl;
    private int force;
    private static final int REQ_UPDATE_VERSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        initTitle();
        initView();
    }

    private void initView() {
//        requestDesc();
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_HAS_NEW_VERSION)) {
                boolean hasNewVersion = intent.getBooleanExtra(IntentKey.INTENT_KEY_HAS_NEW_VERSION, false);
                if (hasNewVersion) {
                    curVersionView.setVisibility(View.VISIBLE);
                } else {
                    curVersionView.setVisibility(View.GONE);
                }
            }
        }

        tvVersion.setText("V" + ApplicationUtil.getVersionName());
    }

    private void requestDesc() {
        String url = Config.WEB_API_SERVER_V3 + "/app/tip/daniujia_desc";
        OkHttpClientManager.getInstance(this).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onResponse(String result) {
                result = result.replace("\\n", "n");
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String desc = jsonObject.getString("tipContent");
                    tvAboutDesc.setText(desc);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initTitle() {
        TextView titleTv = (TextView) findViewById(R.id.id_title);
        titleTv.setText(R.string.about_dnj);
    }

    @OnClick({R.id.id_back, R.id.comment, R.id.welcome, R.id.policy, R.id.cur_version})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
            case R.id.comment:
                WUtil.launchAppCommentPage();
                break;
            case R.id.welcome:
                startActivity(new Intent(this, IntroduceActivity.class));
                break;
            case R.id.policy:
                Intent policyI = new Intent(this, WebActivity.class);
                policyI.putExtra(ExtraConst.EXTRA_WEB_TITLE, getString(R.string.dnj_disclaimer));
                policyI.putExtra(ExtraConst.EXTRA_WEB_URL, Const.URL_DNJ_POLICY);
                startActivity(policyI);
                break;
            case R.id.cur_version:
                doRequestVersionInfo();
                break;
        }
    }

    private void doRequestVersionInfo() {
        final String curVersion = SysUtil.getVerName();
        String url = Config.WEB_API_SERVER + "/android/latestVersion/" + curVersion;

        OkHttpClientManager.getInstance(this).getWithPlatform(true, url, new OkHttpClientManager.ResultCallback<UpdateVersionRespVo>() {
            @Override
            public void onResponse(UpdateVersionRespVo result) {
                if (result == null) {
                    return;
                }
                if (result.getReturncode().equals("SUCCESS")) {
                    if (result.getUpdateFlag() == 0) {
                        showShortToast("当前已是最新版本");
                        return;
                    }
                    if (!result.getLatestVersion().equals(curVersion)) {
                        updateUrl = result.getDownloadUrl();
                        force = result.getForce();
                        DialogManager.showUpdateVersionDialog(AboutDnjActivity.this, result.getForce(), result.getLatestVersion(), result.getContent(), AboutDnjActivity.this);
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

    @Override
    public void updateAction() {
        Intent browserIntent = new Intent();
        browserIntent.setAction("android.intent.action.VIEW");
        Uri content_url = Uri.parse(updateUrl);
        browserIntent.setData(content_url);
        // 用户点击下载链接跳转到浏览器页面之后返回了
        startActivityForResult(browserIntent, REQ_UPDATE_VERSION);
    }

    @Override
    protected void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        if (reqCode == REQ_UPDATE_VERSION) {
            if (force == 1) {
                sendBroadcast(new Intent(ACTION_EXIT));
            }
        }
    }
}
