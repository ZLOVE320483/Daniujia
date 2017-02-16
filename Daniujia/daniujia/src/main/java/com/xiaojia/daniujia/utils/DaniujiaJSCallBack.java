package com.xiaojia.daniujia.utils;

import android.webkit.JavascriptInterface;

/**
 * Created by ZLOVE on 2016/8/17.
 */
public class DaniujiaJSCallBack {

    private DaniujiaJSCallBackListener listener;

    public void setListener(DaniujiaJSCallBackListener listener) {
        this.listener = listener;
    }

    @JavascriptInterface
    public void wantToJoin() {
        if (listener != null) {
            listener.wantToJoin();
        }
    }

    @JavascriptInterface
    public void finishActivity(String result) {
        if (listener != null) {
            listener.finishActivity();
        }
    }

    @JavascriptInterface
    public void share(String shareInfo) {
        if (listener != null) {
            listener.share(shareInfo);
        }
    }

    @JavascriptInterface
    public void shareByNative(String result) {
        if (listener != null) {
            listener.shareByNative(result);
        }
    }

    @JavascriptInterface
    public void auth(String result) {
        if (listener != null) {
            listener.auth(result);
        }
    }

    @JavascriptInterface
    public void showExpert(String result) {
        if (listener != null) {
            listener.showExpert(result);
        }
    }

    @JavascriptInterface
    public void payWx(final String payInfo) {
        if (listener != null) {
            listener.payWx(payInfo);
        }
    }

    @JavascriptInterface
    public void payAli(final String payInfo) {
        if (listener != null) {
            listener.payAli(payInfo);
        }
    }

    @JavascriptInterface
    public void startRecord(String callback) {
        if (listener != null) {
            listener.startRecord(callback);
        }
    }

    @JavascriptInterface
    public void stopRecord(String result) {
        if (listener != null) {
            listener.stopRecord(result);
        }
    }

    @JavascriptInterface
    public void onVoiceRecordEnd(String result) {
        if (listener != null) {
            listener.onVoiceRecordEnd(result);
        }
    }

    @JavascriptInterface
    public void playVoice(String localId) {
        if (listener != null) {
            listener.playVoice(localId);
        }
    }

    @JavascriptInterface
    public void pauseVoice(String localId) {
        if (listener != null) {
            listener.pauseVoice(localId);
        }
    }

    @JavascriptInterface
    public void stopVoice(String localId) {
        if (listener != null) {
            listener.stopVoice(localId);
        }
    }

    @JavascriptInterface
    public void onVoicePlayEnd(String result) {
        if (listener != null) {
            listener.onVoicePlayEnd(result);
        }
    }

    @JavascriptInterface
    public void uploadVoice(String result) {
        if (listener != null) {
            listener.uploadVoice(result);
        }
    }

    @JavascriptInterface
    public void login(String result) {
        if (listener != null) {
            listener.login(result);
        }
    }

    @JavascriptInterface
    public void becomeExpert(String result) {
        if (listener != null) {
            listener.becomeExpert(result);
        }
    }

    public interface DaniujiaJSCallBackListener {
        void wantToJoin();
        void finishActivity();
        void share(String shareInfo);
        void auth(String result);
        void payWx(final String payInfo);
        void payAli(final String payInfo);
        void startRecord(String callback);
        void stopRecord(String result);
        void onVoiceRecordEnd(String result);
        void playVoice(String localId);
        void pauseVoice(String localId);
        void stopVoice(String localId);
        void onVoicePlayEnd(String result);
        void uploadVoice(String result);
        void login(String result);
        void becomeExpert(String result);
        void showExpert(String result);
        void shareByNative(String result);
    }
}
