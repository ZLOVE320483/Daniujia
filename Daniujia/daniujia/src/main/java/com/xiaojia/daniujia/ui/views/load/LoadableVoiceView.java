package com.xiaojia.daniujia.ui.views.load;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.domain.resp.MessageEntity;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.ui.views.audio.PlayVoice;
import com.xiaojia.daniujia.utils.LogUtil;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.UIUtil;

import java.io.File;

/**
 * Created by ZLOVE on 2016/4/13
 */
public class LoadableVoiceView extends ImageView implements QiniuDownloadUtil.DownloadCallback, MediaPlayer.OnCompletionListener {

    private static PlayVoice pv = null;
    private static AnimationDrawable animationDrawable;
    public static Activity activity;
    boolean isSend = false;

    public LoadableVoiceView(Context context) {
        super(context);
    }

    public LoadableVoiceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadableVoiceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void resetImageResource() {
        if (animationDrawable != null) {
            animationDrawable.stop();
            animationDrawable.selectDrawable(2);
        }

    }

    public void stopPlay() {
        if (pv != null && pv.isPlaying()) {
            pv.stopPlaying();
            pv = null;
        }
        MsgHelper.getInstance().sendMsg(InsideMsg.STOP_CONTINUE_PLAY_VOICE);
        resetImageResource();
        messageEntity.setPlaying(false);
    }

    public void setUrl(String url, boolean isSend) {
        this.isSend = isSend;
        String playing_path = null;
        if (!TextUtils.isEmpty(url) && pv != null && pv.isPlaying()) {
            pv.stopPlaying();
            playing_path = pv.getPlayingFilePath();
            resetImageResource();
            pv = null;
        }
        File voiceCache = new File(FileStorage.APP_VOICE_DIR);
        if (TextUtils.isEmpty(url)) {
            UIUtil.showShortToast("录音文件不存在");
            return;
        }
        String[] str = url.split("/");
        if (!TextUtils.isEmpty(playing_path)) {
            String[] play_path = playing_path.split("/");
            if (str[str.length - 1].equals(play_path[play_path.length - 1])) {
                return;
            }
        }
        File file = new File(FileStorage.APP_VOICE_DIR, str[str.length - 1]);
        if (!voiceCache.exists()) {
            voiceCache.mkdirs();
            QiniuDownloadUtil.download(file, url, this);
        } else {
            if (file.length() != 0) {
                downloadComplete(file.getPath());
            } else {
                QiniuDownloadUtil.download(file, url, this);
            }
        }
    }

    public void setActivity(Activity act) {
        if (activity == null)
            activity = act;
    }

    private MessageEntity messageEntity;
    public void setMessageEntity(MessageEntity messageEntity){
        this.messageEntity = messageEntity;
        this.messageEntity.setPlaying(true);
    }

    @Override
    public void downloadComplete(final String path) {
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    play(path);
                }
            });
        }

    }

    @Override
    public void onProgress(int max, int progress) {

    }

    @Override
    public void onFail(Exception e) {

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        LogUtil.d("zptest","onCompletion!: " + mp.isPlaying() + " Current: "+mp.getCurrentPosition());
        resetImageResource();
        MsgHelper.getInstance().sendMsg(InsideMsg.CONTINUE_PLAY_VOICE);
        messageEntity.setPlaying(false);
    }

    // play the voice
    private void play(String path) {
        LogUtil.d("ZLOVE", "----path----" + path);
        if (pv == null) {
            pv = PlayVoice.getInstance();
        }
        pv.startPlaying(path, this);
        if (isSend) {
            setImageResource(R.drawable.voice_send_animation);
        } else {
            setImageResource(R.drawable.voice_receive_animation);
        }
        animationDrawable = (AnimationDrawable) getDrawable();

        if (animationDrawable != null && !animationDrawable.isRunning()) {
            animationDrawable.start();
        }

    }

    public void playAnimation(){
        if (isSend) {
            setImageResource(R.drawable.voice_send_animation);
        } else {
            setImageResource(R.drawable.voice_receive_animation);
        }
        animationDrawable = (AnimationDrawable) getDrawable();

        if (animationDrawable != null) {
            animationDrawable.start();
        }
    }

    public boolean isPlaying(){
        if (pv != null) {
            return pv.isPlaying();
        }

        return false;
    }

}
