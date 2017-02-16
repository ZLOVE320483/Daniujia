package com.xiaojia.daniujia.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.audio.PlayVoice;
import com.xiaojia.daniujia.ui.views.audio.RecordStrategy;

/**
 * Created by ZLOVE on 2016/10/27.
 */
public class RecordUtil implements MediaPlayer.OnCompletionListener {

    private static final int MIN_RECORD_TIME = 1; // 最短录音时间，单位秒
    private static final int RECORD_OFF = 0; // 不在录音
    private static final int RECORD_ON = 1; // 正在录音
    private int recordState = RECORD_OFF; // 录音状态

    private static final int MAX_RECORD_DURATION = 60;

    private Context context;
    private RecordStrategy mAudioRecorder;
    private Thread mRecordThread;
    private float recodeTime = 0.0f; // 录音时长，如果录音时间太短则录音失败
    private boolean isCanceled = false; // 是否取消录音

    private static PlayVoice playVoice;
    private PlayRecordListener listener;

    public RecordUtil(Context context, RecordStrategy mAudioRecorder) {
        this.context = context;
        this.mAudioRecorder = mAudioRecorder;
    }

    public void setListener(PlayRecordListener listener) {
        this.listener = listener;
    }

    public void startRecord() {
        if (!ApplicationUtil.hasSdCard()) {
            Toast.makeText(context, R.string.no_sdcard, Toast.LENGTH_LONG).show();
            return;
        }
        if (mAudioRecorder != null) {
            mAudioRecorder.ready();
            recordState = RECORD_ON;
            mAudioRecorder.start();
            callRecordTimeThread();
        }
    }

    public void stopRecord() {
        LogUtil.d("ZLOVE", "----Stop Recording----");
        if (recordState == RECORD_ON) {
            recordState = RECORD_OFF;
            mAudioRecorder.stop();
            mRecordThread.interrupt();
        }
    }

    // 开启录音计时线程
    private void callRecordTimeThread() {
        LogUtil.d("ZLOVE", "----Start Recording----");
        mRecordThread = new Thread(recordThread);
        mRecordThread.start();
    }

    // 录音线程
    private Runnable recordThread = new Runnable() {
        @Override
        public void run() {
            recodeTime = 0.0f;
            while (recordState == RECORD_ON) {
                    try {
                        Thread.sleep(100);
                        recodeTime += 0.1;
                        // 获取音量，更新dialog
                        if (!isCanceled) {
                            recordHandler.sendEmptyMessage(1);
                        }
                        if (recodeTime > MAX_RECORD_DURATION) {
                            recordHandler.sendEmptyMessage(2);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            }
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler recordHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 2) {
                LogUtil.d("ZLOVE", "----Stop Recording----");
                showWarnToast("录音时间不能超过60秒");
                recordState = RECORD_OFF;
                mAudioRecorder.stop();
                mRecordThread.interrupt();
                if (listener != null) {
                    listener.voiceRecordEnd();
                }
            }
        }
    };

    // 录音时间太短时Toast显示
    private void showWarnToast(String toastText) {
        Toast toast = new Toast(context);
        View warnView = LayoutInflater.from(context).inflate(R.layout.toast_warn, null);
        TextView tvTip = (TextView) warnView.findViewById(R.id.tip);
        tvTip.setText(toastText);
        toast.setView(warnView);
        toast.setGravity(Gravity.CENTER, 0, 0);// 起点位置为中间
        toast.show();
    }

    public void playVoice(String localPath) {
        if (!TextUtils.isEmpty(localPath) && playVoice != null && playVoice.isPlaying()) {
            playVoice.stopPlaying();
            playVoice = null;
        }
        if (playVoice == null) {
            playVoice = PlayVoice.getInstance();
        }
        LogUtil.d("ZLOVE", "---playVoice---");
        playVoice.startPlaying(localPath, this);
    }

    public void pauseVoice(String localPath) {
        LogUtil.d("ZLOVE", "---pauseVoice---");
        if (playVoice != null) {
            playVoice.pausePlaying();
        }
    }

    public void stopVoice(String localPath) {
        LogUtil.d("ZLOVE", "---stopVoice---");
        if (playVoice != null && playVoice.isPlaying()) {
            playVoice.stopPlaying();
            playVoice = null;
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtil.d("ZLOVE", "---onCompletion---");
        LogUtil.d("ZLOVE", "---listener is null---" + (listener == null));
        if (listener != null) {
            listener.voicePlayEnd();
        }
    }

    public interface PlayRecordListener {
        void voicePlayEnd();
        void voiceRecordEnd();
    }
}
