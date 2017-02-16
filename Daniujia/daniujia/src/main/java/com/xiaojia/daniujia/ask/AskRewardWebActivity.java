package com.xiaojia.daniujia.ask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadOptions;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;
import com.xiaojia.daniujia.ctrls.WXPayController;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.dlgs.LoadingDlg;
import com.xiaojia.daniujia.dlgs.ShareDialog;
import com.xiaojia.daniujia.domain.UserInfo;
import com.xiaojia.daniujia.domain.resp.PayOrderRespVo;
import com.xiaojia.daniujia.domain.resp.ShareInfoResp;
import com.xiaojia.daniujia.managers.ThreadWorker;
import com.xiaojia.daniujia.msg.OnMsgCallback;
import com.xiaojia.daniujia.msg.annotation.OnMsg;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.ui.views.audio.AudioRecorder;
import com.xiaojia.daniujia.ui.views.audio.RecordStrategy;
import com.xiaojia.daniujia.utils.DaniujiaJSCallBack;
import com.xiaojia.daniujia.utils.JsonUtil;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.QiniuUtil;
import com.xiaojia.daniujia.utils.RecordUtil;
import com.xiaojia.daniujia.utils.ShareSDKUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by ZLOVE on 2016/10/25.
 */
public class AskRewardWebActivity extends AbsBaseActivity implements View.OnClickListener, DaniujiaJSCallBack.DaniujiaJSCallBackListener, RecordUtil.PlayRecordListener {

    private static final String PUBLISH_ASK_URL = "m/?platform=android#!/reward/publish";
    private static final String ASK_DETAIL_URL = "m/?platform=android#!/reward/";
    private static final String MY_ASK_LIST_URL = "m/?platform=android#!/center/reward/publish";
    private static final int IMAGE_REQUEST = 1011;

    private String urlHead;
    private int askDetailId = 0;
    private String fromWhere = "";

    private LoadingDlg loadingDlg;
    private WebView mWebView;
    private MyWebChromeClient mMyWebChromeClient;
    private ImageView shareView;
    private ShareInfoResp shareInfoResp = null;
    private String shareCallback;
    private String platForm;

    private RecordUtil util;
    private RecordStrategy audioRecorder;

    private boolean isStartPlay = false;

