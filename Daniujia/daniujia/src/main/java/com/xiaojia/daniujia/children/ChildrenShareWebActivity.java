package com.xiaojia.daniujia.children;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.dlgs.ShareDialog;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.ShareInfoResp;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.utils.DaniujiaJSCallBack;
import com.xiaojia.daniujia.utils.JsonUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.ShareSDKUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by ZLOVE on 2016/8/17.
 */
public class ChildrenShareWebActivity extends AbsBaseActivity implements DaniujiaJSCallBack.DaniujiaJSCallBackListener, View.OnClickListener {
    private String commonUrl;

    private WebView mWebView;
    private ProgressBar progressBar;
    private MyWebChromeClient mMyWebChromeClient;
    private ImageView shareView;
    private ShareInfoResp shareInfoResp = null;
    private String shareCallback;
    private String platForm;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 从首页或者“我”点击获活动图标,然后从成为专家逻辑走进入的逻辑
        if (intent != null) {
            String callback = "";
            try {
                JSONObject jsonObject = new JSONObject(becomeExpert);
                callback = jsonObject.getString("callback");
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "onNewIntent---JSONException---" + e.toString());
            }

            final String jsFun = "javascript:" + callback + "()";
            LogUtil.d("ZLOVE", "jsFun: " + jsFun);
            mWebView.post(new Runnable() {
                @Override
                public void run() {
                    mWebView.loadUrl(jsFun);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_CHILDREN_ACTIVITY_URL)) {
                commonUrl = intent.getStringExtra(IntentKey.INTENT_KEY_CHILDREN_ACTIVITY_URL);
            } else {
                // 从“我”这个tab点击成为专家，然后点击我要参加走进来的逻辑。
                if (Config.DEBUG) {
                    commonUrl = "https://refactorapi.daniujia.com/m/?platform=android#!/grant";
                } else {
                    commonUrl = "https://www.daniujia.com/m/?platform=android#!/grant";
                }
            }
        }

