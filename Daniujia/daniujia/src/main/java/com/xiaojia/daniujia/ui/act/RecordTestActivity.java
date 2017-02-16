package com.xiaojia.daniujia.ui.act;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.ui.views.audio.AudioRecorder;
import com.xiaojia.daniujia.ui.views.audio.RecordStrategy;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.RecordUtil;

/**
 * Created by ZLOVE on 2016/11/7.
 */
public class RecordTestActivity extends ActDnjBase implements View.OnTouchListener, View.OnClickListener, RecordUtil.PlayRecordListener {

    private Button btnRecord;
    private RecordUtil util;
    private RecordStrategy audioRecorder;

    private Button btnPlay;
    private Button btnPause;
    private Button btnStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_record_test);
        btnRecord = (Button) findViewById(R.id.start);
        btnPlay = (Button) findViewById(R.id.play);
        btnPause = (Button) findViewById(R.id.pause);
        btnStop = (Button) findViewById(R.id.stop);

        btnRecord.setOnTouchListener(this);
        btnPlay.setOnClickListener(this);
        btnPause.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        if (audioRecorder == null) {
            audioRecorder = new AudioRecorder();
        }
        if (util == null) {
            util = new RecordUtil(this, audioRecorder);
        }
        util.setListener(RecordTestActivity.this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                util.startRecord();
                break;
            case MotionEvent.ACTION_UP:
                util.stopRecord();
                break;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v == btnPlay) {
            LogUtil.d("ZLOVE", "----localPath----" + audioRecorder.getFilePath());
            util.playVoice(audioRecorder.getFilePath());
        } else if (v == btnPause) {
            util.pauseVoice(audioRecorder.getFilePath());
        } else if (v == btnPlay) {
            util.stopVoice(audioRecorder.getFilePath());
        }
    }

    @Override
    public void voicePlayEnd() {
        LogUtil.d("ZLOVE", "---voicePlayEnd---");
        Toast.makeText(this, "播放完成...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void voiceRecordEnd() {

    }
}
