package com.xiaojia.daniujia.managers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.Const;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.LoadingDlg;
import com.xiaojia.daniujia.mqtt.MqttUtils;
import com.xiaojia.daniujia.mqtt.service.MqttServiceConstants;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.NetUtils;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.UIUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author leeks <li>package all http-request here, requests run on UI thread
 *         show a dialog, otherwise not</li>
 */
public class OkHttpClientManager {
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    // it may result in memory leak
    private static Activity mActivity;

    private OkHttpClientManager() {
        this.mOkHttpClient = new OkHttpClient();
//		mOkHttpClient.setHostnameVerifier(new HostnameVerifier() {
//			@Override
//			public boolean verify(String hostname, SSLSession session) {
//				return true;
//			}
//		});

        try {
            setCertificates(new InputStream[]{App.get().getAssets().open(Config.DEBUG ? "daniujia.crt" : "daniujia.crt")});
        } catch (IOException e) {
            e.printStackTrace();
        }
        File cacheFile = new File(ApplicationUtil.getApplicationContext().getExternalCacheDir(), "okHttpCache");
        mOkHttpClient.setCache(new Cache(cacheFile, 10 * 10 * 1024));
        mOkHttpClient.setConnectTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setReadTimeout(15, TimeUnit.SECONDS);
        mOkHttpClient.setWriteTimeout(15, TimeUnit.SECONDS);
        this.mOkHttpClient.setCookieHandler(new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER));
        this.mDelivery = new Handler(Looper.getMainLooper());
    }

    public synchronized static OkHttpClientManager getInstance(Activity activity) {
        if (activity != null) {
            mActivity = activity;
        }
        if (mInstance == null) {
            mInstance = new OkHttpClientManager();
        }
        return mInstance;
    }

    private LoadingDlg loadingDlg;
    /**
     * @param request
     * @param callback if you don't want to deliver a self-make-object, just fill the
     *                 second argument RegsultCallback<String>
     *                 <p/>
     *                 if your response head content changed, please override another
     *                 function
     */
    @SuppressWarnings("rawtypes")
    private void execute(final boolean isShowDialog, Request request, ResultCallback callback) {
        if (callback == null)
            callback = ResultCallback.DEFAULT_RESULT_CALLBACK;
        final ResultCallback resCallBack = callback;

        if (isShowDialog && loadingDlg == null){
            loadingDlg = new LoadingDlg(mActivity);
            loadingDlg.setCanceledOnTouchOutside(false);
            loadingDlg.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                }
            });
        }

        final long startTime = System.currentTimeMillis();
        if (isShowDialog && loadingDlg != null && !loadingDlg.isShowing()) {
            loadingDlg.show();
        }

        this.mOkHttpClient.newCall(request).enqueue(new Callback() {
            public void onFailure(Request request, IOException e) {
                LogUtil.d("ZLOVE", "----->onFailure: " + e.toString());
                OkHttpClientManager.this.sendFailResultCallback(request, e, resCallBack);

                if (isShowDialog && loadingDlg != null){
                    loadingDlg.dismiss();
                    loadingDlg = null;
                }

            }

            public void onResponse(Response response) {
                long endTime = System.currentTimeMillis();
                long timeUse = endTime - startTime;
                if (timeUse < 1000) {
                    SystemClock.sleep(1000 - timeUse);
                }

                if (isShowDialog && loadingDlg != null){
                    loadingDlg.dismiss();
                    loadingDlg = null;
                }

                try {
                    String string = response.body().string();
                    LogUtil.d("request_data", "response: " + string + "  ---->code: " + response.code());
                    if (response.code() != 200 && response.code() != 201) {
                        OkHttpClientManager.this.sendFailResultCallback(string, resCallBack);
                        return;
                    }
                    JSONObject json = new JSONObject(string);
                    String rtnCode = json.getString("returncode");
                    if (rtnCode.equals("FAIL")) {
                        OkHttpClientManager.this.sendFailResultCallback(string, resCallBack);
                        return;
                    }
                    String token = response.header("auth-token");
                    if (!TextUtils.isEmpty(token)) {
                        SysUtil.savePref(PrefKeyConst.PREF_TOKEN, token);
                    }
                    if (resCallBack.mType == String.class) {
                        OkHttpClientManager.this.sendSuccessResultCallback(string, resCallBack);
                    } else {
                        Object o = JSON.parseObject(string, resCallBack.mType);
                        OkHttpClientManager.this.sendSuccessResultCallback(o, resCallBack);
                    }
                } catch (IOException e) {
                    LogUtil.d("request_data", "IOException: " + e.toString());
                    OkHttpClientManager.this.sendFailResultCallback(response.request(), e, resCallBack);
                } catch (Exception e) {
                    LogUtil.d("request_data", "Exception: " + e.toString());
                    OkHttpClientManager.this.sendFailResultCallback(response.request(), e, resCallBack);
                }
            }
        });

        if (request != null) {
            LogUtil.d("request_data", "url: " + request.httpUrl());
        }
    }

    public void postWithPlatform(boolean isShowDialog, String url, ResultCallback<?> callback,
                                 FormEncodingBuilder builder) {
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName()).url(url)
                .post(builder.build()).build();
        execute(isShowDialog, request, callback);
    }

    public void postWithPlatform(String url, ResultCallback<?> callback, FormEncodingBuilder builder) {
        postWithPlatform(true, url, callback, builder);
    }

    public void postWithToken(String url, ResultCallback<?> callback, FormEncodingBuilder builder) {
        postWithToken(true, url, callback, builder);
    }

    public void postWithToken(boolean isShowDlg, String url, ResultCallback<?> callback, FormEncodingBuilder builder) {
        if (SysUtil.getPref(PrefKeyConst.PREF_TOKEN) == null) {
            return;
        }
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_TOKEN, SysUtil.getPref(PrefKeyConst.PREF_TOKEN))
                .addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName()).url(url).post(builder.build()).build();
        execute(isShowDlg, request, callback);
    }

    public void get(String url, ResultCallback<?> callback) {
        get(true, url, callback);
    }

    public void get(boolean isShowDialog, String url, ResultCallback<?> callback) {
        Request request = new Request.Builder().header(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).url(url).build();
        execute(isShowDialog, request, callback);
    }

    public void getWithToken(String url, ResultCallback<?> callback) {
        getWithToken(true, url, callback);
    }

    public void getWithPlatform(String url, ResultCallback<?> callback) {
        getWithPlatform(true, url, callback);
    }

    public void getWithPlatform(boolean isShowDialog, String url, ResultCallback<?> callback) {
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName())
                .url(url).build();
        execute(isShowDialog, request, callback);
    }

    public void getWithToken(boolean isShowDialog, String url, ResultCallback<?> callback) {
        // some exception may occur, like json parse or else lead to crash
        if (SysUtil.getPref(PrefKeyConst.PREF_TOKEN) == null) {
            return;
        }
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName())
                .addHeader(Const.KEY_AUTH_TOKEN, SysUtil.getPref(PrefKeyConst.PREF_TOKEN)).url(url).build();
        execute(isShowDialog, request, callback);
    }

    public String execute(Request request) throws IOException {
        Call call = this.mOkHttpClient.newCall(request);
        Response execute = call.execute();
        String respStr = execute.body().string();
        if (execute.code() != 200 && execute.code() != 201) {
            return null;
        }
        try {
            JSONObject json = new JSONObject(respStr);
            String code = json.getString("returncode");
            if (code.equals("FAIL")) {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String token = execute.header("auth-token");
        if (!TextUtils.isEmpty(token)) {
            SysUtil.savePref(PrefKeyConst.PREF_TOKEN, token);
        }
        return respStr;
    }

    public String getSync(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        return execute(request);
    }

    public String getSyncWithPlatform(String url) throws IOException {
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName()).url(url).build();
        return execute(request);
    }

    public String getSyncWithToken(String url) throws IOException {
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM)
                .addHeader(Const.KEY_AUTH_TOKEN, SysUtil.getPref(PrefKeyConst.PREF_TOKEN)).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName()).url(url).build();
        return execute(request);
    }

    public String postSyncWithToken(String url, FormEncodingBuilder builder) throws IOException {
        Request request = new Request.Builder().addHeader(Const.KEY_AUTH_PLATFORM, Const.PLATFORM)
                .addHeader(Const.KEY_AUTH_TOKEN, SysUtil.getPref(PrefKeyConst.PREF_TOKEN)).addHeader(Const.KEY_AUTH_VERSION, ApplicationUtil.getVersionName()).url(url).post(builder.build()).build();
        return execute(request);
    }

    @SuppressWarnings("rawtypes")
    public void sendFailResultCallback(final Request request, final Exception e, final ResultCallback callback) {
        if (callback == null)
            return;

        this.mDelivery.post(new Runnable() {
            public void run() {
                callback.onError(request, e);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        if (callback == null)
            return;
        this.mDelivery.post(new Runnable() {
            @SuppressWarnings("unchecked")
            public void run() {
                callback.onResponse(object);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    public void sendFailResultCallback(final String errMsg, final ResultCallback callback) {
        if (callback == null) {
            return;
        }
        this.mDelivery.post(new Runnable() {

            @Override
            public void run() {
                callback.onFail(errMsg);
            }
        });
    }

    public void cancelTag(Object tag) {
        this.mOkHttpClient.cancel(tag);
    }

    public void setCertificates(InputStream[] certificates) {
        setCertificates(certificates, null, null);
    }

    private TrustManager[] prepareTrustManager(InputStream[] certificates) {
        if ((certificates == null) || (certificates.length <= 0))
            return null;

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }
            TrustManagerFactory trustManagerFactory = null;

            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);

            return trustManagerFactory.getTrustManagers();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private KeyManager[] prepareKeyManager(InputStream bksFile, String password) {
        try {
            if ((bksFile == null) || (password == null))
                return null;

            KeyStore clientKeyStore = KeyStore.getInstance("BKS");
            clientKeyStore.load(bksFile, password.toCharArray());
            KeyManagerFactory keyManagerFactory = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            keyManagerFactory.init(clientKeyStore, password.toCharArray());
            return keyManagerFactory.getKeyManagers();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setCertificates(InputStream[] certificates, InputStream bksFile, String password) {
        try {
            TrustManager[] trustManagers = prepareTrustManager(certificates);

            KeyManager[] keyManagers = prepareKeyManager(bksFile, password);
            SSLContext sslContext = SSLContext.getInstance("TLS");

            sslContext.init(keyManagers, new TrustManager[]{new MyTrustManager(chooseTrustManager(trustManagers))},
                    new SecureRandom());
            this.mOkHttpClient.setSslSocketFactory(sslContext.getSocketFactory());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }
    }

    private X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if ((trustManager instanceof X509TrustManager)) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }

    private class MyTrustManager implements X509TrustManager {
        private X509TrustManager defaultTrustManager;
        private X509TrustManager localTrustManager;

        public MyTrustManager(X509TrustManager localTrustManager) throws NoSuchAlgorithmException, KeyStoreException {
            TrustManagerFactory var4 = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            var4.init((KeyStore) null);
            this.defaultTrustManager = OkHttpClientManager.this.chooseTrustManager(var4.getTrustManagers());
            this.localTrustManager = localTrustManager;
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            try {
                this.defaultTrustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException ce) {
                this.localTrustManager.checkServerTrusted(chain, authType);
            }
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    public abstract static class ResultCallback<T> {
        public Type mType;
        public static final ResultCallback<String> DEFAULT_RESULT_CALLBACK = new ResultCallback<String>() {
            public void onError(Request request, Exception e) {
            }

            public void onResponse(String response) {
            }

            public void onFail(String errorMsg) {
            }
        };

        public ResultCallback() {
            this.mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if ((superclass instanceof Class)) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return parameterized.getActualTypeArguments()[0];
        }

        public void inProgress(float progress) {
        }

        public void onError(Request paramRequest, Exception e) {
            if (mActivity != null) {
                if (NetUtils.isNetAvailable(mActivity)) {
                    UIUtil.showShortToast("网络超时");
                }
            }
            e.printStackTrace();
        }

        public abstract void onResponse(T result);

        public void onFail(String errorMsg) {
            try {
                JSONObject errJson = new JSONObject(errorMsg);
                String errCode = errJson.optString("errcode");
                if (errCode.equals("1000")) {
                    if (mActivity != null && !mActivity.isFinishing()) {
                        Toast.makeText(mActivity, "登录已过期，请重新登录", Toast.LENGTH_SHORT).show();
                        MqttUtils.publish(MqttServiceConstants.USER_LOGOUT_REQUEST, new JSONObject(), 0);
                        MqttUtils.disconnect();
                        MqttUtils.handleConnectionLost();
                        SysUtil.clearUserInfo();
                        SysUtil.removePref(PrefKeyConst.PREF_PASSWORD);
                        mActivity.finish();
                        Intent intent = new Intent(mActivity, ActLogin.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra(IntentKey.INTENT_KEY_IS_FORM_CONFLICT, true);
                        mActivity.startActivity(intent);
                    }
                } else if (errCode.equals("2000")) {
                    DialogManager.showActsDialogWithMessage(mActivity, errJson.optString("errmsg"));
                } else {
                    if (mActivity != null && !mActivity.isFinishing()) {
                        UIUtil.showShortToast(errJson.optString("errmsg"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}