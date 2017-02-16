package com.xiaojia.daniujia.ui.views.audio;

import android.media.MediaPlayer;

import java.io.IOException;

/**
 * Created by ZLOVE on 2016/4/14
 */
public class PlayVoice {
    private PlayVoice(){}
    private static PlayVoice instance;
    private boolean pause = false;
    public static PlayVoice getInstance() {
        if (instance == null) {
            instance = new PlayVoice();
        }
        return instance;
    }

    private static MediaPlayer mPlayer;
    private String mVoiceFile = null;
    private static boolean isStop = false;


    public boolean startPlaying(String voiceFile, MediaPlayer.OnCompletionListener completionListener) {
        if (mPlayer != null) {
            mPlayer.reset();
        } else {
            mPlayer = new MediaPlayer();
            mPlayer.setOnCompletionListener(completionListener);
        }
        isStop = false;
        mPlayer.setLooping(false);

        try {
            mVoiceFile = voiceFile;
            mPlayer.setDataSource(voiceFile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            mPlayer.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        mPlayer.start();
        return true;
    }

    public void stopPlaying() {
        if (mPlayer != null) {
//            completionListenertemp.onCompletion(mPlayer);
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }
    }

    public void pausePlaying() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
            pause = true;
        } else {
            if (pause) {
                mPlayer.start();
                pause = false;
            }
        }
    }

    public boolean isPlaying() {
        if (null == mPlayer)
            return false;

        return mPlayer.isPlaying();
    }

    public String getPlayingFilePath() {
        return mVoiceFile;
    }

    public int getDuration() {
        if (mPlayer == null) {
            return -1;
        }
        return mPlayer.getDuration();
    }

    public int getCurrentPosition() {
        if (null == mPlayer) {
            return -1;
        }
        return mPlayer.getCurrentPosition();
    }

    public static void stop() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            isStop = true;
        }
    }

    public static boolean isStop() {
        return isStop;
    }
}
