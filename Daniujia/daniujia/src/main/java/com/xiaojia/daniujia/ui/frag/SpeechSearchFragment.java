package com.xiaojia.daniujia.ui.frag;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.base.App;
import com.xiaojia.daniujia.dlgs.BaseMsgDlg;
import com.xiaojia.daniujia.ui.views.LoadingView;
import com.xiaojia.daniujia.ui.views.LoadingZeroVolumeView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.JsonParser;
import com.xiaojia.daniujia.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by ZLOVE on 2016/12/15.
 */
public class SpeechSearchFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SpeechSearchFragment.class.getSimpleName();

    private TextView tvContent;
    private LoadingView loadingView;
    private LoadingZeroVolumeView zeroVolumeView;
    private View bottomContainer;
    private ImageView ivVoiceClick;

    private SpeechRecognizer speechRecognizer;
    private HashMap<String, String> speechResults = new LinkedHashMap<>();
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private BaseMsgDlg dlg;

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_speech_search;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.id_back));
        tvContent = (TextView) view.findViewById(R.id.id_content);
        loadingView = (LoadingView) view.findViewById(R.id.load);
        zeroVolumeView = (LoadingZeroVolumeView) view.findViewById(R.id.load_zero);
        bottomContainer = view.findViewById(R.id.container);
        ivVoiceClick = (ImageView) view.findViewById(R.id.voice);
        ivVoiceClick.setOnClickListener(this);

        loadingView.setVisibility(View.GONE);
        zeroVolumeView.setVisibility(View.VISIBLE);

        speechRecognizer = SpeechRecognizer.createRecognizer(getActivity(), mInitListener);
        startRecognizeListener();
    }

    @Override
    public void onClick(View v) {
        if (v == ivVoiceClick) {
            bottomContainer.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            zeroVolumeView.setVisibility(View.VISIBLE);
            startRecognizeListener();
        }
    }

    private void startRecognizeListener() {
        FlowerCollector.onEvent(getActivity(), "iat_recognize");
        speechResults.clear();
        setParams();
        speechRecognizer.startListening(mRecognizerListener);
    }

    private void setParams() {
        // 清空参数
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");
        // 设置语言
        speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        // 设置语言区域
        speechRecognizer.setParameter(SpeechConstant.ACCENT, "mandarin");
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "2000");
        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1000");
        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "0");
        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        speechRecognizer.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/iat.wav");
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            LogUtil.d(TAG, "SpeechRecognizer init() code = " + code);
        }
    };

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            tvContent.setText("请说话...");
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            tvContent.setText("没有听清 重新尝试");
            tvContent.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
            loadingView.setVisibility(View.GONE);
            zeroVolumeView.setVisibility(View.GONE);
            bottomContainer.setVisibility(View.VISIBLE);
            if (error.getErrorCode() == 20006) {
                if (dlg == null){
                    dlg = new BaseMsgDlg(getActivity());
                    dlg.setTitle("开启录音权限");
                    dlg.setMsg("检测到录音失败，请尝试通过以下途径开启录音权限：\n方法一：设置-->应用程序管理-->大牛家-->权限管理-->" +
                            "录音-->开启\n方法二：设置-->权限管理-->应用程序-->大牛家-->录音-->开启");
                    dlg.setMsgGravity(Gravity.NO_GRAVITY);
                    dlg.setMsgTextSize(16);
                    dlg.setBtnName(DialogInterface.BUTTON_POSITIVE,R.string.know);
                }
                dlg.show();
            }
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            LogUtil.d("ZLOVE", "结束说话");
        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            LogUtil.d("ZLOVE", "getResultString---" + results.getResultString());
            if (isLast) {
                StringBuffer resultBuffer = new StringBuffer();
                for (String key : speechResults.keySet()) {
                    resultBuffer.append(speechResults.get(key));
                }
                String res = resultBuffer.toString();
                if (TextUtils.isEmpty(res.trim())) {
                    tvContent.setText("没有听清 重新尝试");
                    tvContent.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
                    loadingView.setVisibility(View.GONE);
                    zeroVolumeView.setVisibility(View.GONE);
                    bottomContainer.setVisibility(View.VISIBLE);
                    return;
                }
                printResult(res);
            } else {
                String text = JsonParser.parseIatResult(results.getResultString());
                String sn = null;
                // 读取json结果中的sn字段
                try {
                    JSONObject resultJson = new JSONObject(results.getResultString());
                    sn = resultJson.optString("sn");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                speechResults.put(sn, text);
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            tvContent.setText("正在倾听");
            tvContent.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
            if (loadingView != null) {
                if (volume > 2) {
                    loadingView.setVisibility(View.VISIBLE);
                    zeroVolumeView.setVisibility(View.GONE);
                } else {
                    loadingView.setVisibility(View.GONE);
                    zeroVolumeView.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		LogUtil.d(TAG, "session id =" + sid);
            //	}
        }
    };

    private void printResult(final String res) {
        tvContent.setText(res);
        tvContent.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.theme_blue));

        App.get().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent();
                intent.putExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT, res);
                finishActivity(intent);
            }
        }, 500);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        if (speechRecognizer != null && !speechRecognizer.isListening()) {
            speechRecognizer.cancel();
            speechRecognizer.destroy();
        }
    }

    @Override
    public void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(getActivity());
        FlowerCollector.onPageStart(SpeechSearchFragment.class.getSimpleName());
        super.onResume();
    }

    @Override
    public void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(SpeechSearchFragment.class.getSimpleName());
        FlowerCollector.onPause(getActivity());
        super.onPause();
    }
}
