package com.xiaojia.daniujia.ui.act;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;

import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.managers.DnjNotificationManager;
import com.xiaojia.daniujia.ui.frag.MessageFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by ZLOVE on 2016/4/11.
 */
public class ActChat extends ActDnjBase implements SensorEventListener {

    private MessageFragment fragment;

    private AudioManager audioManager = null; // 声音管理器
    private SensorManager _sensorManager = null; // 传感器管理器
    private Sensor mProximiny = null; // 传感器实例
    private float f_proximiny; // 当前传感器距离

    private HeadsetPlugReceiver myNoisyAudioStreamReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_act_fragment_container);
        fragment = new MessageFragment();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.id_framework, fragment).commit();
        }
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        _sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mProximiny = _sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        myNoisyAudioStreamReceiver = new HeadsetPlugReceiver();
        IntentFilter intentFilter = new IntentFilter(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addAction("android.intent.action.HEADSET_PLUG");
        registerReceiver(myNoisyAudioStreamReceiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 注册传感器
        _sensorManager.registerListener(this, mProximiny, SensorManager.SENSOR_DELAY_NORMAL);
        DnjNotificationManager.getInstance(getApplication()).setMsgNum(0);
        DnjNotificationManager.getInstance(getApplication()).cancelAll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // 取消注册传感器
        _sensorManager.unregisterListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myNoisyAudioStreamReceiver);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        f_proximiny = event.values[0];

        if (f_proximiny >= mProximiny.getMaximumRange()) {
            setModeNormal();
        } else {
            setInCallBySdk();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    /**
     * 监听耳机插入 or 拔出
     */
    public class HeadsetPlugReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("state")) {
                if (intent.getIntExtra("state", 0) == 0) {
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                } else if (intent.getIntExtra("state", 0) == 1) {
                    audioManager.setMode(AudioManager.MODE_IN_CALL);
                }
            }
        }
    }

    private void setModeNormal() {
        if (audioManager == null) {
            return;
        }
        audioManager.setSpeakerphoneOn(true);
        audioManager.setMode(AudioManager.MODE_NORMAL);

        if (!audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(true);

            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL,
                    audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }

    }

    private void setInCallBySdk() {
        if (audioManager == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (audioManager.getMode() != AudioManager.MODE_IN_COMMUNICATION) {
                audioManager.setMode(AudioManager.MODE_IN_COMMUNICATION);
            }
            try {
                Class clazz = Class.forName("android.media.AudioSystem");
                Method m = clazz.getMethod("setForceUse", new Class[]{int.class, int.class});
                m.invoke(null, 1, 1);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            if (audioManager.getMode() != AudioManager.MODE_IN_CALL) {
                audioManager.setMode(AudioManager.MODE_IN_CALL);
            }
        }
        if (audioManager.isSpeakerphoneOn()) {
            audioManager.setSpeakerphoneOn(false);
            audioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, audioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL),
                    AudioManager.STREAM_VOICE_CALL);
        }
    }
}