        mWebView = (WebView) findViewById(R.id.webview);
        progressBar = (ProgressBar) findViewById(R.id.id_progress);
        initWeb();
        findViewById(R.id.id_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    finish();
                }
            }
        });
        shareView = (ImageView) findViewById(R.id.id_share);
        shareView.setOnClickListener(this);
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWeb() {
        WebSettings s = mWebView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDefaultTextEncodingName("utf-8");
        s.setSupportZoom(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        // 全屏显示
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);

        DaniujiaJSCallBack jsCallBack = new DaniujiaJSCallBack();
        jsCallBack.setListener(this);
        mWebView.addJavascriptInterface(jsCallBack, "DaniujiaJSCallBack");

        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(mWebViewClient);
        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(mMyWebChromeClient);

        mWebView.loadUrl(commonUrl);
    }

    class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            return super.onJsAlert(view, url, message, result);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            } else {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(newProgress);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            ((TextView) findViewById(R.id.id_title)).setText(title);
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);// 页面下载完毕,却不代表页面渲染完毕显示出来
            LogUtil.d("ZLOVE", "mWebViewClient---url----" + url);
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

    };

    @Override
    @JavascriptInterface
    public void wantToJoin() {
    }

    @Override
    @JavascriptInterface
    public void finishActivity() {
        ChildrenShareWebActivity.this.finish();
    }

    private ShareDialog shareDialog = null;

    @Override
    @JavascriptInterface
    public void share(String shareInfo) {
        LogUtil.d("ZLOVE", "shareInfo----" + shareInfo);
        try {
            JSONObject object = new JSONObject(shareInfo);
            final String title = object.optString("title");
            final String imgUrl = object.optString("imgUrl");
            final String desc = object.optString("desc");
            final String link = object.optString("link");
            shareCallback = object.optString("callback");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (shareDialog == null) {
                        shareDialog = new ShareDialog(ChildrenShareWebActivity.this);
                        shareDialog.setOnShareClickListener(new ShareDialog.ShareClickListener() {
                            @Override
                            public void onShareClick(String platform) {
                                if (platform.equals(SinaWeibo.NAME)) {
                                    handleSinaWebShare(title, desc, imgUrl, link, platform);
                                } else {
                                    ShareSDKUtil.share(title, desc, imgUrl, link, platform);
                                }
                                shareDialog.dismiss();
                            }
                        });
                    }
                    shareDialog.showDialog();
                }
            });

        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "share---JSONException---" + e.toString());
        }
    }

    @Override
    @JavascriptInterface
    public void shareByNative(String result) {
        LogUtil.d("ZLOVE", "shareByNative----result---" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            final ShareInfoResp resp = new ShareInfoResp();

            resp.setDesc(jsonObject.optString("desc"));
            resp.setImgUrl(jsonObject.optString("imgUrl"));
            resp.setLink(jsonObject.optString("link"));
            resp.setTitle(jsonObject.optString("title"));
            resp.setVisible(jsonObject.optBoolean("isVisible"));
            resp.setCallback(jsonObject.optString("callback"));

            if (resp != null) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (resp.isVisible()) {
                            shareView.setVisibility(View.VISIBLE);
                            shareInfoResp = resp;
                        } else {
                            shareView.setVisibility(View.GONE);
                        }
                    }
                });
            }
        } catch (Exception e) {
            LogUtil.d("ZLOVE", "shareByNative---Exception---" + e.toString());
        }
    }

    @Override
    @JavascriptInterface
    public void auth(String result) {
        LogUtil.d("ZLOVE", "auth---result----" + result);
        String callback = getCallBackName(result);
        final String jsFun;
        if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            JSONObject object = new JSONObject();
            try {
                UserInfo userInfo = SysUtil.getUsrInfo();
                object.put("userId", userInfo.getUserId());
                object.put("authToken", SysUtil.getPref(PrefKeyConst.PREF_TOKEN));
                object.put("username", userInfo.getUsername());
                object.put("name", userInfo.getName());
                object.put("identity", userInfo.getIdentity());
                object.put("imgUrl", userInfo.getHead());
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "JSONException---" + e.toString());
            }
            jsFun = "javascript:" + callback + "('" + object.toString() + "')";
        } else {
            jsFun = "javascript:" + callback + "()";
        }
        LogUtil.d("ZLOVE", "jsFun---" + jsFun);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(jsFun);
            }
        });
    }

    @Override
    @JavascriptInterface
    public void showExpert(String result) {
        LogUtil.d("ZLOVE", "showExpert---result---" + result);
        try {
            JSONObject jsonObject = new JSONObject(result);
            int id = jsonObject.getInt("id");
            Intent intent = new Intent(this, ActExpertHome.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, id);
            startActivity(intent);
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "showExpert---JSONException---" + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_ASK_LOGIN) {
            if (data != null) {
                auth(loginSuccessCallback);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            finish();
        }
    }

    private void handleSinaWebShare(final String title, final String dec, String imgUrl, final String linkUrl, final String platform) {
        String[] str = imgUrl.split("/");
        final File file = new File(FileStorage.APP_IMG_DIR, str[str.length - 1]);
        if (file.exists() && file.length() > 0) {
            ShareSDKUtil.share(title, dec, file.getAbsolutePath(), linkUrl, platform);
            return;
        }
        imgUrl = imgUrl + "?imageView2/2/w/240";
        QiniuDownloadUtil.download(file, imgUrl, new QiniuDownloadUtil.DownloadCallback() {
            @Override
            public void downloadComplete(String path) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareSDKUtil.share(title, dec, file.getAbsolutePath(), linkUrl, platform);
                    }
                });
            }

            @Override
            public void onProgress(int max, int progress) {
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }

    @Override
    public void payWx(String payInfo) {
    }

    @Override
    public void payAli(String payInfo) {
    }

    @Override
    public void startRecord(String callback) {
    }

    @Override
    public void stopRecord(String result) {
    }

    @Override
    public void onVoiceRecordEnd(String result) {
    }

    @Override
    public void playVoice(String localId) {
    }

    @Override
    public void pauseVoice(String localId) {
    }

    @Override
    public void stopVoice(String localId) {
    }

    @Override
    public void onVoicePlayEnd(String result) {
    }

    @Override
    public void uploadVoice(String result) {
    }

    private String loginSuccessCallback;

    @Override
    public void login(String result) {
        loginSuccessCallback = result;
        Intent intent = new Intent(this, ActLogin.class);
        startActivityForResult(intent, IntentKey.REQ_CODE_ASK_LOGIN);
    }


    private String becomeExpert = "";

    @Override
    public void becomeExpert(String result) {
        becomeExpert = result;
        Intent intent = new Intent(this, ActToBeExpert.class);
        intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_CHILD);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v == shareView) {
            if (shareInfoResp == null) {
                return;
            }
            shareCallback = shareInfoResp.getCallback();
            if (shareDialog == null) {
                shareDialog = new ShareDialog(ChildrenShareWebActivity.this);
                shareDialog.setOnShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void onShareClick(String platform) {
                        platForm = platform;
                        if (platform.equals(SinaWeibo.NAME)) {
                            handleSinaWebShare(shareInfoResp.getTitle(), shareInfoResp.getDesc(), shareInfoResp.getImgUrl(), shareInfoResp.getLink(), platform);
                        } else {
                            ShareSDKUtil.share(shareInfoResp.getTitle(), shareInfoResp.getDesc(), shareInfoResp.getImgUrl(), shareInfoResp.getLink(), platform);
                        }
                        shareDialog.dismiss();
                    }
                });
            }
            shareDialog.showDialog();
        }
    }

    @OnMsg(msg = {InsideMsg.NOTIFY_SHARE_STATUS}, useLastMsg = false)
    OnMsgCallback onMsgCallback = new OnMsgCallback() {

        @Override
        public boolean handleMsg(Message msg) {
            LogUtil.d("ZLOVE", "OnMsgCallback---msg---" + msg.what + "---" + msg.obj);
            switch (msg.what) {
                case InsideMsg.NOTIFY_SHARE_STATUS:
                    if (!TextUtils.isEmpty(platForm)) {
                        int err = (int) msg.obj;
                        String errStr = "";
                        if (err == 1) {

                        } else if (err == 2) {
                            errStr = "分享失败";
                        } else if (err == 3) {
                            errStr = "取消分享";
                        }
                        JSONObject object = JsonUtil.getShareInfo(platForm, errStr);
                        final String jsFun = "javascript:" + shareCallback + "('" + object.toString() + "')";
                        LogUtil.d("ZLOVE", "shareSuccess---jsFun---" + jsFun);
                        mWebView.post(new Runnable() {
                            @Override
                            public void run() {
                                mWebView.loadUrl(jsFun);
                            }
                        });
                    }
                    break;
            }
            return false;
        }
    };

    private String getCallBackName(String result) {
        String callback = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            callback = jsonObject.getString("callback");
        } catch (JSONException e) {
            LogUtil.w("ZLOVE", "auth---JSONException---" + e.toString());
        }

        return callback;
    }
}