    private String mCameraFilePath;
    private ValueCallback<Uri[]> mUploadCallbackAboveL = null;
    private ValueCallback<Uri> mUploadCallback = null;
    private static final int FILECHOOSER_RESULTCODE = 1011;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tencent_web);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM)) {
                fromWhere = intent.getStringExtra(IntentKey.INTENT_KEY_ASK_REWARD_FROM);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_ASK_REWARD_ID)) {
                askDetailId = intent.getIntExtra(IntentKey.INTENT_KEY_ASK_REWARD_ID, 0);
            }
        }

        findViewById(R.id.id_back).setOnClickListener(this);
        shareView = (ImageView) findViewById(R.id.id_share);
        shareView.setOnClickListener(this);
        if (Config.DEBUG) {
            urlHead = "https://refactorapi.daniujia.com/";// refactor
        } else {
            urlHead = "https://www.daniujia.com/";
        }

        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }
        if (util == null) {
            util = new RecordUtil(this, audioRecorder);
        }
        util.setListener(this);

        loadingDlg = new LoadingDlg(this);
        mWebView = (WebView) findViewById(R.id.webview);
        initWeb();
    }

    @SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
    private void initWeb() {
        loadingDlg.show();
        WebSettings s = mWebView.getSettings();
        s.setJavaScriptEnabled(true);
        s.setDefaultTextEncodingName("utf-8");
        s.setSupportZoom(true);
        s.setDomStorageEnabled(true);
        s.setDatabaseEnabled(true);
        s.setAllowFileAccess(true);
        // 全屏显示
        s.setLoadWithOverviewMode(true);
        s.setUseWideViewPort(true);
        s.setAppCacheEnabled(true);
        final String cachePath = getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        s.setAppCachePath(cachePath);
        s.setAppCacheMaxSize(5*1024*1024);

        DaniujiaJSCallBack jsCallBack = new DaniujiaJSCallBack();
        jsCallBack.setListener(this);
        mWebView.addJavascriptInterface(jsCallBack, "DaniujiaJSCallBack");

        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(mWebViewClient);
        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(mMyWebChromeClient);
        if (fromWhere.equals(IntentKey.ASK_REWARD_PUBLISH)) {
            mWebView.loadUrl(urlHead + PUBLISH_ASK_URL);
        } else if (fromWhere.equals(IntentKey.ASK_REWARD_DETAIL)) {
            mWebView.loadUrl(urlHead + ASK_DETAIL_URL + askDetailId);
        } else if (fromWhere.endsWith(IntentKey.MY_ASK_LIST_URL)) {
            mWebView.loadUrl(urlHead + MY_ASK_LIST_URL);
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, com.tencent.smtt.export.external.interfaces.JsResult jsResult) {
            return super.onJsAlert(webView, s, s1, jsResult);
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            if (newProgress == 100) {
                if (loadingDlg != null && loadingDlg.isShowing()) {
                    loadingDlg.dismiss();
                }
            } else {
                if (loadingDlg != null && !loadingDlg.isShowing()) {
                    loadingDlg.show();
                }
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            ((TextView) findViewById(R.id.id_title)).setText(title);
        }


        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
            LogUtil.d("ZLOVE", "---onShowFileChooser---");
            mUploadCallbackAboveL = valueCallback;
            AskRewardWebActivity.this.startActivityForResult(createDefaultOpenableIntent(), FILECHOOSER_RESULTCODE);
            return true;
        }

        @Deprecated
        public void openFileChooser(ValueCallback<Uri> uploadFile, String acceptType, String capture) {
            LogUtil.d("ZLOVE", "---openFileChooser---");
            mUploadCallback = uploadFile;
            AskRewardWebActivity.this.startActivityForResult(createDefaultOpenableIntent(), FILECHOOSER_RESULTCODE);
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);// 页面下载完毕,却不代表页面渲染完毕显示出来

            LogUtil.d("ZLOVE", "mWebViewClient---url----" + url);
            isStartPlay = false;
            if (loadingDlg != null && loadingDlg.isShowing()) {
                loadingDlg.dismiss();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.id_back) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        } else if (v.getId() == R.id.id_share) {
            if (shareInfoResp == null) {
                return;
            }
            shareCallback = shareInfoResp.getCallback();
            if (shareDialog == null) {
                shareDialog = new ShareDialog(AskRewardWebActivity.this);
                shareDialog.setOnShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void onShareClick(String platform) {
                        if (platform.equals(SinaWeibo.NAME)) {
                            handleSinaWebShare(shareInfoResp.getTitle(), shareInfoResp.getDesc(), shareInfoResp.getImgUrl(), shareInfoResp.getLink(), platform);
                        } else {
                            ShareSDKUtil.share(shareInfoResp.getTitle(), shareInfoResp.getDesc(), shareInfoResp.getImgUrl(), shareInfoResp.getLink(), platform);
                        }
                        shareDialog.dismiss();
                        platForm = platform;
                    }
                });
            }
            shareDialog.showDialog();
        }
    }

    @Override
    @JavascriptInterface
    public void wantToJoin() {
    }

    @Override
    @JavascriptInterface
    public void finishActivity() {
        finish();
    }

    private ShareDialog shareDialog = null;

    @Override
    @JavascriptInterface
    public void share(String shareInfo) {
        LogUtil.d("ZLOVE", "shareInfo----" + shareInfo);
        try {
            JSONObject object = new JSONObject(shareInfo);
            final String title = object.getString("title");
            final String imgUrl = object.getString("imgUrl");
            final String desc = object.getString("desc");
            final String link = object.getString("link");
            shareCallback = object.optString("callback");

            if (shareDialog == null) {
                shareDialog = new ShareDialog(AskRewardWebActivity.this);
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

    private String paySuccessFunction;

    @Override
    @JavascriptInterface
    public void payWx(String payInfo) {
        // TODO
        LogUtil.d("ZLOVE", "payWx: " + payInfo);

        final PayOrderRespVo resp = com.alibaba.fastjson.JSONObject.parseObject(payInfo, PayOrderRespVo.class);
        paySuccessFunction = resp.getCallback();
        IWXAPI wxApi = WXAPIFactory.createWXAPI(AskRewardWebActivity.this, null);
        final WXPayController mWxpayCtrl = new WXPayController(AskRewardWebActivity.this);
        if (wxApi.isWXAppInstalled() && wxApi.isWXAppSupportAPI()) {
            ThreadWorker.execute(new Runnable() {
                @Override
                public void run() {
                    mWxpayCtrl.pay(resp);
                }
            });
        } else {
            showShortToast("请先安装微信");
        }
    }

    @Override
    public void payAli(String payInfo) {
        // TODO
        LogUtil.d("ZLOVE", "payAli: " + payInfo);
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
    @JavascriptInterface
    public void startRecord(String result) {
        LogUtil.d("ZLOVE", "startRecord---callback---" + result);

        String callback = getCallBackName(result);
        final String jsFun = "javascript:" + callback + "('{}')";
        LogUtil.d("ZLOVE", "startRecord---jsFun---" + jsFun);

        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(jsFun);
            }
        });
        util.startRecord();
    }

    @Override
    @JavascriptInterface
    public void stopRecord(String result) {
        LogUtil.d("ZLOVE", "stopRecord---" + result);
        util.stopRecord();
        String callback = getCallBackName(result);
        JSONObject object = new JSONObject();
        try {
            object.put("localId", audioRecorder.getFilePath().toString());
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "JSONException---" + e.toString());
        }

        final String jsFun = "javascript:" + callback + "('" + object.toString() + "')";
        LogUtil.d("ZLOVE", "stopRecord---jsFun---" + jsFun);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(jsFun);
            }
        });

    }

    private String recordEndFunction;

    // 开始录音时发起请求，获取本地录音60s后需要回调的方法名
    @Override
    @JavascriptInterface
    public void onVoiceRecordEnd(String result) {
        LogUtil.d("ZLOVE", "onVoiceRecordEnd---result---" + result);
        JSONObject object = new JSONObject();
        try {
            object.put("localId", audioRecorder.getFilePath().toString());
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "onVoiceRecordEnd---JSONException---" + e.toString());
        }
        recordEndFunction = "javascript:" + getCallBackName(result) + "('" + object.toString() + "')";
    }

    @Override
    @JavascriptInterface
    public void playVoice(String result) {
        LogUtil.d("ZLOVE", "playVoice---result---" + result);
        if (isStartPlay) {
            pauseVoice(result);
        } else {
            isStartPlay = true;
            String localId = "";
            try {
                JSONObject jsonObject = new JSONObject(result);
                localId = jsonObject.getString("localId");
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "playVoice---JSONException---" + e.toString());
            }
            util.playVoice(localId);
        }
    }

    @Override
    public void pauseVoice(String result) {
        LogUtil.d("ZLOVE", "playVoice---pauseVoice---" + result);
        String localId = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            localId = jsonObject.getString("localId");
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "pauseVoice---JSONException---" + e.toString());
        }
        util.pauseVoice(localId);
    }

    @Override
    public void stopVoice(String result) {
        LogUtil.d("ZLOVE", "playVoice---stopVoice---" + result);
        isStartPlay = false;
        String localId = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            localId = jsonObject.getString("localId");
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "stopVoice---JSONException---" + e.toString());
        }
        util.stopVoice(localId);
    }

    private String playEndFunction;
    private BaseMsgDlg dlg;

    @Override
    @JavascriptInterface
    public void onVoicePlayEnd(String result) {
        playEndFunction = "javascript:" + getCallBackName(result) + "({})";
    }

    @Override
    public void uploadVoice(final String result) {
        LogUtil.d("ZLOVE", "uploadVoice---result---" + result);
        String localId = "";
        try {
            JSONObject jsonObject = new JSONObject(result);
            localId = jsonObject.getString("localId");
        } catch (JSONException e) {
            LogUtil.d("ZLOVE", "stopVoice---JSONException---" + e.toString());
        }
        final File file = new File(localId);

        if (file == null || file.length() <= 0){
            if (dlg == null){
                dlg = new BaseMsgDlg(AskRewardWebActivity.this);
                dlg.setTitle("开启录音权限");
                dlg.setMsg("检测到录音失败，请尝试通过以下途径开启录音权限：\n方法一：设置-->应用程序管理-->大牛家-->权限管理-->" +
                        "录音-->开启\n方法二：设置-->权限管理-->应用程序-->大牛家-->录音-->开启");
                dlg.setMsgGravity(Gravity.NO_GRAVITY);
                dlg.setMsgTextSize(16);
                dlg.setBtnName(DialogInterface.BUTTON_POSITIVE,R.string.know);
            }
            dlg.show();
            return;
        } else {
            LogUtil.d("zptest","file size: " + file.length());
        }

        ThreadWorker.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final String fileName = file.getName();
                    QiniuUtil.uploadVoiceAmrToMp3(file, fileName, new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo responseInfo, JSONObject jsonObject) {
                            try {
                                String persistentId = jsonObject.getString("persistentId");
                                JSONObject object = new JSONObject();
                                try {
                                    object.put("persistentId", persistentId);
                                } catch (JSONException e) {
                                    LogUtil.d("ZLOVE", "JSONException---" + e.toString());
                                }

                                final String jsFunction = "javascript:" + getCallBackName(result) + "('" + object.toString() + "')";
                                LogUtil.d("ZLOVE", "uploadVoice---jsFunction---" + jsFunction);
                                mWebView.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mWebView.loadUrl(jsFunction);
                                    }
                                });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new UploadOptions(null, null, false, new UpProgressHandler() {

                        @Override
                        public void progress(String s, final double v) {

                        }
                    }, null));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

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

    @Override
    public void voicePlayEnd() {
        LogUtil.d("ZLOVE", "voicePlayEnd---playEndFunction---" + playEndFunction);
        isStartPlay = false;
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(playEndFunction);
            }
        });
    }

    @Override
    public void voiceRecordEnd() {
        LogUtil.d("ZLOVE", "voiceRecordEnd---recordEndFunction---" + recordEndFunction);
        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(recordEndFunction);
            }
        });
    }

    private String loginSuccessCallback;

    @Override
    public void login(String result) {
        LogUtil.d("ZLOVE", "login---result---" + result);
        loginSuccessCallback = result;
        Intent intent = new Intent(this, ActLogin.class);
        startActivityForResult(intent, IntentKey.REQ_CODE_ASK_LOGIN);
    }

    @OnMsg(msg = {InsideMsg.PAY_WEIXIN_SUCCESS, InsideMsg.PAY_WEIXIN_FAIL, InsideMsg.NOTIFY_SHARE_STATUS}, useLastMsg = false)
    OnMsgCallback onMsgCallback = new OnMsgCallback() {

        @Override
        public boolean handleMsg(Message msg) {
            LogUtil.d("ZLOVE", "OnMsgCallback---msg---" + msg.what + "---" + msg.obj);
            switch (msg.what) {
                case InsideMsg.PAY_WEIXIN_SUCCESS:
                    showShortToast(R.string.pay_success);
                    paySuccess("");
                    break;
                case InsideMsg.PAY_WEIXIN_FAIL:
                    paySuccess("支付失败");
                    break;
                case InsideMsg.NOTIFY_SHARE_STATUS:
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
                    break;
            }
            return false;
        }
    };

    private void paySuccess(String errStr) {
        JSONObject object = new JSONObject();
        if (!TextUtils.isEmpty(errStr)) {
            try {
                object.put("err", errStr);
            } catch (JSONException e) {
                LogUtil.d("ZLOVE", "paySuccess---JSONException---" + e.toString());
            }
        }

        final String jsFun = "javascript:" + paySuccessFunction + "('" + object.toString() + "')";
        LogUtil.d("ZLOVE", "paySuccess---jsFun---" + jsFun);

        mWebView.post(new Runnable() {
            @Override
            public void run() {
                mWebView.loadUrl(jsFun);
            }
        });
    }

    @Override
    public void becomeExpert(String result) {
        startActivity(new Intent(this, ActToBeExpert.class));
    }

    /** 默认支持上传多种文件类型 */
    private Intent createDefaultOpenableIntent() {
        Intent imageIntent = new Intent(Intent.ACTION_GET_CONTENT);//选择图片文件
        imageIntent.setType("image/*");
        Intent chooser = createChooserIntent(createCameraIntent());
        chooser.putExtra(Intent.EXTRA_INTENT, imageIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "选择操作");
        return chooser;
    }

    private Intent createCameraIntent() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File externalDataDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        File cameraDataDir = new File(externalDataDir.getAbsolutePath() + File.separator + "browser-photos");
        cameraDataDir.mkdirs();
        mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mCameraFilePath)));
        return cameraIntent;
    }

    private Intent createChooserIntent(Intent... intents) {
        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
        return chooser;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_ASK_LOGIN) {
            if (data != null) {
                auth(loginSuccessCallback);
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            Uri result = getImageUri(resultCode, data);
            if (mUploadCallbackAboveL != null) {
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{result});
            }
            if (mUploadCallback != null) {
                mUploadCallback.onReceiveValue(result);
            }
        }
    }

    private Uri getImageUri(int resultCode, Intent data) {
        Uri result = null;
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                LogUtil.d("ZLOVE", "data is null");
                LogUtil.d("ZLOVE", "mCameraFilePath---" + mCameraFilePath);
                File cameraFile = new File(mCameraFilePath);
                if (cameraFile.exists()) {
                    result = Uri.fromFile(cameraFile);
                }
            } else {
                String dataString = data.getDataString();
                LogUtil.d("ZLOVE", "dataString---" + dataString);
                if (!TextUtils.isEmpty(dataString)) {
                    result = Uri.parse(dataString);
                } else {
                    LogUtil.d("ZLOVE", "mCameraFilePath---" + mCameraFilePath);
                    File cameraFile = new File(mCameraFilePath);
                    if (cameraFile.exists()) {
                        result = Uri.fromFile(cameraFile);
                    }
                }
            }
        }
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("cameraFilePath", mCameraFilePath);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCameraFilePath = savedInstanceState.getString("cameraFilePath");
    }
}
