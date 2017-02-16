package com.xiaojia.daniujia.ui.act;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.AbsBaseActivity;

@ContentView(R.layout.activity_web)
public class WebActivity extends AbsBaseActivity {
    @ViewInject(R.id.webview)
    private WebView mWebView;
    private MyWebChromeClient mMyWebChromeClient;

    @ViewInject(R.id.id_progress)
    ProgressBar progressBar;

    private String webTitle;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        webTitle = getIntent().getStringExtra(ExtraConst.EXTRA_WEB_TITLE);
        url = getIntent().getStringExtra(ExtraConst.EXTRA_WEB_URL);
        initTitle();
        initWeb(url, null);
    }

    private void initTitle() {
        TextView titleTv = (TextView) findViewById(R.id.id_title);
        titleTv.setText(webTitle);
    }

    private void initWeb(final String url, final byte[] params) {
        WebSettings s = mWebView.getSettings();
        s.setBuiltInZoomControls(true);
        s.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        s.setUseWideViewPort(true);
        s.setLoadWithOverviewMode(true);
        s.setSavePassword(true);
        s.setSaveFormData(true);
        s.setJavaScriptEnabled(true);
        if (url.endsWith("txt")) {
            s.setDefaultTextEncodingName("gbk");
        }
        s.setGeolocationEnabled(true);// enable navigator.geolocation
        s.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");
        s.setDomStorageEnabled(true);// enable Web Storage: localStorage, sessionStorage
        mWebView.requestFocus();
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebView.setWebViewClient(mWebViewClient);
        mWebView.getSettings().setAllowFileAccess(true);//设置允许访问文件数据
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mMyWebChromeClient = new MyWebChromeClient();
        mWebView.setWebChromeClient(mMyWebChromeClient);
        if (params == null) {
            mWebView.loadUrl(url);
        } else {
            mWebView.postUrl(url, params);
        }
    }

    /**
     * Provides a hook for calling "alert" from javascript. Useful for
     * debugging your javascript.
     */
    final class MyWebChromeClient extends WebChromeClient {
        @Override
        public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
            result.confirm();
            return true;
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
            if (TextUtils.isEmpty(webTitle)) {
                ((TextView) findViewById(R.id.id_title)).setText(title);
            }
        }
    }

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);// 页面下载完毕,却不代表页面渲染完毕显示出来
            progressBar.setVisibility(View.GONE);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //调用拨号程序
            if (url.startsWith("mailto:") || url.startsWith("geo:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    showShortToast("请先下载邮箱APP");
                }
                return true;
            }
            view.loadUrl(url);
            return true;
        }
    };

    @OnClick({R.id.id_back})
    private void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_back:
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        mMyWebChromeClient.onCloseWindow(mWebView);
        mWebView.clearCache(false);
        CookieManager.getInstance().removeAllCookie();// 清除之前登录数据
        mWebView.setVisibility(View.GONE);
        mWebView.destroy();
        super.onDestroy();
    }

    /**
     * handle the exception: service not register ZoomButtonController
     */
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }
}
